
package com.octopod.nixium.nxsuite;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.octopod.nixium.nxsuite.commands.CommandGeneric;
import com.octopod.nixium.nxsuite.commands.CommandPluginControl;
import com.octopod.nixium.nxsuite.commands.CommandPluginUpdater;

public class NXsuite extends JavaPlugin{
	
	final static public String pre = ChatColor.DARK_GRAY + "> " + ChatColor.WHITE;
	
	static private NXsuite plugin;
	static public NXsuite getInstance(){return plugin;}

    @Override
    public void onEnable(){
    	
        plugin = this;
        getCommand("nxs").			setExecutor(new CommandGeneric());
        
        getCommand("nxsload").		setExecutor(new CommandPluginControl());
        getCommand("nxsunload").	setExecutor(new CommandPluginControl());
        getCommand("nxsreload").	setExecutor(new CommandPluginControl());
        getCommand("nxsenable").	setExecutor(new CommandPluginControl());
        getCommand("nxsdisable").	setExecutor(new CommandPluginControl());
        
        getCommand("nxsinfo").		setExecutor(new CommandPluginUpdater());
        getCommand("nxsset").		setExecutor(new CommandPluginUpdater());
        getCommand("nxslist").		setExecutor(new CommandPluginUpdater());
        getCommand("nxsscan").		setExecutor(new CommandPluginUpdater());
        getCommand("nxsget").		setExecutor(new CommandPluginUpdater());

    }
    
    @Override
    public void onDisable(){
        
        
    }
    
}