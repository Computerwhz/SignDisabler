package com.computerwhz.signdisabler.commands;

import com.computerwhz.signdisabler.SignDisabler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignDisablerCommand implements TabExecutor {
    String[] arg1Completions = { "enable", "disable", "globaldisable", "player" };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /signdisabler <enable|disable|globaldisable|player>");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "enable" -> {
                SignDisabler.getInstance().setPluginEnabled(true);
                sender.sendMessage(ChatColor.GREEN + "Enabled " + ChatColor.WHITE + "Sign disabler");
            }

            case "disable" -> {
                SignDisabler.getInstance().setPluginEnabled(false);
                sender.sendMessage(ChatColor.RED + "Disabled " + ChatColor.WHITE + "Sign disabler");
            }

            case "globaldisable" -> {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /signdisabler globaldisable <true|false>");
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "true" -> {
                        SignDisabler.getInstance().setGloballyDisabled(true);

                        sender.sendMessage(
                                ChatColor.GREEN + "Enabled " +
                                        ChatColor.WHITE + "global sign disablement"
                        );
                    }

                    case "false" -> {
                        SignDisabler.getInstance().setGloballyDisabled(false);

                        sender.sendMessage(
                                ChatColor.RED + "Disabled " +
                                        ChatColor.WHITE + "global sign disablement"
                        );
                    }

                    default -> sender.sendMessage(
                            ChatColor.RED + "You must specify true or false."
                    );
                }
            }

            case "player" -> {
                if (args.length < 2) {
                    sender.sendMessage("Usage: /signdisabler player <add|list|remove>");
                    return true;
                }

                switch (args[1].toLowerCase()) {

                    case "add" -> {
                        if (args.length < 3) {
                            sender.sendMessage("Usage: /signdisabler player add <player>");
                            return true;
                        }

                        Player player = Bukkit.getPlayerExact(args[2]);

                        if (player == null) {
                            sender.sendMessage(
                                    ChatColor.RED + "Could not get player " +
                                            ChatColor.WHITE + args[2]
                            );
                            return true;
                        }

                        boolean alreadyDisabled = SignDisabler.getInstance()
                                .getDisabledPlayers()
                                .stream()
                                .anyMatch(p -> p.getUniqueId().equals(player.getUniqueId()));

                        if (alreadyDisabled) {
                            sender.sendMessage(
                                    ChatColor.RED + "Player " +
                                            ChatColor.WHITE + player.getName() +
                                            ChatColor.RED + " is already disabled."
                            );
                            return true;
                        }

                        SignDisabler.getInstance().addDisabledPlayer(player);

                        sender.sendMessage(
                                ChatColor.GREEN + "Successfully added player " +
                                        ChatColor.WHITE + player.getName() +
                                        ChatColor.GREEN + " to sign disabled players"
                        );
                    }

                    case "list" -> {
                        StringBuilder output = new StringBuilder(
                                ChatColor.GRAY.toString() +
                                        ChatColor.BOLD +
                                        "Disabled players:" +
                                        ChatColor.RESET +
                                        "\n"
                        );

                        for (Player player : SignDisabler.getInstance().getDisabledPlayers()) {
                            output.append(player.getName()).append("\n");
                        }

                        sender.sendMessage(output.toString());
                    }

                    case "remove" -> {
                        if (args.length < 3) {
                            sender.sendMessage("Usage: /signdisabler player remove <player>");
                            return true;
                        }

                        Player player = SignDisabler.getInstance()
                                .getDisabledPlayers()
                                .stream()
                                .filter(p -> p.getName().equalsIgnoreCase(args[2]))
                                .findFirst()
                                .orElse(null);

                        if (player == null) {
                            sender.sendMessage(
                                    ChatColor.RED + "Could not find disabled player " +
                                            ChatColor.WHITE + args[2]
                            );
                            return true;
                        }

                        SignDisabler.getInstance().removeDisabledPlayer(player);

                        sender.sendMessage(
                                ChatColor.GREEN + "Removed player " +
                                        ChatColor.WHITE + player.getName() +
                                        ChatColor.GREEN + " from sign disabled players"
                        );
                    }

                    default -> sender.sendMessage(
                            "Usage: /signdisabler player <add|list|remove>"
                    );
                }
            }

            default -> sender.sendMessage(
                    "Usage: /signdisabler <enable|disable|globaldisable|player>"
            );
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return Arrays.stream(arg1Completions)
                    .toList();
        }

        if (args.length == 1) {
            String prefix = args[0];
            return Arrays.stream(arg1Completions).
                    filter(s -> s.startsWith(prefix))
                    .toList();
        }

        if (args.length == 2) {
            List<String> completions = new ArrayList<>();
            
            switch (args[0]) {
                case "globaldisable" -> {
                    completions = List.of("true", "false");
                }
                case "player" -> {
                    completions = List.of("add", "list", "remove");
                }
            }

            String prefix = args[1];
            return completions.stream()
                    .filter(s -> s.startsWith(prefix))
                    .toList();
        }
        
        if (args.length == 3) {
            if (args[1].equals("add")) {
                String prefix = args[2];
                return Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .filter(s -> s.startsWith(prefix))
                        .toList();
            }

            if (args[1].equals("remove")) {
                String prefix = args[2];
                return SignDisabler.getInstance().getDisabledPlayers()
                        .stream()
                        .map(Player::getName)
                        .filter(s -> s.startsWith(prefix))
                        .toList();
            }
        }

        return List.of();
    }
}
