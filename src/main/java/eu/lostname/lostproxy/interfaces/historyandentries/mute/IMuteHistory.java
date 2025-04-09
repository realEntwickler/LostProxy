/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:48
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IMuteHistory.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.historyandentries.mute;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IMuteHistory extends IHistory {

    private final ArrayList<IMuteEntry> history;

    public IMuteHistory(UUID uniqueId, ArrayList<IMuteEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IMuteEntry> getHistory() {
        return history;
    }

    public void addEntry(IMuteEntry iMuteEntry) {
        history.add(iMuteEntry);
    }
}
