package de.blautoad.playerkeepinventory;

import de.blautoad.playerkeepinventory.listeners.command;
import de.blautoad.playerkeepinventory.listeners.deathListener;
import de.blautoad.playerkeepinventory.listeners.tab;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    public void onEnable() {
        PluginManager manager = Bukkit.getPluginManager();
        NamespacedKey nk = new NamespacedKey(this, "keepinventory");
        manager.registerEvents(new deathListener(nk), this);
        getCommand("keepinventory").setExecutor(new command(nk));
        getCommand("keepinventory").setTabCompleter(new tab());

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "KeepInventoryToggle enabled!");
    }

    public void onDisable() {
        super.onDisable();
    }
}
