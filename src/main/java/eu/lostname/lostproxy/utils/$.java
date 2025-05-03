/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 11.01.2021 @ 22:23:16
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * $.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.utils;


import net.md_5.bungee.api.chat.TextComponent;

public class $ {

    public static final String LOSTPROXY = "§4•§c● LostProxy §8┃ §7";
    public static final String LOSTNAME = "§6•§e● LostName §8┃ §7";
    public static final String NOTIFICATIONS = "§3•§b● Notify §8┃ §7";
    public static final String WARTUNGEN = "§d•§5● Maintenance §8┃ §7";
    public static final String BKMS = "§c•§4● BKMS §8┃ §7";
    //public static final String TEAMSPEAK = "•● §8┃ §7";
    public static final String TMS = "§6•§e● Team §8┃ §7";
    public static final String TEAM_CHAT = "§6•§e● TeamChat §8┃ §7";
    public static final String FRIENDS = "§2•§a● Freunde §8┃ §7";
    public static final String BROADCAST = "§6•§e● Broadcast §8┃ §7";
    public static final String PARTY = "§d•§5● Party §8┃ §7";
    public static final String CLANS = "§6•§e● Clans §8┃ §7";
    public static final String REPORT = "§4•§c● Report §8┃ §7";
    public static final String littleDot = "•";
    public static final String bigDot = "●";
    public static final String line = "┃";
    public static final String arrow = "➜";
    public static TextComponent PLAYER_NOT_FOUND(String prefix) { return new TextComponent(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde §cnicht gefunden§7.")); }
    public static TextComponent NO_PERMISSION(String prefix) { return new TextComponent(TextComponent.fromLegacyText(prefix + "Dafür hast du §ckeine Berechtigung§7.")); }
}
