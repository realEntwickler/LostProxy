/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:52
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * TCCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.interfaces.IPlayer;
import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TCCommand extends Command {

    public TCCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer player) {

            if (strings.length == 0) {
                player.sendMessage(new MessageBuilder($.TMS + "Benutzung von §e/tc§8:").build());
                player.sendMessage(new MessageBuilder("§8┃ §e/tc [Nachricht] §8» §7Schreibe in den TeamChat").addClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tc ").addHoverEvent(HoverEvent.Action.SHOW_TEXT, "§a☑").build());
            } else {
                if (LostProxy.getInstance().getTeamManager().isLoggedIn(player)) {
                    String msg = LostProxy.getInstance().formatArrayToString(0, strings);

                    IPlayer iPlayer = new IPlayer(player.getUniqueId());
                    LostProxy.getInstance().getTeamManager().getLoggedIn().forEach(all -> all.sendMessage(new MessageBuilder($.TEAM_CHAT + iPlayer.getDisplaywithPlayername() + " §8➡ §7" + msg).build()));
                } else {
                    player.sendMessage(new MessageBuilder($.TMS + "Du bist §cnicht §7eingeloggt.").build());
                }
            }
        } else {
            commandSender.sendMessage(new MessageBuilder($.TMS + "Du kannst diesen Befehl §cnicht §7als Konsole ausführen.").build());
        }
    }
}
