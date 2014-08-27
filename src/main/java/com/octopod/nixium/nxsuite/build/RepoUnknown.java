package com.octopod.nixium.nxsuite.build;

public class RepoUnknown extends PluginRepo{

	@Override
	public boolean isRepoModifiable() {return false;}

	@Override
	public boolean isFilterModifiable() {return false;}

	@Override
	public boolean isPluginModifiable() {return false;}

	@Override
	public boolean isVersionModifiable() {return false;}

	@Override
	public String[] listFilters() {return null;}

	@Override
	public String[] listPlugins() {return null;}

	@Override
	public String[] listVersions() {return null;}

	@Override
	public boolean isRepoValid() {return false;}

	@Override
	public boolean isFilterValid() {return false;}

	@Override
	public boolean isPluginValid() {return false;}

	@Override
	public boolean isVersionValid() {return false;}

	@Override
	public void setRepo(String repo) {}

	@Override
	public void setFilter(String filter) {}

	@Override
	public void setPlugin(String plugin) {}

	@Override
	public void setVersion(int version) {}

	@Override
	public void setOutput(String output) {}
	
	@Override
	public String getTypeName() {return "Unknown";}
	
	@Override
	public String getFilterName() {return null;}

	@Override
	public String getPluginName() {return null;}

	@Override
	public String getVersionName() {return null;}

	@Override
	public String getDownload() {return null;}

}