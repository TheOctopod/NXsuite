package com.octopod.nixium.nxsuite.build;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.octopod.nixium.utils.WebUtils;

public class RepoBukGet extends PluginRepo {
	
	JSONArray json_plugins = null;
	JSONParser json_parser = new JSONParser();
	
	public RepoBukGet() {
		try {
			String api = WebUtils.URLtoString(new URL("http://api.bukget.org/3/plugins/bukkit/"));
			json_plugins = (JSONArray)json_parser.parse(api);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	@Override
	public boolean isRepoModifiable() {return false;}
	@Override
	public boolean isFilterModifiable() {return true;}
	@Override
	public boolean isPluginModifiable() {return true;}
	@Override
	public boolean isVersionModifiable() {return true;}

	@Override
	public String[] listFilters() {
		List<String> filters = new ArrayList<String>();
		try{
			JSONArray json_filters = (JSONArray)json_parser.parse(
					WebUtils.URLtoString(new URL("http://api.bukget.org/3/categories/bukkit/"))
			);
			for(int i = 0; i < json_filters.size(); i++){
				JSONObject filter_info = (JSONObject)json_parser.parse(json_plugins.get(i).toString());
				String filter_name = filter_info.get("name").toString();
				filters.add(filter_name);
			}
		} catch (Exception e) { e.printStackTrace(); }
		return filters.toArray(new String[filters.size()]);
	}
	
	@Override
	public String[] listPlugins() {

		List<String> plugins = new ArrayList<String>();
		try {
			for(int i = 0; i < json_plugins.size(); i++){
				JSONObject plugin_info = (JSONObject)json_parser.parse(json_plugins.get(i).toString());
				String plugin_name = plugin_info.get("plugin_name").toString();
				if(plugin_name.equalsIgnoreCase("")){
					plugins.add(plugin_info.get("slug").toString());
				}else{
					plugins.add(plugin_name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plugins.toArray(new String[plugins.size()]);

	}
	
	@Override
	public String[] listVersions() {

		List<String> plugins = new ArrayList<String>();
		try {
			for(int i = 0; i < json_plugins.size(); i++){
				JSONObject plugin_info = (JSONObject)json_parser.parse(json_plugins.get(i).toString());
				String plugin_name = plugin_info.get("plugin_name").toString();
				if(plugin_name.equalsIgnoreCase("")){
					plugins.add(plugin_info.get("slug").toString());
				}else{
					plugins.add(plugin_name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plugins.toArray(new String[plugins.size()]);

	}
	
	@Override
	public boolean isRepoValid() {return true;}
	
	@Override
	public boolean isFilterValid() {return true;}

	@Override
	public boolean isPluginValid() {
		
		if(plugin == null) return false;
		
		try {
			for(int i = 0; i < json_plugins.size(); i++){
				JSONObject plugin_info = (JSONObject)json_parser.parse(json_plugins.get(i).toString());
				if(plugin_info.get("plugin_name").toString().equalsIgnoreCase(plugin)) return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	public boolean isVersionValid() {return true;}

	@Override
	public String getPluginName() {
		if(plugin == null) return null;
		try {
			for(int i = 0; i < json_plugins.size(); i++){
				JSONObject plugin_info = (JSONObject)json_parser.parse(json_plugins.get(i).toString());
				String plugin_name = plugin_info.get("plugin_name").toString();
				if(plugin_name.equalsIgnoreCase(plugin)) return plugin_name;
			}
		} catch (Exception e) {e.printStackTrace();}
		
		return plugin;
	}
	
	@Override
	public String getTypeName() {return "BukGet";}

	@Override
	public String getFilterName() {
		return null;
	}

	@Override
	public String getVersionName() {
		return null;
	}
	@Override
	public String getDownload() {
		
		if(plugin == null) return null;
		
		try {	
			String slug = null;
			for(int i = 0; i < json_plugins.size(); i++){
				JSONObject plugin_info = (JSONObject)json_parser.parse(json_plugins.get(i).toString());
				if(plugin_info.get("plugin_name").toString().equalsIgnoreCase(plugin)) { 
					slug = plugin_info.get("slug").toString();
					break;
				}
			}
			if(slug == null) return null;
	
			JSONObject plugin_info = 
					(JSONObject)json_parser.parse(WebUtils.URLtoString(new URL("http://api.bukget.org/3/plugins/bukkit/" + slug + "/")));
			JSONArray plugin_versions = (JSONArray)plugin_info.get("versions");
			if(plugin_versions.size() < version) return null;
			
			JSONObject plugin_version = (JSONObject)json_parser.parse(plugin_versions.get(version).toString());
			return plugin_version.get("download").toString();

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void setRepo(String repo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFilter(String filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlugin(String plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOutput(String output) {
		// TODO Auto-generated method stub
		
	}

}
