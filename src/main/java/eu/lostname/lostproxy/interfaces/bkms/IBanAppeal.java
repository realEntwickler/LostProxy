/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:37:56
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IBanAppeal.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.bkms;

import java.util.UUID;

public class IBanAppeal {

    private final String _id;
    private final String invoker;
    private final long timestamp;

    public IBanAppeal(UUID _id, String invoker, long timestamp) {
        this._id = _id.toString();
        this.invoker = invoker;
        this.timestamp = timestamp;
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public String getInvoker() {
        return invoker;
    }

    public long getTimestamp() {
        return timestamp;
    }
}