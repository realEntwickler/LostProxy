package eu.lostname.lostproxy.manager;

import eu.lostname.lostproxy.interfaces.party.IParty;
import eu.lostname.lostproxy.interfaces.party.IPartyInvitation;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PartyManager {

    private ArrayList<IParty> parties;
    private ArrayList<IPartyInvitation> invitations;

    public PartyManager() {
        this.parties = new ArrayList<>();
        this.invitations = new ArrayList<>();
    }

    public List<IPartyInvitation> getInvitations (ProxiedPlayer proxiedPlayer) {
        return invitations.stream().filter(filter -> filter.getInvitationFor().getUniqueId().toString().equalsIgnoreCase(proxiedPlayer.getUniqueId().toString())).collect(Collectors.toList());
    }

    public IParty getParty (ProxiedPlayer proxiedPlayer) {
        return parties.stream().filter(filter -> filter.getMembers().containsKey(proxiedPlayer)).findFirst().orElse(null);
    }

    public ArrayList<IParty> getParties() {
        return parties;
    }

    public ArrayList<IPartyInvitation> getInvitations() {
        return invitations;
    }
}