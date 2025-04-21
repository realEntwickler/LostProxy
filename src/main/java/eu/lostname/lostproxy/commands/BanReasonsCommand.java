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
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung von §c/banreasons§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons list §8» §7Listet dir alle Banngruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons list").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons add <ID> <Name> <Zeit> <Zeiteinheit> <Berechtigung> §8» §7Fügt einen neuen Banngrund hinzu").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons add ID NAME TIME TIMEUNIT PERMISSION").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> §8» §7Zeigt Informationen über einen Banngrund an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> set <id,name,time,timeunit,permission> <Wert> §8» §7Bearbeitet einen Banngrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID set ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
            commandSender.sendMessage(new MessageBuilder("§8┃ §c/banreasons <ID> delete §8» §7Löscht einen Banngrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/banreasons ID delete").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if ("list".equals(strings[0])) {
                if (commandSender.hasPermission("lostproxy.command.banreasons.list")) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Folgende Banngründe sind registriert§8:").build());
                    LostProxy.getInstance().getReasonManager().getRegistedBanReasons().stream().sorted(Comparator.comparingInt(IReason::getId)).forEach(iBanReason -> commandSender.sendMessage(new MessageBuilder("§8┃ §e" + iBanReason.getId() + " §8» §e" + iBanReason.getName()).addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §7Klicke diese Nachricht§8, §7um genaue Informationen zu diesem Banngrund zu erhalten§8.").build()));
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                }
            } else {
                try {
                    IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                    if (iBanReason != null) {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Informationen zum angegebene Banngrund§8:").build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Name §8» §c" + iBanReason.getName()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7ID §8» §c" + iBanReason.getId()).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Zeit §8» §c" + (iBanReason.getTime() == -1 ? "permanent" : iBanReason.getTime() + " " + ETimeUnit.getDisplayName(iBanReason.getTime(), iBanReason.getETimeUnit()))).build());
                        commandSender.sendMessage(new MessageBuilder("§8┃ §7Berechtigung §8» §c" + iBanReason.getPermission()).build());
                        commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());

                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            }
        } else if (strings.length == 2) {
            try {
                IBanReason iBanReason = LostProxy.getInstance().getReasonManager().getBanReasonByID(Integer.parseInt(strings[0]));

                if (iBanReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.banreasons.delete")) {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Soll der Banngrund §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7tatsächlich gelöscht werden§8?").build());
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "§7[§aKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/banreasons " + iBanReason.getId() + " delete confirmed").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());

                            if (!LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getReasonManager().getBanReasonCommandProcess().add(commandSender.getName());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
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

                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Banngrund §e" + iBanReason.getName() + " §8(§e" + iBanReason.getId() + "§8) §7wurde §aerfolgreich §7gelöscht§8.").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§8.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
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
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue ID des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + iBanReason.getId() + "§8.").build());
                                    break;
                                case "name":
                                    String oldName = iBanReason.getName();
                                    iBanReason.setName(strings[3].replaceAll("_", " "));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der neue Name des Banngrunds §e" + oldName + " §7lautet nun §a" + iBanReason.getName() + "§8.").build());
                                    break;
                                case "time":
                                    iBanReason.setTime(Long.parseLong(strings[3]));

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der neue Zeitwert des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + iBanReason.getTime() + "§8.").build());

                                    break;
                                case "timeunit":
                                    ETimeUnit eTimeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);
                                    if (eTimeUnit == null) {
                                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§8.").build());
                                        return;
                                    }

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue Zeiteinheit des Banngrunds §e" + iBanReason.getName() + " §7lautet nun §a" + ETimeUnit.getDisplayName(0, eTimeUnit) + "§8.").build());
                                    break;
                                case "permission":
                                    iBanReason.setPermission(strings[3]);

                                    LostProxy.getInstance().getReasonManager().saveBanReason(iBanReason);
                                    LostProxy.getInstance().getReasonManager().reloadBanReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue Berechtigung des Banngrunds §e" + iBanReason.getName() + " §7ist nun §a" + iBanReason.getPermission()+ "§8.").build());
                                    break;
                                default:
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                                    break;
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Banngrund wurde §cnicht §7gefunden§8.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        } else {
            if (strings[0].equalsIgnoreCase("add")) {
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
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §aerfolgreich §7den Banngrund §e" + iBanReason.getName() + " §7mit der ID §e" + iBanReason.getId() + " §7erstellt§8.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§8.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Es existiert §cbereits §7ein Banngrund mit der ID §e" + id + "§8.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§8.").build());
            }
        }
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