/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * TeamManager.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ELocale;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.bkms.IMuteReason;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class TeamManager {

    private final ArrayList<ProxiedPlayer> notificationOn;
    private final ArrayList<ProxiedPlayer> loggedIn;

    public TeamManager() {
        this.notificationOn = new ArrayList<>();
        this.loggedIn = new ArrayList<>();
    }

    /**
     * Adds the given player to the ArrayList: notificationOn
     *
     * @param player the player who gets the notifications enabled
     * @since 08.12.2020
     */
    public boolean enableNotifications(ProxiedPlayer player) {
        return notificationOn.add(player);
    }

    /**
     * Removes the given player from the ArrayList: notificationOn
     * @param player   the player who gets the notifications disabled
     * @since 08.12.2020
     */
    public boolean disableNotifications(ProxiedPlayer player) {
        return notificationOn.remove(player);
    }

    /**
     * Returns a boolean which says whether the player is in the ArrayList: notificationOn or not
     * @param proxiedPlayer the player who has to be checked
     * @return whether the proxiedPlayer is in the notificationOn list
     */
    public boolean hasNotificationsEnabled(ProxiedPlayer proxiedPlayer) {
        return getNotificationOn().contains(proxiedPlayer);
    }

    /**
     * Adds the given player to the ArrayList: loggedIn
     * @param proxiedPlayer the player who has to be logged in
     */
    public boolean login(ProxiedPlayer proxiedPlayer) {
        return loggedIn.add(proxiedPlayer);
    }

    /**
     * Removes the given player from the ArrayList: loggedIn
     * @param proxiedPlayer the player who has to be logged out
     */
    public boolean logout(ProxiedPlayer proxiedPlayer) {
        return loggedIn.remove(proxiedPlayer);
    }

    /**
     * REturns a boolean which says whether the player is in the ArrayList: loggedIn
     * @param proxiedPlayer the player who has to be checked
     * @return whether the proxiedPlayer is logged in
     */
    public boolean isLoggedIn(ProxiedPlayer proxiedPlayer) {
        return getLoggedIn().contains(proxiedPlayer);
    }


    /**
     * Sends a message to all players in the ArrayList: notificationOn which describes that a player has been kicked
     * @param invokerDisplay the name of the invoker and some color codes
     * @param targetDisplay  the name of the target and some color codes
     * @param reason         the reason the player got kicked
     */
    public void sendKickNotify(String invokerDisplay, String targetDisplay, String reason) {
        notificationOn.forEach(all -> {
            all.sendMessage(new MessageBuilder(Prefix.BKMS + invokerDisplay + " §8➼ " + targetDisplay).build());
            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §cKick").build());
            all.sendMessage(new MessageBuilder("§8» §7Grund §8» §e" + reason).build());
            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        });
    }

    /**
     * Sends a message to all players in the ArrayList: notificationOn which describes that a player has been unbanned
     * @param invokerDisplay the name of the invoker and some color codes
     * @param targetDisplay  the name of the target and some color codes
     * @param reason         the reason the player got unbanned
     */
    public void sendUnbanNotify(String invokerDisplay, String targetDisplay, String reason) {
        notificationOn.forEach(all -> {
            all.sendMessage(new MessageBuilder(Prefix.BKMS + invokerDisplay + " §8➼ " + targetDisplay).build());
            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §aUnban").build());
            all.sendMessage(new MessageBuilder("§8» §7Grund §8» §e" + reason).build());
            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        });
    }

    /**
     * Sends a message to all players in the ArrayList: notificationOn which describes that a player has been unmutet
     *
     * @param invokerDisplay the name of the invoker and some color codes
     * @param targetDisplay  the name of the target and some color codes
     * @param reason         the reason the player got unmutet
     */
    public void sendUnmuteNotify(String invokerDisplay, String targetDisplay, String reason) {
        notificationOn.forEach(all -> {
            all.sendMessage(new MessageBuilder(Prefix.BKMS + invokerDisplay + " §8➼ " + targetDisplay).build());
            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §aUnmute").build());
            all.sendMessage(new MessageBuilder("§8» §7Grund §8» §e" + reason).build());
            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        });
    }

    /**
     * Sends a message to all players in the ArrayList: notificationOn which describes that a player has been banned
     *
     * @param invokerDisplay the name of the invoker and some color codes
     * @param targetDisplay  the name of the target and some color codes
     * @param iBanReason     the ban reason the player got banned with
     */
    public void sendBanNotify(String invokerDisplay, String targetDisplay, IBanReason iBanReason) {
        notificationOn.forEach(all -> {
            all.sendMessage(new MessageBuilder(Prefix.BKMS + invokerDisplay + " §8➼ " + targetDisplay).build());
            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §4Ban").build());
            all.sendMessage(new MessageBuilder("§8» §7Grund §8» §e" + iBanReason.getName()).build());
            all.sendMessage(new MessageBuilder("§8» §7Dauer §8» §e" + (iBanReason.getTime() == -1 ? "Permanent" : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit()))).build());
            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        });
    }

    /**
     * Sends a message to all players in the ArrayList: notificationOn which describes that a player has been muted
     *
     * @param invokerDisplay the name of the invoker and some color codes
     * @param targetDisplay  the name of the target and some color codes
     * @param iMuteReason    the mute reason the player got muted with
     */
    public void sendMuteNotify(String invokerDisplay, String targetDisplay, IMuteReason iMuteReason) {
        notificationOn.forEach(all -> {
            all.sendMessage(new MessageBuilder(Prefix.BKMS + invokerDisplay + " §8➼ " + targetDisplay).build());
            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §cMute").build());
            all.sendMessage(new MessageBuilder("§8» §7Grund §8» §e" + iMuteReason.getName()).build());
            all.sendMessage(new MessageBuilder("§8» §7Dauer §8» §e" + (iMuteReason.getTime() == -1 ? "Permanent" : iMuteReason.getTime() + " " + ETimeUnit.getDisplayName(iMuteReason.getTime(), iMuteReason.getETimeUnit()))).build());
            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        });
    }

    /**
     * Sends a message to all players in the ArrayList: notificationOn which describes that a player has been eaed
     *
     * @param invokerDisplay the name of the invoker and some color codes
     * @param targetDisplay  the name of the target and some color codes
     */
    public void sendEANotify(String invokerDisplay, String targetDisplay) {
        notificationOn.forEach(all -> {
            all.sendMessage(new MessageBuilder(Prefix.BKMS + invokerDisplay + " §8➼ " + targetDisplay).build());
            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §dEA").build());
            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        });
    }

    public ArrayList<ProxiedPlayer> getNotificationOn() {
        return notificationOn;
    }

    public ArrayList<ProxiedPlayer> getLoggedIn() {
        return loggedIn;
    }
}
