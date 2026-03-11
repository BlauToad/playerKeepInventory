package de.blautoad.playerkeepinventory.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class tab implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = null;
        if (args.length == 1) {
            String[] l = null;
            if (sender.isOp()) {
                l = new String[] { "true", "false", "toggle", "check", "default", "reload" };
            } else {
                l = new String[] { "true", "false", "toggle" };
            }
            String input = args[0].toLowerCase();
            completions = new ArrayList<>();
            for (String s : l) {
                if (s.startsWith(input))
                    completions.add(s);
            }
            if (completions.size() == 0) {
                completions.add("toggle");
            } else {
                Collections.sort(completions);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("default")) {
            String[] l = null;
            if (sender.isOp()) {
                l = new String[] { "set", "get" };
            }
            String input = args[1].toLowerCase();
            completions = new ArrayList<>();
            for (String s : l) {
                if (s.startsWith(input))
                    completions.add(s);
            }
            if (completions.size() == 0) {
                completions.add("get");
            } else {
                Collections.sort(completions);
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("default") && args[1].equalsIgnoreCase("set")) {
            String[] l = null;
            if (sender.isOp()) {
                l = new String[] { "true", "false" };
            }
            String input = args[2].toLowerCase();
            completions = new ArrayList<>();
            for (String s : l) {
                if (s.startsWith(input))
                    completions.add(s);
            }
            if (completions.size() == 0) {
                for (String s : l) {
                    completions.add(s);
                }
            }
            Collections.sort(completions);
        }
        return completions;
    }
}
