package com.octopod.suite.build;

import com.octopod.suite.PluginSuite;

import java.io.File;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class PluginFinderSession
{
	private PluginFinder finder = PluginSuite.getPluginFinder("bukget");

	public void changeFinder(PluginFinder finder)
	{
		this.finder = finder;
	}

	public PluginFinder getFinder() {return finder;}

	public PluginDownload download = null;

	public String getSource() {
		return finder.getSource();
	}

	public void setSource(String URL) throws PluginFinderException
	{
		if(finder.lockedSource()) throw new PluginFinderException("You can't change the source of this plugin finder!");
		finder.setSource(URL);
	}

	public void findPlugin(String pluginName) throws PluginFinderException
	{
		download = finder.findPlugin(pluginName);
		if(download == null) throw new PluginFinderException("A plugin could not be found!");
	}

	public void findPlugin(String pluginName, String versionName) throws PluginFinderException
	{
		download = finder.findPlugin(pluginName, versionName);
		if(download == null) throw new PluginFinderException("A plugin could not be found!");
	}

	public void setOutputFile(String outputFile) throws PluginFinderException
	{
		if(download == null) throw new PluginFinderException("Find a plugin before setting the output!");
		download.setOutput(new File(outputFile));
	}

	public void startDownload(final PluginDownload.Finished finish) throws PluginFinderException
	{
		if(download == null) throw new PluginFinderException("Find a plugin before starting a download!");
		download.startDownload(finish);
	}

}
