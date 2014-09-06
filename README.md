PluginSuite
======
# By Octopod

A plugin manager that doubles as a plugin downloader and updater.

Goals
----
- [ ] Add permission checks
- [ ] Add list commands
- [ ] Add update command

- [ ] Decide on a final interface for PluginFinder
- [ ] Organize utility classes

Commands
------

`/suite enable <plugin>` - Enables a plugin.
`/suite disable <plugin>` - Disables a plugin.
`/suite load <plugin>` - Loads a plugin.
`/suite unload <plugin>` - "Unloads" a plugin.

`/suite finder [finder]` - Views info about/changes the current finder (currently http://api.bukget.org).
`/suite find <name> [version]` - Finds the plugin with the closest name match (and version match). This will prepare a download.
`/suite download [start/output] [file]` - Views info about/starts/changes the output of the currently prepared download.

Planned features include:

`/suite update <plugin>` - Will temporarily download the latest version of a plugin from a finder, then compares it with the currently enabled plugin.
`/suite list plugin [keyword]` - Will list all (or matching the keyword) plugins from the current finder.
`/suite list version <plugin>` - Will list all versions from a plugin name.

