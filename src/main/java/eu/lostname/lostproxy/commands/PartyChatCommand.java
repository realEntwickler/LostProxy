package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.interfaces.party.IParty;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyChatCommand extends Command {
    public PartyChatCommand(String partychat, String s, String pc) {
        super(partychat, s, pc);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;

            if (strings.length == 0) {
                proxiedPlayer.sendMessage(new MessageBuilder(Prefix.PARTY + "Benutzung§8: §5/pc [Nachricht]").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pc ").build());
            } else {
                IParty party = LostProxy.getInstance().getPartyManager().getParty(proxiedPlayer);

                if (party != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String string : strings) {
                        stringBuilder.append(string).append(" ");
                    }

                    party.sendMessage(new MessageBuilder(Prefix.PARTY + new IPlayer(proxiedPlayer.getUniqueId()).getColorWithPlayername() + " §8➡ §7" + stringBuilder).build());
                } else {
                    proxiedPlayer.sendMessage(new MessageBuilder(Prefix.PARTY + "Du bist in §ckeiner §7Party§8.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.PARTY + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}

