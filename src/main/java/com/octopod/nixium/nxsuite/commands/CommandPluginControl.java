package com.octopod.nixium.nxsuite.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octopod.nixium.nxsuite.NXsuite;
import com.octopod.nixium.nxsuite.PluginControl;

public class CommandPluginControl implements CommandExecutor{
    
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player) || args.length == 0) {return false;}
		Player player = (Player)sender;
		switch(label) {
			case "nxsload":
				loadCmd(player, args[0]);
				return true;
			case "nxsunload":
				unloadCmd(player, args[0]);
				return true;
			case "nxsenable":
				enableCmd(player, args[0]);
				return true;
			case "nxsdisable":
				disableCmd(player, args[0]);
				return true;
			case "nxsreload":
				reloadCmd(player, args[0]);
				return true;
			default:
				return false;
		}
	}
    
    public static void loadCmd(Player player, String pluginName){
    	if(pluginName == null){
			player.sendMessage("/nxs load <plugin>");
    	}else {
			player.sendMessage(NXsuite.pre + "Attempting to load " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.loadPlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    public static void unloadCmd(Player player, String pluginName) {
    	if(pluginName == null){
			player.sendMessage("/nxs unload <plugin>");
    	}else {
			player.sendMessage(NXsuite.pre + "Attempting to unload " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.unloadPlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    public static void reloadCmd(Player player, String pluginName) {
    	unloadCmd(player, pluginName);
    	loadCmd(player, pluginName);
    }
    
    public static void enableCmd(Player player, String pluginName){
    	if(pluginName == null){
			player.sendMessage("/nxs enable <plugin>");
    	}else {
			player.sendMessage(NXsuite.pre + "Attempting to enable " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.enablePlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    public static void disableCmd(Player player, String pluginName) {
    	if(pluginName == null){
			player.sendMessage("/nxs disable <plugin>");
    	}else {
			player.sendMessage(NXsuite.pre + "Attempting to disable " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.disablePlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }

}
