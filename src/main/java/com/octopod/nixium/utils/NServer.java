
package com.octopod.nixium.utils;

import com.octopod.nixium.nxsuite.NXsuite;

public class NServer {
    
    static NXsuite plugin;
    
    public static void setPlugin(NXsuite plugin){NServer.plugin = plugin;}
    public static NXsuite getPlugin(){return NServer.plugin;}
    
    public static void broadcast(String message){plugin.getServer().broadcastMessage(message);}
    public static void console(String message){plugin.getLogger().info(message);}
    
}