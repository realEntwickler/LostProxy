package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportsCommand extends Command {
    public ReportsCommand(String reports, String s, String s1) {
        super(reports, s, s1);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;
            if (strings.length == 0) {
                proxiedPlayer.sendMessage(new MessageBuilder(Prefix.REPORT + "Benutzung von §c/reports§8:").build());
                proxiedPlayer.sendMessage(new MessageBuilder("§8┃ §c/reports list §8» §7Listet alle offenen Reports auf").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§8[§aKlick§8]").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reports list").build());
            }
        } else {
            commandSender.sendMessage(new MessageBuilder(Prefix.REPORT + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§8.").build());
        }
    }
}
