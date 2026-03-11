package de.blautoad.playerkeepinventory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.blautoad.playerkeepinventory.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class command implements CommandExecutor {
    private final NamespacedKey nk;

    public command(NamespacedKey nk) {
        this.nk = nk;
    }

    public boolean onCommand(CommandSender sender, Command command1, String label, String[] args) {
        if (sender instanceof Player p) {
            if (check(sender, args)) {
                return true;
            }
            PersistentDataContainer pdc = p.getPersistentDataContainer();
            byte keepInventory = 1;
            if (args.length >= 1) {
                if (setOrGetDefault(sender, args)) {
                    return true;
                } else if (reloadConfig(sender, args)) {
                    return true;
                } else if (!args[0].equalsIgnoreCase("true")) {
                    if (args[0].equalsIgnoreCase("false")) {
                        keepInventory = 0;
                    } else if (pdc.has(nk, PersistentDataType.BYTE)) {
                        keepInventory = (byte) (keepInventory - pdc.get(nk, PersistentDataType.BYTE));
                    } else {
                        keepInventory = (byte) (Main.getKeepInventoryDefaultState() ? 0 : 1);
                    }
                }
            } else if (pdc.has(nk, PersistentDataType.BYTE)) {
                keepInventory = (byte) (keepInventory - pdc.get(nk, PersistentDataType.BYTE));
            } else {
                keepInventory = (byte) (Main.getKeepInventoryDefaultState() ? 0 : 1);
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
        } else {
            if (check(sender, args)) {
                return true;
            }
        }
        return true;
    }

    private boolean isElevated(CommandSender sender) {
        boolean sender_player = sender instanceof Player;
        if (sender_player && !((Player) sender).isOp()) {
            return false;
        }
        boolean sender_console = sender instanceof ConsoleCommandSender;
        if (!(sender_player || sender_console)) {
            return false;
        }
        return true;
    }

    private boolean check(CommandSender sender, String[] args) {
        if (!(args.length == 2 && Objects.equals(args[0], "check"))) {
            return false;
        }

        if (!isElevated(sender)) {
            return false;
        }

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            players.add(op.getPlayer());
        }

        Player p_data = null;
        for (Player p : players) {
            if (p.getName().equals(args[1])) {
                p_data = p;
                break;
            }
        }

        if (p_data != null) {
            PersistentDataContainer pdc = p_data.getPersistentDataContainer();
            byte status = -1;
            if (pdc.has(nk, PersistentDataType.BYTE)) {
                status = pdc.get(nk, PersistentDataType.BYTE);
            }

            if (status == 0) {
                sender.sendMessage(ChatColor.UNDERLINE + p_data.getName() + ChatColor.RESET + " has KeepInventory "
                        + ChatColor.RED + "disabled" + ChatColor.RESET + "!");
            } else if (status == 1) {
                sender.sendMessage(ChatColor.UNDERLINE + p_data.getName() + ChatColor.RESET + " has KeepInventory "
                        + ChatColor.GREEN + "enabled" + ChatColor.RESET + "!");
            } else if (status == -1) {
                sender.sendMessage(ChatColor.UNDERLINE + p_data.getName() + ChatColor.RESET + " has KeepInventory "
                        + ChatColor.GRAY + "not set "
                        + "(default: " + (Main.getKeepInventoryDefaultState() ? ChatColor.GREEN : ChatColor.RED)
                        + (Main.getKeepInventoryDefaultState() ? "enabled" : "disabled") + ChatColor.GRAY + ")"
                        + ChatColor.RESET + "!");
            }
        } else {
            sender.sendMessage(
                    "Player with the Name " + ChatColor.UNDERLINE + args[1] + ChatColor.RESET + " doesn't exist!");
        }

        return true;
    }

    private boolean setOrGetDefault(CommandSender sender, String[] args) {
        if (!args[0].equalsIgnoreCase("default")) {
            return false;
        }

        if (!isElevated(sender)) {
            return false;
        }

        if (args.length < 3 || args[1].equalsIgnoreCase("get")) {
            sender.sendMessage(
                    "KeepInventory is " + (Main.getKeepInventoryDefaultState() ? ChatColor.GREEN + "enabled"
                            : ChatColor.RED + "disabled") + ChatColor.RESET + " by default!");
            return true;
        }

        if (!args[1].equalsIgnoreCase("set")) {
            sender.sendMessage(ChatColor.GOLD + "[ERROR] Please specify the operation either get or set!");
            return true;
        }

        if (args[2].equalsIgnoreCase("true")) {
            Main.setKeepInventoryDefaultState(true);
            sender.sendMessage(ChatColor.GREEN + "Enabled" + ChatColor.RESET + " KeepInventory by default!");
        } else if (args[2].equalsIgnoreCase("false")) {
            Main.setKeepInventoryDefaultState(false);
            sender.sendMessage(ChatColor.RED + "Disabled" + ChatColor.RESET + " KeepInventory by default!");
        } else {
            sender.sendMessage(ChatColor.GOLD + "[ERROR] Please specify the default as either true or false!");
        }
        return true;
    }

    private boolean reloadConfig(CommandSender sender, String[] args) {
        if (!args[0].equalsIgnoreCase("reload")) {
            return false;
        }

        if (!isElevated(sender)) {
            return false;
        }
        Main.reloadMyConfig();
        sender.sendMessage(ChatColor.GOLD + "Config reloaded!");

        String[] a = { "default", "get" };
        setOrGetDefault(sender, a);

        return true;
    }

}
