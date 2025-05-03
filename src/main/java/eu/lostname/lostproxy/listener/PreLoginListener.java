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
import eu.lostname.lostproxy.builder.DisconnectScreenBuilder;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class PreLoginListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPreLogin(LoginEvent event) {
        IBan iBan = LostProxy.getInstance().getBanManager().getBan(event.getConnection().getUniqueId());
        if (iBan != null) {
            if (iBan.getEnd() == -1) {
                event.setCancelled(true);
                event.setCancelReason(new DisconnectScreenBuilder()
                        .add("§7Du bist §4§npermanent§r §7vom Netzwerk §4gebannt§7.")
                        .newLine()
                        .newLine()
                        .add("§7Grund §8" + $.arrow + " §e" + iBan.getReason())
                        .build());
            } else if (iBan.getEnd() > System.currentTimeMillis()) {
                String remainingTime = LostProxy.getInstance().getBanManager().calculateRemainingTime(iBan.getEnd());

                event.setCancelled(true);
                event.setCancelReason(new DisconnectScreenBuilder()
                        .add("§7Du bist §4temporär §7vom Netzwerk §4gebannt§7.")
                        .newLine()
                        .newLine()
                        .add("§7Grund §8" + $.arrow + " §e" + iBan.getReason())
                        .newLine().add("§7Verbleibende Zeit §8" + $.arrow + " §c" + remainingTime)
                                .newLine().add("§7Läuft ab am §8" + $.arrow + " §c" + new SimpleDateFormat("dd.MM.yyyy").format(iBan.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(iBan.getEnd()) + " §7Uhr")
                        .build());
            }
        }
    }
}
