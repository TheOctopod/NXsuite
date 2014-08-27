
package com.octopod.nixium.nxsuite.build;

import com.octopod.nixium.utils.Web;

public abstract class PluginRepo {
	
	static final public int TYPE_UNKNOWN = 0;
	static final public int TYPE_JENKINS = 1;
	static final public int TYPE_BUKKIT = 2;
	
	public int type = TYPE_UNKNOWN;
	public String repo = null;
	public String filter = null;
	public String plugin = null;
	public int version = 0;
	public String output = "plugin.jar";

	abstract public boolean isRepoModifiable();
	abstract public boolean isFilterModifiable();
	abstract public boolean isPluginModifiable();
	abstract public boolean isVersionModifiable();
	
    //Gets all the avaliable builds from this website
    abstract public String[] listFilters();
    abstract public String[] listPlugins();
    abstract public String[] listVersions();
    
    //Gets if a field is valid
    abstract public boolean isRepoValid();
    abstract public boolean isFilterValid();
    abstract public boolean isPluginValid();
    abstract public boolean isVersionValid();

    abstract public void setRepo(String repo);
    abstract public void setFilter(String filter);
    abstract public void setPlugin(String plugin);
    abstract public void setVersion(int version);
    abstract public void setOutput(String output);
    
    //Corrects a plugin's name, if possible
    abstract public String getTypeName();
    abstract public String getFilterName();
    abstract public String getPluginName();
    abstract public String getVersionName();
    //Gets the latest plugin download link, given the plugin's name
    abstract public String getDownload();
    
    
    static public PluginRepo instance(int type){
    	PluginRepo instance;
        switch(type){
            case TYPE_JENKINS:
                instance = new RepoUnknown();
                break;
            case TYPE_BUKKIT:
            	instance = new RepoBukGet();
            	break;
            default:
                instance = new RepoUnknown();
                break;
        }
        instance.type = type;
        return instance;
    }
    
    public String getOutput() 	{return output;}
    public int getType() 		{return type;}
    public String getRepo() 	{return repo;}
    public String getFilter() 	{return filter;}
    public String getPlugin() 	{return plugin;}
    public int getVersion() 	{return version;}
    
   
    
    public void rawSetFilter(String filter){this.filter = filter;}
    public void rawSetType(int type){this.type = type;}
    public void rawSetRepo(String repo) {this.repo = repo;}
    
    public void autoSelectType(){

    	if(getType() == TYPE_BUKKIT){return;}
    	Web site = new Web("http://" + repo + "/rssAll");
    	if(site.getResponseCode() == 200) {
    		type = TYPE_JENKINS;
    		return;
    	};
    	type = TYPE_UNKNOWN;
    	
    }
    
    public PluginRepo toRepo(int type){
    	PluginRepo instance;
        switch(type){
            case TYPE_JENKINS:
                instance = new RepoUnknown();
                break;
            case TYPE_BUKKIT:
            	instance = new RepoBukGet();
            	break;
            default:
                instance = new RepoUnknown();
                break;
        }
        instance.type = type;
        instance.repo = this.getRepo();
        instance.filter = this.getFilter();
        instance.plugin = this.getPlugin();
        instance.version = this.getVersion();
        instance.output = this.getOutput();
        return instance;
    }
      
}