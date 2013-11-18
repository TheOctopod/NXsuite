
package com.octopod.nixium.nxsuite.build;

public abstract class PluginRepo {
    
	static private String url = "ci.nixium.com";
    static private String using = "Jenkins";
    
    public static void setSource(String newUrl) {url = newUrl;}
    public static String getSource() {return url;}
      
    static public PluginRepo instance(){return instance(url);}
    static public PluginRepo instance(String url){
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