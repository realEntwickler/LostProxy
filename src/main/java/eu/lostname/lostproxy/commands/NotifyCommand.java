/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:51
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * NotifyCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NotifyCommand extends Command {

    public NotifyCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer player) {

            if (LostProxy.getInstance().getTeamManager().hasNotificationsEnabled(player)) {
                if (LostProxy.getInstance().getTeamManager().disableNotifications(player)) {
                    player.sendMessage(new MessageBuilder($.NOTIFICATIONS + "§7Du erhälst nun §ckeine §7Benachrichtigungen mehr§7.").build());
                } else {
                    player.sendMessage(new MessageBuilder($.NOTIFICATIONS + "Beim §eVerarbeiten §7dieser Anfrage §7ist ein §4Fehler §7aufgetreten§7. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                }
            } else {
                if (LostProxy.getInstance().getTeamManager().enableNotifications(player)) {
                    player.sendMessage(new MessageBuilder($.NOTIFICATIONS + "§7Du erhälst nun §aBenachrichtigungen§7.").build());
                } else {
                    player.sendMessage(new MessageBuilder($.NOTIFICATIONS + "Beim §eVerarbeiten §7dieser Anfrage §7ist ein §4Fehler §7aufgetreten§7. §7Bitte kontaktiere sofort das Referat §4DEV/01§8!").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.NOTIFICATIONS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§7.").build());
        }
    }
}