package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.report.IReport;
import eu.lostname.lostproxy.interfaces.report.IReportReason;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

    public ReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer proxiedPlayer) {

            if (strings.length == 0) {
                sendHelpMessage(proxiedPlayer);
            } else if (strings.length == 1) {
                ProxiedPlayer targetProxiedPlayer = ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.getName().equalsIgnoreCase(strings[0])).findFirst().orElse(null);

                if (targetProxiedPlayer != null) {
                    if (!targetProxiedPlayer.getName().equalsIgnoreCase(proxiedPlayer.getName())) {
                        if (!LostProxy.getInstance().getReportManager().getReportReasons().isEmpty()) {
                            proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Für welches Verhalten möchtest du " + new IPlayer(targetProxiedPlayer.getUniqueId()).getDisplaywithPlayername() + " §7melden?").build());
                            LostProxy.getInstance().getReportManager().getReportReasons().forEach(iReportReason -> proxiedPlayer.sendMessage(new MessageBuilder("§4" + $.littleDot + " §c" + iReportReason.getID() + " §8" + $.arrow + " §c" + iReportReason.getName() + " §8" + $.arrow +" §7" + iReportReason.getDescription()).build()));
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Es konnten §ckeine §7Meldegründe abgerufen werden§7.").build());
                        }
                    } else {
                        proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Du kannst dich §cnicht §7selbst melden§7.").build());
                    }
                } else {
                    proxiedPlayer.sendMessage($.PLAYER_NOT_FOUND($.REPORT));
                }
            } else if (strings.length == 2) {
                ProxiedPlayer targetProxiedPlayer = ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.getName().equalsIgnoreCase(strings[0])).findFirst().orElse(null);

                if (targetProxiedPlayer != null) {
                    if (!targetProxiedPlayer.getName().equalsIgnoreCase(proxiedPlayer.getName())) {
                        int id;

                        try {
                            id = Integer.parseInt(strings[1]);
                        } catch (NumberFormatException exception) {
                            proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Du hast §ckeine §7Zahl angegeben§7.").build());
                            return;
                        }

                        IReportReason reportReason = LostProxy.getInstance().getReportManager().getReportReasonByID(id);

                        if (reportReason != null) {
                            if (LostProxy.getInstance().getReportManager().getReports().stream().noneMatch(iReport -> (iReport.getInvokerProxiedPlayer().getUniqueId().equals(proxiedPlayer.getUniqueId()) && iReport.getTargetProxiedPlayer().getUniqueId().equals(targetProxiedPlayer.getUniqueId())))) {
                                IReport report = new IReport(proxiedPlayer, targetProxiedPlayer, reportReason);
                                LostProxy.getInstance().getReportManager().getReports().add(report);

                                String targetPlayerNameWithColor = new IPlayer(targetProxiedPlayer.getUniqueId()).getDisplaywithPlayername();
                                proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Du hast " + targetPlayerNameWithColor + " §7wegen §c" + reportReason.getName() + " §7gemeldet§7.").build());

                                TextComponent reportNotificationMessage = new MessageBuilder($.REPORT + new IPlayer(proxiedPlayer.getUniqueId()).getDisplaywithPlayername() + " §8" + $.arrow + " " + targetPlayerNameWithColor + " §8" + $.arrow + " §7" + reportReason.getName()).build();
                                LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(notification -> notification.sendMessage(reportNotificationMessage));
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Du hast diesen Spieler §cbereits §7gemeldet§7.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Der angegebene Meldegrund wurde §cnicht §7gefunden§7.").build());
                        }
                    } else {
                        proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Du kannst dich §cnicht §7selbst melden§7.").build());
                    }
                } else {
                    proxiedPlayer.sendMessage($.PLAYER_NOT_FOUND($.REPORT));
                }
            } else {
                sendHelpMessage(proxiedPlayer);
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.REPORT + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§7.").build());
        }
    }

    private static void sendHelpMessage(ProxiedPlayer proxiedPlayer)
    {
        proxiedPlayer.sendMessage(new MessageBuilder($.REPORT + "Benutzung §8" + $.arrow + " §c/report <Spieler>").build());
    }
}
