/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * PreLoginListener.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class PreLoginListener implements Listener {

    @EventHandler
    public void onPreLogin(LoginEvent event) {
        IBan iBan = LostProxy.getInstance().getBanManager().getBan(event.getConnection().getUniqueId());
        if (iBan != null) {
            if (iBan.getEnd() == -1) {
                event.setCancelled(true);
                event.setCancelReason(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                        "\n" +
                        "§7Du bist §4§npermanent§r §7vom Netzwerk §4gebannt§7." +
                        "\n" +
                        "\n" +
                        "§7Grund §8➡ §e" + iBan.getReason() +
                        "\n" +
                        "\n" +
                        "§7FÜr weitere Fragen oder zum Stellen eines Entbannugsantrag besuche das Forum§8!" +
                        "\n" +
                        " §8» §cforum§7.§clostname§7.§ceu §8«" +
                        "\n" +
                        "\n" +
                        "§8§m--------------------------------------§r").build());
            } else if (iBan.getEnd() > System.currentTimeMillis()) {
                String remainingTime = LostProxy.getInstance().getBanManager().calculateRemainingTime(iBan.getEnd());

                event.setCancelled(true);
                event.setCancelReason(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                        "\n" +
                        "§7Du bist §4temporär §7vom Netzwerk §4gebannt§7." +
                        "\n" +
                        "\n" +
                        "§7Grund §8➡ §e" + iBan.getReason() +
                        "\n" +
                        "§7Verbleibende Zeit §8➡ §c" + remainingTime +
                        "\n" +
                        "§7Läuft ab am §8➡ §c" + new SimpleDateFormat("dd.MM.yyyy").format(iBan.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(iBan.getEnd()) + " §7Uhr" +
                        "\n" +
                        "\n" +
                        "§7FÜr weitere Fragen oder zum Stellen eines Entbannugsantrag besuche das Forum§8!" +
                        "\n" +
                        " §8» §cforum§7.§clostname§7.§ceu §8«" +
                        "\n" +
                        "\n" +
                        "§8§m--------------------------------------§r").build());
            }
        }
    }
}
