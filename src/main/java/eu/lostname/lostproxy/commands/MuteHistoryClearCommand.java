/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:51
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MuteHistoryClearCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.$;
import eu.lostname.lostproxy.utils.MongoCollection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class MuteHistoryClearCommand extends Command implements TabExecutor {

    public MuteHistoryClearCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length >= 3) {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung von §c/mhclear§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/mhclear <Spieler> §8┃ §7Leert die Mute-History des angegebenen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mhclear ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayer targetiPlayer = new IPlayer(targetUUID);
                IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                if (iMuteHistory.getHistory().size() > 0) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Möchtest du wirklich die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §clöschen§8?").build());
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mhclear " + strings[0] + " confirmed").build());
                    if (!LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
            }
        } else {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayer targetiPlayer = new IPlayer(targetUUID);
                        IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().remove(commandSender.getName());
                        if (iMuteHistory.getHistory().size() > 0) {
                            iMuteHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §aerfolgreich §7die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §cgelöscht§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Spieler konnte §cnicht §7gefunden werden§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 0) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).find().forEach((Consumer<? super Document>) one -> {
                IMuteHistory iMuteHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IMuteHistory.class);
                if (iMuteHistory.getHistory().size() > 1) {
                    list.add(new IPlayer(iMuteHistory.getUniqueId()).getPlayerName());
                }
            });
        }
        return list;
    }
}
