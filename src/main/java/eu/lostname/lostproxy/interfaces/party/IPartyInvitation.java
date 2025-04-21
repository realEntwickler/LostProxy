package eu.lostname.lostproxy.interfaces.party;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class IPartyInvitation {

    private final ProxiedPlayer invitationFor;
    private final IParty party;
    private final Timer timer;

    public IPartyInvitation(ProxiedPlayer invitationFor, IParty party) {
        this.invitationFor = invitationFor;
        this.party = party;
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (invitationFor != null) {
                    LostProxy.getInstance().getPartyManager().getInvitations().remove(IPartyInvitation.this);
                    invitationFor.sendMessage(new MessageBuilder(Prefix.PARTY + "Deine Einladung für die Party von " + new IPlayer(party.getLeader().getUniqueId()).getDisplaywithPlayername() + " §7ist §cabgelaufen§8.").build());
                    party.getLeader().sendMessage(new MessageBuilder(Prefix.PARTY + "Die Einladung für " + new IPlayer(invitationFor.getUniqueId()).getDisplaywithPlayername() + " §7ist §cabgelaufen§8.").build());
                }
            }
        }, TimeUnit.MINUTES.toMillis(5));
    }

    public void delete() {
        LostProxy.getInstance().getPartyManager().getInvitations().remove(this);
    }

    public Timer getTimer() {
        return timer;
    }

    public ProxiedPlayer getInvitationFor() {
        return invitationFor;
    }

    public IParty getParty() {
        return party;
    }
}