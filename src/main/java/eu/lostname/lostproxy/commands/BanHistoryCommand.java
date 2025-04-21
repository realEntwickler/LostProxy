/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanHistoryCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.$;
import eu.lostname.lostproxy.utils.MongoCollection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class BanHistoryCommand extends Command implements TabExecutor {

    public BanHistoryCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung§8: §c/banhistory <Spieler>").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bh ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§a☑").build());
        } else {
            UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (targetUUID != null) {
                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(targetUUID);
                IPlayer targetIPlayer = new IPlayer(targetUUID);
                if (!iBanHistory.getHistory().isEmpty()) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bans von " + targetIPlayer.getDisplaywithPlayername() + "§8:").build());
                    commandSender.sendMessage(new MessageBuilder("§8┃ §7Anzahl §8» §e" + iBanHistory.getHistory().size()).build());
                    AtomicInteger currentEntry = new AtomicInteger(-1);

                    iBanHistory.getHistory().forEach(iBanEntry -> {

                        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getTimestamp()));
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getTimestamp()));

                        switch (iBanEntry.getEBanEntryType()) {
                            case BAN_ENTRY:

                                String unbanDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date(iBanEntry.getEnd()));
                                String unbanTime = new SimpleDateFormat("HH:mm:ss").format(new Date(iBanEntry.getEnd()));

                                boolean banIsPermanent = iBanEntry.getTime() == -1;
                                commandSender.sendMessage(new MessageBuilder("§8┃ §cBan §8» §e" + date + " §7@ §e" + time + " §8» " + (iBanEntry.isInvokerConsole() ? "§4System" : new IPlayer(UUID.fromString(iBanEntry.getInvokerId())).getDisplaywithPlayername()) + " §8» §e" + iBanEntry.getReason() + " §8» §c" + (banIsPermanent ? "§4permanent" : iBanEntry.getTime() + " " + ETimeUnit.getDisplayName(iBanEntry.getTime(), iBanEntry.getETimeUnit())) + " §8» §a" + (banIsPermanent ? "/" : unbanDate + " §7@ §a" + unbanTime)).build());
                                break;
                            case UNBAN_ENTRY:
                                commandSender.sendMessage(new MessageBuilder("§8┃ §aUnban §8» §e" + date + " §7@ §e" + time + " §8» " + (iBanEntry.isInvokerConsole() ? "§4System" : new IPlayer(UUID.fromString(iBanEntry.getInvokerId())).getDisplaywithPlayername()) + " §8» §e" + iBanEntry.getReason()).build());
                                break;
                            case BAN_APPEAL_ENTRY:
                                commandSender.sendMessage(new MessageBuilder("§8┃ §eEA §8» §e" + date + " §7@ §e" + time + " §8» " + (iBanEntry.isInvokerConsole() ? "§4System" : new IPlayer(UUID.fromString(iBanEntry.getInvokerId())).getDisplaywithPlayername()) + " §8» §e" + iBanEntry.getReason()).build());
                                break;
                        }

                        currentEntry.set(currentEntry.get() + 1);

                        /*if (iBanHistory.getHistory().size() == currentEntry.get()) {
                            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        }*/
                    });
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + targetIPlayer.getDisplaywithPlayername() + " §7hat §ckeine §7Ban-History.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden.").build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find().forEach((Consumer<? super Document>) all -> {
                IPlayer iPlayer = new IPlayer(UUID.fromString(all.getString("_id")));

                if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
