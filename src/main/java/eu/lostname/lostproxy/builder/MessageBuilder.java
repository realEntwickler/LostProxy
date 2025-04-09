/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:32:28
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MessageBuilder.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.builder;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.List;

public class MessageBuilder {

    private final TextComponent textComponent;

    public MessageBuilder(String content) {
        this.textComponent = new TextComponent(TextComponent.fromLegacyText(content));
    }

    public MessageBuilder addClickEvent(ClickEvent.Action action, String content) {
        textComponent.setClickEvent(new ClickEvent(action, content));
        return this;
    }

    public MessageBuilder addHoverEvent(HoverEvent.Action action, String content) {
        textComponent.setHoverEvent(new HoverEvent(action, new Text(content)));
        return this;
    }

    public MessageBuilder addExtra(TextComponent textComponent) {
        textComponent.addExtra(textComponent);
        return this;
    }

    public MessageBuilder setExtra(List<BaseComponent> textComponents) {
        textComponent.setExtra(textComponents);
        return this;
    }

    public TextComponent build() {
        return textComponent;
    }
}
