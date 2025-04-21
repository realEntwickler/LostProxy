/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * TSCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class TSCommand extends Command implements TabExecutor {

    public TSCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        /*if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Benutzung von §b/ts§8:").build());
                player.sendMessage(new MessageBuilder("§8» §b/ts set <Identität> §8┃ §7Verknüfe manuell deine TeamSpeak Identität").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts set ").build());
                player.sendMessage(new MessageBuilder("§8» §b/ts unlink §8┃ §7Hebe die Teamspeak-Verknüpfung auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts unlink").build());
                player.sendMessage(new MessageBuilder("§8» §b/ts info §8┃ §7Zeige dir Informationen zu deiner TeamSpeak Verknüfung an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts info").build());
                if (player.hasPermission("lostproxy.command.ts.iinfo")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts iinfo <Identität> §8┃ §7Lasse dir Informationen zu einer TeamSpeak Identität anzeigen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts iinfo ").build());
                }
                if (player.hasPermission("lostproxy.command.ts.ninfo")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts ninfo <Spielernamen> §8┃ §7Lasse dir Informationen zu einem Spielernamen anzeigen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts ninfo ").build());
                }
                if (player.hasPermission("lostproxy.command.ts.delete")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts delete <Name> §8┃ §7Lösche die TeamSpeak Verknüpfung eines anderen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts delete Name").build());
                }
                if (player.hasPermission("lostproxy.command.ts.set")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts set <Rang> <ID> §8┃ §7Setzte einer Permission-Gruppe die dazugehörige TS-Servergruppen-ID").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts set ").build());
                }
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else if (strings.length == 1) {
                ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(player.getUniqueId());
                switch (strings[0]) {
                    case "unlink":
                        if (iTeamSpeakLinkage != null) {
                            LostProxy.getInstance().getLinkageManager().deleteTeamSpeakLinkage(iTeamSpeakLinkage);
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Die Verknüpfung wurde §aerfolgreich §cgelöscht§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §ckeine §7TeamSpeak §eIdentität §7mit deinem Minecraft-Account verknüpft§7.").build());
                        }
                        break;
                    case "info":
                        if (iTeamSpeakLinkage != null) {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Verknüpfungsinformationen§8:").build());
                            player.sendMessage(new MessageBuilder("§8» §7TeamSpeak-Identität §8┃ §b" + iTeamSpeakLinkage.getIdentity()).build());
                            player.sendMessage(new MessageBuilder("§8» §7Zeitstempel §8┃ §7Am §b" + new SimpleDateFormat("dd.MM.yyyy").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7um §b" + new SimpleDateFormat("HH:mm:ss").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7Uhr").build());
                            player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast deinen Minecraft-Account §cnicht §7mit einer TeamSpeak-Identität §everknüpft§7.").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                        break;
                }
            } else if (strings.length == 2) {
                String argument = strings[1];

                switch (strings[0]) {
                    case "set":
                        ITeamSpeakLinkage identityCheck = LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(player.getUniqueId());
                        if (identityCheck == null) {
                            if (!LostProxy.getInstance().getLinkageManager().isTeamSpeakIdentityInUse(argument)) {
                                ITeamSpeakLinkage linkage = LostProxy.getInstance().getLinkageManager().createTeamSpeakLinkage(player.getUniqueId(), player.getName(), argument);
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §aerfolgreich §7deinen Minecraft-Account mit der folgenden TeamSpeak-Identität verknüpft§8: §b" + linkage.getIdentity()).build());
                            } else {
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Diese Identität ist §cbereits §7in Verwendung§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §cbereits §7deinen Minecraft-Account mit einer TeamSpeak-Identität verknüpft§7.").build());
                        }
                        break;
                    case "iinfo":
                        if (player.hasPermission("lostproxy.command.ts.iinfo")) {
                            ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(argument);
                            if (iTeamSpeakLinkage != null) {
                                IPlayerSync iPlayer = new IPlayerSync(iTeamSpeakLinkage.getUuid());
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Verknüpfungsinformationen§8:").build());
                                player.sendMessage(new MessageBuilder("§8» §7TeamSpeak-Identität §8┃ §b" + iTeamSpeakLinkage.getIdentity()).addClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, iTeamSpeakLinkage.getIdentity()).build());
                                player.sendMessage(new MessageBuilder("§8» §7Minecraft-Account §8┃ " + iPlayer.getDisplay() + iPlayer.getPlayerName()).build());
                                player.sendMessage(new MessageBuilder("§8» §7Zeitstempel §8┃ §7Am §b" + new SimpleDateFormat("dd.MM.yyyy").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7um §b" + new SimpleDateFormat("HH:mm:ss").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7Uhr").build());
                                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                            } else {
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Die angegebene Identität ist mit §ckeinem §7Minecraft-Account verknüpft§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                        }
                        break;
                    case "ninfo":
                        if (player.hasPermission("lostproxy.command.ts.ninfo")) {
                            UUID uuid = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(argument);
                            if (uuid != null) {
                                ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(uuid);
                                if (iTeamSpeakLinkage != null) {
                                    IPlayerSync iPlayer = new IPlayerSync(uuid);
                                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Verknüpfungsinformationen§8:").build());
                                    player.sendMessage(new MessageBuilder("§8» §7Minecraft-Account §8┃ " + iPlayer.getDisplay() + iPlayer.getPlayerName()).build());
                                    player.sendMessage(new MessageBuilder("§8» §7TeamSpeak-Identität §8┃ §b" + iTeamSpeakLinkage.getIdentity()).addClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, iTeamSpeakLinkage.getIdentity()).build());
                                    player.sendMessage(new MessageBuilder("§8» §7Zeitstempel §8┃ §7Am §b" + new SimpleDateFormat("dd.MM.yyyy").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7um §b" + new SimpleDateFormat("HH:mm:ss").format(iTeamSpeakLinkage.getCreationTimestamp()) + " §7Uhr").build());
                                    player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                                } else {
                                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Dieser Minecraft-Account hat §ckeine §7TeamSpeak-Identität §7verknüpft§7.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                        }
                        break;
                    case "delete":
                        if (player.hasPermission("lostproxy.command.ts.delete")) {
                            UUID uniqueId = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(argument);
                            if (uniqueId != null) {
                                ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getLinkageManager().getTeamSpeakLinkage(uniqueId);
                                if (iTeamSpeakLinkage != null) {
                                    LostProxy.getInstance().getLinkageManager().deleteTeamSpeakLinkage(iTeamSpeakLinkage);
                                    IPlayerSync iTargetPlayer = new IPlayerSync(uniqueId);
                                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §aerfolgreich §7die §eVerknüpfung §7für den Minecraft-Account §b" + iTargetPlayer.getDisplay() + iTargetPlayer.getPlayerName() + " §7gelöscht§7.").build());
                                } else {
                                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Dieser Minecraft-Account hat §ckeine §7TeamSpeak-Identität §7verknüpft§7.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Zu dem angegebenen Spielernamen konnte §ckeine §7UUID gefunden werden§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                        break;
                }
            } else if (strings.length == 3) {
                if ("set".equals(strings[0])) {
                    if (player.hasPermission("lostproxy.command.ts.set")) {
                        CloudServices.LUCKPERMS.getGroupAsync(strings[1]).onComplete(iPermissionGroup -> {
                            if (iPermissionGroup != null) {
                                AtomicInteger tsGroupId = new AtomicInteger(0);

                                try {
                                    tsGroupId.set(Integer.parseInt(strings[2]));
                                } catch (NumberFormatException exception) {
                                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §ckeine §7Zahl angegeben§7.").build());
                                    return;
                                }

                                LostProxy.getInstance().getTeamSpeakManager().getServerGroup(tsGroupId.get(), serverGroup -> {
                                    if (serverGroup != null) {
                                        iPermissionGroup.getProperties().append("tsGroupId", tsGroupId.get());
                                        CloudServices.LUCKPERMS.updateGroupAsync(iPermissionGroup).onComplete(unused -> player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §aerfolgreich §7die hinterlegte §eTS-Servergruppen-ID §7auf §b" + tsGroupId.get() + " §7gesetzt§7.").build())).onFailure(e -> player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Beim §eSpeichern §7der §eRechtegruppe §7ist ein §4Fehler §7aufgetreten§7.").build()));
                                    } else {
                                        player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Die angegebene §eTeamSpeak-Servergruppe §7wurde §cnicht §7gefunden§7.").build());
                                    }
                                });
                            } else {
                                player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Die angegebene §eRechtegruppe §7wurde §cnicht §7gefunden§7.").build());
                            }
                        }).onFailure(e -> player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Es trat ein §4Fehler §7bei der Suche der angegebenen §eRechtegruppe §7auf§7.").build()));
                    } else {
                        player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du hast §cnicht §7die erforderlichen Rechte§8, §7um dieses Kommando auszuführen§7.").build());
                    }
                } else {
                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.TEAMSPEAK + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§7.").build());
        }*/
    }


    /*
                    player.sendMessage(new MessageBuilder($.TEAMSPEAK + "Benutzung von §b/ts§8:").build());
                player.sendMessage(new MessageBuilder("§8» §b/ts set <Identität> §8» §7Verknüfe manuell deine TeamSpeak Identität").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts set ").build());
                player.sendMessage(new MessageBuilder("§8» §b/ts unlink §8» §7Hebe die Teamspeak-Verknüpfung auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts unlink").build());
                player.sendMessage(new MessageBuilder("§8» §b/ts info §8» §7Zeige dir Informationen zu deiner TeamSpeak Verknüfung an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts info").build());
                if (player.hasPermission("lostproxy.command.ts.iinfo")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts iinfo <Identität> §8» §7Lasse dir Informationen zu einer TeamSpeak Identität anzeigen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts iinfo ").build());
                }
                if (player.hasPermission("lostproxy.command.ts.ninfo")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts ninfo <Spielernamen> §8» §7Lasse dir Informationen zu einem Spielernamen anzeigen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts ninfo ").build());
                }
                if (player.hasPermission("lostproxy.command.ts.delete")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts delete <Name> §8» §7Lösche die TeamSpeak Verknüpfung eines anderen Spielers").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts delete Name").build());
                }
                if (player.hasPermission("lostproxy.command.ts.set")) {
                    player.sendMessage(new MessageBuilder("§8» §b/ts set <Rang> <ID> §8» §7Setzte einer Permission-Gruppe die dazugehörige TS-Servergruppen-ID").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ts set ").build());
                }
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
     */

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        /*if (strings.length == 1) {
            list.addAll(Arrays.asList("set", "unlink", "info"));

            if (commandSender.hasPermission("lostproxy.command.ts.iinfo"))
                list.add("iinfo");
            if (commandSender.hasPermission("lostproxy.command.ts.ninfo"))
                list.add("ninfo");
            if (commandSender.hasPermission("lostproxy.command.ts.delete"))
                list.add("delete");
            if (commandSender.hasPermission("lostproxy.command.ts.set"))
                list.add("set");

            list.removeIf(filter -> !filter.toLowerCase().startsWith(strings[0].toLowerCase()));
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("iinfo") && commandSender.hasPermission("lostproxy.command.ts.iinfo")) {
                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find().forEach(one -> {
                    ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getGson().fromJson(one.toJson(), ITeamSpeakLinkage.class);
                    list.add(iTeamSpeakLinkage.getIdentity());
                });
            } else if (strings[0].equalsIgnoreCase("ninfo") && commandSender.hasPermission("lostproxy.command.ts.ninfo")) {
                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find().forEach(one -> {
                    ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getGson().fromJson(one.toJson(), ITeamSpeakLinkage.class);
                    list.add(new IPlayerSync(iTeamSpeakLinkage.getUuid()).getPlayerName());
                });
            } else if (strings[0].equalsIgnoreCase("delete") && commandSender.hasPermission("lostproxy.command.ts.delete")) {
                LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.TEAMSPEAK_LINKAGES).find().forEach(one -> {
                    ITeamSpeakLinkage iTeamSpeakLinkage = LostProxy.getInstance().getGson().fromJson(one.toJson(), ITeamSpeakLinkage.class);
                    list.add(new IPlayerSync(iTeamSpeakLinkage.getUuid()).getPlayerName());
                });
            } else if (strings[0].equalsIgnoreCase("set") && commandSender.hasPermission("lostproxy.command.ts.set")) {
                CloudServices.LUCKPERMS.getGroups().forEach(one -> list.add(one.getName()));
            }
            list.removeIf(filter -> !filter.toLowerCase().startsWith(strings[1].toLowerCase()));
        } else if (strings.length == 3) {
            if (strings[0].equalsIgnoreCase("set") && commandSender.hasPermission("lostproxy.command.ts.set")) {
                LostProxy.getInstance().getTeamSpeakManager().getApi().getServerGroups().onSuccess(serverGroups -> serverGroups.forEach(one -> list.add(String.valueOf(one.getId()))));
                list.removeIf(filter -> !filter.toLowerCase().startsWith(strings[2].toLowerCase()));
            }
        }

*/
        return list;
    }
}
