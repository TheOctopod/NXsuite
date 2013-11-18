package com.octopod.nixium.nxsuite;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public class PluginControl {

	public final static int SUCCESS = 0;
	public final static int FAILURE = 1;
	
	public final static int LOAD_REPEAT_PLUGIN = 2;
	public final static int LOAD_INVALID_PLUGIN = 3;
	public final static int LOAD_MISSING_DEPENDANCY = 4;
	public final static int LOAD_INVALID_DESCRIPTION = 5;
	public final static int LOAD_NONEXISTANT = 6;

	public final static int UNLOAD_NONEXISTANT = 8;
	
	public final static int ENABLE_REPEAT = 9;
	public final static int ENABLE_NONEXISTANT = 10;
	
	public final static int DISABLE_REPEAT = 11;
	public final static int DISABLE_NONEXISTANT = 12;
	
	public static void resultMessage(Player player, int code) {
		String pre = Commands.pre;
		switch(code){
		case PluginControl.SUCCESS:
			player.sendMessage(pre + "Success!");
			break;
		case PluginControl.LOAD_NONEXISTANT:
			player.sendMessage(pre + "Failed: Plugin not found!");
			break;
		case PluginControl.LOAD_INVALID_PLUGIN:
			player.sendMessage(pre + "Failed: Found, but isn't a valid plugin file.");
			break;
		case PluginControl.LOAD_MISSING_DEPENDANCY:
			player.sendMessage(pre + "Failed: Found, but is missing dependancies.");
			break;
		case PluginControl.LOAD_INVALID_DESCRIPTION:
			player.sendMessage(pre + "Failed: Found, but has an invalid description.");
			break;
		case PluginControl.LOAD_REPEAT_PLUGIN:
			player.sendMessage(pre + "Failed: This plugin is already loaded!");
			break;
		case PluginControl.UNLOAD_NONEXISTANT:
			player.sendMessage(pre + "Failed: Plugin not found!");
			break;
		case PluginControl.ENABLE_REPEAT:
			player.sendMessage(pre + "Failed: This plugin is already enabled!");
			break;
		case PluginControl.ENABLE_NONEXISTANT:
			player.sendMessage(pre + "Failed: Plugin not found!");
			break;
		case PluginControl.DISABLE_REPEAT:
			player.sendMessage(pre + "Failed: This plugin is already disabled!");
			break;
		case PluginControl.DISABLE_NONEXISTANT:
			player.sendMessage(pre + "Failed: Plugin not found!");
			break;			
		default:
			player.sendMessage(pre + "Failed!");
			break;
		}
	}
		
    public static Plugin[] getAllPlugins(){
        
        return NXsuite.getInstance().getServer().getPluginManager().getPlugins();
        
    }
    
    public static String getPluginFileName(Plugin plugin){
        
        String[] path = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile().split("/");
        return path[path.length - 1];
        
    }
    
    public static String getPluginFileName(String pluginName){
        
    	File[] filelist = new File("plugins").listFiles();
    	
    	for(File file:filelist){
    		
    		try {
	    		if(getPluginName(file.getPath()).equalsIgnoreCase(pluginName)){
	    			return file.getPath();
	    		}
			} catch (Exception e) {}
    		
    	}

        return null;
        
    }
    
    public static PluginDescriptionFile getPluginConfig(String path){
    	
    	try {
	    	File file = new File(path);
	    	PluginDescriptionFile yml = NXsuite.getInstance().getPluginLoader().getPluginDescription(file);
			return yml;
		} catch (Exception e) {
			return null;
		}
    	
    }
    
    //Get a plugin's version from a path or filename
    
    public static String getPluginVersion(Plugin plugin){
    	
    	return getPluginVersion(getPluginFileName(plugin));
    }
    
    public static String getPluginVersion(String path){
    	
    	PluginDescriptionFile yml = getPluginConfig(path);

		if(yml == null) {
			return "0";
		} else {
			return yml.getVersion();
		}
		
    }
    
    //Get a plugin's name from a path or filename
    
    public static String getPluginName(Plugin plugin){
    	
    	return getPluginName(getPluginFileName(plugin));
    }
    
    public static String getPluginName(String path){

	    PluginDescriptionFile yml = getPluginConfig(path);

		if(yml == null) {
			return "";
		} else {
			return yml.getName();
		}
		
    }
    
    //Plugin Control Methods

    private static Plugin getPlugin(String p) {
        for (Plugin pl : NXsuite.getInstance().getServer().getPluginManager().getPlugins()) {
            if (pl.getDescription().getName().equalsIgnoreCase(p)) {
                return pl;
            }
        }
        return null;
    }
    
	public static int loadPlugin(String pluginName){

		String filename = getPluginFileName(pluginName);
		if(filename == null) {return LOAD_NONEXISTANT;}
		
		File file = new File(filename);
		if(getPlugin(pluginName) != null) {return LOAD_REPEAT_PLUGIN;}
		
		try {
			
			NXsuite.getInstance().getServer().getPluginManager().loadPlugin(file);
			Plugin plugin = getPlugin(pluginName);
			plugin.onLoad();
			NXsuite.getInstance().getServer().getPluginManager().enablePlugin(plugin);
			return SUCCESS;
        } catch (UnknownDependencyException e) {
            return LOAD_MISSING_DEPENDANCY;
        } catch (InvalidPluginException e) {
            return LOAD_INVALID_PLUGIN;
        } catch (InvalidDescriptionException e) {
            return LOAD_INVALID_DESCRIPTION;
        } catch (Exception e) {
        	return FAILURE;
        }

	}
	
	public static int unloadPlugin(String pluginName){

		Plugin plugin = getPlugin(pluginName);
		if(plugin == null){return UNLOAD_NONEXISTANT;}
		
		PluginManager pm = NXsuite.getInstance().getServer().getPluginManager();

			pm.disablePlugin(plugin);
			
			Field pluginsField;
			try {
				pluginsField = pm.getClass().getDeclaredField("plugins");

				pluginsField.setAccessible(true);
				List<Plugin> plugins = (List<Plugin>)pluginsField.get(pm);
				
				Field namesField = pm.getClass().getDeclaredField("lookupNames");
				namesField.setAccessible(true);
				Map<String, Plugin> names = (Map<String, Plugin>)namesField.get(pm);
				
				if(plugins != null && plugins.contains(plugin)){plugins.remove(plugin);}
				if(names != null && names.containsKey(pluginName)){names.remove(pluginName);}
				
			} catch (Exception e) {
				return FAILURE;
			}
			
		return SUCCESS;

	}
	
	public static int enablePlugin(String pluginName){
		PluginManager pm = NXsuite.getInstance().getServer().getPluginManager();
		Plugin plugin = getPlugin(pluginName);
		if(plugin == null) {return ENABLE_NONEXISTANT;}
		if(pm.isPluginEnabled(plugin)) {return ENABLE_REPEAT;}
		pm.enablePlugin(plugin);
		return SUCCESS;
	}
	
	public static int disablePlugin(String pluginName){
		PluginManager pm = NXsuite.getInstance().getServer().getPluginManager();
		Plugin plugin = getPlugin(pluginName);
		if(plugin == null) {return DISABLE_NONEXISTANT;}
		if(!pm.isPluginEnabled(plugin)) {return DISABLE_REPEAT;}
		pm.disablePlugin(plugin);
		return SUCCESS;
	}

}
