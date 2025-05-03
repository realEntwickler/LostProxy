/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.01.2021 @ 23:00:34
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanReasonsCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BanReasonsCommand extends Command implements TabExecutor {
    public BanReasonsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length == 5 || strings.length >= 7) {
            sendHelpMessage(commandSender);
        } else if (strings.length == 1) {
            if ("list".equals(strings[0])) {
                if (commandSender.hasPermission("lostproxy.command.banreasons.list")) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Folgende §cBanngründe §7sind §eregistriert§8:").build());
                    LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().sorted(Comparator.comparingInt(IReason::getId)).forEach(iBanReason -> commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §e" + iBanReason.getId() + " §8" + $.arrow + " §e" + iBanReason.getName()).addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§aKlicke §7für mehr Infos").build()));
                } else {
                    commandSender.sendMessage($.NO_PERMISSION($.BKMS));
                }
            } else {
                try {
                    IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                    if (iBanReason != null) {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Informationen zum Banngrund§8:").build());
                        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §7Name §8" + $.arrow + " §c" + iBanReason.getName()).build());
                        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §7ID §8" + $.arrow + " §c" + iBanReason.getId()).build());
                        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §7Zeit §8" + $.arrow + " §c" + (iBanReason.getTime() == -1 ? "permanent" : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit()))).build());
                        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §7Berechtigung §8" + $.arrow + " §c" + iBanReason.getPermission()).build());
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Banngrund wurde §cnicht gefunden§7.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                }
            }
        } else if (strings.length == 2) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Soll der Banngrund §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7wirklich gelöscht werden§8? ").addExtra(new MessageBuilder($.BKMS + "§a☑").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId() + " delete confirmed").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§aKlicke §7zum löschen").build()).build());

                            if (!LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().add(commandSender.getName());
                            }
                        } else {
                            commandSender.sendMessage($.NO_PERMISSION($.BKMS));
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Banngrund wurde §cnicht §7gefunden.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
            }
        } else if (strings.length == 3) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            if (LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                if (strings[2].equalsIgnoreCase("confirmed")) {
                                    LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().remove(commandSender.getName());
                                    LostProxy.getInstance().getReasonManager().deleteBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();

                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Banngrund §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7wurde §aerfolgreich §7gelöscht.").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt.").build());
                            }
                        } else {
                            commandSender.sendMessage($.NO_PERMISSION($.BKMS));
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
            }
        } else if (strings.length == 4) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("set")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.set")) {
                            switch (strings[2]) {
                                case "id":
                                    int newId = Integer.parseInt(strings[3]);
                                    iBanReason.setId(newId);

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue ID des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + iBanReason.getId() + "§7.").build());
                                    break;
                                case "name":
                                    String oldName = iBanReason.getName();
                                    iBanReason.setName(strings[3].replaceAll("_", " "));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der neue Name des Banngrunds §e" + oldName + " §7lautet nun §a" + iBanReason.getName() + "§7.").build());
                                    break;
                                case "time":
                                    iBanReason.setTime(Long.parseLong(strings[3]));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der neue Zeitwert des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + iBanReason.getTime() + "§7.").build());

                                    break;
                                case "timeunit":
                                    ETimeUnit eTimeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);
                                    if (eTimeUnit == null) {
                                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden.").build());
                                        return;
                                    }

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue Zeiteinheit des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + ETimeUnit.getDisplayName(0, eTimeUnit) + "§7.").build());
                                    break;
                                case "permission":
                                    iBanReason.setPermission(strings[3]);

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue Berechtigung des Banngrunds §e" + iBanReason.getName() + " §7ist nun §a" + iBanReason.getPermission()+ "§7.").build());
                                    break;
                                default:
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                                    break;
                            }
                        } else {
                            commandSender.sendMessage($.NO_PERMISSION($.BKMS));
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
            }
        } else {
            if (strings[0].equalsIgnoreCase("add")) {
                if (commandSender.hasPermission("lostproxy.command.banreasons.add")) {
                    try {
                        int id = Integer.parseInt(strings[1]);

                        if (LostProxy.getInstance().getReasonManager().getBanReasonByID(id) == null) {
                            String name = strings[2].replaceAll("_", " ");
                            int time = Integer.parseInt(strings[3]);
                            ETimeUnit timeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.name().equalsIgnoreCase(strings[4])).findFirst().orElse(null);

                            if (timeUnit != null) {
                                String permission = strings[5];
                                IBanReason iBanReason = new IBanReason(id, name, time, timeUnit, permission);

                                LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §aerfolgreich §7den Banngrund §e" + iBanReason.getName() + " §7mit der ID §e" + iBanReason.getId() + " §7erstellt.").build());
                            } else {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht gefunden§7.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Es §cexistiert bereits §7ein Banngrund mit der ID §e" + id + "§7.").build());
                        }
                    } catch (NumberFormatException numberFormatException) {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
                    }
                } else {
                    commandSender.sendMessage($.NO_PERMISSION($.BKMS));
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos.").build());
            }
        }
    }

    private static void sendHelpMessage(CommandSender commandSender)
    {
        commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung §8" + $.arrow + " §c/banreasons").build());
        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §c/banreasons list §8" + $.arrow + " §7Listet dir alle Banngruende auf").build());
        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §c/banreasons add <ID> <Name> <Zeit> <Zeiteinheit> <Berechtigung> §8" + $.arrow + " §7Fügt einen neuen Banngrund hinzu").build());
        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §c/banreasons <ID> §8" + $.arrow + " §7Zeigt Informationen über einen Banngrund an").build());
        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §c/banreasons <ID> set <id,name,time,timeunit,permission> <Wert> §8" + $.arrow + " §7Bearbeitet einen Banngrund").build());
        commandSender.sendMessage(new MessageBuilder("§c" + $.littleDot + " §c/banreasons <ID> delete §8" + $.arrow + " §7Löscht einen Banngrund").build());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.addAll(Arrays.asList("list", "add"));
            LostProxy.getInstance().getReasonManager().getRegistedBanReasons().forEach(one -> list.add(String.valueOf(one.getId())));
            list.removeIf(s -> !s.toLowerCase().startsWith(strings[0].toLowerCase()));
        } else if (strings.length == 2) {
            try {
                int check = Integer.parseInt(strings[0]);
                list.addAll(Arrays.asList("set", "delete"));
                list.removeIf(s -> !s.toLowerCase().startsWith(strings[1].toLowerCase()));
            } catch (NumberFormatException ignored) {
            }
        } else if (strings.length == 3) {
            try {
                int check = Integer.parseInt(strings[0]);
                if (strings[1].equalsIgnoreCase("set")) {
                    list.addAll(Arrays.asList("id", "name", "time", "timeunit", "permission"));
                    list.removeIf(s -> !s.toLowerCase().startsWith(strings[2].toLowerCase()));
                }
            } catch (NumberFormatException ignored) {
            }
        } else if (strings.length == 4) {
            if (strings[2].equalsIgnoreCase("timeunit")) {
                Arrays.stream(ETimeUnit.values()).forEach(one -> list.add(one.name()));
                list.removeIf(s -> !s.toLowerCase().startsWith(strings[3].toLowerCase()));
            }
        } else if (strings.length == 5) {
            Arrays.stream(ETimeUnit.values()).forEach(one -> list.add(one.name()));
            list.removeIf(s -> !s.toLowerCase().startsWith(strings[4].toLowerCase()));
        }
        return list;
    }
}