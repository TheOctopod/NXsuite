package com.octopod.nixium.nxsuite.commands;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.octopod.nixium.nxsuite.NXsuite;
import com.octopod.nixium.nxsuite.PluginControl;
import com.octopod.nixium.nxsuite.build.PluginRepo;
import com.octopod.nixium.utils.TextUtils;
import com.octopod.nixium.utils.Web;
import com.octopod.nixium.utils.WebUtils;

public class CommandPluginUpdater implements CommandExecutor{
	
	public static PluginRepo repo = PluginRepo.instance(PluginRepo.TYPE_BUKKIT);
	
	final static String bVer = ChatColor.DARK_GRAY + "+---------------------------------------------------+";
	final static String bHor1 = ChatColor.DARK_GRAY + "\u2019| " + ChatColor.WHITE;
	final static String bHor2 = ChatColor.DARK_GRAY + "|";
	final static int midSize = 306;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {return false;}
		Player player = (Player)sender;
		
		switch(label.toLowerCase()){
		
			case "nxsinfo":
				
				showInfo(player);
				return true;
			
			case "nxsget":
				
	            switch(args.length){
	                case 0:
	                    player.sendMessage("/nxsget <plugin>");
	                    break;
	                default:
	                    String link = repo.getDownload();
	                    
	                    if(link == null){
	                    	player.sendMessage(NXsuite.pre + "Plugin not found from this source.");
	                    	return true;
	                    }
	                    if(!getFileExtension(link).equalsIgnoreCase("jar")) {
	                    	player.sendMessage(NXsuite.pre + "The file to be downloaded is a '" + getFileExtension(link) + "', which isn't supported.");
	                    	return true;                    	
	                    }
	                    
	                    String name = repo.getPluginName();
	                    String path = name + ".jar"; //New File Path
	                    
	                    player.sendMessage(NXsuite.pre + "Downloading " + ChatColor.GREEN + name + ChatColor.WHITE + " into the file " + ChatColor.GREEN + path);
	                    player.sendMessage(NXsuite.pre + "Using " + ChatColor.GREEN + link);
	                    boolean result = savePluginToFile(link, "plugins/" + path); //Saves link to the path
	           
	                    if(result) {
	                    	player.sendMessage(NXsuite.pre + "Done. Loading plugin...");
	                    	CommandPluginControl.loadCmd(player, name);
	                    }else{
	                    	player.sendMessage(NXsuite.pre + "Download failed.");
	                    }
	
	                    break;
	            }
	            return true;

			case "nxsset":
        	
	        	switch(args.length){
	        		case 0: case 1:
	        			player.sendMessage(NXsuite.pre + "Valid Fields: TYPE, REPO, FILTER, PLUGIN, VERSION, OUTPUT");
	        			break;
	        		default:
	        			switch(args[0].toUpperCase()){
	        				case "TYPE":
	        					int type = 0;
	        					try{type = Integer.valueOf(args[1]);} catch (NumberFormatException e) {
	        						player.sendMessage(NXsuite.pre + "Type can only be a number.");
	        						return true;
	        					}
	        					repo = repo.toRepo(type);
	        					break;
	        				case "REPO":
	        					repo.setRepo(args[1]);
	        					break;
	        				case "FILTER":
	        				case "PLUGIN":
	        				case "VERSION":
	        				case "OUTPUT":
	        					break;
	        				default:
	        					player.sendMessage(NXsuite.pre + "Invalid Field '" + args[0].toUpperCase() + "'");
	        					return true;
	        			}
	        			showInfo(player);
	        			break;
	        	}
	        	
	        	return true;
        	
			case "nxslist":
				
	            String[] plugins = repo.listPlugins();
	            
	            if(plugins == null) {
	            	player.sendMessage(NXsuite.pre + "Listing plugins isn't supported using this source type.");
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
	            int maxPage = (int)Math.ceil(plugins.length / 10) + 1;
	            
				player.sendMessage(bVer);
				player.sendMessage(
						bHor1 +
						TextUtils.block(ChatColor.AQUA + "" + ChatColor.BOLD + "Avaliable Items (Page " + (page + 1) + "/" + maxPage + ")", midSize, 2) + 
						bHor2
				);
				player.sendMessage(
						bHor1 +
						TextUtils.block(ChatColor.GRAY + "Some items might not be valid bukkit plugins.", midSize, 2) + 
						bHor2
				);
				player.sendMessage(bVer);
	            for(int i = min; i < max; i++){
	                try{
	                	String b1 = TextUtils.block((i+1) + "", 50, 0);
	                	String b2 = TextUtils.block(plugins[i], 256, 0);
	                    player.sendMessage(bHor1 + b1 + b2 + bHor2);
	                }catch(ArrayIndexOutOfBoundsException e){
	                    break;
	                }
	            }
	            player.sendMessage(bVer);
	            return true;
	        default:
	        	player.sendMessage(NXsuite.pre + "Unknown command.");
	        	return true;
        }

	}
	
	public static boolean savePluginToFile(String surl, String path){
	    try{
	        URL url = new URL(surl);
	        ReadableByteChannel bytes = WebUtils.URLtoRBC(url);
	        if(bytes == null){return false;}
	        try (FileOutputStream output = new FileOutputStream(path)) {
	            output.getChannel().transferFrom(bytes, 0, Long.MAX_VALUE);
	        }
	    }catch(Exception e){
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	public static String getFileExtension(String path) {
		String extension = "";
		String[] split = path.split("\\.");
		if(split.length > 0)
			return split[split.length - 1];
			return extension;
	}
	
	public void showInfo(Player player) {

		String typeName = repo.getTypeName();
		String repoName, filterName, pluginName, versionName;
		
		if(repo.isRepoModifiable()) {
			repoName = repo.getRepo();
			if(repoName == null) {
				repoName = ChatColor.GRAY + "None";
			}else{
				repoName = ChatColor.GOLD + repoName;
			}
		} else {
			repoName = ChatColor.DARK_GRAY + "Unmodifiable";
		}
		
		if(repo.isFilterModifiable()) {
			filterName = repo.getFilter();
			if(filterName == null) {
				filterName = ChatColor.GRAY + "None";
			}else{
				filterName = ChatColor.LIGHT_PURPLE + filterName;
			}
		} else {
			filterName = ChatColor.DARK_GRAY + "Unmodifiable";
		}
		
		if(repo.isPluginModifiable()) {
			pluginName = repo.getPlugin();
			if(pluginName == null) {
				pluginName = ChatColor.GRAY + "None";
			}else{
				pluginName = ChatColor.AQUA + pluginName;
			}
		} else {
			pluginName = ChatColor.DARK_GRAY + "Unmodifiable";
		}
		
		if(repo.isVersionModifiable()) {
			versionName = String.valueOf(repo.getVersion());
			if(versionName == null) {
				versionName = ChatColor.GRAY + "None";
			}else{
				versionName = ChatColor.YELLOW + versionName;
			}
		} else {
			versionName = ChatColor.DARK_GRAY + "Unmodifiable";
		}
		
		
		player.sendMessage(new String[]{
				NXsuite.pre + "Source Type: " + ChatColor.BLUE + typeName + " (#" + repo.getType() + ")",
				NXsuite.pre + "Repository: " + repoName,
				NXsuite.pre + "Filter: " + filterName,
				NXsuite.pre + "Plugin: " + pluginName,
				NXsuite.pre + "Version: " + versionName,
				NXsuite.pre + "Output: " + ChatColor.GREEN + repo.getOutput(),
		});
		
	}

}
