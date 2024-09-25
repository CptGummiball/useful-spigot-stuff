# CommandManager

### Description

The `CommandManager` class simplifies the process of registering and managing commands in your Spigot plugin. It provides a user-friendly structure that allows developers to easily add new commands without extensive boilerplate code. This class centralizes command management, making it easier to maintain and expand your plugin's functionality.

### Usage

1. Copy the `CommandManager.java` file into your Spigot plugin project.
2. Create your command classes by implementing the `CommandExecutor` interface.
3. In your main plugin class, instantiate the `CommandManager` and register your commands using the `registerCommand` method.

### Example Code

```java
// Example of how to use the CommandManager in your plugin

package com.yourplugin;

import com.yourplugin.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        commandManager = new CommandManager(this);
        registerCommands();
    }

    private void registerCommands() {
        commandManager.registerCommand("example", new ExampleCommand());
        // Add more commands here as needed
    }
}
````

### Example Command Class
```java
package com.yourplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ExampleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("This is an example command!");
        return true;
    }
}
````
### NOTE
Ensure that you declare your commands in the ``plugin.yml`` file of your Spigot plugin, as shown below:
```yaml
commands:
  example:
    description: An example command
    usage: /<command>
````
