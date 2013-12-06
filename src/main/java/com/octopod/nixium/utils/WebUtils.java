package com.octopod.nixium.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils {
	
	public static ReadableByteChannel URLtoRBC(URL url){
        try{
            return Channels.newChannel(url.openStream());
        }catch(IOException e){
            return null;
        }
	}
	
	public static String URLtoString(URL url){
		try {
			URLConnection con = url.openConnection();
			
			Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
			Matcher m = p.matcher(con.getContentType());

			String charset = m.matches()?m.group(1):"UTF-8";
			
			Reader r = new InputStreamReader(con.getInputStream(), charset);
			
			StringBuilder buf = new StringBuilder();
			while (true) {
			  int ch = r.read();
			  if (ch < 0) break;
			  buf.append((char) ch);
			}
			return buf.toString();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
