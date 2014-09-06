
package com.octopod.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
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
    
    public int getResponseCode(){
        HttpURLConnection connection;
		try {
			connection = (HttpURLConnection)website.openConnection();
	        connection.setRequestMethod("GET");
	        connection.connect();
	        return connection.getResponseCode();   
		}catch (IOException e) {
			return 404;
		}
    }

    public ReadableByteChannel getBytes(){
        try{
            return Channels.newChannel(website.openStream());
        }catch(IOException e){
            return null;
        }
    }
    
}
