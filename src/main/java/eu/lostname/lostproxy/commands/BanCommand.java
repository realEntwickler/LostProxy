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
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EBanEntryType;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanEntry;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.utils.CloudServices;
import eu.lostname.lostproxy.utils.Prefix;
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
                        if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getIPermissionGroup().getName().toLowerCase())) {
                            if (LostProxy.getInstance().getReasonManager().getRegistedBanReasons().size() > 0) {
                                List<IBanReason> iBanReasons = LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission())).collect(Collectors.toList());

                                if (iBanReasons.size() > 0) {
                                    iBanReasons.sort(Comparator.comparingInt(IReason::getId));

                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Verfügbare Banngründe§8:").build());
                                    iBanReasons.forEach(iBanReason -> {
                                        TextComponent tc1 = new MessageBuilder("§8┃ §7" + iBanReason.getId() + " §8┃ §c" + iBanReason.getName() + " §8» ").build();
                                        TextComponent tc2 = new MessageBuilder("§7[§aKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + targetIPlayer.getPlayerName() + " " + iBanReason.getId()).build();
                                        tc1.addExtra(tc2);
                                        commandSender.sendMessage(tc1);
                                    });
                                    commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeine §7Banngründe für dich verfügbar§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Zurzeit sind §ckeine §7Banngründe registriert§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7zu §7bannen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gebannt§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du kannst dich §cnicht §7selber bannen§8.").build());
            }
        } else if (strings.length == 2) {
            if (!commandSender.getName().equalsIgnoreCase(strings[0])) {
                UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[0]);
                if (uuid != null) {
                    IPlayer targetIPlayer = new IPlayer(uuid);
                    IBan iBan = LostProxy.getInstance().getBanManager().getBan(uuid);
                    if (iBan == null) {
                        if (commandSender.hasPermission("lostproxy.command.ban.group." + targetIPlayer.getIPermissionGroup().getName().toLowerCase())) {
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
                                                target.disconnect(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                                        "\n" +
                                                        "§7Du wurdest §4permanent §7vom Netzwerk §4gebannt§8." +
                                                        "\n" +
                                                        "\n" +
                                                        "§7Grund §8➡ §e" + iBanReason.getName() +
                                                        "\n" +
                                                        "\n" +
                                                        "§7Für weitere Fragen oder zum Stellen eines Entbannungsantrags besuche das Forum§8!" +
                                                        "\n" +
                                                        " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                                        "\n" +
                                                        "\n" +
                                                        "§8§m--------------------------------------§r").build());
                                            } else {
                                                target.disconnect(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                                        "\n" +
                                                        "§7Du wurdest §4temporär §7vom Netzwerk §4gebannt§8." +
                                                        "\n" +
                                                        "\n" +
                                                        "§7Grund §8➡ §e" + iBanReason.getName() +
                                                        "\n" +
                                                        "§7Verbleibende Zeit §8➡ §c" + LostProxy.getInstance().getBanManager().calculateRemainingTime(ban.getEnd()) +
                                                        "\n" +
                                                        "§7Läuft ab am §8➡ §c" + new SimpleDateFormat("dd.MM.yyyy").format(ban.getEnd()) + " um §c" + new SimpleDateFormat("HH:mm:ss").format(ban.getEnd()) + " §7Uhr" +
                                                        "\n" +
                                                        "\n" +
                                                        "§7Für weitere Fragen oder zum Stellen eines Entbannungsantrags besuche das Forum§8!" +
                                                        "\n" +
                                                        " §8» §cforum§8.§clostname§8.§ceu §8«" +
                                                        "\n" +
                                                        "\n" +
                                                        "§8§m--------------------------------------§r").build());
                                            }
                                        }

                                        if (commandSender instanceof ProxiedPlayer) {
                                            ProxiedPlayer sender = (ProxiedPlayer) commandSender;
                                            IPlayer iPlayerSync = new IPlayer(sender.getUniqueId());
                                            LostProxy.getInstance().getTeamManager().sendBanNotify(iPlayerSync.getDisplay() + sender.getName(), targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);
                                        } else {
                                            LostProxy.getInstance().getTeamManager().sendBanNotify("System", targetIPlayer.getDisplay() + targetIPlayer.getPlayerName(), iBanReason);
                                        }


                                        IBanHistory iBanHistory = LostProxy.getInstance().getHistoryManager().getBanHistory(uuid);
                                        iBanHistory.addEntry(new IBanEntry(EBanEntryType.BAN_ENTRY, uuid, invoker, currentTimeMillis, iBanReason.getName(), iBanReason.getTime(), iBanReason.getETimeUnit(), end));

                                        LostProxy.getInstance().getHistoryManager().saveBanHistory(iBanHistory);
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7wegen §e" + iBanReason.getName() + " §7für §c" + "%time%" + (ban.getEnd() == -1 ? "§4permanent" : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit()) + " §7gebannt§8.")).build());
                                    } else {
                                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du darfst den Banngrund §e" + iBanReason.getName() + " §cnicht §7benutzen§8.").build());
                                    }
                                } else {
                                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                                }
                            } catch (NumberFormatException numberFormatException) {
                                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Zahl angegeben§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du hast §ckeine §7Rechte§8, §7um " + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7zu bannen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + targetIPlayer.getDisplay() + targetIPlayer.getPlayerName() + " §7ist §cbereits §7gebannt§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Du kannst dich §cnicht §7selber bannen§8.").build());
            }
        } else {
            sendHelp(commandSender);
        }
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage(new MessageBuilder(Prefix.BKMS + "Benutzung§8: §c/ban <Spieler> (ID)").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
    }

    @SuppressWarnings("deprecation")
    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            CloudServices.PLAYER_MANAGER.getOnlinePlayers().stream().filter(one -> one.getName().startsWith(strings[0])).forEach(one -> list.add(one.getName()));
        } else if (strings.length == 2) {
            LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().filter(one -> commandSender.hasPermission(one.getPermission()) && String.valueOf(one.getId()).startsWith(strings[1])).forEach(one -> list.add(String.valueOf(one.getId())));
        }
        return list;
    }
}
