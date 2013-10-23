package com.octopod.nixium.nxsuite.build;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import com.octopod.nixium.utils.JarResources;
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
        
    	File[] filelist = new File("plugins/").listFiles();
    	for(File file:filelist){
    		
    		if(getPluginName(file.getPath()).equalsIgnoreCase(pluginName)){
    			return file.getPath();
    		}
    		
    	}

        return null;
        
    }
    
    public static YamlConfiguration getPluginConfig(String path){
    	
    	JarResources jar = new JarResources(path);
    	YamlConfiguration yml = new YamlConfiguration();
    	try {
    		yml.loadFromString(new String(jar.getResource("plugin.yml")));
			return yml;
		} catch (Exception e) {
			return null;
		}
    	
    }
    
    public static String getPluginVersion(String path){
    	
    	YamlConfiguration yml = getPluginConfig(path);

		if(yml == null) {
			return "0";
		} else {
			return yml.getString("version");
		}
		
    }
    
    public static String getPluginName(String path){
    	
    	YamlConfiguration yml = getPluginConfig(path);

		if(yml == null) {
			return "0";
		} else {
			return yml.getString("name");
		}
		
    }
	
	public static boolean loadPlugin(String pluginName){
		
		File file = new File(getPluginFileName(pluginName));
		
		try {
			NServer.getPlugin().getPluginLoader().loadPlugin(file);
		} catch (UnknownDependencyException | InvalidPluginException e) {
			return false;
		}
		
		return true;
		
	}

}
