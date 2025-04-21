package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.party.IParty;
import eu.lostname.lostproxy.interfaces.party.IPartyInvitation;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyCommand extends Command {

    public PartyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer proxiedPlayer) {
            IPlayer iPlayer = new IPlayer(proxiedPlayer.getUniqueId());

            if (strings.length == 0) {
                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Benutzung von §5/party§8:").build());
                proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party invite <Spieler> §8» §7Lädt einen Spieler ein").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party invite ").build());
                proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party leave §8» §7Verlässt die Party").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party leave").build());
                proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party list §8» §7Listet Mitglieder der Party auf").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party list").build());
                proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party toggle §8» §7De- und aktiviert Partyeinladungen").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party toggle").build());
                proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party 2 §8» §7zeigt weitere Hilfe").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party 2").build());
            } else if (strings.length == 1) {
                switch (strings[0]) {
                    case "2":
                        proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Weitere Hilfe§8:").build());
                        proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party kick <Spieler> §8» §7Entfernt einen Spieler").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party kick ").build());
                        proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party promote <Spieler> §8» §7Legt jemanden als Partyleader fest").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party promote ").build());
                        proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party jump §8» §7Verbindet zum aktuellen Server").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party jump").build());
                        proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party accept <Spieler> §8» §7Akzeptiert Partyeinladungen").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party jump").build());
                        proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §5/party deny <Spieler> §8» §7Lehnt Partyeinladungen ab").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party jump").build());
                        break;
                    case "leave":
                        IParty party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                        if (party != null) {
                            if (party.getMembers().size() > 1) {
                                if (party.isLeader(proxiedPlayer)) {
                                    party.removeMember(proxiedPlayer);
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du hast die Party §cverlassen§8.").build());

                                    ProxiedPlayer newLeader = party.getMembers().keySet().stream().findAny().orElse(null);
                                    if (newLeader != null) {
                                        party.setLeader(newLeader);
                                        party.setCurrentServer(newLeader.getServer().getInfo());
                                        party.sendMessage(new MessageBuilder($.PARTY + new IPlayer(newLeader.getUniqueId()).getDisplaywithPlayername() + " §7ist der neue §ePartyleiter§8.").build());
                                    } else {
                                        party.sendMessage(new MessageBuilder($.PARTY + "Es trat ein §cFehler §7bei der Suche nach einem neuen Partyleiter auf§8.").build());
                                    }
                                } else {
                                    party.removeMember(proxiedPlayer);
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du hast die Party §cverlassen§8.").build());
                                }
                            } else {
                                party.delete();
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Die Party wurde §caufgelöst§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist in §ckeiner §7Party§8.").build());
                        }
                        break;
                    case "list":
                        party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                        if (party != null) {
                            if (party.getMembers().size() > 1) {
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Mitglieder der Party von " + new IPlayer(party.getLeader().getUniqueId()).getDisplaywithPlayername() + "§8:").build());
                                party.getOnlyMembers().forEach(one -> proxiedPlayer.sendMessage(new MessageBuilder("§8┃ " + new IPlayer(one.getUniqueId()).getDisplaywithPlayername()).build()));
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist der §cEinzige §7in der Party§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist in §ckeiner §7Party§8.").build());
                        }
                        break;
                    case "toggle":
                        if (LostProxy.getInstance().getSettingsManager().allowPartyInvitations(proxiedPlayer.getUniqueId())) {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du erhälst nun §ckeine §7Party-Einladungen mehr§8.").build());
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du erhälst nun §awieder §7Party-Einladungen§8.").build());
                        }
                        LostProxy.getInstance().getSettingsManager().updatePartyInvitations(proxiedPlayer);
                        break;
                    case "jump":
                        party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                        if (party != null) {
                            if (!party.isLeader(proxiedPlayer)) {
                                if (!party.getCurrentServer().getName().equalsIgnoreCase(proxiedPlayer.getServer().getInfo().getName())) {
                                    proxiedPlayer.connect(party.getCurrentServer(), ServerConnectEvent.Reason.COMMAND);
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du wirst mit dem Server §e" + party.getCurrentServer().getName() + " §7verbunden§8.").build());
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du befindest dich §cbereits §7auf dem Server der Party§8.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Als Partyleiter kannst du diesen Befehl §cnicht §7verwenden§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist in §ckeiner §7Party§8.").build());
                        }
                        break;
                }
            } else if (strings.length == 2) {
                switch (strings[0]) {
                    case "invite":
                        if (!strings[1].equalsIgnoreCase(proxiedPlayer.getName())) {
                            UUID targetUniqueId = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[1]);

                            if (targetUniqueId != null) {
                                IPlayer targetIPlayer = new IPlayer(targetUniqueId);

                                if (targetIPlayer.isOnline()) {
                                    ProxiedPlayer targetProxiedPlayer = ProxyServer.getInstance().getPlayer(targetUniqueId);

                                    if (LostProxy.getInstance().getSettingsManager().allowPartyInvitations(targetUniqueId)) {
                                        if (LostProxy.getInstance().getPartyManager().getParty(targetProxiedPlayer) == null) {
                                            IParty party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                                            if (party == null) {
                                                party = new IParty(proxiedPlayer);
                                                party.register();
                                            }

                                            if (party.isLeader(proxiedPlayer)) {
                                                if (LostProxy.getInstance().getPartyManager().getInvitations(targetProxiedPlayer).stream().filter(filter -> filter.getParty().getLeader() == proxiedPlayer).findFirst().orElse(null) == null) {
                                                    LostProxy.getInstance().getPartyManager().getInvitations().add(new IPartyInvitation(targetProxiedPlayer, party));

                                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + targetIPlayer.getDisplaywithPlayername() + " §7wurde in die Party eingeladen").build());

                                                    TextComponent informationComponent = new MessageBuilder($.PARTY + "Du hast eine Einladung zur Party von " + iPlayer.getDisplaywithPlayername() + " §7erhalten§8. ").build();
                                                    TextComponent acceptComponent = new MessageBuilder("§a§l✔").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + proxiedPlayer.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Partyeinladung §aanzunehmen§8.").build();
                                                    TextComponent seperateComponent = new MessageBuilder(" §8┃ ").build();
                                                    TextComponent denyComponent = new MessageBuilder("§c§l✖").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + proxiedPlayer.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §eKlicke§8, §7um diese Partyeinladung §cabzulehnen§8.").build();

                                                    informationComponent.addExtra(acceptComponent);
                                                    informationComponent.addExtra(seperateComponent);
                                                    informationComponent.addExtra(denyComponent);

                                                    targetProxiedPlayer.sendMessage(informationComponent);
                                                } else {
                                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + targetIPlayer.getDisplaywithPlayername() + " §7hat §cbereits §7eine Einladung erhalten§8.").build());
                                                }
                                            } else {
                                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist §cnicht §7der Leiter der Party§8.").build());
                                            }
                                        } else {
                                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + targetIPlayer.getDisplaywithPlayername() + " §7ist §cbereits §7in einer anderen Party§8.").build());
                                        }
                                    } else {
                                        proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + targetIPlayer.getDisplaywithPlayername() + " §7hat Party-Einladungen §cdeaktiviert§8.").build());
                                    }
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + targetIPlayer.getDisplaywithPlayername() + " §7ist §cnicht §7online§8.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du kannst dich §cnicht §7selber einladen§8.").build());
                        }
                        break;
                    case "accept":
                        List<IPartyInvitation> invitations = LostProxy.getInstance().getPartyManager().getInvitations(proxiedPlayer);

                        IPartyInvitation partyInvitation = invitations.stream().filter(filter -> filter.getParty().getLeader().getName().equalsIgnoreCase(strings[1])).collect(Collectors.toList()).stream().findFirst().orElse(null);

                        if (partyInvitation != null) {
                            IParty party = partyInvitation.getParty();

                            partyInvitation.getTimer().cancel();
                            partyInvitation.delete();
                            party.addMember(proxiedPlayer);
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du hast §ckeine §7Einladung zu dieser Party erhalten§8.").build());
                        }
                        break;
                    case "deny":
                        invitations = LostProxy.getInstance().getPartyManager().getInvitations(proxiedPlayer);

                        partyInvitation = invitations.stream().filter(filter -> filter.getParty().getLeader().getName().equalsIgnoreCase(strings[1])).toList().stream().findFirst().orElse(null);

                        if (partyInvitation != null) {
                            IParty party = partyInvitation.getParty();

                            partyInvitation.getTimer().cancel();
                            partyInvitation.delete();

                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du hast die Einladung §cabgelehnt§8.").build());
                            party.getLeader().sendMessage(new MessageBuilder($.PARTY + iPlayer.getDisplaywithPlayername() + " §7hat die Einladung §cabgelehnt§8.").build());
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du hast §ckeine §7Einladung zu dieser Party erhalten§8.").build());
                        }
                        break;
                    case "kick":
                        IParty party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                        if (party != null) {
                            if (party.isLeader(proxiedPlayer)) {
                                List<ProxiedPlayer> searchedPlayers = party.getOnlyMembers().stream().filter(filter -> filter.getName().equalsIgnoreCase(strings[1])).toList();
                                if (searchedPlayers.size() == 1) {
                                    ProxiedPlayer targetProxiedPlayer = searchedPlayers.getFirst();
                                    IPlayer targetIPlayer = new IPlayer(targetProxiedPlayer.getUniqueId());

                                    party.removeMember(targetProxiedPlayer);

                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du hast " + targetIPlayer.getDisplaywithPlayername() + " §7aus der Party §cgekickt§8.").build());
                                    targetProxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du wurdest aus der Party §cgekickt§8.").build());
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist §cnicht §7der Partyleiter§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "§7Du bist in §ckeiner §7Party§8.").build());
                        }
                        break;
                    case "promote":
                        party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                        if (party != null) {
                            if (party.isLeader(proxiedPlayer)) {
                                List<ProxiedPlayer> searchedPlayers = party.getOnlyMembers().stream().filter(filter -> filter.getName().equalsIgnoreCase(strings[1])).toList();

                                if (searchedPlayers.size() == 1) {
                                    ProxiedPlayer targetProxiedPlayer = searchedPlayers.getFirst();

                                    party.setLeader(targetProxiedPlayer);
                                    party.setCurrentServer(targetProxiedPlayer.getServer().getInfo());
                                    party.sendMessage(new MessageBuilder($.PARTY + new IPlayer(targetProxiedPlayer.getUniqueId()).getDisplaywithPlayername() + " §7ist der neue §ePartyleiter§8.").build());
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist §cnicht §7der Partyleiter§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.PARTY + "Du bist in §ckeiner §7Party§8.").build());
                        }
                        break;
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.PARTY + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}