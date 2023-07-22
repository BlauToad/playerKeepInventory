package de.blautoad.playerkeepinventory.listeners;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class command implements CommandExecutor {
    private final NamespacedKey nk;

    public command(NamespacedKey nk){
        this.nk = nk;
    }
    public boolean onCommand(CommandSender sender, Command command1, String label, String[] args) {
        if (sender instanceof Player p) {
            PersistentDataContainer pdc = p.getPersistentDataContainer();
            byte keepInventory = 1;
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("false")) {
                    keepInventory = 0;
                } else if (pdc.has(nk, PersistentDataType.BYTE)) {
                    keepInventory = (byte)(keepInventory - pdc.get(nk, PersistentDataType.BYTE));
                }
            } else if (pdc.has(nk, PersistentDataType.BYTE)) {
                keepInventory = (byte)(keepInventory - pdc.get(nk, PersistentDataType.BYTE));
            }
            if (keepInventory < 0) {
                keepInventory = 0;
            } else if (keepInventory > 1) {
                keepInventory = 1;
            }
            pdc.set(nk, PersistentDataType.BYTE, keepInventory);
            if (keepInventory == 0) {
                p.sendMessage(ChatColor.RED + "Disabled" + ChatColor.RESET + " KeepInventory!");
            } else {
                p.sendMessage(ChatColor.GREEN + "Enabled" + ChatColor.RESET + " KeepInventory!");
            }
        }
        return true;
    }
}
