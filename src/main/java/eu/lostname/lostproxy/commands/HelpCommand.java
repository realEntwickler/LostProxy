/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 05.01.2021 @ 11:22:51
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * HelpCommand.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.commands;

import eu.lostname.lostproxy.builder.MessageBuilder;
import eu.lostname.lostproxy.utils.Prefix;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class HelpCommand extends Command {

    public HelpCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(new MessageBuilder(Prefix.LOSTNAME + "Informationen zum LostName.eu-Netzwerk").build());
        commandSender.sendMessage(new MessageBuilder("§8┃ §c/hub §8» §7Bringt dich auf die Hauptlobby zurück").build());
        commandSender.sendMessage(new MessageBuilder("§8┃ §c/friend §8» §7Verwalte deine Freunde").build());
        commandSender.sendMessage(new MessageBuilder("§8┃ §c/party §8» §7Erstelle eine Party").build());
        commandSender.sendMessage(new MessageBuilder("§8┃ §c/clan §8» §7Verwalte deinen Clan").build());
        commandSender.sendMessage(new MessageBuilder("§8┃ §c/report §8» §7Melde einen regelbrechenden Spieler").build());
        commandSender.sendMessage(new MessageBuilder("§7Für weitere Informationen besuche unser §eForum§8: §cforum.lostname.eu").build());
        commandSender.sendMessage(new MessageBuilder("§7Folge uns auch auf §bTwitter§8: §e@LostNameEU").build());
        commandSender.sendMessage(new MessageBuilder("§7Betrete ebenfalls auch unseren §9Discord-Server§8: §elostname.eu/discord").build());
    }
}
