
package com.octopod.nixium.nxsuite;

import com.octopod.nixium.utils.NServer;
import com.octopod.nixium.nxsuite.build.PluginBuild;
import com.octopod.nixium.nxsuite.build.PluginControl;
import com.octopod.nixium.utils.NWeb;

import java.io.FileOutputStream;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class PluginUpdater implements Listener{

    public PluginUpdater(NXsuite plugin){plugin.getServer().getPluginManager().registerEvents(this, plugin);}
    final public String pre = ChatColor.GRAY + "[" + ChatColor.AQUA + "NXS" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        
        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        
        if(args[0].equalsIgnoreCase("/nxs")){
            
            event.setCancelled(true);
             
            if(args.length == 1){
                
                player.sendMessage(pre + "NXsuite " + NServer.getPlugin().getDescription().getVersion() + " by Octopod");
                return;
            }
            
            if(args[1].equalsIgnoreCase("find")){
            	switch(args.length){
            		case 2:
            			player.sendMessage("/nxs find <plugin>");
            			break;
            		default:
            			String path = PluginControl.getPluginFileName(args[2]);
            			if(path == null){
            				player.sendMessage(pre + "No plugin found.");
            			}else {
            				player.sendMessage(pre + "Plugin found @ " + path);
            			}
            	}
            	
            	
            	return;
            }
            
            if(args[1].equalsIgnoreCase("update")){
                switch(args.length){
                    case 2:
                        player.sendMessage("/nxs update <plugin>");
                        //TODO: Help Command
                        break;
                    default:
                        PluginBuild xml = PluginBuild.instance("http://ci.nixium.com/view/Plugins%20and%20Backports/api/xml");
                        String link = xml.getPluginLink(args[2]);
                        if(link == null){player.sendMessage("Plugin not found on this build site.");}
                        else{
                            
                            String name = xml.correctPluginCase(args[2]);
                            player.sendMessage("Downloading " + name + "...");
                            String path = name + ".jar"; //New File Path
                            boolean result = savePluginToFile(link, "plugins/" + path); //Saves link to the path
                            
                            if(result){player.sendMessage("Download saved as " + path);}
                            else{player.sendMessage("Download failed.");}
                            
                        }
                        break;
                }
                return;
            }

            if(args[1].equalsIgnoreCase("list")){
                
                PluginBuild xml = PluginBuild.instance("http://ci.nixium.com/view/Plugins%20and%20Backports/api/xml");
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
                
                PluginBuild xml = PluginBuild.instance("http://ci.nixium.com/view/Plugins%20and%20Backports/api/xml");
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
            
            NWeb web = new NWeb(url);
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
