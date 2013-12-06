package com.octopod.nixium.nxsuite.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octopod.nixium.nxsuite.NXsuite;

public class CommandGeneric implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandExecutor plugincontrol = new CommandPluginControl();
		CommandExecutor pluginupdater = new CommandPluginUpdater();
		Player player = (Player)sender;
		String pre = NXsuite.pre;
		if(args.length == 0) {
			player.sendMessage(pre + "NXsuite by Octopod");
			return true;
		}
		String root = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		switch(root){
			case "load":
				return plugincontrol.onCommand(sender, command, "nxsload", args);
			case "unload":
				return plugincontrol.onCommand(sender, command, "nxsunload", args);
			case "enable":
				return plugincontrol.onCommand(sender, command, "nxsenable", args);
			case "disable":
				return plugincontrol.onCommand(sender, command, "nxsdisable", args);
			case "repo":
				return pluginupdater.onCommand(sender, command, "nxsrepo", args);
			case "list":
				return pluginupdater.onCommand(sender, command, "nxslist", args);
			case "scan":
				return pluginupdater.onCommand(sender, command, "nxsscan", args);
			case "update":
				return pluginupdater.onCommand(sender, command, "nxsupdate", args);
		}
		return false;
	}

}