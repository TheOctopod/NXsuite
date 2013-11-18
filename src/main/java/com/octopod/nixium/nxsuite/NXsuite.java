
package com.octopod.nixium.nxsuite;

import org.bukkit.plugin.java.JavaPlugin;

public class NXsuite extends JavaPlugin{
	
	static public NXsuite plugin;
	static public NXsuite getInstance(){return plugin;}

    @Override
    public void onEnable(){
        plugin = this;
        new Commands(this);
    }
    
    @Override
    public void onDisable(){
        
        
    }
    
}