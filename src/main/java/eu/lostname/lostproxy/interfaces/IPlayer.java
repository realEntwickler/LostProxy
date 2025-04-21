/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:33
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IPlayerSync.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;

import eu.cloudnetservice.modules.bridge.player.CloudOfflinePlayer;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.lostname.lostproxy.utils.CloudServices;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import java.util.Objects;
import java.util.UUID;

public class IPlayer {

    private final UUID uniqueId;
    private final CloudOfflinePlayer cloudOfflinePlayer;
    private final String playerName;
    private final boolean exists;

    public IPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.cloudOfflinePlayer = CloudServices.PLAYER_MANAGER.offlinePlayer(uniqueId);
        if (cloudOfflinePlayer == null) {
            this.exists = false;
            this.playerName = "NOT_FOUND";
            return;
        }
        this.exists = true;
        this.playerName = cloudOfflinePlayer.name();
    }

    public String getPrefix() {
        return getPermissionGroup().getCachedData().getMetaData().getPrefix();
    }

    public String getSuffix() {
        return getPermissionGroup().getCachedData().getMetaData().getSuffix();
    }

    public String getDisplay() {
        return getPermissionGroup().getCachedData().getMetaData().getMetaValue("display");
    }

    public String getDisplaywithPlayername() {
        return getDisplay() + getPlayerName();
    }

    public int getPotency() {
        return getPermissionGroup().getWeight().orElse(0);
    }

    public int getSortId() {
        return Integer.parseInt(Objects.requireNonNull(getPermissionGroup().getCachedData().getMetaData().getMetaValue("sortid")));
    }

    public String getGroupName() {
        return getPermissionGroup().getDisplayName();
    }

    /*public String getChatColor() {
        if (getIPermissionGroup().getProperties().contains("chatColor")) {
            return getIPermissionGroup().getProperties().getString("chatColor");
        } else {
            if (CloudServices.LUCKPERMS.getDefaultPermissionGroup().getProperties().contains("chatColor")) {
                return CloudServices.LUCKPERMS.getDefaultPermissionGroup().getProperties().getString("chatColor");
            } else {
                return "§f";
            }
        }
    }*/

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Group getPermissionGroup() {
        return CloudServices.LUCKPERMS.getGroupManager().loadGroup(getPermissionUser().getPrimaryGroup()).join().get();
    }

    public User getPermissionUser() {
        return CloudServices.LUCKPERMS.getUserManager().loadUser(uniqueId).join();
    }
    /**
     * @return A boolean whether the player is online. This is being checked by getting a ICloudPlayer instance via the UUID and if this is null, the player isn't online.
     */
    public boolean isOnline() {
        return getCloudPlayer() != null;
    }

    /**
     * @return The instance of the online ICloudPlayer which the CloudNet DB has cached
     */
    public CloudPlayer getCloudPlayer() {
        return CloudServices.PLAYER_MANAGER.onlinePlayer(uniqueId);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public CloudOfflinePlayer getCloudOfflinePlayer() {
        return cloudOfflinePlayer;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isExists() {
        return exists;
    }
}
