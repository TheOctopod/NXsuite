
package com.octopod.nixium.nxsuite.build;

public abstract class PluginBuild {
    
    static private String using = "Jenkins";
    
    static public PluginBuild instance(String url){
        switch(using){
            case "Jenkins":
                return new Jenkins(url);
            default:
                return null;
        }
    }
    
    //Gets all the avaliable builds from this website
    abstract public String[] getAvaliableBuilds();    
    
    //Corrects the casing of the plugin name
    abstract public String correctPluginCase(String pluginName);
    
    //Gets the latest plugin download link, given the plugin's name
    abstract public String getPluginLink(String pluginName);
    
}
