/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * UnbanCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class UnbanCommand extends Command implements TabExecutor {

    public UnbanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length <= 1) {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung von §c/unban§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/unban <Spieler> [Grund] §8┃ §7Entbannt einen gebannten Spieler").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unban ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else {
            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
            if (uuid != null) {
                IPlayer targetIPlayer = new IPlayer(uuid);
                IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                if (iBan != null) {
                    LostProxy.getInstance().getBanManager().deleteBan(iBan);
                    IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                    String reason = LostProxy.getInstance().formatArrayToString(1, strings);
                    String invokerId = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");

                    iBanHistory.addEntry(new IBanEntry(EBanEntryType.UNBAN_ENTRY, uuid, invokerId, System.currentTimeMillis(), reason, 0, null, 0));
                    LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7wurde §aentbannt§7.").build());
                    LostProxy.getInstance().getTeamManager().sendUnbanNotify(commandSender instanceof ProxiedPlayer ? new IPlayer(((ProxiedPlayer) commandSender).getUniqueId()).getDisplay() + commandSender.getName() : "§4Konsole", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), reason);
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Spieler " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cnicht §7gebannt§7.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§7.").build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();

        if (strings.length == 1) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).find().forEach((Consumer<? super Document>) one -> {
                IBan iBan = LostProxy.getInstance().getGson().fromJson(one.toJson(), IBan.class);
                String playerName = new IPlayer(iBan.getUniqueId()).getPlayerName();

                if (playerName.toLowerCase().startsWith(strings[0].toLowerCase()))
                    list.add(playerName);
            });
        }
        return list;
    }
}