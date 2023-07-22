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
            String[] l = new String[]{"true", "false", "toggle"};
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
        }
        return completions;
    }
}
