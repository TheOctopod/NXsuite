package com.octopod.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class WebUtils {
	
	public static ReadableByteChannel URLtoRBC(URL url){
        try{
            return Channels.newChannel(url.openStream());
        }catch(IOException e){
            return null;
        }
	}

	public static String downloadWebpage(String url) {
		try {
			return downloadWebpage(new URL(url));
		} catch (MalformedURLException e) {
			//Not a valid URL
			return null;
		}
	}
	
	public static String downloadWebpage(URL url){

		InputStream is = null;
		BufferedReader br;

		try {
//			Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
//			Matcher m = p.matcher(con.getContentType());
//
//			String charset = m.matches()?m.group(1):"UTF-8";
//
//			Reader r = new InputStreamReader(con.getInputStream(), charset);

			is = url.openStream();
			br = new BufferedReader(new InputStreamReader(is));

			int ch;
			StringBuilder buf = new StringBuilder();

			while((ch = br.read()) != -1)
				buf.append((char)ch);

			return buf.toString();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if(is != null) is.close();
			} catch (IOException e) {}
		}
	}

}
