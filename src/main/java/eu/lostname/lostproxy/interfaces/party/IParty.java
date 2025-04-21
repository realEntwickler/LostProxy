package eu.lostname.lostproxy.interfaces.party;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.enums.EPartyRole;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class IParty {

    private final HashMap<ProxiedPlayer, EPartyRole> members;
    private ServerInfo currentServer;

    public IParty(ProxiedPlayer proxiedPlayer) {
        this.members = new HashMap<>();
        members.put(proxiedPlayer, EPartyRole.LEADER);

        this.currentServer = proxiedPlayer.getServer().getInfo();
    }

    public void setLeader (ProxiedPlayer proxiedPlayer) {
        members.replace(proxiedPlayer, EPartyRole.LEADER);
    }

    public ProxiedPlayer getLeader() {
        return members.keySet().stream().filter(filter -> members.get(filter) == EPartyRole.LEADER).findFirst().orElse(null);
    }

    public Set<ProxiedPlayer> getOnlyMembers() {
        return members.keySet().stream().filter(filter -> members.get(filter) == EPartyRole.MEMBER).collect(Collectors.toSet());
    }

    public void sendMessage (BaseComponent baseComponent) {
        members.keySet().forEach(one -> one.sendMessage(baseComponent));
    }

    public void addMember (ProxiedPlayer proxiedPlayer) {
        members.put(proxiedPlayer, EPartyRole.MEMBER);
        sendMessage(new MessageBuilder($.PARTY + new IPlayer(proxiedPlayer.getUniqueId()).getDisplaywithPlayername() + " §7ist der Party beigetreten§7.").build());
    }

    public void removeMember (ProxiedPlayer proxiedPlayer) {
        members.remove(proxiedPlayer);
        sendMessage(new MessageBuilder($.PARTY + new IPlayer(proxiedPlayer.getUniqueId()).getDisplaywithPlayername() + " §7hat die Party verlassen§7.").build());
    }

    public boolean isLeader (ProxiedPlayer proxiedPlayer) {
        return members.get(proxiedPlayer) == EPartyRole.LEADER;
    }

    public void delete () {
        LostProxy.getInstance().getPartyManager().getParties().remove(this);
    }

    public void register() { LostProxy.getInstance().getPartyManager().getParties().add(this); }

    public HashMap<ProxiedPlayer, EPartyRole> getMembers() {
        return members;
    }

    public ServerInfo getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(ServerInfo currentServer) {
        this.currentServer = currentServer;
    }
}
