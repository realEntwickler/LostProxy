/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:27
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IBanHistory.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.historyandentries.ban;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IBanHistory extends IHistory {

    private final ArrayList<IBanEntry> history;

    public IBanHistory(UUID uniqueId, ArrayList<IBanEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IBanEntry> getHistory() {
        return history;
    }

    public void addEntry(IBanEntry iBanEntry) {
        history.add(iBanEntry);
    }
}
