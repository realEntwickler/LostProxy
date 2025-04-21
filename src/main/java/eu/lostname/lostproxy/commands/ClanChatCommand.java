package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.clan.IClan;
import eu.lostname.lostproxy.interfaces.clan.IClanPlayerData;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClanChatCommand extends Command {
    public ClanChatCommand(String clanchat, String s, String cc) {
        super(clanchat, s, cc);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer proxiedPlayer) {
            IPlayer iPlayer = new IPlayer(proxiedPlayer.getUniqueId());

            if (strings.length == 0) {
                proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Benutzung§8: §e/clanchat [Nachricht]").build());
            } else {
                IClanPlayerData clanPlayerData = LostProxy.getInstance().getClanManager().getClanPlayerData(proxiedPlayer.getUniqueId());
                if (clanPlayerData != null) {
                    IClan clan = LostProxy.getInstance().getClanManager().getClanByUniqueId(clanPlayerData.getClanUid());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String string : strings)
                        stringBuilder.append(string).append(" ");

                    LostProxy.getInstance().getClanManager().getClanPlayerDatasFromClan(clan.getId()).stream().filter(filter -> ProxyServer.getInstance().getPlayer(filter.getUniqueId()) != null).forEach(all -> ProxyServer.getInstance().getPlayer(all.getUniqueId()).sendMessage(new MessageBuilder($.CLANS + iPlayer.getDisplaywithPlayername() + " §8➡ §7" + stringBuilder).build()));
                } else {
                    proxiedPlayer.sendMessage(new MessageBuilder($.CLANS + "Du bist in §ckeinem §7Clan.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.CLANS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen.").build());
        }
    }
}
