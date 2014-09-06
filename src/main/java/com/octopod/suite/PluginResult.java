package com.octopod.suite;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PluginResult {

	public static enum PluginResultType {

		SUCCESS_LOADED(true),

		SUCCESS_ENABLED(true),

		SUCCESS_DISABLED(true),

		SUCCESS_UNLOADED(true),

		FAILED_INVALID_PLUGIN_FILE,

		FAILED_MISSING_DEPENDENCIES,

		FAILED_INVALID_PLUGIN_YML,

		FAILED_PLUGIN_NOT_FOUND,

		FAILED_ALREADY_LOADED,

		FAILED_ALREADY_ENABLED,

		FAILED_ALREADY_DISABLED,

		FAILED_GENERIC;

		boolean successful;

		private PluginResultType() {this(false);}

		private PluginResultType(boolean successful)
		{
			this.successful = successful;
		}

		public boolean isSuccessful()
		{
			return successful;
		}

	}

	String pluginTag = "";
	PluginResultType type;

	public PluginResult(PluginResultType type)
	{
		this(type, null);
	}

	public PluginResult(PluginResultType type, Plugin plugin)
	{
		this.type = type;
		if(plugin != null) {
			PluginDescriptionFile yml = plugin.getDescription();
			pluginTag = yml.getName() + " " + yml.getVersion();
		}
	}

	public PluginResultType getType()
	{
		return type;
	}

	public boolean isSuccessful()
	{
		return getType().isSuccessful();
	}

	public void printResult(CommandSender sender)
	{
		String pre = PluginSuitePlugin.pre;

		switch(getType()){
			case SUCCESS_LOADED:
				sender.sendMessage(pre + ChatColor.GREEN + "Successfully loaded " + pluginTag);
				break;
			case SUCCESS_UNLOADED:
				sender.sendMessage(pre + ChatColor.GREEN + "Successfully unloaded " + pluginTag);
				break;
			case SUCCESS_ENABLED:
				sender.sendMessage(pre + ChatColor.GREEN + "Successfully enabled " + pluginTag);
				break;
			case SUCCESS_DISABLED:
				sender.sendMessage(pre + ChatColor.GREEN + "Successfully disabled " + pluginTag);
				break;
			case FAILED_PLUGIN_NOT_FOUND:
				sender.sendMessage(pre + ChatColor.RED + "This plugin was not found!");
				break;
			case FAILED_INVALID_PLUGIN_FILE:
				sender.sendMessage(pre + ChatColor.RED + "This is not a valid plugin file!");
				break;
			case FAILED_MISSING_DEPENDENCIES:
				sender.sendMessage(pre + ChatColor.RED + "This plugin is missing dependencies!");
				break;
			case FAILED_INVALID_PLUGIN_YML:
				sender.sendMessage(pre + ChatColor.RED + "This plugin has an invalid description YML!");
				break;
			case FAILED_ALREADY_LOADED:
				sender.sendMessage(pre + ChatColor.RED + "This plugin is already loaded!");
				break;
			case FAILED_ALREADY_ENABLED:
				sender.sendMessage(pre + ChatColor.RED + "This plugin is already enabled!");
				break;
			case FAILED_ALREADY_DISABLED:
				sender.sendMessage(pre + ChatColor.RED + "This plugin is already loaded!");
				break;
			case FAILED_GENERIC:
				sender.sendMessage(pre + ChatColor.RED + "This action failed for unknown reasons.");
		}
	}
}
