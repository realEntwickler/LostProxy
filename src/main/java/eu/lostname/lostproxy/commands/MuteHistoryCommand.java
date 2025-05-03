/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 11.01.2021 @ 18:46:29
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MuteHistoryCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.$;
import eu.lostname.lostproxy.utils.MongoCollection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

public class MuteHistoryCommand extends Command implements TabExecutor {

    public MuteHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            //TODO: Weiter machen
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung §8" + $.arrow + " §c/mutehistory <Spieler>").build());
        } else {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(targetUUID);
                IPlayer targetIPlayer = new IPlayer(targetUUID);
                if (!iMuteHistory.getHistory().isEmpty()) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Mutes von " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName()).build());
                    commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §7Anzahl §8"+ $.arrow + " §c" + iMuteHistory.getHistory().size()).build());
                    iMuteHistory.getHistory().forEach(iMuteEntry -> {

                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getTimestamp()));

                        switch (iMuteEntry.getEMuteEntryType()) {
                            case MUTE_ENTRY:

                                String unmuteDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iMuteEntry.getEnd()));
                                String unmuteTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iMuteEntry.getEnd()));

                                boolean muteIsPermanent = iMuteEntry.getTime() == -1;
                                if (iMuteEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §cMute §8" + $.arrow + " §e" + date + " §7@ §e" + time + " §8" + $.arrow + " §4System §8" + $.arrow + " §e" + iMuteEntry.getReason() + " §8" + $.arrow + " §c" + (muteIsPermanent ? "permanent" : iMuteEntry.getTime() + " " + ETimeUnit.getDisplayName(iMuteEntry.getTime(), iMuteEntry.getETimeUnit())) + " §8" + $.arrow + " §a" + (muteIsPermanent ? "/" : unmuteDate + " §7@ §a" + unmuteTime)).build());
                                } else {
                                    IPlayer iPlayer = new IPlayer(UUID.fromString(iMuteEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §cMute §8" + $.arrow + " §e" + date + " §7@ §e" + time + " §8" + $.arrow + " " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8" + $.arrow + " §e" + iMuteEntry.getReason() + " §8" + $.arrow + " §c" + (muteIsPermanent ? "permanent" : iMuteEntry.getTime() + " " + ETimeUnit.getDisplayName(iMuteEntry.getTime(), iMuteEntry.getETimeUnit())) + " §8" + $.arrow + " §a" + (muteIsPermanent ? "/" : unmuteDate + " §7@ §a" + unmuteTime)).build());
                                }
                                break;
                            case UNMUTE_ENTRY:
                                if (iMuteEntry.isInvokerConsole()) {
                                    commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §aUnmute §8" + $.arrow + " §e" + date + " §7@ §e" + time + " §8" + $.arrow + " §4System §8" + $.arrow + " §e" + iMuteEntry.getReason()).build());
                                } else {
                                    IPlayer iPlayer = new IPlayer(UUID.fromString(iMuteEntry.getInvokerId()));
                                    commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §aUnmute §8" + $.arrow + " §e" + date + " §7@ §e" + time + " §8" + $.arrow + " " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §8" + $.arrow + " §e" + iMuteEntry.getReason()).build());
                                }
                                break;
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7hat §ckeine §7Mute-History§7.").build());
                }
            } else {
                commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).find().forEach((Consumer<? super Document>) one -> {
                IMuteHistory iMuteHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IMuteHistory.class);
                if (!iMuteHistory.getHistory().isEmpty()) {
                    IPlayer iPlayer = new IPlayer(iMuteHistory.getUniqueId());
                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
