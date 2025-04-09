package eu.lostname.lostproxy.listener;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.party.IParty;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onSwitch (ServerSwitchEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        if (LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer) != null) {
            IParty party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

            if (party.isLeader(proxiedPlayer)) {
                party.setCurrentServer(proxiedPlayer.getServer().getInfo());

                if (!party.getCurrentServer().getName().startsWith("Lobby-")) {
                    party.sendMessage(new MessageBuilder(Prefix.PARTY + "Die Party betritt den Server §e" + party.getCurrentServer().getName() + "§8.").build());
                    party.getOnlyMembers().forEach(one -> one.connect(party.getCurrentServer()));
                }
            }
        }
    }
}
