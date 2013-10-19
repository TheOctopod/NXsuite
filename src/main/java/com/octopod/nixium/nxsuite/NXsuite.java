
package com.octopod.nixium.nxsuite;
import com.octopod.nixium.utils.NServer;
import org.bukkit.plugin.java.JavaPlugin;

public class NXsuite extends JavaPlugin{

    @Override
    public void onEnable(){
        NServer.setPlugin(this);
        new PluginUpdater(this);
    }
    
    @Override
    public void onDisable(){
        
        
    }
    
}