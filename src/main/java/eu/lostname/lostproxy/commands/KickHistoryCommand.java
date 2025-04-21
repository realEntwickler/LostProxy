/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:51
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * KickHistoryCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class KickHistoryCommand extends Command implements TabExecutor {
    public KickHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung von §c/kickhistory§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/kickhistory <Spieler> §8┃ §7Zeigt dir die History-Einträge eines Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kickinfo ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IKickHistory iKickHistory = LostProxy.getInstance().getHistoryManager().getKickHistory(targetUUID);
                IPlayer iPlayer = new IPlayer(targetUUID);
                if (!iKickHistory.getHistory().isEmpty()) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Kicks von " + iPlayer.getDisplay() + iPlayer.getPlayerName() + "§8:").build());
                    commandSender.sendMessage(new MessageBuilder("§8» §7Anzahl §8┃ §c" + iKickHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    iKickHistory.getHistory().stream().sorted(Comparator.comparingLong(IEntry::getTimestamp)).forEach(iKickEntry -> {
                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iKickEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iKickEntry.getTimestamp()));

                        if (iKickEntry.getInvokerId().equalsIgnoreCase("console")) {
                            commandSender.sendMessage(new MessageBuilder("§8» §e" + date + " §7@ §e" + time + " §8┃ §4Konsole §8» §c" + iKickEntry.getReason()).build());
                        } else {
                            IPlayer invokerIPlayer = new IPlayer(UUID.fromString(iKickEntry.getInvokerId()));
                            commandSender.sendMessage(new MessageBuilder("§8» §e" + date + " §7@ §e" + time + " §8┃ " + invokerIPlayer.getDisplay() + invokerIPlayer.getPlayerName() + " §8» §c" + iKickEntry.getReason()).build());
                        }

                        currentEntry.set(currentEntry.get() + 1);

                        if (iKickHistory.getHistory().size() == currentEntry.get()) {
                            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        }
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Spieler " + iPlayer.getDisplay() + iPlayer.getPlayerName() + " §7hat §ckeine §7Kick-History§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§8.").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).find().forEach((Consumer<? super Document>) one -> {
                IKickHistory iKickHistory = LostProxy.getInstance().getGson().fromJson(one.toJson(), IKickHistory.class);
                if (!iKickHistory.getHistory().isEmpty()) {
                    IPlayer iPlayer = new IPlayer(iKickHistory.getUniqueId());
                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
