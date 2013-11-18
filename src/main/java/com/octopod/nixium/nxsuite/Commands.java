
package com.octopod.nixium.nxsuite;

import com.octopod.nixium.nxsuite.build.PluginRepo;
import com.octopod.nixium.utils.Web;

import java.io.FileOutputStream;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;


public class Commands implements Listener{

    public Commands(NXsuite plugin){plugin.getServer().getPluginManager().registerEvents(this, plugin);}
    final static public String pre = ChatColor.DARK_GRAY + "> " + ChatColor.WHITE;
    
    public void rootCmd(Player player) {
    	player.sendMessage(pre + "NXsuite " + NXsuite.getInstance().getDescription().getVersion() + " by Octopod");
    }
    
    public void loadCmd(Player player, String pluginName){
    	if(pluginName == null){
			player.sendMessage("/nxs load <plugin>");
    	}else {
			player.sendMessage(pre + "Attempting to load " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.loadPlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    public void unloadCmd(Player player, String pluginName) {
    	if(pluginName == null){
			player.sendMessage("/nxs unload <plugin>");
    	}else {
			player.sendMessage(pre + "Attempting to unload " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.unloadPlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    public void enableCmd(Player player, String pluginName){
    	if(pluginName == null){
			player.sendMessage("/nxs enable <plugin>");
    	}else {
			player.sendMessage(pre + "Attempting to enable " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.enablePlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    public void disableCmd(Player player, String pluginName) {
    	if(pluginName == null){
			player.sendMessage("/nxs disable <plugin>");
    	}else {
			player.sendMessage(pre + "Attempting to disable " + ChatColor.GREEN + pluginName + ChatColor.WHITE + "...");
			int result = PluginControl.disablePlugin(pluginName);
			PluginControl.resultMessage(player, result);
    	}
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        
        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        
        if(args[0].equalsIgnoreCase("/nxs")){
            
            event.setCancelled(true);
             
            if(args.length == 1){
                rootCmd(player);
                return;
            }
            
            if(args[1].equalsIgnoreCase("find")){
            	if(args.length > 2) {
            		String file = PluginControl.getPluginFileName(args[2]);
            		if(file == null){
            			player.sendMessage(pre + "Plugin was not found.");
            		}else{
            			player.sendMessage(pre + "Plugin found under filename " + ChatColor.GREEN + file);
            		}
            	}else{
            		player.sendMessage(pre + "/nxs find <plugin>");
            	}
            	return;
            }
            
            if(args[1].equalsIgnoreCase("load")){
            	if(args.length == 2) {loadCmd(player, null);} 
            	else{loadCmd(player, args[2]);}
            	return;
            }
            
            if(args[1].equalsIgnoreCase("unload")){
            	if(args.length == 2) {unloadCmd(player, null);} 
            	else{unloadCmd(player, args[2]);}
            	return;
            }
            
            if(args[1].equalsIgnoreCase("enable")){
            	if(args.length == 2) {enableCmd(player, null);} 
            	else{enableCmd(player, args[2]);}
            	return;
            }
            
            if(args[1].equalsIgnoreCase("disable")){
            	if(args.length == 2) {disableCmd(player, null);} 
            	else{disableCmd(player, args[2]);}
            	return;
            }
            
            if(args[1].equalsIgnoreCase("update")){
                switch(args.length){
                    case 2:
                        player.sendMessage("/nxs update <plugin>");
                        //TODO: Help Command
                        break;
                    default:
                        PluginRepo xml = PluginRepo.instance();
                        String link = xml.getPluginLink(args[2]);
                        if(link == null){player.sendMessage(pre + "Plugin not found from this source.");}
                        else{
                            
                            String name = xml.correctPluginCase(args[2]);
                            String path = name + ".jar"; //New File Path
                            player.sendMessage(pre + "Downloading " + ChatColor.GREEN + name + ChatColor.WHITE + " into the file " + ChatColor.GREEN + path);
                            boolean result = savePluginToFile(link, "plugins/" + path); //Saves link to the path
                   
                            if(result) {
                            	player.sendMessage(pre + "Done. Loading plugin...");
                            	loadCmd(player, name);
                            }else{
                            	player.sendMessage(pre + "Download failed.");
                            }
                            
                        }
                        break;
                }
                return;
            }
            
            if(args[1].equalsIgnoreCase("source")){
            	
            	switch(args.length){
            		case 2:
            			player.sendMessage(pre + "Current source: " + ChatColor.GREEN + PluginRepo.getSource());
            			break;
            		default:
            			PluginRepo.setSource(args[2]);
            			player.sendMessage(pre + "Source set to " + ChatColor.GREEN + args[2]);
            			break;
            	}
            	
            }

            if(args[1].equalsIgnoreCase("list")){
                
                PluginRepo xml = PluginRepo.instance();
                String[] plugins = xml.getAvaliableBuilds();
                
                int page;
                switch(args.length){
                    case 2:
                        page = 0;
                        break;
                    default:
                        try{page = Integer.parseInt(args[2]) - 1;}
                        catch(NumberFormatException e){page = 0;}
                        break;
                }
                int min = page * 10;
                int max = min + 10;
                int maxPage = (int)Math.ceil(plugins.length / 10) + 1;

                player.sendMessage(pre + "Plugins avaliable for download (Page " + (page + 1) + "/ " + maxPage + ")");
                
                for(int i = min; i < max; i++){
                    try{
                        player.sendMessage(pre + plugins[i]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        break;
                    }
                }

                return;                
                
            }

            if(args[1].equalsIgnoreCase("scan")){
                
                PluginRepo xml = PluginRepo.instance();
                Plugin[] plugins = PluginControl.getAllPlugins();
                String[] list = new String[plugins.length];
                for(int i = 0; i < plugins.length; i++){
                    if(xml.correctPluginCase(plugins[i].getName()) != null){
                        list[i] = ChatColor.GREEN + plugins[i].getName() + ChatColor.GRAY;
                    }else{
                        list[i] = ChatColor.RED + plugins[i].getName() + ChatColor.GRAY;
                    }
                }
                String line = StringUtils.join(list, ", ");
                player.sendMessage(pre + "The plugins in green can be updated.");
                player.sendMessage(pre + line);
                
                return;

            }
            
            player.sendMessage(pre + "Unknown command.");
            
        }
        
    }

    public static boolean savePluginToFile(String url, String path){
        try{
            
            Web web = new Web(url);
            ReadableByteChannel bytes = web.getBytes();
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
  
}
