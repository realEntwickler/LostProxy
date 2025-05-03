/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:51
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * KickCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.DisconnectScreenBuilder;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.utils.$;
import eu.lostname.lostproxy.utils.CloudServices;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class KickCommand extends Command implements TabExecutor {

    public KickCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            sendHelpMessage(commandSender);
        } else if (strings.length == 1) {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
        } else {

            ProxiedPlayer target = null;
            for (ProxiedPlayer one : ProxyServer.getInstance().getPlayers()) {
                if (one.getName().equalsIgnoreCase(strings[0]))
                    target = one;
            }

            if (target != null) {
                if (!commandSender.getName().equalsIgnoreCase(target.getName())) {

                    IPlayer targetIPlayer = new IPlayer(target.getUniqueId());
                    if (commandSender.hasPermission("lostproxy.command.kick." + targetIPlayer.getPermissionGroup().getDisplayName().toLowerCase())) {
                        String reason = LostProxy.getInstance().formatArrayToString(1, strings);

                        IKickHistory iKickHistory = LostProxy.getInstance().getHistoryManager().getKickHistory(target.getUniqueId());
                        iKickHistory.addEntry(new IKickEntry(target.getUniqueId(), (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console"), reason, System.currentTimeMillis()));
                        LostProxy.getInstance().getHistoryManager().saveKickHistory(iKickHistory);
                        target.disconnect(new DisconnectScreenBuilder()
                                .add("§7Deine bestehende Verbindung zum Netzwerk wurde §egetrennt§7.")
                                .newLine()
                                .newLine()
                                .add("§7Grund §8" + $.arrow + " §e" + reason)
                                .build());

                        if (commandSender instanceof ProxiedPlayer) {
                            LostProxy.getInstance().getTeamManager().sendKickNotify(new IPlayer(((ProxiedPlayer) commandSender).getUniqueId()).getDisplaywithPlayername(), targetIPlayer.getDisplaywithPlayername(), reason);
                        } else {
                            LostProxy.getInstance().getTeamManager().sendKickNotify("§4System", targetIPlayer.getDisplaywithPlayername(), reason);
                        }
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast " + targetIPlayer.getDisplaywithPlayername() + " §7wegen §e" + reason + " §7gekickt.").build());
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplaywithPlayername() + " §7zu §ekicken§7.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Du darfst dich §cnicht §7selber §ekicken§7.").build());
                }
            } else {
                commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
            }
        }
    }

    private static void sendHelpMessage(CommandSender commandSender)
    {
        commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung §8" + $.arrow + " §c/kick <Spieler> [Grund]").build());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            CloudServices.PLAYER_MANAGER.onlinePlayers().players().forEach(one -> list.add(one.name()));
            list.removeIf(filter -> !filter.toLowerCase().startsWith(strings[0].toLowerCase()));
        }
        return list;
    }
}
