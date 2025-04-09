/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:40:04
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PlayerDisconnectListener.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.listener;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.party.IParty;
import eu.lostname.lostproxy.interfaces.party.IPartyInvitation;
import eu.lostname.lostproxy.utils.MongoCollection;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        if (proxiedPlayer.hasPermission("lostproxy.notify")) {
            LostProxy.getInstance().getTeamManager().disableNotifications(proxiedPlayer);
        }

        IPlayer iPlayer = new IPlayer(proxiedPlayer.getUniqueId());
        if (proxiedPlayer.hasPermission("lostproxy.command.team") && proxiedPlayer.hasPermission("lostproxy.command.team.logout")) {
            if (LostProxy.getInstance().getTeamManager().logout(proxiedPlayer)) {
                LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder(Prefix.TMS + iPlayer.getColor() + iPlayer.getPlayerName() + " §7hat das Netzwerk §cverlassen§8.").build()));
            }
        }

        IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(proxiedPlayer.getUniqueId());
        iFriendData.setLastLogoutTimestamp(System.currentTimeMillis());
        iFriendData.save();

        if (iFriendData.canFriendsSeeOnlineStatusAllowed()) {
            iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayer(UUID.fromString(filter)).isOnline()).forEach(one -> {
                UUID friendUUID = UUID.fromString(one);
                if (LostProxy.getInstance().getFriendManager().getFriendData(friendUUID).areNotifyMessagesEnabled())
                    ProxyServer.getInstance().getPlayer(friendUUID).sendMessage(new MessageBuilder(Prefix.FRIENDS + iPlayer.getColor() + proxiedPlayer.getName() + " §7ist nun §coffline§8.").build());
            });
        }

        if (LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer) != null) {
            IParty party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

            if (party.getMembers().size() > 1) {
                if (party.isLeader(proxiedPlayer)) {
                    party.removeMember(proxiedPlayer);

                    ProxiedPlayer newLeader = party.getMembers().keySet().stream().findAny().orElse(null);
                    if (newLeader != null) {
                        party.setLeader(newLeader);
                        party.setCurrentServer(newLeader.getServer().getInfo());
                        party.sendMessage(new MessageBuilder(Prefix.PARTY + new IPlayer(newLeader.getUniqueId()).getColorWithPlayername() + " §7ist der neue §ePartyleiter§8.").build());
                    } else {
                        party.sendMessage(new MessageBuilder(Prefix.PARTY + "Es trat ein §cFehler §7bei der Suche nach einem neuen Partyleiter auf§8.").build());
                    }
                } else {
                    party.removeMember(proxiedPlayer);
                    proxiedPlayer.sendMessage(new MessageBuilder(Prefix.PARTY + "Du hast die Party §cverlassen§8.").build());
                }
            } else {
                party.delete();
            }
        }
        LostProxy.getInstance().getPartyManager().getInvitations(proxiedPlayer).forEach(IPartyInvitation::delete);

        if (LostProxy.getInstance().getSettingsManager().getCacheData().containsKey(proxiedPlayer.getUniqueId())) {
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.PLAYER_SETTINGS).replaceOne(Filters.eq("_id", proxiedPlayer.getUniqueId().toString()), LostProxy.getInstance().getSettingsManager().getCacheData().remove(proxiedPlayer.getUniqueId()), new ReplaceOptions().upsert(true));
        }
    }
}
