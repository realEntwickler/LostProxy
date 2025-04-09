/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ELocale.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.enums;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ELocale {

    GERMAN("Deutsch", "de_DE"),
    AMERICAN_ENGLISH("English", "en_US"),
    SPANISH("Español", "en_ES");

    private final String displayName;
    private final String fileName;

    ELocale(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public String getMessage(String key) {
        String[] s = fileName.split("_");
        ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale(s[0], s[1]));
        return bundle.getString(key);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }
}
