package com.octopod.nixium.nxsuite.build;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import com.octopod.nixium.utils.NServer;

public class PluginControl {
    
    public static Plugin[] getAllPlugins(){
        
        return NServer.getPlugin().getServer().getPluginManager().getPlugins();
        
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
	    	PluginDescriptionFile yml = NServer.getPlugin().getPluginLoader().getPluginDescription(file);
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
	
	public static boolean loadPlugin(String pluginName){
		
		File file = new File(getPluginFileName(pluginName));
		
		try {
			NServer.getPlugin().getPluginLoader().loadPlugin(file);
		} catch (Exception e) {
			return false;
		}
		
		return true;
		
	}
	
	public static boolean unloadPlugin(String pluginName){

		Plugin found = null;
		PluginManager pluginManager = NServer.getPlugin().getServer().getPluginManager();
		List<Plugin> plugins;
		Map<String, Plugin> names;
		
		for(Plugin plugin:getAllPlugins()){
			if(plugin.getDescription().getName().equalsIgnoreCase(pluginName)){
				found = plugin;
				break;
			}
		}
		
		if(found == null){return false;}
		
		try {
			
			Field pluginsField = pluginManager.getClass().getDeclaredField("plugins");
			pluginsField.setAccessible(true);
			plugins = (List<Plugin>) pluginsField.get(pluginManager);
			
			Field namesField = pluginManager.getClass().getDeclaredField("plugins");
			namesField.setAccessible(true);
			names = (Map<String, Plugin>) namesField.get(pluginManager);
			
			if(plugins != null && plugins.contains(found)){plugins.remove(found);}
			if(names != null && names.containsKey(pluginName)){names.remove(pluginName);}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

	}

}
