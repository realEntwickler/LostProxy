/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * TeamCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TeamCommand extends Command implements TabExecutor {
    public TeamCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer player) {

            if (strings.length == 0) {
                sendHelpMessage(player);
            } else if (strings.length == 1) {
                IPlayer iPlayer = new IPlayer(player.getUniqueId());
                switch (strings[0]) {
                    case "login":
                        if (player.hasPermission("lostproxy.command.team.login")) {
                            if (!LostProxy.getInstance().getTeamManager().isLoggedIn(player)) {
                                if (LostProxy.getInstance().getTeamManager().login(player)) {
                                    LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder($.TMS + iPlayer.getDisplay() + player.getName() + " §7hat sich §aeingeloggt§7.").build()));
                                } else {
                                    player.sendMessage(new MessageBuilder($.TMS + "§7Es ist ein §4Fehler §7aufgetreten.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.TMS + "Du bist §cbereits §7eingeloggt.").build());
                            }
                        } else {
                            player.sendMessage($.NO_PERMISSION($.TMS));
                        }
                        break;
                    case "logout":
                        if (player.hasPermission("lostproxy.command.team.logout")) {
                            if (LostProxy.getInstance().getTeamManager().isLoggedIn(player)) {
                                if (LostProxy.getInstance().getTeamManager().logout(player)) {
                                    player.sendMessage(new MessageBuilder($.TMS + "Du wurdest §causgeloggt§7.").build());
                                    LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder($.TMS + iPlayer.getDisplay() + player.getName() + " §7hat sich §causgeloggt§7.").build()));
                                } else {
                                    player.sendMessage(new MessageBuilder($.TMS + "§7Es ist ein §4Fehler §7aufgetreten.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.TMS + "Du bist §cnicht §7eingeloggt.").build());
                            }
                        } else {
                            player.sendMessage($.NO_PERMISSION($.TMS));
                        }
                        break;
                    case "list":
                        if (player.hasPermission("lostproxy.command.team.list")) {
                            player.sendMessage(new MessageBuilder($.TMS + "Übersicht der Teammitglieder§8:").build());
                            ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.hasPermission("lostproxy.command.team")).sorted(Comparator.comparingInt(one -> new IPlayer(one.getUniqueId()).getSortId())).forEach(all -> {
                                IPlayer allIPlayer = new IPlayer(all.getUniqueId());
                                player.sendMessage(new MessageBuilder("§6" + $.littleDot + " " + allIPlayer.getDisplaywithPlayername() + " §8" + $.arrow + " " + (LostProxy.getInstance().getTeamManager().isLoggedIn(all) ? "§a✔" : "§c✖") + " §8" + $.arrow + " §7" + all.getServer().getInfo().getName()).build());
                            });
                        } else {
                            player.sendMessage(new MessageBuilder($.TMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen.").build());
                        }
                        break;
                    default:
                        player.sendMessage($.NO_PERMISSION($.TMS));
                        break;
                }
            } else {
                player.sendMessage(new MessageBuilder($.TMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.TMS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen.").build());
        }
    }

    private static void sendHelpMessage(ProxiedPlayer player)
    {
        player.sendMessage(new MessageBuilder($.TMS + "Benutzung §8" + $.arrow + " §e/team").build());
        player.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/team login §8" + $.arrow + " §7Loggt dich in das Team Management System ein").build());
        player.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/team logout §8" + $.arrow + " §7Loggt dich aus dem Team Management System aus").build());
        player.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/team list §8" + $.arrow + " §7Liste dir alle verfügbaren Teammitglieder auf").build());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.addAll(Arrays.asList("login", "logout", "list"));
            list.removeIf(filter -> !filter.toLowerCase().startsWith(strings[0].toLowerCase()));
        }
        return list;
    }
}