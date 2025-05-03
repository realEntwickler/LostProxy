package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EClanRole;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.clan.IClan;
import eu.lostname.lostproxy.interfaces.clan.IClanInvitation;
import eu.lostname.lostproxy.interfaces.clan.IClanPlayerData;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;

public class ClanCommand extends Command {
    public ClanCommand(String clan, String s, String clans) {
        super(clan, s, clans);
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer proxiedPlayer) {
            IPlayer iPlayer = new IPlayer(proxiedPlayer.getUniqueId());

            if (strings.length == 0) {
                sendHelpMessage(proxiedPlayer, 0);
            } else if (strings.length == 1) {
                switch (strings[0]) {
                    case "delete":
                        IClanPlayerData clanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                        if (clanPlayerData != null) {
                            if (clanPlayerData.getClanRole() == EClanRole.LEADER) {
                                if (LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(clanPlayerData.getClanUid()).size() == 1) {
                                    IClan clan = LostProxy.getInstance().getClanManager().getClanByUniqueId(clanPlayerData.getClanUid());

                                    clanPlayerData.delete();
                                    clan.delete();

                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Der Clan §e" + clan.getName() + " §7wurde erfolgreich §cgelöscht§7.").build());
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Im Clan befinden sich noch §eSpieler. §7Du musst diese erst aus dem Clan §ckicken§8, §7bevor du den Clan löschen kannst.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §ckeine §7Rechte§8, §7um den Clan zu löschen.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                        }
                        break;
                    case "info":
                        clanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                        if (clanPlayerData != null) {
                            IClan iClan = LostProxy.getInstance().getClanManager().getClanByUniqueId(clanPlayerData.getClanUid());

                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Informationen zu §e" + iClan.getName()).build());
                            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §7Tag §8" + $.arrow + " " + iClan.getTagColor() + iClan.getTag()).build());
                            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §7Anzahl an maximalen Mitgliedern §8" + $.arrow + " §e" + iClan.getMaxSize()).build());

                            List<IClanPlayerData> playerData = LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(iClan.getId());
                            List<IClanPlayerData> leaders = playerData.stream().filter(filter -> filter.getClanRole() == EClanRole.LEADER).toList();
                            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §7Leader §8(§c" + leaders.size() + "§8):").build());

                            leaders.forEach(one -> proxiedPlayer.sendMessage(new MessageBuilder("§8" + $.arrow + " " + new IPlayer(one.getUniqueId()).getDisplaywithPlayername()).build()));

                            List<IClanPlayerData> moderatoren = playerData.stream().filter(filter -> filter.getClanRole() == EClanRole.MODERATOR).toList();
                            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §7Moderatoren §8(§9" + moderatoren.size() + "§8):").build());
                            moderatoren.forEach(one -> proxiedPlayer.sendMessage(new MessageBuilder("§8" + $.arrow + " " + new IPlayer(one.getUniqueId()).getDisplaywithPlayername()).build()));

                            List<IClanPlayerData> mitglieder = playerData.stream().filter(filter -> filter.getClanRole() == EClanRole.MEMBER).toList();
                            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §7Mitglieder §8(§e" + mitglieder.size() + "§8):").build());
                            mitglieder.forEach(one -> proxiedPlayer.sendMessage(new MessageBuilder("§8" + $.arrow + " " + new IPlayer(one.getUniqueId()).getDisplaywithPlayername()).build()));
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                        }
                        break;
                    case "leave":
                        clanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                        if (clanPlayerData != null) {
                            IClan clan = LostProxy.getInstance().getClanManager().getClanByUniqueId(clanPlayerData.getClanUid());
                            List<IClanPlayerData> clanPlayerDatasFromClan = LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(clan.getId());

                            if (clanPlayerData.getClanRole() == EClanRole.LEADER) {
                                if (clanPlayerDatasFromClan.stream().filter(filter -> filter.getClanRole() == EClanRole.LEADER).count() > 1) {
                                    clanPlayerData.delete();
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §e" + clan.getName() + " §cverlassen§7.").build());


                                    clanPlayerDatasFromClan.forEach(one -> {
                                        ProxiedPlayer oneClanProxiedPlayers = ProxyServer.getInstance().getPlayer(one.getUniqueId());
                                        if (oneClanProxiedPlayers != null) {
                                            oneClanProxiedPlayers.sendMessage(new MessageBuilder($.CLANS + iPlayer.getDisplaywithPlayername() + " §7hat den Clan §cverlassen§7.").build());
                                        }
                                    });
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Da du der Einzige §eClanleader §7bist§8, §7musst du den Clan §clöschen§8, §7um ihn zu verlassen.").build());
                                }
                            } else {

                                clanPlayerData.delete();
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §e" + clan.getName() + " §cverlassen§7.").build());


                                clanPlayerDatasFromClan.forEach(one -> {
                                    ProxiedPlayer oneClanProxiedPlayers = ProxyServer.getInstance().getPlayer(one.getUniqueId());
                                    if (oneClanProxiedPlayers != null) {
                                        oneClanProxiedPlayers.sendMessage(new MessageBuilder($.CLANS + iPlayer.getDisplaywithPlayername() + " §7hat den Clan §cverlassen§7.").build());
                                    }
                                });
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                        }
                        break;
                }
            } else if (strings.length == 2) {
                switch (strings[0]) {
                    case "invite":
                        UUID targetUniqueId = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[1]);

                        if (targetUniqueId != null) {
                            IPlayer targetIPlayer = new IPlayer(targetUniqueId);
                            if (LostProxy.getInstance().getClanManager().getClanPlayerData(targetUniqueId) == null) {
                                if (LostProxy.getInstance().getSettingsManager().allowClanInvitations(targetUniqueId)) {
                                    IClanPlayerData clanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                                    if (clanPlayerData != null) {
                                        IClan clan = LostProxy.getInstance().getClanManager().getClanByUniqueId(clanPlayerData.getClanUid());
                                        if (clanPlayerData.getClanRole() != EClanRole.MEMBER) {
                                            if (LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(clan.getId()).size() < clan.getMaxSize()) {
                                                if (LostProxy.getInstance().getClanManager().getClanInvitationsByPlayer(targetUniqueId).stream().noneMatch(filter -> filter.getClanUid().equalsIgnoreCase(clan.getId()))) {
                                                    new IClanInvitation(targetUniqueId.toString(), clan.getId()).save();

                                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetIPlayer.getDisplaywithPlayername() + " §7wurde in den Clan eingeladen.").build());

                                                    ProxiedPlayer targetProxiedPlayer = ProxyServer.getInstance().getPlayer(targetUniqueId);
                                                    if (targetProxiedPlayer != null) {
                                                        TextComponent informationComponent = new MessageBuilder($.CLANS + "Du hast eine Einladung zum Clan §e" + clan.getName() + " §7erhalten§7. ").build();
                                                        TextComponent acceptComponent = new MessageBuilder("§a☑").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " + clan.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§aKlicke §7zum §aannehmen").build();
                                                        TextComponent seperateComponent = new MessageBuilder(" §8┃ ").build();
                                                        TextComponent denyComponent = new MessageBuilder("§c☒").addClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan deny " + clan.getName()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§cKlicke §7zum §cablehnen").build();

                                                        informationComponent.addExtra(acceptComponent);
                                                        informationComponent.addExtra(seperateComponent);
                                                        informationComponent.addExtra(denyComponent);

                                                        targetProxiedPlayer.sendMessage(informationComponent);
                                                    }
                                                } else {
                                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetIPlayer.getDisplaywithPlayername() + " §7hat §cbereits §7eine Einladung zum Clan erhalten.").build());
                                                }
                                            } else {
                                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Der Clan ist §cvoll§7.").build());
                                            }
                                        } else {
                                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast innerhalb des Clans dazu §ckeine §7Rechte.").build());
                                        }
                                    } else {
                                        proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                                    }
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetIPlayer.getDisplaywithPlayername() + " §7hat Clan-Einladungen §cdeaktiviert§7.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetIPlayer.getDisplaywithPlayername() + " §7ist §cbereits §7in einem Clan.").build());

                            }
                        } else {
                            proxiedPlayer.sendMessage($.PLAYER_NOT_FOUND($.CLANS));
                        }
                        break;
                    case "accept":
                        List<IClanInvitation> clanInvitations = LostProxy.getInstance().getClanManager().getClanInvitationsByPlayer(proxiedPlayer.getUniqueId());
                        IClanPlayerData iClanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                        if (iClanPlayerData == null) {
                            if (!clanInvitations.isEmpty()) {
                                IClan clan = LostProxy.getInstance().getClanManager().getClanByName(strings[1]);

                                if (clan != null) {
                                    IClanInvitation clanInvitation = clanInvitations.stream().filter(filter -> filter.getClanUid().equalsIgnoreCase(clan.getId())).findFirst().orElse(null);
                                    if (clanInvitation != null) {
                                        List<IClanPlayerData> playerDatasFromClan = LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(clan.getId());
                                        if (playerDatasFromClan.size() < clan.getMaxSize()) {
                                            clanInvitation.delete();

                                            iClanPlayerData = new IClanPlayerData(proxiedPlayer.getUniqueId().toString(), clan.getId(), EClanRole.MEMBER, System.currentTimeMillis());
                                            iClanPlayerData.save();

                                            playerDatasFromClan.add(iClanPlayerData);
                                            playerDatasFromClan.stream().filter(filter -> new IPlayer(filter.getUniqueId()).isOnline()).forEach(one -> ProxyServer.getInstance().getPlayer(one.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + iPlayer.getDisplaywithPlayername() + " §7ist dem Clan beigetreten.").build()));
                                        } else {
                                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Die Einladung konnte §cnicht §7akzeptiert werden§8, §7da der Clan §cvoll §7ist.").build());
                                        }
                                    } else {
                                        proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §ckeine §7Einladung von diesem Clan erhalten.").build());
                                    }
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Der Clan wurde §cnicht §7gefunden.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §ckeine §7Einladungen zu einem Clan erhalten.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist §cbereits §7in einem Clan.").build());
                        }
                        break;
                    case "deny":
                        clanInvitations = LostProxy.getInstance().getClanManager().getClanInvitationsByPlayer(proxiedPlayer.getUniqueId());

                        if (!clanInvitations.isEmpty()) {
                            IClan clan = LostProxy.getInstance().getClanManager().getClanByName(strings[1]);

                            if (clan != null) {
                                IClanInvitation clanInvitation = clanInvitations.stream().filter(filter -> filter.getClanUid().equalsIgnoreCase(clan.getId())).findFirst().orElse(null);
                                if (clanInvitation != null) {
                                    List<IClanPlayerData> playerDatasFromClan = LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(clan.getId());
                                    clanInvitation.delete();

                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast die Einladung von §e" + clan.getName() + " §7erfolgreich §cabgelehnt§7.").build());
                                    playerDatasFromClan.stream().filter(filter -> filter.getClanRole() != EClanRole.MEMBER && new IPlayer(filter.getUniqueId()).isOnline()).forEach(one -> ProxyServer.getInstance().getPlayer(one.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + iPlayer.getDisplaywithPlayername() + " §7hat die Claneinladung §cabgelehnt§7.").build()));
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §ckeine §7Einladung von diesem Clan erhalten.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Der Clan wurde §cnicht §7gefunden.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast §ckeine §7Einladungen zu einem Clan erhalten.").build());
                        }
                        break;
                    case "promote":
                        iClanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                        if (iClanPlayerData != null) {
                            if (iClanPlayerData.getClanRole() == EClanRole.LEADER) {
                                targetUniqueId = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[1]);

                                if (targetUniqueId != null) {
                                    IClanPlayerData targetClanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(targetUniqueId);
                                    String targetColorWithPlayername = new IPlayer(targetUniqueId).getDisplaywithPlayername();

                                    if (targetClanPlayerData != null && targetClanPlayerData.getClanUid().equalsIgnoreCase(iClanPlayerData.getClanUid())) {
                                        switch (targetClanPlayerData.getClanRole()) {
                                            case MEMBER:
                                                targetClanPlayerData.setClanRole(EClanRole.MODERATOR);
                                                targetClanPlayerData.save();
                                                LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(targetClanPlayerData.getClanUid()).forEach(all -> {
                                                    if (ProxyServer.getInstance().getPlayer(all.getUniqueId()) != null)
                                                        ProxyServer.getInstance().getPlayer(all.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7ist nun §9Moderator§7.").build());
                                                });
                                                break;
                                            case MODERATOR:
                                                targetClanPlayerData.setClanRole(EClanRole.LEADER);
                                                targetClanPlayerData.save();
                                                LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(targetClanPlayerData.getClanUid()).forEach(all -> {
                                                    if (ProxyServer.getInstance().getPlayer(all.getUniqueId()) != null)
                                                        ProxyServer.getInstance().getPlayer(all.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7ist nun §cLeader§7.").build());
                                                });
                                                break;
                                            default:
                                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7kann §cnicht §7mehr befördert werden.").build());
                                                break;
                                        }
                                    } else {
                                        proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7ist §cnicht §7in deinem Clan.").build());
                                    }
                                } else {
                                    proxiedPlayer.sendMessage($.PLAYER_NOT_FOUND($.CLANS));
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast innerhalb des Clans dazu §ckeine §7Rechte.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                        }
                        break;
                    case "demote":
                        iClanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());

                        if (iClanPlayerData != null) {
                            if (iClanPlayerData.getClanRole() == EClanRole.LEADER) {
                                if (!proxiedPlayer.getName().equalsIgnoreCase(strings[1])) {
                                    targetUniqueId = LostProxy.getInstance().getPlayerManager().getUUIDofPlayername(strings[1]);

                                    if (targetUniqueId != null) {
                                        IClanPlayerData targetClanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(targetUniqueId);
                                        String targetColorWithPlayername = new IPlayer(targetUniqueId).getDisplaywithPlayername();

                                        if (targetClanPlayerData != null && targetClanPlayerData.getClanUid().equalsIgnoreCase(iClanPlayerData.getClanUid())) {
                                            switch (targetClanPlayerData.getClanRole()) {
                                                case LEADER:
                                                    targetClanPlayerData.setClanRole(EClanRole.MODERATOR);
                                                    targetClanPlayerData.save();
                                                    LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(targetClanPlayerData.getClanUid()).forEach(all -> {
                                                        if (ProxyServer.getInstance().getPlayer(all.getUniqueId()) != null)
                                                            ProxyServer.getInstance().getPlayer(all.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7ist nun §9Moderator§7.").build());
                                                    });
                                                    break;
                                                case MODERATOR:
                                                    targetClanPlayerData.setClanRole(EClanRole.MEMBER);
                                                    targetClanPlayerData.save();
                                                    LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(targetClanPlayerData.getClanUid()).forEach(all -> {
                                                        if (ProxyServer.getInstance().getPlayer(all.getUniqueId()) != null)
                                                            ProxyServer.getInstance().getPlayer(all.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7ist nun §eMember§7.").build());
                                                    });
                                                    break;
                                                default:
                                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7kann §cnicht §7mehr heruntergestuft werden.").build());
                                                    break;
                                            }
                                        } else {
                                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + targetColorWithPlayername + " §7ist §cnicht §7in deinem Clan.").build());
                                        }
                                    } else {
                                        proxiedPlayer.sendMessage($.PLAYER_NOT_FOUND($.CLANS));
                                    }
                                } else {
                                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du kannst dich §cnicht §7selber herunterstufen.").build());
                                }
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du hast innerhalb des Clans dazu §ckeine §7Rechte.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                        }
                        break;
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.CLANS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen.").build());
        }
    }

    private static void sendHelpMessage(ProxiedPlayer proxiedPlayer, int page)
    {
        if (page == 0) {
            proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Benutzung §8" + $.arrow + " §e/clan").build());
            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/clan create <Name> <Tag> §8" + $.arrow + " §7Erstellt einen Clan").build());
            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/clan delete §8" + $.arrow + " §7Löscht einen Clan").build());
            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/clan info §8" + $.arrow + " §7Zeigt Informationen über deinen Clan").build());
            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/clan leave §8" + $.arrow + " §7Verlässt einen Clan").build());
            proxiedPlayer.sendMessage(new MessageBuilder("§6" + $.littleDot + " §e/clan 2 §8" + $.arrow + " §7Zeigt weitere Hilfe").build());
        }
    }
}