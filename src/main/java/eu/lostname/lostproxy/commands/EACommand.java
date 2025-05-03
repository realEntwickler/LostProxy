/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:14:55
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * EACommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.$;
import eu.lostname.lostproxy.utils.MongoCollection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EACommand extends Command implements TabExecutor {
    public EACommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            sendHelpMessage(commandSender);
        } else {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayer iPlayer = new IPlayer(uuid);
                IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                if (iBan != null) {
                    if (iBan.getBanAppeal() == null) {
                        if (iBan.getDuration() != -1) {
                            if ((iBan.getEnd() - System.currentTimeMillis()) >= TimeUnit.DAYS.toMillis(3)) {
                                iBan.setEnd(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3));

                                IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_APPEAL_ENTRY, uuid, (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console"), System.currentTimeMillis(), "EA-Command", 0, null, 0));

                                LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                LostProxy.getInstance().getBanManager().saveBan(iBan);
                                if (commandSender instanceof ProxiedPlayer) {
                                    IPlayer invoker = new IPlayer(((ProxiedPlayer) commandSender).getUniqueId());
                                    LostProxy.getInstance().getTeamManager().sendEANotify(invoker.getDisplaywithPlayername(), iPlayer.getDisplaywithPlayername());
                                } else {
                                    LostProxy.getInstance().getTeamManager().sendEANotify("§4System", iPlayer.getDisplay() + iPlayer.getPlayerName());
                                }

                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Ban von " + iPlayer.getDisplaywithPlayername() + " §7läuft nun in §e3 Tagen §7ab.").build());
                            } else {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Da der Bann bereits in §e3 Tagen §cabläuft§8, §7kann der Bann §cnicht §7nochmal verkürzt werden.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Ein §cpermanenter §7Bann kann §cnicht §7verkürzt werden.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Bann von " + iPlayer.getDisplaywithPlayername() + " §7wurde §cbereits §7verkürzt.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + iPlayer.getDisplaywithPlayername() + " §7ist §cnicht §7gebannt.").build());
                }
            } else {
                commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
            }
        }
    }

    private static void sendHelpMessage(CommandSender commandSender)
    {
        commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung §8" + $.arrow + " §c/ea <Spieler>").build());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).find().forEach((Consumer<? super Document>) one -> {
                IBan iBan = LostProxy.getInstance().getGson().fromJson(one.toJson(), IBan.class);
                if (iBan.getDuration() != -1) {
                    IPlayer iPlayer = new IPlayer(iBan.getUniqueId());

                    if (iPlayer.getPlayerName().toLowerCase().startsWith(strings[0].toLowerCase()))
                        list.add(iPlayer.getPlayerName());
                }
            });
        }
        return list;
    }
}
