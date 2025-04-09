/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:37:49
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IBan.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.bkms;

import java.util.UUID;

public class IBan {

    private final String _id;
    private String playerName;
    private final long timestamp;
    private final long duration;
    private String invoker;
    private String reason;
    private long end;
    private final boolean verified;
    private IBanAppeal banAppeal;

    public IBan(UUID _id, String playerName, String invoker, String reason, long timestamp, long duration, boolean verified, IBanAppeal banAppeal) {
        this._id = _id.toString();
        this.playerName = playerName;
        this.invoker = invoker;
        this.reason = reason;
        this.timestamp = timestamp;
        this.duration = duration;
        this.end = (duration == -1 ? -1 : timestamp + duration);
        this.verified = verified;
        this.banAppeal = banAppeal;
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

    public boolean hasBanAppeal() {
        return banAppeal != null;
    }

    public IBanAppeal getBanAppeal() {
        return banAppeal;
    }

    public void setBanAppeal(IBanAppeal banAppeal) {
        this.banAppeal = banAppeal;
    }
}
