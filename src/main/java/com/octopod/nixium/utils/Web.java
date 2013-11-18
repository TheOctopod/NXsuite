
package com.octopod.nixium.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Web {
    
    private URL website;
    private boolean validURL;
    
    public Web(String url){
        try{
            website = new URL(url);
            validURL = true;
        }catch(MalformedURLException e){
            validURL = false;
        }
    }
    
    public boolean isValid(){
        return validURL;
    }
    
    public ReadableByteChannel getBytes(){
        try{
            return Channels.newChannel(website.openStream());
        }catch(IOException e){
            return null;
        }
    }
    
}
