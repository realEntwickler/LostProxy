/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PingCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class PingCommand extends Command implements TabExecutor {


    public PingCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder($.LOSTNAME + "Dein Ping beträgt aktuell§8: §c" + player.getPing() + "ms").build());
            } else if (strings.length == 1) {
                if (player.hasPermission("lostproxy.command.ping.other")) {
                    String argument = strings[0];
                    ProxiedPlayer target = null;
                    for (ProxiedPlayer one : ProxyServer.getInstance().getPlayers()) {
                        if (one.getName().equalsIgnoreCase(argument))
                            target = one;
                    }

                    if (target != null) {
                        IPlayer iPlayer = new IPlayer(target.getUniqueId());
                        player.sendMessage(new MessageBuilder($.LOSTNAME + "Der Ping von " + iPlayer.getDisplay() + target.getName() + " §7beträgt aktuell§8: §c" + target.getPing() + "ms").build());
                    } else {
                        player.sendMessage(new MessageBuilder($.LOSTNAME + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                    }
                } else {
                    player.sendMessage(new MessageBuilder($.LOSTNAME + "Dein Ping beträgt aktuell §8» §c" + player.getPing() + "ms").build());
                }
            } else {
                player.sendMessage(new MessageBuilder($.LOSTNAME + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.LOSTNAME + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();

        if (commandSender.hasPermission("lostproxy.command.ping.other")) {
            ProxyServer.getInstance().getPlayers().forEach(all -> {
                if (all.getName().toLowerCase().startsWith(strings[0]))
                    list.add(all.getName());
            });
        }
        return list;
    }
}
