package com.octopod.suite;

import com.octopod.suite.build.PluginDownload.Finished;
import com.octopod.suite.build.PluginDownload.Result;
import com.octopod.suite.build.PluginFinder;
import com.octopod.suite.build.PluginFinderException;
import com.octopod.suite.build.PluginFinderSession;
import com.octopod.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandGeneric implements CommandExecutor{

	public static PluginFinderSession session = new PluginFinderSession();

	final static String bVer = ChatColor.DARK_GRAY + "+---------------------------------------------------+";
	final static String bHor1 = ChatColor.DARK_GRAY + "\u2019| " + ChatColor.WHITE;
	final static String bHor2 = ChatColor.DARK_GRAY + "|";
	final static int midSize = 306;

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args)
	{
		String prefix = PluginSuitePlugin.pre;

		//Root message
		if(args.length == 0) {
			sender.sendMessage(prefix + "PluginSuitePlugin " + PluginSuitePlugin.self.getDescription().getVersion() + " by Octopod");
			return true;
		}

		String root = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);

		switch(root){
			case "load":

				if(args.length == 0) return false;
				PluginSuite.loadPlugin(args[0]).printResult(sender);
				return true;

			case "unload":

				if(args.length == 0) return false;
				PluginSuite.unloadPlugin(args[0]).printResult(sender);
				return true;

			case "enable":

				if(args.length == 0) return false;
				PluginSuite.enablePlugin(args[0]).printResult(sender);
				return true;

			case "disable":

				if(args.length == 0) return false;
				PluginSuite.disablePlugin(args[0]).printResult(sender);
				return true;

			case "reload":

				if(args.length == 0) return false;
				PluginResult unloadResult = PluginSuite.unloadPlugin(args[0]);
				unloadResult.printResult(sender);
				if(unloadResult.isSuccessful()) {
					PluginSuite.loadPlugin(args[0]).printResult(sender);
				}
				return true;

			case "finder":

				if(args.length == 0) {
					printFinderInfo(sender);
				} else {
					PluginFinder finder = PluginSuite.getPluginFinder(args[0]);
					if(finder != null) {
						session.changeFinder(finder);
						sender.sendMessage(PluginSuitePlugin.pre + "Finder changed to " + finder.getName());
					} else {
						sender.sendMessage(PluginSuitePlugin.pre + "Finder with ID " + args[0] + " not found");
					}
				}
				return true;

			case "find":

				if(args.length == 0) {
					return true;
				} else {
					if(args.length == 1) {
						try {
							session.findPlugin(args[0]);
						} catch (PluginFinderException e) {
							sender.sendMessage(PluginSuitePlugin.pre + e.getMessage());
						}
					} else {
						try {
							session.findPlugin(args[0], args[1]);
						} catch (PluginFinderException e) {
							sender.sendMessage(PluginSuitePlugin.pre + e.getMessage());
						}
					}
				}
			case "download":

				if(args.length == 0) {
					printDownloadInfo(sender);
				} else {
					if(session.download != null) {
						try {
							session.startDownload(new Finished() {
								public void finish(Result result) {
									if (result == Result.SUCCESS) {
										sender.sendMessage(PluginSuitePlugin.pre + "Download completed!");
									} else {
										sender.sendMessage(PluginSuitePlugin.pre + "Download was unsuccessful.");
									}
								}
							});
							sender.sendMessage(
									PluginSuitePlugin.pre + "Downloading " +
											ChatColor.GREEN + session.download.getName() + ChatColor.WHITE +
											" into the file " + ChatColor.GREEN + session.download.getOutput().getName()
							);
						} catch (PluginFinderException e) {
							e.printStackTrace();
						}
					} else {
						sender.sendMessage(PluginSuitePlugin.pre + "Plugin download not created yet.");
					}
				}
				return true;

			case "list":

				List<String> pluginNames = session.getFinder().listPluginNames();

				if(pluginNames == null) {
					sender.sendMessage(PluginSuitePlugin.pre + "Listing plugins isn't supported using this source type.");
					return true;
				}

				int page;
				switch(args.length){
					case 0:
						page = 0;
						break;
					default:
						try{page = Integer.parseInt(args[0]) - 1;}
						catch(NumberFormatException e){page = 0;}
						break;
				}

				int min = page * 10;
				int max = min + 10;
				int maxPage = (int)Math.ceil(pluginNames.size() / 10) + 1;

				sender.sendMessage(bVer);
				sender.sendMessage(
						bHor1 +
								TextUtils.block(ChatColor.AQUA + "" + ChatColor.BOLD + "Avaliable Items (Page " + (page + 1) + "/" + maxPage + ")", midSize, 2) +
								bHor2
				);
				sender.sendMessage(
						bHor1 +
								TextUtils.block(ChatColor.GRAY + "Some items might not be valid bukkit plugins.", midSize, 2) +
								bHor2
				);
				sender.sendMessage(bVer);
				for(int i = min; i < max; i++){
					try{
						String b1 = TextUtils.block((i+1) + "", 50, 0);
						String b2 = TextUtils.block(pluginNames.get(i), 256, 0);
						sender.sendMessage(bHor1 + b1 + b2 + bHor2);
					}catch(ArrayIndexOutOfBoundsException e){
						break;
					}
				}
				sender.sendMessage(bVer);
				return true;

		}
		return false;
	}

	public void printDownloadInfo(CommandSender sender)
	{
		boolean downloadFound = session.download != null;
		String pluginName, versionName, outputName;

		if(!downloadFound) {

			sender.sendMessage(PluginSuitePlugin.pre + "Plugin download not created yet.");

		} else {

			pluginName = session.download.getName();

			if(pluginName == null) {
				pluginName = ChatColor.GRAY + "NOT SET";
			}else{
				pluginName = ChatColor.AQUA + pluginName;
			}
			sender.sendMessage("Plugin: " + pluginName);

			versionName = session.download.getVersion();

			if(versionName == null) {
				versionName = ChatColor.GRAY + "NOT SET";
			}else{
				versionName = ChatColor.YELLOW + versionName;
			}
			sender.sendMessage("Version: " + versionName);

			outputName = ChatColor.GREEN + session.download.getOutput().getName();

			sender.sendMessage("Output File: " + outputName);
		}

	}

	public void printFinderInfo(CommandSender sender)
	{
		String sourceName, finderName = session.getFinder().getName();

		sender.sendMessage(PluginSuitePlugin.pre + "Finder Type: " + ChatColor.BLUE + finderName);

		if(session.getFinder().lockedSource())
		{
			sourceName = session.getFinder().getSource();
			if(sourceName == null) {
				sourceName = ChatColor.GRAY + "NOT SET";
			}else{
				sourceName = ChatColor.GOLD + sourceName;
			}
			sender.sendMessage(PluginSuitePlugin.pre + "Source Type: " + sourceName);
		}

	}
}