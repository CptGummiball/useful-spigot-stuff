# Translator

## Description
The ``Translator`` class allows Spigot plugins to load translations from ``YAML`` files stored in the ``/lang`` folder of the plugin. Translations are loaded based on the file name (e.g., ``de-DE.yaml`` for German or ``en-US.yaml`` for English). This is useful for integrating multilingual support into your plugin, enhancing the user experience for players from various language regions.

## Usage
1. Copy the ``Translator.java`` file into your Spigot plugin project.
2. Ensure you create the necessary ``YAML`` files (e.g., ``de-DE.yaml`` and ``en-US.yaml``) in the ``/lang`` folder of your plugin.
3. Integrate the ``Translator`` class into your plugin by creating an instance with the desired language code.
```java
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin {
    private Translator translator;

    @Override
    public void onEnable() {
        // Set the desired language, e.g., based on the player's language
        String playerLanguage = "de-DE"; // Example for the chosen language
        translator = new Translator(this, playerLanguage);

        // Example of using the translations
        String welcomeMessage = translator.translate("welcome");
        getLogger().info(welcomeMessage);
    }
}
````
## YAML File Structure
```bash
/your-plugin
 ├─ /lang
 │   ├─ de-DE.yaml
 │   └─ en-US.yaml
 ├─ plugin.yml
 └─ ...
````
## Example of a YAML File
### de-DE.yaml
```yaml
welcome: "Willkommen im Server!"
goodbye: "Auf Wiedersehen!"
````
### en-US.yaml
```yaml
welcome: "Welcome to the server!"
goodbye: "Goodbye!"
````

With this ``Translator`` class, you can make your Spigot plugins multilingual and provide your players with a user-friendly experience.
