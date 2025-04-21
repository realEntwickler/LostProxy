/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:00:34
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanHistoryClearCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class BanHistoryClearCommand extends Command implements TabExecutor {

    public BanHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length >= 3) {
            sendHelp(commandSender);
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayer targetiPlayer = new IPlayer(targetUUID);
                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                if (iBanHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Möchtest du wirklich die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7löschen§8?").build());
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "§7[§aKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/bhclear " + strings[0] + " confirmed").build());
                    if (!LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
            }
        } else {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayer targetiPlayer = new IPlayer(targetUUID);
                        IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getBanHistoryClearCommandProcess().remove(commandSender.getName());
                        if (iBanHistory.getHistory().size() > 0) {
                            iBanHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §aerfolgreich §7die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + "  §cgelöscht§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Die §eBan-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                }
            } else {
                sendHelp(commandSender);
            }
        }
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung§8: §c/bhclear <Spieler>").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bhclear ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find().forEach((Consumer<? super Document>) one -> {
                IBanHistory iBanHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IBanHistory.class);
                if (iBanHistory.getHistory().size() > 0) {
                    IPlayer iPlayer = new IPlayer(iBanHistory.getUniqueId());
                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
