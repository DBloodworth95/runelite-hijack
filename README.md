Port of the RuneLiteHijack project maintained by Arnuh, his repository can be found [here](https://github.com/Arnuh/RuneLiteHijack).
**Not a detected 3rd party client.** Does not expand on RuneLites api like some RuneLite forks use to do.

# Installation

- Download latest runelite-hijack from the releases section.
- Find RuneLite install directory.
- Place `runelite-hijack-1.0.jar` in the same folder as `config.json`
- Open `config.json`
- Add a comma after `"RuneLite.jar"`
- Add `"runelite-hijack-1.0.jar"` after classPath entry `"RuneLite.jar"`
- Change mainClass to `com.runelitehijack.ClientHijackLauncher`
- Run RuneLite normally.

![example](https://im.arnah.ca/3cB8zf5ZaE.png)

If properly done, you should see "RuneLiteHijack Plugin Hub" in plugin configuration

![example](https://im.arnah.ca/Bn1tEIgJLC9rWGF.png)

# Adding additional Plugin Hubs

The default plugin hub is a GitHub repository viewable [here](https://github.com/Arnuh/RuneLiteHijack-PluginHub). You can use this as a template to create
your own plugin hub, while also using the provided plugin to customize additional plugin hubs by using a comma separated list.

For GitHub repositories, you need to make sure the url is a "raw githubusercontent" link like `https://raw.githubusercontent.
com/Arnuh/RuneLiteHijack-PluginHub/master/`.

# Structure of Plugin Hubs

Every Plugin Hub must have a `plugins.json` file at the root, which will list all available plugins. The "provider" value will be a subfolder which will
contain the `internalName.jar` file.

Description of each property for each plugin in `plugins.json`:

- "internalName" is the name of the plugin jar file, not including the extension.
- "hash" is the SHA-256 hash of the plugin jar file.
- "size" is the size of the plugin jar file in bytes.
- "plugins" is a list of main plugin classes
- "displayName" is the name of the plugin when displayed in the plugin hub.
- "provider" is the name of the subfolder containing the plugin jar file.

Example viewable [here](https://github.com/Arnuh/RuneLiteHijack-PluginHub/tree/master).

# How it works

Since RuneLite lets you add to the classpath and modify the main class, this project is able to proceed normally, while loading alongside RuneLite.

1. LauncherHijack launches the normal RuneLite Launcher and waits for the Client to start while scanning for the Client ClassLoader.
2. When the ClassLoader is found, LauncherHijack adds the RuneLiteHijack jar into the ClassLoader and creates ClientHijack.
3. ClientHijacks waits for the RuneLite Injector to be initialized and adds HijackedClient into the injector.
4. With HijackedClient being created in RuneLites classloader and being started via the Injector, RuneLiteHijack has full access to the same functionality as
   RuneLite.
5. With the access given, RuneLiteHijack initializes its Plugin Manager and adds the button to the original plugin ui.

Limitations are being restricted to what can be modified at runtime in java.
