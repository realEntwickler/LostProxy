package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportsCommand extends Command {
    public ReportsCommand(String reports, String s, String s1) {
        super(reports, s, s1);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer proxiedPlayer) {

        } else {
            commandSender.sendMessage(new MessageBuilder($.REPORT + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen§7.").build());
        }
    }
}
