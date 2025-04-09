package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.report.IReport;
import eu.lostname.lostproxy.interfaces.report.IReportReason;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

    public ReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Benutzung§8: §c/report <Spieler>").build());
            } else if (strings.length == 1) {
                ProxiedPlayer targetProxiedPlayer = ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.getName().equalsIgnoreCase(strings[0])).findFirst().orElse(null);

                if (targetProxiedPlayer != null) {
                    if (!targetProxiedPlayer.getName().equalsIgnoreCase(proxiedPlayer.getName())) {
                        if (LostProxy.getInstance().getReportManager().getReportReasons().size() > 0) {
                            proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Für welches Verhalten möchtest du " + new IPlayer(targetProxiedPlayer.getUniqueId()).getColorWithPlayername() + " §7melden?").build());
                            LostProxy.getInstance().getReportManager().getReportReasons().forEach(iReportReason -> proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §c" + iReportReason.getID() + " §8» §c" + iReportReason.getName() + " §8┃ §7" + iReportReason.getDescription()).addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8» §7aKlicke§8, §7um diesen Meldegrund auszuwählen").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/report " + targetProxiedPlayer.getName() + " " + iReportReason.getID()).build()));
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Es konnten §ckeine §7Meldegründe abgerufen werden§8.").build());
                        }
                    } else {
                        proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Du kannst dich §cnicht §7selbst melden§8.").build());
                    }
                } else {
                    proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            } else if (strings.length == 2) {
                ProxiedPlayer targetProxiedPlayer = ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.getName().equalsIgnoreCase(strings[0])).findFirst().orElse(null);

                if (targetProxiedPlayer != null) {
                    if (!targetProxiedPlayer.getName().equalsIgnoreCase(proxiedPlayer.getName())) {
                        int id;

                        try {
                            id = Integer.parseInt(strings[1]);
                        } catch (NumberFormatException exception) {
                            proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Du hast §ckeine §7Zahl angegeben§8.").build());
                            return;
                        }

                        IReportReason reportReason = LostProxy.getInstance().getReportManager().getReportReasonByID(id);

                        if (reportReason != null) {
                            if (LostProxy.getInstance().getReportManager().getReports().stream().noneMatch(iReport -> (iReport.getInvokerProxiedPlayer().getUniqueId().equals(proxiedPlayer.getUniqueId()) && iReport.getTargetProxiedPlayer().getUniqueId().equals(targetProxiedPlayer.getUniqueId())))) {
                                IReport report = new IReport(proxiedPlayer, targetProxiedPlayer, reportReason);
                                LostProxy.getInstance().getReportManager().getReports().add(report);

                                String targetPlayerNameWithColor = new IPlayer(targetProxiedPlayer.getUniqueId()).getColorWithPlayername();
                                proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Du hast " + targetPlayerNameWithColor + " §7wegen §c" + reportReason.getName() + " §7gemeldet§8.").build());

                                TextComponent reportNotificationMessage = new MessageBuilder(Prefix.REPORT + new IPlayer(proxiedPlayer.getUniqueId()).getColorWithPlayername() + " §8➡ " + targetPlayerNameWithColor + " §8┃ §7" + reportReason.getName()).build();
                                LostProxy.getInstance().getTeamManager().getNotificationOn().forEach(notification -> notification.sendMessage(reportNotificationMessage));
                            } else {
                                proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Du hast diesen Spieler §cbereits §7gemeldet§8.").build());
                            }
                        } else {
                            proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Der angegebene Meldegrund wurde §cnicht §7gefunden§8.").build());
                        }
                    } else {
                        proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Du kannst dich §cnicht §7selbst melden§8.").build());
                    }
                } else {
                    proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Der angegebene Spieler wurde §cnicht §7gefunden§8.").build());
                }
            } else {
                proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Benutzung§8: §c/report <Spieler>").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.REPORT + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}
