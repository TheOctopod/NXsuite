package com.octopod.suite.build;

import com.octopod.utils.WebUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PluginDownload
{
	String downloadURL;
	String name;
	String version;
	File output;

	public PluginDownload(String downloadURL, String name, String version, File output) {
		this.downloadURL = downloadURL;
		this.name = name;
		this.version = version;
		this.output = output;
	}

	public String getName() {return name;}
	public String getVersion() {return version;}
	public File getOutput() {return output;}

	public void setOutput(File output) {this.output = output;}

	public void startDownload(final Finished finished)
	{
		new Thread() {
			public void run() {
				try
				{
					URL url = new URL(downloadURL);
					ReadableByteChannel bytes = WebUtils.URLtoRBC(url);
					if(bytes == null){return;}
					try (FileOutputStream os = new FileOutputStream(output)) {
						os.getChannel().transferFrom(bytes, 0, Long.MAX_VALUE);
					}
					if(finished != null) finished.finish(Result.SUCCESS);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					if(finished != null) finished.finish(Result.FAILURE);
				}
			}
		}.start();
	}

	public static interface Finished
	{
		public void finish(Result result);
	}

	public static enum Result
	{
		SUCCESS, FAILURE;
	}
}
