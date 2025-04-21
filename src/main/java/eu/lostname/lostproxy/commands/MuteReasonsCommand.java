/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:51
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MuteReasonsCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.ETimeUnit;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.interfaces.bkms.IMuteReason;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MuteReasonsCommand extends Command implements TabExecutor {
    public MuteReasonsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0 || strings.length == 5 || strings.length >= 7) {
            commandSender.sendMessage(new MessageBuilder($.BKMS + "Benutzung von §c/mutereasons§8:").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/mutereasons list §8┃ §7Liste dir alle Mutegruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons list").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/mutereasons add <ID> <Name> <Zeit> <Zeiteinheit> <Permission> §8┃ §7Liste dir alle Mutegruende auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons add NAME ZEIT ZEITEINHEIT PERMISSION").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/mutereasons <ID> §8┃ §7Zeige Informationen über einen Mutegrund an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons ID").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/mutereasons <ID> set <id,name,time,timeunit,permission> <Wert> §8┃ §7Bearbeite einen Mutegrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8» §c/mutereasons <ID> delete §8┃ §7Lösche einen Mutegrund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mutereasons ID set ").build());
            commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
        } else if (strings.length == 1) {
            if ("list".equals(strings[0])) {
                if (commandSender.hasPermission("lostproxy.command.mutereasons.list")) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Folgende Mutegründe sind registriert§8:").build());
                    LostProxy.getInstance().getReasonManager().getRegistedMuteReasons().stream().sorted(Comparator.comparingInt(IReason::getId)).forEach(iMuteReason -> commandSender.sendMessage(new MessageBuilder("§8» §e" + iMuteReason.getId() + " §8» §e" + iMuteReason.getName()).addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mutereasons " + iMuteReason.getId()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §7Klicke diese Nachricht§8, §7um genaue Informationen zu diesem Mutegrund zu erhalten§7.").build()));
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                }
            } else {
                try {
                    IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                    if (iMuteReason != null) {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Informationen zum angegebenen Mutegrund§8:").build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7Name §8┃ §c" + iMuteReason.getName()).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7ID §8┃ §c" + iMuteReason.getId()).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7Mutezeit §8┃ §c" + (iMuteReason.getTime() == -1 ? "permanent" : iMuteReason.getTime() + " " + ETimeUnit.getDisplayName(iMuteReason.getTime(), iMuteReason.getETimeUnit()))).build());
                        commandSender.sendMessage(new MessageBuilder("§8» §7Berechtigung §8┃ §c" + iMuteReason.getPermission()).build());
                        commandSender.sendMessage(new MessageBuilder("§8§m--------------------§r").build());

                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§7.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                }
            }
        } else if (strings.length == 2) {
            try {
                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                if (iMuteReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.mutereasons.delete")) {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Soll der Mutegrund §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7tatsächlich gelöscht werden§7.").build());
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "§7[§a§lKlick§7]").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/mutereasons " + iMuteReason.getId() + " delete confirmed").build());

                            if (!LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().contains(commandSender.getName())) {
                                LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().add(commandSender.getName());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§7.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
            }
        } else if (strings.length == 3) {
            try {
                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                if (iMuteReason != null) {
                    if (strings[1].equalsIgnoreCase("delete")) {
                        if (commandSender.hasPermission("lostproxy.command.mutereasons.delete")) {
                            if (LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().contains(commandSender.getName())) {
                                if (strings[2].equalsIgnoreCase("confirmed")) {
                                    LostProxy.getInstance().getReasonManager().getMuteReasonCommandProcess().remove(commandSender.getName());
                                    LostProxy.getInstance().getReasonManager().deleteMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der Mutegrund §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7wurde erfolgreich §cgelöscht§7.").build());
                                } else {
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                                }
                            } else {
                                commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §ckeine §eVerifizierung §7für diesen §eProzess §7beantragt§7.").build());
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§7.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
            }
        } else if (strings.length == 4) {
            try {
                IMuteReason iMuteReason = LostProxy.getInstance().getReasonManager().getMuteReasonByID(Integer.parseInt(strings[0]));

                if (iMuteReason != null) {
                    if (strings[1].equalsIgnoreCase("set")) {
                        if (commandSender.hasPermission("lostproxy.command.mutereasons.set")) {
                            switch (strings[2]) {
                                case "id":
                                    int newId = Integer.parseInt(strings[3]);
                                    iMuteReason.setId(newId);

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue ID des Mutegrunds §e" + iMuteReason.getName() + " §7lautet nun §a" + newId + "§7.").build());
                                    break;
                                case "name":
                                    iMuteReason.setName(strings[3].replaceAll("_", " "));

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der neue Name des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7lautet nun §a" + iMuteReason.getName() + "§7.").build());
                                    break;
                                case "time":
                                    iMuteReason.setTime(Long.parseLong(strings[3]));

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der neue Zeitwert des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7ist nun §a" + iMuteReason.getTime() + "§7.").build());
                                    break;
                                case "timeunit":
                                    ETimeUnit eTimeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.toString().equalsIgnoreCase(strings[3])).findFirst().orElse(null);
                                    if (eTimeUnit == null) {
                                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§7.").build());
                                        return;
                                    }

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue Zeiteinheit des Mutegrunds §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7ist nun §a" + ETimeUnit.getDisplayName(0, eTimeUnit) + "§7.").build());
                                    break;
                                case "permission":
                                    iMuteReason.setPermission(strings[3]);

                                    LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                                    LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Die neue Berechtigung für den Mutegrund §e" + iMuteReason.getName() + " §8(§e" + iMuteReason.getId() + "§8) §7ist nun §a" + iMuteReason.getPermission() + "§7.").build());
                                    break;
                                default:
                                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                                    break;
                            }
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Der angegebene Mutegrund wurde §cnicht §7gefunden§7.").build());
                }
            } catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
            }
        } else {
            if (strings[0].equalsIgnoreCase("add")) {
                try {
                    int id = Integer.parseInt(strings[1]);

                    if (LostProxy.getInstance().getReasonManager().getMuteReasonByID(id) == null) {
                        String name = strings[2].replaceAll("_", " ");
                        int time = Integer.parseInt(strings[3]);
                        ETimeUnit eTimeUnit = Arrays.stream(ETimeUnit.values()).filter(one -> one.name().equalsIgnoreCase(strings[4])).findFirst().orElse(null);

                        if (eTimeUnit != null) {
                            String permission = strings[5];
                            IMuteReason iMuteReason = new IMuteReason(id, name, time, eTimeUnit, permission);

                            LostProxy.getInstance().getReasonManager().saveMuteReason(iMuteReason);
                            LostProxy.getInstance().getReasonManager().reloadMuteReasons();
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast §aerfolgreich §7den Mutegrund §e" + name + " §7mit der ID §e" + id + " §7erstellt§7.").build());
                        } else {
                            commandSender.sendMessage(new MessageBuilder($.BKMS + "Die angegebene §eZeiteinheit §7wurde §cnicht §7gefunden§7.").build());
                        }
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.BKMS + "Es existiert §cbereits §7ein Mutegrund mit der ID §e" + id + "§7.").build());
                    }
                } catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new MessageBuilder($.BKMS + "Du hast als Argument §ckeine §7Zahl angegeben§7.").build());
                }
            } else {
                commandSender.sendMessage(new MessageBuilder($.BKMS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.addAll(Arrays.asList("list", "add"));
            LostProxy.getInstance().getReasonManager().getRegistedMuteReasons().forEach(one -> list.add(String.valueOf(one.getId())));
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