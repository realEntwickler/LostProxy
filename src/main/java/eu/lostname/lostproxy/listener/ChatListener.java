/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ChatListener.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EMuteEntryType;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.bkms.IMute;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            IPlayer iPlayer = new IPlayer(player.getUniqueId());

            if (!event.getMessage().startsWith("/")) {
                IMute iMute = LostProxy.getInstance().getMuteManager().getMute(player.getUniqueId());
                if (iMute != null) {
                    event.setCancelled(true);

                    if (iMute.getDuration() == -1) {
                        if (player.hasPermission("lostproxy.bkms.bypasschat")) {
                            return;
                        }
                        player.sendMessage(new MessageBuilder($.BKMS + "Du bist §4permanent §7gemutet§7.").build());
                        player.sendMessage(new MessageBuilder("§8» §7Grund §8» §c" + iMute.getReason()).build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                    } else if (iMute.getEnd() > System.currentTimeMillis()) {
                        if (player.hasPermission("lostproxy.bkms.bypasschat")) {
                            return;
                        }
                        player.sendMessage(new MessageBuilder($.BKMS + "Du bist §ctemporär §7gemutet§7.").build());
                        player.sendMessage(new MessageBuilder("§8» §7Grund §8» §c" + iMute.getReason()).build());
                        player.sendMessage(new MessageBuilder("§8» §7Verleibende Zeit §8» §c" + LostProxy.getInstance().getMuteManager().calculateRemainingTime(iMute.getEnd())).build());
                        player.sendMessage(new MessageBuilder("§8» §7Läuft ab am §8» §c" + new SimpleDateFormat("dd.MM.yyyy").format(iMute.getEnd()) + " §7um §c" + new SimpleDateFormat("HH:mm:ss").format(iMute.getEnd()) + " §7Uhr").build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                    } else if (iMute.getEnd() < System.currentTimeMillis()) {
                        LostProxy.getInstance().getMuteManager().deleteMute(iMute);
                        IMuteHistory iMuteHistory = LostProxy.getInstance().getHistoryManager().getMuteHistory(player.getUniqueId());
                        iMuteHistory.addEntry(new IMuteEntry(EMuteEntryType.UNMUTE_ENTRY, player.getUniqueId(), "console", System.currentTimeMillis(), "MUTE_EXPIRED", 0, null, 0));
                        LostProxy.getInstance().getHistoryManager().saveMuteHistory(iMuteHistory);
                        LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(all -> {
                            all.sendMessage(new MessageBuilder($.BKMS + "§4BKM-System" + " §8➼ " + iPlayer.getDisplay() + player.getName()).build());
                            all.sendMessage(new MessageBuilder("§8» §7Typ §8» §aUnmute").build());
                            all.sendMessage(new MessageBuilder("§8» §7Grund §8» §eAbgelaufen").build());
                            all.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        });
                    }
                }

            }
        }
    }
}
