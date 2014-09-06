
package com.octopod.suite.build;

import java.util.List;

public interface PluginFinder {

	public String getName();

	public boolean setSource(String URL);
	public String getSource();
	public boolean lockedSource();

	public List<String> listPluginNames(String pluginName);

	public List<String> listPluginNames();

	public List<String> listPluginVersionNames(String pluginKey);

	public PluginDownload findPlugin(String pluginName);

	/**
	 * Returns a PluginDownload of a plugin, given the plugin key and version key
	 * @param pluginKey
	 * @param versionKey
	 * @return
	 */
    public PluginDownload findPlugin(String pluginKey, String versionKey);

}