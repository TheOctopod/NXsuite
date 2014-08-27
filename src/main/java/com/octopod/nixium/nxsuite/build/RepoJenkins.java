
package com.octopod.nixium.nxsuite.build;

import com.octopod.nixium.utils.XML;

public class RepoJenkins{
	
	/* 
    private XML api;
    private String view = "";
    
    public Jenkins(){setView(view);}
    
    public void setView(String view){
 
    	this.view = view.replace(" ", "%20");
    	
    	if(view.equalsIgnoreCase("")){
    		this.api = new XML("http://" + getRepo() + "/api/xml");
    	}else {
    		this.api = new XML("http://" + getRepo() + "/view/" + view + "/api/xml");
    	}
    	
    }
    
    public String getView(){return view;}
    
    @Override
    public String[] getAvaliableBuilds(){return api.getTags("name", "job");}
    
    @Override
    public boolean getPluginExists(String pluginName){
        String[] nameList = api.getTags("name", "job");
        for(int i = 0; i < nameList.length; i++){
            if(pluginName.toLowerCase().equals(nameList[i].toLowerCase())){return true;}
        }
        return false;
    }
    
    @Override
    public String getPluginName(String pluginName){
        String[] nameList = api.getTags("name", "job");
        for(int i = 0; i < nameList.length; i++){
            if(pluginName.toLowerCase().equals(nameList[i].toLowerCase())){return nameList[i];}
        }
        return pluginName;
    }
    
    @Override
    public String getPluginLink(String pluginName){

        String[] nameList = api.getTags("name", "job");
        String[] urlList = api.getTags("url", "job");
        String link = null;
        for(int i = 0; i < nameList.length; i++){
            if(pluginName.toLowerCase().equals(nameList[i].toLowerCase())){link = urlList[i]; break;}
        }
        if(link == null){return null;}

        XML buildPage = new XML(link + "lastSuccessfulBuild/api/xml");
        String[] fileNames = buildPage.getTags("fileName");
        if(fileNames.length == 0){return null;}
        
        return link + "lastSuccessfulBuild/artifact/target/" + buildPage.getTags("fileName")[0];

    }
    */
    
}
