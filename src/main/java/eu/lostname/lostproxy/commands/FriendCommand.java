/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 25.01.2021 @ 00:10:05
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * FriendCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendCommand extends Command {

    public FriendCommand(String name, String s, String friends) {
        super(name, s, friends);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            IFriendData iFriendData = LostProxy.getInstance().getFriendManager().getFriendData(player.getUniqueId());
            IPlayer iPlayer = new IPlayer(player.getUniqueId());

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder($.FRIENDS + "Benutzung von §a/friend§8:").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend 2 §8» §7Zeigt Hilfeseite 2").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend 2").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend add <Spieler> §8» §7Fügt jemanden als Freund hinzu").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend add ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend remove <Spieler> §8» §7Entfernt jemanden als Freund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend remove ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend accept <Spieler> §8» §7Nimmt Freundschaftsanfragen an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend accept ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend deny <Spieler> §8» §7Lehnt Freundschaftsanfragen ab").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend deny ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend list §8» §7Listet alle Freunde auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend list").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8┃ §a/friend requests §8» §7Listet alle Freundschaftsanfragen auf").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend requests").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
            } else if (strings.length == 1) {
                switch (strings[0]) {
                    case "2":
                        player.sendMessage(new MessageBuilder($.FRIENDS + "Hilfeseite 2§8:").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend acceptall §8» §7Nimmt alle Freundschaftsanfragen an").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend acceptall").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend denyall §8» §7Lehnt alle Freundschaftsanfragen ab").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend denyall").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend broadcast [Nachricht] §8» §7Schicke eine Nachricht an alle Freunde").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend broadcast ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend clear §8» §7Leert deine Freundesliste").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend clear").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend jump <Spieler> §8» §7Springt zu einem Freund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend clear").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend toggle §8» §7De- oder aktiviert Freundschaftsanfragen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend toggle").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend togglenotify §8» §7De- oder aktiviert Benachrichtigungen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglenotify").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend togglemsg §8» §7De- oder aktiviert Nachrichten von Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglemsg").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend togglejump §8» §7De- oder aktiviert Nachspringen von Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend togglejump").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend toggleonline §8» §7De- oder aktiviert den Onlinestatus vor anderen Freunden").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend toggleonline").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8┃ §a/friend info <Spieler> §8» §7Zeigt Informationen über einen Freund").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend info ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").build());
                        player.sendMessage(new MessageBuilder("§8§m--------------------§r").build());
                        break;
                    case "list":
                        if (!iFriendData.getFriends().isEmpty()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Deine Freundesliste §8(§e" + iFriendData.getFriends().size() + "§8):").build());
                            List<String> onlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayer(UUID.fromString(filter)).isOnline()).filter(filter -> LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSeeOnlineStatusAllowed()).toList();
                            List<String> offlineFriends = iFriendData.getFriends().keySet().stream().filter(filter -> !new IPlayer(UUID.fromString(filter)).isOnline() || !LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSeeOnlineStatusAllowed()).toList();

                            onlineFriends.forEach(online -> {
                                IPlayer friendiPlayer = new IPlayer(UUID.fromString(online));

                                TextComponent playerNameComponent = new MessageBuilder("§8┃ " + friendiPlayer.getDisplaywithPlayername() + " §8» ").build();
                                TextComponent serverComponent = new MessageBuilder("§e§n" + friendiPlayer.getCloudPlayer().connectedService().serverName()).addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend jump " + friendiPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um " + friendiPlayer.getDisplaywithPlayername() + " §7nachzuspringen.").build();
                                playerNameComponent.addExtra(serverComponent);

                                player.sendMessage(playerNameComponent);
                            });

                            offlineFriends.forEach(offline -> {
                                IPlayer friendiPlayer = new IPlayer(UUID.fromString(offline));
                                IFriendData friendData = LostProxy.getInstance().getFriendManager().getFriendData(friendiPlayer.getUniqueId());

                                TextComponent playerNameComponent = new MessageBuilder("§8┃ " + friendiPlayer.getDisplaywithPlayername() + " §8» ").build();
                                TextComponent extraComponent;

                                if (friendData.canFriendsSeeOnlineStatusAllowed()) {
                                    extraComponent = new MessageBuilder("§7zul. §7online am §e" + new SimpleDateFormat("dd.MM.yyyy").format(friendData.getLastLogoutTimestamp()) + " §7um §e" + new SimpleDateFormat("HH:mm:ss").format(friendData.getLastLogoutTimestamp()) + " §7Uhr").build();
                                } else {
                                    extraComponent = new MessageBuilder("§7Onlinestatus verborgen").build();
                                }
                                playerNameComponent.addExtra(extraComponent);

                                player.sendMessage(playerNameComponent);
                            });

                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Deine Freundesliste ist §cleer§7.").build());
                        }
                        break;
                    case "requests":
                        if (!iFriendData.getRequests().isEmpty()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freundschaftsanfragen §8(§e" + iFriendData.getRequests().size() + "§8):").build());
                            iFriendData.getRequests().keySet().forEach(all -> {
                                IPlayer friendiPlayer = new IPlayer(UUID.fromString(all));

                                TextComponent playerNameComponent = new MessageBuilder("§8┃ " + friendiPlayer.getDisplaywithPlayername() + " §8» ").build();
                                TextComponent acceptComponent = new MessageBuilder("§a§l✔").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend accept " + friendiPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §aanzunehmen§7.").build();
                                TextComponent seperateComponent = new MessageBuilder(" §8┃ ").build();
                                TextComponent denyComponent = new MessageBuilder("§c§l✖").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/friend deny " + friendiPlayer.getPlayerName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §cabzulehnen§7.").build();

                                playerNameComponent.addExtra(acceptComponent);
                                playerNameComponent.addExtra(seperateComponent);
                                playerNameComponent.addExtra(denyComponent);

                                player.sendMessage(playerNameComponent);
                            });
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast §ckeine §7Freundschaftsanfragen erhalten.").build());
                        }
                        break;
                    case "acceptall":
                        if (!iFriendData.getRequests().isEmpty()) {
                            iFriendData.getRequests().keySet().forEach(request -> {
                                IPlayer requestIPlayer = new IPlayer(UUID.fromString(request));

                                iFriendData.addFriend(requestIPlayer.getUniqueId());
                                iFriendData.save();

                                IFriendData requestIFriendData = LostProxy.getInstance().getFriendManager().getFriendData(requestIPlayer.getUniqueId());
                                requestIFriendData.addFriend(player.getUniqueId());
                                requestIFriendData.save();

                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du bist nun mit " + requestIPlayer.getDisplaywithPlayername() + " §7befreundet.").build());

                                if (requestIPlayer.isOnline()) {
                                    ProxiedPlayer requestPlayer = ProxyServer.getInstance().getPlayer(requestIPlayer.getUniqueId());
                                    requestPlayer.sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §7hat deine Freundschaftsanfrage §aakzeptiert§7.").build());
                                }
                            });
                            iFriendData.getRequests().clear();
                            iFriendData.save();
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast §ckeine §7Freundschaftsanfragen erhalten§7.").build());
                        }
                        break;
                    case "denyall":
                        if (!iFriendData.getRequests().isEmpty()) {
                            iFriendData.getRequests().keySet().forEach(request -> {
                                IPlayer requestIPlayer = new IPlayer(UUID.fromString(request));

                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast die Freundschaftsanfrage von " + requestIPlayer.getDisplaywithPlayername() + " §cabgelehnt§7.").build());

                                if (requestIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(requestIPlayer.getUniqueId()).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §7hat deine Freundschaftsanfrage §cabgelehnt§7.").build());
                            });
                            iFriendData.getRequests().clear();
                            iFriendData.save();
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast §ckeine §7Freundschaftsanfragen §7erhalten§7.").build());
                        }
                        break;
                    case "toggle":
                        iFriendData.setFriendRequestsAllowed(!iFriendData.canFriendsSeeOnlineStatusAllowed());
                        iFriendData.save();

                        if (iFriendData.canFriendsSeeOnlineStatusAllowed()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du erhältst nun §awieder §7Freundschaftsanfragen§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du erhältst nun §ckeine §7Freundschaftsanfragen mehr§7.").build());
                        }
                        break;
                    case "togglenotify":
                        iFriendData.setNotifyMessagesEnabled(!iFriendData.areNotifyMessagesEnabled());
                        iFriendData.save();

                        if (iFriendData.areNotifyMessagesEnabled()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du erhältst nun §awieder §7Benachrichtigungen§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du erhältst nun §ckeine §7Benachrichtigungen mehr§7.").build());
                        }
                        break;
                    case "togglemsg":
                        iFriendData.setSentMessagesAllowed(!iFriendData.canFriendsSentMessages());
                        iFriendData.save();

                        if (iFriendData.canFriendsSentMessages()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freunde können dir nun wieder §eNachrichten §7schreiben§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freunde können dir nun §ckeine §eNachrichten §7mehr schreiben§7.").build());
                        }
                        break;
                    case "togglejump":
                        iFriendData.setFriendJumpAllowed(!iFriendData.isFriendJumpAllowed());
                        iFriendData.save();

                        if (iFriendData.isFriendJumpAllowed()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freunde können dir nun §awieder §7hinterher springen§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freunde können dir nun §cnicht §7mehr hinterher springen§7.").build());
                        }
                        break;
                    case "toggleonline":
                        iFriendData.setFriendsSeeOnlineStatusAllowed(!iFriendData.canFriendsSeeOnlineStatusAllowed());
                        iFriendData.save();

                        if (iFriendData.canFriendsSeeOnlineStatusAllowed()) {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freunde sehen deinen Onlinestatus nun §awieder§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Freunde sehen deinen Onlinestatus §cnicht §7mehr§7.").build());
                        }
                        break;
                    case "clear":
                        iFriendData.getFriends().keySet().forEach(friend -> {
                            IPlayer friendIPlayer = new IPlayer(UUID.fromString(friend));
                            IFriendData friendData = LostProxy.getInstance().getFriendManager().getFriendData(friendIPlayer.getUniqueId());

                            friendData.removeFriend(player.getUniqueId());
                            friendData.save();

                            player.sendMessage(new MessageBuilder($.FRIENDS + "Die Freundschaft mit " + friendIPlayer.getDisplaywithPlayername() + " §7wurde §caufgelöst§7.").build());

                            if (friendIPlayer.isOnline())
                                ProxyServer.getInstance().getPlayer(friendIPlayer.getUniqueId()).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §7hat die Freundschaft §caufgelöst§7.").build());
                        });
                        iFriendData.getFriends().clear();
                        iFriendData.save();
                    case "party":
                        player.sendMessage(new MessageBuilder($.FRIENDS + "Diese Funktion folgt bald").build());
                    default:
                        player.sendMessage(new MessageBuilder($.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                }
            } else if (strings.length == 2) {
                String argument = strings[1];
                UUID targetUUID = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(argument);
                switch (strings[0]) {
                    case "add":
                        if (!argument.equalsIgnoreCase(player.getName())) {
                            if (targetUUID != null) {
                                IPlayer targetIPlayer = new IPlayer(targetUUID);
                                IFriendData targetFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);
                                if (!iFriendData.isFriend(targetUUID)) {
                                    if (!targetFriendData.haveRequest(player.getUniqueId())) {
                                        if (targetFriendData.areFriendRequestsAllowed()) {
                                            targetFriendData.addRequest(player.getUniqueId());
                                            targetFriendData.save();

                                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast " + targetIPlayer.getDisplaywithPlayername() + " §7eine §eFreundschaftsanfrage §7gesendet§7.").build());

                                            if (targetIPlayer.isOnline()) {
                                                TextComponent informationComponent = new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §7hat dir eine Freundschaftsanfrage gesendet§7. ").build();
                                                TextComponent acceptComponent = new MessageBuilder("§a§l✔").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + player.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §aanzunehmen§7.").build();
                                                TextComponent seperateComponent = new MessageBuilder(" §8┃ ").build();
                                                TextComponent denyComponent = new MessageBuilder("§c§l✖").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + player.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Freundschaftsanfrage §cabzulehnen§7.").build();

                                                informationComponent.addExtra(acceptComponent);
                                                informationComponent.addExtra(seperateComponent);
                                                informationComponent.addExtra(denyComponent);

                                                ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(informationComponent);
                                            }
                                        } else {
                                            player.sendMessage(new MessageBuilder($.FRIENDS + targetIPlayer.getDisplaywithPlayername() + " §7hat Freundschaftsanfragen §cdeaktiviert§7.").build());
                                        }
                                    } else {
                                        player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast " + targetIPlayer.getDisplaywithPlayername() + " §7bereits eine Freundschaftsanfrage gesendet§7.").build());
                                    }
                                } else {
                                    player.sendMessage(new MessageBuilder($.FRIENDS + "Du bist §cbereits §7mit " + targetIPlayer.getDisplaywithPlayername() + " §7befreundet§7.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du kannst §cnicht §7mit dir selbst befreundet sein§7.").build());
                        }
                        break;
                    case "remove":
                        if (!argument.equalsIgnoreCase(player.getName())) {
                            if (targetUUID != null) {
                                IPlayer targetIPlayer = new IPlayer(targetUUID);
                                IFriendData targetFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);
                                if (iFriendData.isFriend(targetUUID)) {
                                    targetFriendData.removeFriend(player.getUniqueId());
                                    targetFriendData.save();

                                    iFriendData.removeFriend(targetUUID);
                                    iFriendData.save();

                                    player.sendMessage(new MessageBuilder($.FRIENDS + "Die Freundschaft mit " + targetIPlayer.getDisplaywithPlayername() + " wurde §caufgelöst§7.").build());

                                    if (targetIPlayer.isOnline()) {
                                        ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder($.FRIENDS + "Die Freundschaft mit " + iPlayer.getDisplaywithPlayername() + " §7wurde §caufgelöst§7.").build());
                                    }
                                } else {
                                    player.sendMessage(new MessageBuilder($.FRIENDS + "Du bist §cnicht §7mit " + targetIPlayer.getDisplaywithPlayername() + " §7befreundet§7.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§7.").build());
                            }
                        }
                        break;
                    case "accept":
                        if (targetUUID != null) {
                            IPlayer targetIPlayer = new IPlayer(targetUUID);
                            if (iFriendData.getRequests().containsKey(targetUUID.toString())) {
                                iFriendData.removeRequest(targetUUID);
                                iFriendData.addFriend(targetUUID);
                                iFriendData.save();

                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du bist nun mit " + targetIPlayer.getDisplaywithPlayername() + " §7befreundet§7.").build());

                                IFriendData targetIFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);
                                targetIFriendData.addFriend(player.getUniqueId());
                                targetIFriendData.save();

                                if (targetIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder($.FRIENDS + "Du bist nun mit " + iPlayer.getDisplaywithPlayername() + " §7befreundet§7.").build());
                            } else {
                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast §ckeine §7Freundschaftsanfrage von " + targetIPlayer.getDisplaywithPlayername() + " §7erhalten§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§7.").build());
                        }
                        break;
                    case "deny":
                        if (targetUUID != null) {
                            IPlayer targetIPlayer = new IPlayer(targetUUID);

                            if (iFriendData.haveRequest(targetUUID)) {
                                iFriendData.removeRequest(targetUUID);
                                iFriendData.save();

                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast die Freundschaftsanfrage von " + targetIPlayer.getDisplaywithPlayername() + " §cabgelehnt§7.").build());

                                if (targetIPlayer.isOnline())
                                    ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §7hat deine Freundschaftsanfrage §cabgelehnt§7.").build());
                            } else {
                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du hast §ckeine §7Freundschaftsanfrage von " + targetIPlayer.getDisplaywithPlayername() + " §7bekommen§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§7.").build());
                        }
                        break;
                    case "jump":
                        if (targetUUID != null) {
                            IPlayer targetIPlayer = new IPlayer(targetUUID);

                            if (iFriendData.isFriend(targetUUID)) {
                                IFriendData targetIFriendData = LostProxy.getInstance().getFriendManager().getFriendData(targetUUID);

                                if (targetIFriendData.isFriendJumpAllowed()) {
                                    if (targetIPlayer.isOnline()) {
                                        ServerInfo targetServer = ProxyServer.getInstance().getPlayer(targetUUID).getServer().getInfo();

                                        if (!targetServer.getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
                                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du wirst nun mit dem Server von " + targetIPlayer.getDisplaywithPlayername() + " §7verbunden§7.").build());
                                            player.connect(targetServer);

                                            if (targetIFriendData.areNotifyMessagesEnabled())
                                                ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §7ist zu dir §egesprungen§7.").build());
                                        } else {
                                            player.sendMessage(new MessageBuilder($.FRIENDS + "Du befindest dich §cbereits §7auf dem Server von " + targetIPlayer.getDisplaywithPlayername() + "§7.").build());
                                        }
                                    } else {
                                        player.sendMessage(new MessageBuilder($.FRIENDS + targetIPlayer.getDisplaywithPlayername() + " §7ist §cnicht §7online§7.").build());
                                    }
                                } else {
                                    player.sendMessage(new MessageBuilder($.FRIENDS + targetIPlayer.getDisplaywithPlayername() + " §7hat das Nachspringen §cdeaktiviert§7.").build());
                                }
                            } else {
                                player.sendMessage(new MessageBuilder($.FRIENDS + "Du bist §cnicht §7mit " + targetIPlayer.getDisplaywithPlayername() + " §7befreundet§7.").build());
                            }
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Der angegebene Spieler wurde §cnicht §7gefunden§7.").build());
                        }
                        break;
                    case "broadcast":
                        List<String> sortedFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayer(UUID.fromString(filter)).isOnline() && LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSentMessages()).toList();

                        if (!sortedFriends.isEmpty()) {
                            sortedFriends.forEach(sortedFriend -> {
                                UUID sortedFriendUUID = UUID.fromString(sortedFriend);
                                IPlayer sortedFriendIPlayer = new IPlayer(sortedFriendUUID);
                                ProxyServer.getInstance().getPlayer(sortedFriendUUID).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §8➡ " + sortedFriendIPlayer.getDisplaywithPlayername() + " §8» §e" + argument).build());
                            });
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Deine Nachricht wurde an §e" + sortedFriends.size() + " Freunde §7versendet§7.").build());
                        } else {
                            player.sendMessage(new MessageBuilder($.FRIENDS + "Deine Nachricht konnte §cnicht §7zugestellt werden§7. §7Sind vielleicht keine Freunde online oder haben die Freunde, die online sind, Nachrichten ausgeschaltet§8?").build());
                        }
                        break;
                    default:
                        player.sendMessage(new MessageBuilder($.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                        break;
                }
            } else {
                if (strings[0].equalsIgnoreCase("broadcast")) {
                    List<String> sortedFriends = iFriendData.getFriends().keySet().stream().filter(filter -> new IPlayer(UUID.fromString(filter)).isOnline() && LostProxy.getInstance().getFriendManager().getFriendData(UUID.fromString(filter)).canFriendsSentMessages()).toList();

                    if (!sortedFriends.isEmpty()) {
                        String message = LostProxy.getInstance().formatArrayToString(1, strings);

                        sortedFriends.forEach(sortedFriend -> {
                            UUID sortedFriendUUID = UUID.fromString(sortedFriend);
                            IPlayer sortedFriendIPlayer = new IPlayer(sortedFriendUUID);
                            ProxyServer.getInstance().getPlayer(sortedFriendUUID).sendMessage(new MessageBuilder($.FRIENDS + iPlayer.getDisplaywithPlayername() + " §8➡ " + sortedFriendIPlayer.getDisplaywithPlayername() + " §8» §e" + message).build());
                        });
                        player.sendMessage(new MessageBuilder($.FRIENDS + "Deine Nachricht wurde an §e" + sortedFriends.size() + " Freunde §7versendet§7.").build());
                    } else {
                        player.sendMessage(new MessageBuilder($.FRIENDS + "Deine Nachricht konnte §cnicht §7zugestellt werden§7. §7Sind vielleicht keine Freunde online oder haben die Freunde, die online sind, Nachrichten ausgeschaltet§8?").build());
                    }
                } else {
                    player.sendMessage(new MessageBuilder($.FRIENDS + "Bitte beachte die §eBenutzung §7dieses Kommandos§7.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.FRIENDS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§7.").build());
        }
    }
}