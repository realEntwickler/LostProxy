/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:24
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ILinkage.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;

import java.util.UUID;

public class ILinkage {

    private String playerName;
    private UUID _id;
    private long creationTimestamp;

    public ILinkage(String playerName, UUID _id, long creationTimestamp) {
        this.playerName = playerName;
        this._id = _id;
        this.creationTimestamp = creationTimestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return _id;
    }

    public void setUuid(UUID uuid) {
        this._id = uuid;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
