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

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import eu.lostname.lostproxy.utils.CloudServices;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerManager {

    @SuppressWarnings("UnstableApiUsage")
    public UUID getUUIDofPlayername(String playername) {
        ICloudOfflinePlayer player = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getRegisteredPlayers().stream().filter(any -> any.getName().equalsIgnoreCase(playername)).findFirst().orElse(null);
        return player != null ? player.getUniqueId() : null;
    }

    public void getCloudOfflinePlayer(UUID uniqueId, Consumer<ICloudOfflinePlayer> consumer) {
        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOfflinePlayerAsync(uniqueId).onComplete(consumer).onFailure(throwable -> consumer.accept(null));
    }

    public void getCloudPlayer(UUID uniqueId, Consumer<ICloudPlayer> consumer) {
        CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayerAsync(uniqueId).onComplete(consumer::accept).onFailure(throwable -> consumer.accept(null));
    }

    public void getPermissionUser(UUID uniqueId, Consumer<IPermissionUser> consumer) {
        CloudServices.PERMISSION_MANAGEMENT.getUserAsync(uniqueId).onComplete(consumer).onFailure(throwable -> consumer.accept(null));
    }

    public void getPermissionGroup(UUID uniqueId, Consumer<IPermissionGroup> consumer) {
        getPermissionUser(uniqueId, iPermissionUser -> {
            consumer.accept(CloudServices.PERMISSION_MANAGEMENT.getHighestPermissionGroup(iPermissionUser));
        });
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
                                iPermissionGroup.getColor(),
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