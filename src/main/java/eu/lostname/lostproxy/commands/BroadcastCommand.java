/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:04:44
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BroadcastCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

    public BroadcastCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder($.BROADCAST + "Benutzung§8: §e/broadcast [Nachricht]").build());
        } else {
            String message = LostProxy.getInstance().formatArrayToString(0, strings).replaceAll("&", "§");

            ProxyServer.getInstance().broadcast(new MessageBuilder("").build());
            ProxyServer.getInstance().broadcast(new MessageBuilder($.BROADCAST + message).build());
            ProxyServer.getInstance().broadcast(new MessageBuilder("").build());
        }
    }
}
