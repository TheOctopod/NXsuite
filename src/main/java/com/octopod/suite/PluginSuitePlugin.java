
package com.octopod.suite;

import com.octopod.suite.build.BukgetPluginFinder;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginSuitePlugin extends JavaPlugin{
	
	final static public String pre = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "PluginSuite" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
	
	static public PluginSuitePlugin self;

    @Override
    public void onEnable()
	{
        self = this;

		PluginSuite.registerFinder("bukget", new BukgetPluginFinder());
		//PluginSuite.registerRepo("jenkins", new JenkinsPluginFinder());
        getCommand("suite").setExecutor(new CommandGeneric());

    }
    
    @Override
    public void onDisable()
	{

    }
    
}