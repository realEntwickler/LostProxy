/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:31
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IKickEntry.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.historyandentries.kick;

import eu.lostname.lostproxy.interfaces.historyandentries.IEntry;

import java.util.UUID;

public class IKickEntry extends IEntry {

    private final String reason;

    public IKickEntry(UUID _id, String invokerId, String reason, long timestamp) {
        super(_id, invokerId, timestamp);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
