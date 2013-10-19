
package com.octopod.nixium.nxsuite.build;

import com.octopod.nixium.utils.NXML;

public class Jenkins extends PluginBuild{

    private NXML api;
    
    public Jenkins(String url){this.api = new NXML(url);}
    
    @Override
    public String[] getAvaliableBuilds(){return api.getTags("name", "job");}
    
    @Override
    public String correctPluginCase(String pluginName){

        String[] nameList = api.getTags("name", "job");
        for(int i = 0; i < nameList.length; i++){
            if(pluginName.toLowerCase().equals(nameList[i].toLowerCase())){return nameList[i];}
        }
        return null;
        
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

        NXML buildPage = new NXML(link + "lastSuccessfulBuild/api/xml");
        String[] fileNames = buildPage.getTags("fileName");
        if(fileNames.length == 0){return null;}
        
        return link + "lastSuccessfulBuild/artifact/target/" + buildPage.getTags("fileName")[0];

    }
    
}
