package com.octopod.suite;

import com.octopod.suite.build.PluginFinder;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static com.octopod.suite.PluginResult.PluginResultType.*;

public class PluginSuite {

	private static Map<String, PluginFinder> pluginFinders = new HashMap<>();

	public static void registerFinder(String key, PluginFinder repo) {
		pluginFinders.put(key, repo);
	}

	public static void unregisterFinder(String key) {
		pluginFinders.remove(key);
	}

	public static PluginFinder getPluginFinder(String key) {
		return pluginFinders.get(key);
	}

	private static PluginManager getPluginManager()
	{
		return PluginSuitePlugin.self.getServer().getPluginManager();
	}

    private static Plugin[] getPlugins()
	{
        return getPluginManager().getPlugins();
    }

	/**
	 * Gets the filename that the plugin originated from.
	 * @param plugin
	 * @return
	 */
    public static File findPluginFileByName(Plugin plugin)
	{
        return new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
    }

    public static File findPluginFileByName(String pluginName)
	{
    	File[] filelist = new File("plugins").listFiles();
		if(filelist != null)
		{
			for(File file: filelist)
			{
				String filePluginName = getPluginName(file);
				if(filePluginName == null) continue;
				if(filePluginName.equalsIgnoreCase(pluginName)){
					return file;
				}
			}
		}
        return null;
    }

	public static PluginDescriptionFile extractPluginDescription(String filepath) {
		return extractPluginDescription(new File(filepath));
	}
    
    public static PluginDescriptionFile extractPluginDescription(File file)
	{
    	try {
			return PluginSuitePlugin.self.getPluginLoader().getPluginDescription(file);
		} catch (InvalidDescriptionException e) {
			return null;
		}
    }

    public static String getPluginVersion(File file)
	{
    	PluginDescriptionFile yml = extractPluginDescription(file);
		return yml == null ? null : yml.getVersion();
    }

    public static String getPluginName(File file)
	{
	    PluginDescriptionFile yml = extractPluginDescription(file);
		return yml == null ? null : yml.getName();
    }

	public static String getPluginDescription(File file)
	{
		PluginDescriptionFile yml = extractPluginDescription(file);
		return yml == null ? null : yml.getDescription();
	}

	public static String getPluginNameTag(Plugin plugin) {
		PluginDescriptionFile yml = plugin.getDescription();
		return yml.getName() + " " + yml.getVersion();
	}

    //Plugin Control Methods

	public static boolean isPluginLoaded(String pluginName) {
		return getPluginByName(pluginName) != null;
	}

	public static boolean isPluginEnabled(String pluginName) {
		Plugin plugin = getPluginByName(pluginName);
		return plugin != null && plugin.isEnabled();
	}

    public static Plugin getPluginByName(String pluginName)
	{
        for (Plugin pl : getPlugins()) {
            if (pl.getDescription().getName().equalsIgnoreCase(pluginName)) {
                return pl;
            }
        }
        return null;
    }

	public static PluginResult loadPlugin(String pluginName)
	{
		if(isPluginLoaded(pluginName)) {return new PluginResult(FAILED_ALREADY_LOADED);}
		File file = findPluginFileByName(pluginName);
		if(file == null) {return new PluginResult(FAILED_PLUGIN_NOT_FOUND);}

		PluginManager pm = getPluginManager();

		try {
			
			pm.loadPlugin(file);
			Plugin plugin = getPluginByName(pluginName);
			plugin.onLoad();
			pm.enablePlugin(plugin);
			
			return new PluginResult(SUCCESS_LOADED, plugin);
        } catch (UnknownDependencyException e) {
            return new PluginResult(FAILED_MISSING_DEPENDENCIES);
        } catch (InvalidPluginException e) {
            return new PluginResult(FAILED_INVALID_PLUGIN_FILE);
        } catch (InvalidDescriptionException e) {
            return new PluginResult(FAILED_INVALID_PLUGIN_YML);
        } catch (Exception e) {
        	return new PluginResult(FAILED_GENERIC);
        }
	}

	public static PluginResult unloadPlugin(String pluginName)
	{
		Plugin plugin = getPluginByName(pluginName);
		if(plugin == null)
			return new PluginResult(FAILED_PLUGIN_NOT_FOUND);
			return unloadPlugin(plugin);
	}
	
	@SuppressWarnings("unchecked")
	public static PluginResult unloadPlugin(Plugin plugin)
	{
		Map<Event, SortedSet<RegisteredListener>> listeners = null;
		Field pluginsField;

		String pluginName = plugin.getName();

		boolean reloadlisteners = true;

		PluginManager pm = getPluginManager();
		pm.disablePlugin(plugin);

		try
		{
			pluginsField = pm.getClass().getDeclaredField("plugins");
			pluginsField.setAccessible(true);
			List<Plugin> plugins = (List<Plugin>)pluginsField.get(pm);
			
			Field namesField = pm.getClass().getDeclaredField("lookupNames");
			namesField.setAccessible(true);
			Map<String, Plugin> names = (Map<String, Plugin>)namesField.get(pm);
			
            Field commandMapField = pm.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap cmdMap = (SimpleCommandMap) commandMapField.get(pm);

            Field knownCommandsField = cmdMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            Map<String, Command> commands = (Map<String, Command>)knownCommandsField.get(cmdMap);
            
            try {
                Field listenersField = pm.getClass().getDeclaredField("listeners");
                listenersField.setAccessible(true);
                listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pm);
            } catch (Exception e) {
                reloadlisteners = false;
            }
			
			if(plugins != null && plugins.contains(plugin)){plugins.remove(plugin);}
			if(names != null && names.containsKey(pluginName)){names.remove(pluginName);}
			
            if (listeners != null && reloadlisteners)
			{
                for (SortedSet<RegisteredListener> set : listeners.values()) {
                    for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext(); ) {
                        RegisteredListener value = it.next();
                        if (value.getPlugin() == plugin) {it.remove();}
                    }
                }
            }

            if (cmdMap != null)
			{
                for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, Command> entry = it.next();
                    if (entry.getValue() instanceof PluginCommand) {
                        PluginCommand c = (PluginCommand) entry.getValue();
                        if (c.getPlugin() == plugin) {
                            c.unregister(cmdMap);
                            it.remove();
                        }
                    }
                }
            }
		} catch (Exception e) {
			return new PluginResult(FAILED_GENERIC);
		}
			
		return new PluginResult(SUCCESS_UNLOADED, plugin);
	}

	public static PluginResult enablePlugin(String pluginName)
	{
		Plugin plugin = getPluginByName(pluginName);
		if(plugin == null)
			return new PluginResult(FAILED_PLUGIN_NOT_FOUND);
			return enablePlugin(plugin);
	}
	
	public static PluginResult enablePlugin(Plugin plugin)
	{
		PluginManager pm = getPluginManager();
		if(pm.isPluginEnabled(plugin)) {return new PluginResult(FAILED_ALREADY_ENABLED);}
		pm.enablePlugin(plugin);
		return new PluginResult(SUCCESS_ENABLED, plugin);
	}

	public static PluginResult disablePlugin(String pluginName)
	{
		Plugin plugin = getPluginByName(pluginName);
		if(plugin == null)
			return new PluginResult(FAILED_PLUGIN_NOT_FOUND);
			return disablePlugin(plugin);
	}
	
	public static PluginResult disablePlugin(Plugin plugin)
	{
		PluginManager pm = getPluginManager();
		if(!pm.isPluginEnabled(plugin)) {return new PluginResult(FAILED_ALREADY_DISABLED);}
		pm.disablePlugin(plugin);
		return new PluginResult(SUCCESS_DISABLED, plugin);
	}

}
