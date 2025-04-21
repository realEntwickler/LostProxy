/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PostLoginListener.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.clan.IClanInvitation;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        IPlayer iPlayer = new IPlayer(player.getUniqueId());

        IBan iBan = LostProxy.getInstance().getBanManager().getBan(player.getUniqueId());
        if (iBan != null && iBan.getEnd() < System.currentTimeMillis()) {
            LostProxy.getInstance().getBanManager().deleteBan(iBan);
            IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(player.getUniqueId());
            iBanHistory.addEntry(new IBanEntry(EBanEntryType.UNBAN_ENTRY, player.getUniqueId(), "console", System.currentTimeMillis(), "BAN_EXPIRED", 0, null, 0));
            LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
            LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(all -> {
                all.sendMessage(new MessageBuilder($.BKMS + "§cBKM-System" + " §8➼ " + iPlayer.getDisplay() + player.getName()).build());
                all.sendMessage(new MessageBuilder("§8» §7Typ §8» §aUnban").build());
                all.sendMessage(new MessageBuilder("§8» §7Grund §8» §eAbgelaufen").build());
                all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            });
        }

        if (player.hasPermission("lostproxy.notify")) {
            if (LostProxy.getInstance().getTeamManager().enableNotifications(player)) {
                player.sendMessage(new MessageBuilder($.NOTIFICATIONS + "§7Du erhälst nun §aBenachrichtigungen§8.").build());
            }
        }

        if (player.hasPermission("lostproxy.command.team") && player.hasPermission("lostproxy.command.team.login")) {
            LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder($.TMS + iPlayer.getDisplaywithPlayername() + " §7hat das Netzwerk §abetreten§8.").build()));
            if (LostProxy.getInstance().getTeamManager().login(player)) {
                player.sendMessage(new MessageBuilder($.TMS + "§7Du wurdest §aeingeloggt§8.").build());
            }
        }

        IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(player.getUniqueId());
        final List<String> onlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayer(UUID.fromString(filter)).isOnline()).toList();

        if (!onlineFriends.isEmpty()) {
            if (iFriendData.canFriendsSeeOnlineStatusAllowed())
                onlineFriends.forEach(one -> {
                    UUID friendUUID = UUID.fromString(one);
                    if (LostProxy.getInstance().getFriendManager().getFriendData(friendUUID).areNotifyMessagesEnabled())
                        ProxyServer.getInstance().getPlayer(friendUUID).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplay() + player.getName() + " §7ist nun §aonline§8.").build());
                });


            player.sendMessage(new MessageBuilder($.FRIENDS + "Zurzeit " + (onlineFriends.size() == 1 ? "ist" : "sind") + " §e" + onlineFriends.size() + " §7" + (onlineFriends.size() == 1 ? "Freund" : "Freunde") + " online§8.").build());
        }

        HashMap<String, Long> requests = iFriendData.getRequests();
        if (!requests.isEmpty()) {
            player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast noch §e" + (requests.size() == 1 ? "eine" : requests.size()) + " §7offene Freundschaftsanfragen§8.").build());
        }

        List<IClanInvitation> invitations = LostProxy.getInstance().getClanManager().getClanInvitationsByPlayer(player.getUniqueId());
        if (!invitations.isEmpty()) {
            player.sendMessage(new MessageBuilder($.CLANS + "Du hast noch §e" + (invitations.size() == 1 ? "eine" : invitations.size()) + " §7offene Claneinladungen§8.").build());
        }
    }
}