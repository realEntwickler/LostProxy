package eu.lostname.lostproxy.records;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public record RCommandWatch(CommandSender commandSender, ProxiedPlayer target) {
}
