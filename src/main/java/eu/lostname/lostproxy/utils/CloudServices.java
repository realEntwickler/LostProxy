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

import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.syncproxy.SyncProxyManagement;
import net.luckperms.api.LuckPerms;

public class CloudServices {

    public static PlayerManager PLAYER_MANAGER;
    public static LuckPerms LUCKPERMS;
    public static SyncProxyManagement SYNCPROXY_MANAGEMENT;
}
