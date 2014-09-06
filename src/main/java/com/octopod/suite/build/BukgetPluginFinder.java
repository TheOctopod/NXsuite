package com.octopod.suite.build;

import com.octopod.utils.StringUtils;
import com.octopod.utils.WebUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BukgetPluginFinder implements PluginFinder {

	JSONParser parser = new JSONParser();

	Map<String, String> pluginMap;

	private String jGetString(JSONObject obj, String key) {
		Object o = obj.get(key);
		if(o instanceof String) return (String)o;
		return null;
	}

	public JSONArray jGetArray(JSONObject obj, String key) {
		Object o = obj.get(key);
		if(o instanceof JSONArray) return (JSONArray)o;
		return null;
	}

	public String getName() {return "BukGet";}
	
	public BukgetPluginFinder() {
		pluginMap = new HashMap<>();
		try {
			//Create plugin map
			JSONArray jPluginArray = (JSONArray)parser.parse(WebUtils.downloadWebpage("http://api.bukget.org/3/plugins/bukkit?fields=slug,plugin_name"));
			for(Object o: jPluginArray) {
				if(!(o instanceof JSONObject)) continue;
				JSONObject jPluginObject = ((JSONObject)o);
				pluginMap.put(jPluginObject.get("slug").toString(), jPluginObject.get("plugin_name").toString());
			}
		} catch (Exception e) {e.printStackTrace();}
}

	public boolean lockedSource() {return true;}
	public boolean setSource(String URL) {return false;}
	public String getSource() {return "http://api.bukget.org/";}

	private JSONObject jPluginInfo(String pluginName) {
		String pluginKey = getPluginKeyByName(pluginName);
		try {
			return (JSONObject)parser.parse(WebUtils.downloadWebpage("http://api.bukget.org/3/plugins/bukkit/" + pluginKey + "/"));
		} catch (ClassCastException | ParseException e) {
			return null;
		}
	}

	@Override
	public PluginDownload findPlugin(String pluginName)
	{
		JSONObject jPluginInfo = jPluginInfo(pluginName);
		return findPlugin(jPluginInfo, 0);
	}

	@Override
	public PluginDownload findPlugin(String pluginName, String versionName)
	{
		JSONObject jPluginInfo = jPluginInfo(pluginName);
		int versionKey = getVersionByName(jPluginInfo, versionName);
		if(versionKey < 0) return null;
		return findPlugin(jPluginInfo, versionKey);
	}

	private PluginDownload findPlugin(JSONObject jPluginInfo, int versionKey) {
		String pluginName = jPluginInfo.get("plugin_name").toString();
		JSONArray jPluginVersions = (JSONArray)jPluginInfo.get("versions");
		JSONObject jPluginVersion;
		try {
			jPluginVersion = (JSONObject)parser.parse(jPluginVersions.get(versionKey).toString());
			String downloadURL = jPluginVersion.get("download").toString();
			String versionName = jPluginVersion.get("slug").toString();
			File outputFile = new File(jPluginVersion.get("filename").toString());
			return new PluginDownload(downloadURL, pluginName, versionName, outputFile);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> listPluginNames(String pluginKey)
	{
		return listPluginNames();
	}

	@Override
	public List<String> listPluginNames()
	{
		return new ArrayList<>(pluginMap.values());
	}

	@Override
	public List<String> listPluginVersionNames(String pluginName)
	{
		JSONObject jPluginInfo = jPluginInfo(pluginName);
		JSONArray jPluginVersions = (JSONArray)jPluginInfo.get("versions");

		List<String> versions = new ArrayList<>();
		for(Object o: jPluginVersions) {
			JSONObject obj = (JSONObject)o;
			versions.add(obj.get("slug").toString());
		}

		return versions;
	}

	private String getPluginKeyByName(String pluginName) {
		Integer minDistance = null;
		String closestPluginKey = null;

		for(Entry<String, String> e: pluginMap.entrySet()) {
			//Get the distance from the provided plugin name to the plugin name in the entry
			int distance = StringUtils.levenshteinDistance(e.getValue(), pluginName);
			if(minDistance == null || distance < minDistance) {
				minDistance = distance;
				closestPluginKey = e.getKey();
			}
		}
		return closestPluginKey;
	}

	private int getVersionByName(JSONObject jPluginInfo, String versionName)
	{
		if(jPluginInfo == null) return -1;
		JSONArray jPluginVersions = (JSONArray)jPluginInfo.get("versions");

		Integer minDistance = null;
		int closestVersionKey = -1;

		for(int i = 0; i < jPluginVersions.size(); i++)
		{
			Object o = jPluginVersions.get(i);
			if(!(o instanceof JSONObject)) continue;
			JSONObject jPluginVersion = (JSONObject)o;
			//The version "name"
			String slug = jPluginVersion.get("slug").toString();

			int distance = StringUtils.levenshteinDistance(slug, versionName);
			if(minDistance == null || distance < minDistance)
			{
				minDistance = distance;
				closestVersionKey = i;
			}
		}
		return closestVersionKey;
	}
}
