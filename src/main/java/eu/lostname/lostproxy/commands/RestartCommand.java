package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class RestartCommand extends Command {

    public RestartCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new MessageBuilder($.LOSTNAME + "Benutzung§8: §c/restart <now,30,60,cancel> [Grund]").build());
        } else {
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("now")) {
                    ProxyServer.getInstance().getPlayers().forEach(all -> all.disconnect(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                            "\n" +
                            "§7Deine bestehende Verbindung zum Netzwerk wurde §egetrennt§7." +
                            "\n" +
                            "\n" +
                            "§7Grund §8➡ §eNeustart des Netzwerkes" +
                            "\n" +
                            "\n" +
                            "§7Bei weiteren Fragen besuche unser §eForum§8!" +
                            "\n" +
                            " §8» §cforum§7.§clostname§7.§ceu §8«" +
                            "\n" +
                            "\n" +
                            "§8§m--------------------------------------§r").build()));
                    ProxyServer.getInstance().stop("COMMAND_RESTART_NOW");
                } else if (strings[0].equalsIgnoreCase("30")) {
                    if (LostProxy.getInstance().getRestartTask() == null) {
                        final int[] seconds = new int[1];
                        seconds[0] = 30;
                        LostProxy.getInstance().setRestartTask(ProxyServer.getInstance().getScheduler().schedule(LostProxy.getInstance(), () -> {
                            switch (seconds[0]) {
                                case 30:
                                case 20:
                                case 10:
                                case 5:
                                case 3:
                                case 2:
                                case 1:
                                    ProxyServer.getInstance().broadcast(new MessageBuilder($.LOSTNAME + "Das Netzwerk wird in §c" + seconds[0] + " Sekunde" + (seconds[0] == 1 ? "" : "n") + " §7neugestartet§7.").build());
                                    break;
                                case 0:
                                    ProxyServer.getInstance().getPlayers().forEach(all -> all.disconnect(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                            "\n" +
                                            "§7Deine bestehende Verbindung zum Netzwerk wurde §egetrennt§7." +
                                            "\n" +
                                            "\n" +
                                            "§7Grund §8➡ §eNeustart des Netzwerkes" +
                                            "\n" +
                                            "\n" +
                                            "§7Bei weiteren Fragen besuche unser §eForum§8!" +
                                            "\n" +
                                            " §8» §cforum§7.§clostname§7.§ceu §8«" +
                                            "\n" +
                                            "\n" +
                                            "§8§m--------------------------------------§r").build()));

                                    ProxyServer.getInstance().stop("COMMAND_RESTART_30");
                                    return;
                            }
                            seconds[0]--;
                        }, 0, 1, TimeUnit.SECONDS));
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.LOSTPROXY + "Es ist §cbereits §7ein Neustart im Gange§7.").build());
                    }
                } else if (strings[0].equalsIgnoreCase("60")) {
                    if (LostProxy.getInstance().getRestartTask() == null) {
                        final int[] seconds = new int[1];
                        seconds[0] = 60;
                        LostProxy.getInstance().setRestartTask(ProxyServer.getInstance().getScheduler().schedule(LostProxy.getInstance(), () -> {
                            switch (seconds[0]) {
                                case 60:
                                case 30:
                                case 20:
                                case 10:
                                case 5:
                                case 3:
                                case 2:
                                case 1:
                                    ProxyServer.getInstance().broadcast(new MessageBuilder($.LOSTNAME + "Das Netzwerk wird in §c" + seconds[0] + " Sekunde" + (seconds[0] == 1 ? "" : "n") + " §7neugestartet§7.").build());
                                    break;
                                case 0:
                                    ProxyServer.getInstance().getPlayers().forEach(all -> all.disconnect(new MessageBuilder("§6§o■§r §8┃ §cLostName §8● §7the new version of us §8┃ §6§o■§r \n" +
                                            "\n" +
                                            "§7Deine bestehende Verbindung zum Netzwerk wurde §egetrennt§7." +
                                            "\n" +
                                            "\n" +
                                            "§7Grund §8➡ §eNeustart des Netzwerkes" +
                                            "\n" +
                                            "\n" +
                                            "§7Bei weiteren Fragen besuche unser §eForum§8!" +
                                            "\n" +
                                            " §8» §cforum§7.§clostname§7.§ceu §8«" +
                                            "\n" +
                                            "\n" +
                                            "§8§m--------------------------------------§r").build()));

                                    ProxyServer.getInstance().stop("COMMAND_RESTART_30");
                                    return;
                            }
                            seconds[0]--;
                        }, 0, 1, TimeUnit.SECONDS));
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.LOSTPROXY + "Es ist §cbereits §7ein Neustart im Gange§7.").build());
                    }
                } else if (strings[0].equalsIgnoreCase("cancel")) {
                    if (LostProxy.getInstance().getRestartTask() != null) {
                        LostProxy.getInstance().getRestartTask().cancel();
                        LostProxy.getInstance().setRestartTask(null);

                        commandSender.sendMessage(new MessageBuilder($.LOSTPROXY + "Der Neustart wurde §cabgebrochen§7.").build());
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.LOSTPROXY + "Es ist §ckein §7Neustart im Gange§7.").build());
                    }
                }
            } else if (strings.length >= 2) {
                //TODO: Restart with custom grund
            }
        }
    }
}
