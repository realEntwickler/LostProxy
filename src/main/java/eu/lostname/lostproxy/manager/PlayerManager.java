/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:41:20
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PlayerManager.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import eu.cloudnetservice.modules.bridge.player.CloudOfflinePlayer;
import eu.lostname.lostproxy.utils.CloudServices;

import java.util.List;
import java.util.UUID;

public class PlayerManager {

    public UUID getUUIDofPlayername(String playername) {
        List<CloudOfflinePlayer> cloudOfflinePlayers = CloudServices.PLAYER_MANAGER.registeredPlayers().stream().filter(filter -> filter.name().equalsIgnoreCase(playername)).toList();
        if (cloudOfflinePlayers.isEmpty())
            return null;
        CloudOfflinePlayer player = cloudOfflinePlayers.getFirst();
        return player != null ? player.uniqueId() : null;
    }

    public void getIPlayerAsync(UUID uniqueId) {
        /*getCloudOfflinePlayer(uniqueId, iCloudOfflinePlayer -> {
            getPermissionUser(uniqueId, iPermissionUser -> {
                getPermissionGroup(uniqueId, iPermissionGroup -> {
                    getCloudPlayer(uniqueId, iCloudPlayer -> {
                        IPlayer async = new IPlayer(uniqueId,
                                iCloudOfflinePlayer.getName(),
                                iPermissionGroup.getPrefix(),
                                iPermissionGroup.getSuffix(),
                                iPermissionGroup.getDisplay(),
                                iPermissionGroup.getDisplay(),
                                (iPermissionGroup.getProperties().contains("chatColor") ? iPermissionGroup.getProperties().getString("chatColor") : "§f"),
                                iPermissionGroup,
                                iPermissionUser,
                                iCloudOfflinePlayer,
                                iCloudPlayer,
                                iCloudPlayer != null,
                                iCloudOfflinePlayer != null);

                        consumer.accept(async);
                    });
                });
            });
        });*/
    }
}