package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.records.RCommandWatch;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommandWatchCommand extends Command implements TabExecutor {


    public CommandWatchCommand(String name, String permission, String... aliases)
    {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings)
    {
        if (strings.length == 0) {
            sendHelpMessage(commandSender);
        } else if (strings.length == 1) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);
            if (target != null && target.isConnected()) {
                IPlayer targetIPlayer = new IPlayer(target.getUniqueId());

                List<RCommandWatch> commandWatches = LostProxy.getInstance().getPlayerManager().getCommandWatches(commandSender);
                if (commandWatches != null) {
                    RCommandWatch commandWatch = commandWatches.stream().filter(filter -> filter.target().getUniqueId().equals(target.getUniqueId())).findFirst().orElse(null);
                    if (commandWatch != null) {
                        commandSender.sendMessage(new MessageBuilder($.LOSTNAME + "Du beobachtest " + targetIPlayer.getDisplaywithPlayername() + " §cnicht mehr§7.").build());
                        LostProxy.getInstance().getPlayerManager().getCommandWatchers().remove(commandWatch);
                    } else {
                        commandSender.sendMessage(new MessageBuilder($.LOSTNAME + "Du beobachtest " + targetIPlayer.getDisplaywithPlayername() + " §anun§7.").build());
                        LostProxy.getInstance().getPlayerManager().getCommandWatchers().add(new RCommandWatch(commandSender, target));
                    }
                } else {
                    commandSender.sendMessage(new MessageBuilder($.LOSTNAME + "Du beobachtest " + targetIPlayer.getDisplaywithPlayername() + " §anun§7.").build());
                    LostProxy.getInstance().getPlayerManager().getCommandWatchers().add(new RCommandWatch(commandSender, target));
                }
            } else {
                commandSender.sendMessage($.PLAYER_NOT_FOUND($.LOSTNAME));
            }
        }
    }

    private void sendHelpMessage(CommandSender commandSender)
    {
        commandSender.sendMessage($.LOSTNAME + "Benutzung §8" + $.arrow + " §e/commandwatcher <Spieler>");
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings)
    {
        List<String> list = new ArrayList<>();
        
        if (strings.length == 1) {
            List<ProxiedPlayer> list1 = ProxyServer.getInstance().getPlayers().stream().filter(filter -> filter.getName().startsWith(strings[0])).toList();
            if (list1 != null && !list1.isEmpty())
                list1.forEach(all -> list.add(all.getName()));
        }

        return list;
    }
}
