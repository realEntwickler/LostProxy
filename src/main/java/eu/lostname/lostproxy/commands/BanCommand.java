/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 24.01.2021 @ 19:41:43
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.DisconnectScreenBuilder;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.$;
import eu.lostname.lostproxy.utils.CloudServices;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BanCommand extends Command implements TabExecutor {

    public BanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            sendHelp(commandSender);
        } else if (strings.length == 1) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayer targetIPlayer = new IPlayer(uuid);
                    IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                    if (iBan == null) {
                        if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getPermissionGroup().getDisplayName().toLowerCase())) {
                            if (!LostProxy.getInstance().getReasonManager().getRegistedBanReasons().isEmpty()) {
                                List<IBanReason> iBanReasons = LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission())).collect(Collectors.toList());

                                if (!iBanReasons.isEmpty()) {
                                    iBanReasons.sort(Comparator.comparingInt(IReason::getId));

                                    commandSender.sendMessage(new MessageBuilder($.BKMS + targetIPlayer.getDisplaywithPlayername() + " §7kann wie folgt gebannt werden:").build());
                                    iBanReasons.forEach(iBanReason -> commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot +" §7" + iBanReason.getId() + " §8" + $.arrow + " §c" + iBanReason.getName() + " §8" + $.arrow + " ").addExtra(new MessageBuilder("§a☑").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + targetIPlayer.getPlayerName() + " " + iBanReason.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§aKlicke §7zum Bannen").build()).build()));
                                } else {
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Es sind §ckeine Banngründe verfügbar.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Es sind §ckeine Banngründe §7registriert.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplaywithPlayername() + " §7zu bannen.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits gebannt§7.").build());
                    }
                } else {
                    commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du kannst dich §cnicht selber bannen§7.").build());
            }
        } else if (strings.length == 2) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayer targetIPlayer = new IPlayer(uuid);
                    IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                    if (iBan == null) {
                        if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getPermissionGroup().getDisplayName().toLowerCase())) {
                            try {
                                int reasonId = Integer.parseInt(strings[1]);

                                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(reasonId);

                                if (iBanReason != null) {
                                    if (commandSender.hasPermission(iBanReason.getPermission())) {
                                        long currentTimeMillis = System.currentTimeMillis();
                                        long banDuration = (iBanReason.getTime() == -1 ? -1 : iBanReason.getETimeUnit().toMillis(iBanReason.getTime()));
                                        long end = (banDuration == -1 ? -1 : currentTimeMillis + banDuration);
                                        String invoker = (commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId().toString() : "console");
                                        IBan ban = new IBan(uuid, targetIPlayer.getPlayerName(), invoker, iBanReason.getName(), currentTimeMillis, banDuration, true, null);

                                        LostProxy.getInstance().getBanManager().insertBan(ban);

                                        if (targetIPlayer.isOnline()) {
                                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                            if (banDuration == -1) {
                                                target.disconnect(new DisconnectScreenBuilder()
                                                        .add("§7Du wurdest §4permanent §7vom Netzwerk §4gebannt§7.")
                                                        .newLine()
                                                        .newLine()
                                                        .add("§7Grund §8" + $.arrow + " §e" + iBanReason.getName())
                                                        .build());
                                            } else {
                                                target.disconnect(new DisconnectScreenBuilder()
                                                        .add("§7Du wurdest §4temporär §7vom Netzwerk §4gebannt§7.")
                                                        .newLine()
                                                        .newLine()
                                                        .add("§7Grund §8" + $.arrow + " §e" + iBanReason.getName())
                                                        .newLine().add("§7Verbleibende Zeit §8" + $.arrow + " §c" + LostProxy.getInstance().getBanManager().calculateRemainingTime(ban.getEnd()))
                                                        .newLine().add("§7Läuft ab am §8" + $.arrow + " §c" + new SimpleDateFormat("dd.MM.yyyy").format(ban.getEnd()) + " um §c" + new SimpleDateFormat("HH:mm:ss").format(ban.getEnd()) + " §7Uhr")
                                                        .build()
                                                );
                                            }
                                        }

                                        if (commandSender instanceof ProxiedPlayer sender) {
                                            IPlayer iPlayerSync = new IPlayer(sender.getUniqueId());
                                            LostProxy.getInstance().getTeamManager().sendBanNotify(iPlayerSync.getDisplay() + sender.getName(), targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);
                                        } else {
                                            LostProxy.getInstance().getTeamManager().sendBanNotify("§4System", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);
                                        }


                                        IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                        iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_ENTRY, uuid, invoker, currentTimeMillis, iBanReason.getName(), iBanReason.getTime(), iBanReason.getETimeUnit(), end));

                                        LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast " + targetIPlayer.getDisplaywithPlayername() + " §7wegen §e" + iBanReason.getName() + " §7für §c" + (ban.getEnd() == -1 ? "§4permanent" : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit()) + " §7gebannt.")).build());
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Du darfst den Banngrund " + iBanReason.getName() + " §cnicht benutzen§7.").build());
                                    }
                                } else {
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der §eBanngrund wurde §cnicht gefunden§7.").build());
                                }
                            } catch (NumberFormatException numberFormatException) {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine Zahl §7angegeben.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine Rechte§7, um " + targetIPlayer.getDisplaywithPlayername() + " §7zu §cbannen§7.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + targetIPlayer.getDisplaywithPlayername() + " §7ist §cbereits gebannt§7.").build());
                    }
                } else {
                    commandSender.sendMessage($.PLAYER_NOT_FOUND($.BKMS));
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du kannst dich §cnicht selber bannen§7.").build());
            }
        } else {
            sendHelp(commandSender);
        }
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung §8" + $.arrow + " §c/ban <Spieler> [ID]").build());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            CloudServices.PLAYER_MANAGER.onlinePlayers().players().stream().filter(one -> one.name().startsWith(strings[0])).forEach(one -> list.add(one.name()));
        } else if (strings.length == 2) {
            LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission()) && String.valueOf(one.getId()).startsWith(strings[1])).forEach(one -> list.add(String.valueOf(one.getId())));
        }
        return list;
    }
}
