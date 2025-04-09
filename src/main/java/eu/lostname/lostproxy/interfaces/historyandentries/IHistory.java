/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:58
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IHistory.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.historyandentries;

import java.util.UUID;

public class IHistory {

    private final String _id;

    public IHistory(UUID _id) {
        this._id = _id.toString();
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }
}
