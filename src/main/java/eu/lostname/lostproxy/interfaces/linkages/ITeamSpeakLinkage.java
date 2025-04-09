/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:07
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ITeamSpeakLinkage.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces.linkages;

import eu.lostname.lostproxy.interfaces.ILinkage;

import java.util.UUID;

public class ITeamSpeakLinkage extends ILinkage {

    private String identity;

    public ITeamSpeakLinkage(String playerName, UUID _id, long creationTimestamp, String identity) {
        super(playerName, _id, creationTimestamp);
        this.identity = identity;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
