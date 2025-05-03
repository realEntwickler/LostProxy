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
import net.md_5.bungee.api.chat.HoverEvent;
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
            sendHelpMessage(commandSender);
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IPlayer targetiPlayer = new IPlayer(targetUUID);
                IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                if (!iMuteHistory.getHistory().isEmpty()) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Möchtest du wirklich die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §clöschen§7? ").addExtra(new MessageBuilder($.BKMS + "§a☑").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mhclear " + strings[0] + " confirmed").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§aKlicke §7zum löschen").build()).build());
                    if (!LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().contains(commandSender.getName())) {
                        LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().add(commandSender.getName());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§7.").build());
                }
            } else {
                commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
            }
        } else {
            if (strings[1].equalsIgnoreCase("confirmed")) {
                if (LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().contains(commandSender.getName())) {
                    UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                    if (targetUUID != null) {
                        IPlayer targetiPlayer = new IPlayer(targetUUID);
                        IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                        LostProxy.getInstance().getHistoryManager().getMuteHistoryClearCommandProcess().remove(commandSender.getName());
                        if (!iMuteHistory.getHistory().isEmpty()) {
                            iMuteHistory.getHistory().clear();
                            LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §aerfolgreich §7die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §cgelöscht§7.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Die §eMute-History §7von " + targetiPlayer.getDisplay() + targetiPlayer.getPlayerName() + " §7ist §cleer§7.").build());
                        }
                    } else {
                        commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§7.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
            }
        }
    }

    private static void sendHelpMessage(CommandSender commandSender)
    {
        commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung §8" + $.arrow + "§c/mhclear <Spieler>").build());
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
