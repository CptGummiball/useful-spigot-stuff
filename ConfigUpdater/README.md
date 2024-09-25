# ConfigUpdater

### Description

This class helps you update your plugin's `config.yml` file when you release a new version of your plugin. It ensures that new configuration options are added while preserving any custom user settings from the previous configuration file.

### Usage

1. Copy the `ConfigUpdater.java` file into your Spigot plugin project.
2. In your main plugin class, call the `updateConfig()` method, typically inside the `onEnable()` method, to ensure the config file is updated when the plugin starts.

### Example Code

```java
@Override
public void onEnable() {
    ConfigUpdater configUpdater = new ConfigUpdater(this);
    configUpdater.updateConfig();
}
````
### Notes
- This class assumes that your ``config.yml`` file has a ``version`` key to track the configuration version.
- Update the ``currentVersion`` string in the class to reflect the correct version of your plugin.
- If the config version is outdated or missing, the class will update the config while keeping the old values intact.

---
This should allow for seamless implementation into your Spigot plugin project. Let me know if you need further customization!
