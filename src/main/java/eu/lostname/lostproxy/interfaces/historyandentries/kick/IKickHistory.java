/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:38:39
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IKickHistory.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.historyandentries.kick;

import eu.lostname.lostproxy.interfaces.historyandentries.IHistory;

import java.util.ArrayList;
import java.util.UUID;

public class IKickHistory extends IHistory {

    private final ArrayList<IKickEntry> history;

    public IKickHistory(UUID uniqueId, ArrayList<IKickEntry> history) {
        super(uniqueId);
        this.history = history;
    }

    public ArrayList<IKickEntry> getHistory() {
        return history;
    }

    public void addEntry(IKickEntry iKickEntry) {
        history.add(iKickEntry);
    }
}
