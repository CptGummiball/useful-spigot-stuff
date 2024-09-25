package com.yourplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final JavaPlugin plugin;
    private final Map<String, CommandExecutor> commands;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
    }

    public void registerCommand(String name, CommandExecutor executor) {
        if (!commands.containsKey(name)) {
            commands.put(name, executor);
            plugin.getCommand(name).setExecutor(executor);
        } else {
            plugin.getLogger().warning("Command " + name + " is already registered.");
        }
    }

    public void unregisterCommand(String name) {
        if (commands.containsKey(name)) {
            commands.remove(name);
            // Optionally set executor to null if needed
            plugin.getCommand(name).setExecutor(null);
        } else {
            plugin.getLogger().warning("Command " + name + " is not registered.");
        }
    }

    public void handleCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandExecutor executor = commands.get(command.getName());
        if (executor != null) {
            executor.onCommand(sender, command, label, args);
        } else {
            sender.sendMessage("Unbekannter Befehl.");
        }
    }
}
