/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:06
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IMute.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.bkms;

import java.util.UUID;

public class IMute {

    private final String _id;
    private final long timestamp;
    private final long duration;
    private final boolean verified;
    private String playerName;
    private String invoker;
    private String reason;
    private long end;

    public IMute(UUID _id, String playerName, String invoker, String reason, long timestamp, long duration, boolean verified) {
        this._id = _id.toString();
        this.playerName = playerName;
        this.invoker = invoker;
        this.reason = reason;
        this.timestamp = timestamp;
        this.duration = duration;
        this.end = (duration == -1 ? -1 : timestamp + duration);
        this.verified = verified;
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getInvoker() {
        return invoker;
    }

    public void setInvoker(String invoker) {
        this.invoker = invoker;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean isVerified() {
        return verified;
    }

}
