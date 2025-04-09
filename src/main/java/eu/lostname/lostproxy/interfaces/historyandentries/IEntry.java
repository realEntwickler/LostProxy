/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:54
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IEntry.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.historyandentries;

import java.util.UUID;

public class IEntry {

    private final String _id;
    private final String invokerId;
    private final long timestamp;

    public IEntry(UUID _id, String invokerId, long timestamp) {
        this._id = _id.toString();
        this.invokerId = invokerId;
        this.timestamp = timestamp;
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public String getInvokerId() {
        return invokerId;
    }

    public boolean isInvokerConsole() {
        return invokerId.equalsIgnoreCase("console");
    }

    public long getTimestamp() {
        return timestamp;
    }
}
