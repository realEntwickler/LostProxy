/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:01
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IBanReason.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.bkms;

import eu.lostname.lostproxy.enums.EReasonType;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IReason;

public class IBanReason extends IReason {

    private long time;
    private ETimeUnit eTimeUnit;
    private String permission;

    public IBanReason(int _id, String name, long time, ETimeUnit eTimeUnit, String permission) {
        super(_id, name, EReasonType.BAN);
        this.time = time;
        this.eTimeUnit = eTimeUnit;
        this.permission = permission;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ETimeUnit getETimeUnit() {
        return eTimeUnit;
    }

    public void setETimeUnit(ETimeUnit eTimeUnit) {
        this.eTimeUnit = eTimeUnit;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
