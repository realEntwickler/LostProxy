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

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import eu.lostname.lostproxy.utils.CloudServices;

import java.util.UUID;

public class IPlayer {

    private final UUID uniqueId;
    private final ICloudOfflinePlayer iCloudOfflinePlayer;
    private final String playerName;
    private final boolean exists;

    public IPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.iCloudOfflinePlayer = CloudServices.PLAYER_MANAGER.getOfflinePlayer(uniqueId);
        if (iCloudOfflinePlayer == null) {
            this.exists = false;
            this.playerName = "NOT_FOUND";
            return;
        }
        this.exists = true;
        this.playerName = iCloudOfflinePlayer.getName();
    }

    public String getPrefix() {
        return getIPermissionGroup().getPrefix();
    }

    public String getSuffix() {
        return getIPermissionGroup().getSuffix();
    }

    public String getDisplay() {
        return getIPermissionGroup().getDisplay();
    }

    public String getColor() {
        return getIPermissionGroup().getColor();
    }

    public String getColorWithPlayername() {
        return getIPermissionGroup().getColor() + getPlayerName();
    }

    public int getPotency() {
        return getIPermissionGroup().getPotency();
    }

    public int getSortId() {
        return getIPermissionGroup().getSortId();
    }

    public String getHighestGroupName() {
        return getIPermissionGroup().getName();
    }

    public String getChatColor() {
        if (getIPermissionGroup().getProperties().contains("chatColor")) {
            return getIPermissionGroup().getProperties().getString("chatColor");
        } else {
            if (CloudServices.PERMISSION_MANAGEMENT.getDefaultPermissionGroup().getProperties().contains("chatColor")) {
                return CloudServices.PERMISSION_MANAGEMENT.getDefaultPermissionGroup().getProperties().getString("chatColor");
            } else {
                return "§f";
            }
        }
    }


    /**
     * @return An instance of an IPermissionUser that belongs to the provided UUID in the constructor
     * @throws NullPointerException When the provided UUID in the IPlayer Constructor cannot be found in the CloudNet Player database
     */
    public IPermissionUser getIPermissionUser() {
        return CloudServices.PERMISSION_MANAGEMENT.getUser(uniqueId);
    }

    /**
     * @return An instance of an IPermissionGroup that is the highest permission group sorted by the potency the IPermissionUser have
     * @throws NullPointerException When IPermissionUser is null
     */
    public IPermissionGroup getIPermissionGroup() {
        return CloudServices.PERMISSION_MANAGEMENT.getHighestPermissionGroup(getIPermissionUser());
    }

    /**
     * @return A boolean whether the player is online. This is being checked by getting a ICloudPlayer instance via the UUID and if this is null, the player isn't online.
     */
    public boolean isOnline() {
        return getICloudPlayer() != null;
    }

    /**
     * @return The instance of the online ICloudPlayer which the CloudNet DB has cached
     */
    public ICloudPlayer getICloudPlayer() {
        return CloudServices.PLAYER_MANAGER.getOnlinePlayer(uniqueId);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ICloudOfflinePlayer getiCloudOfflinePlayer() {
        return iCloudOfflinePlayer;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isExists() {
        return exists;
    }
}
