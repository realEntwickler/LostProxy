/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:41:41
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * CloudServices.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.utils;

import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.syncproxy.AbstractSyncProxyManagement;

public class CloudServices {

    public static IPlayerManager PLAYER_MANAGER;
    public static IPermissionManagement PERMISSION_MANAGEMENT;
    public static AbstractSyncProxyManagement SYNCPROXY_MANAGEMENT;
}
