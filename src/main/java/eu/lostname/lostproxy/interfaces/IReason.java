/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:40
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IReason.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;


import eu.lostname.lostproxy.enums.EReasonType;

public class IReason {

    private int _id;
    private String name;
    private EReasonType eReasonType;

    public IReason(int _id, String name, EReasonType eReasonType) {
        this._id = _id;
        this.name = name;
        this.eReasonType = eReasonType;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EReasonType getReasonType() {
        return eReasonType;
    }

    public void setReasonType(EReasonType eReasonType) {
        this.eReasonType = eReasonType;
    }
}
