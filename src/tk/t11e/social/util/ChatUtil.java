package tk.t11e.social.util;
// Created by booky10 in SocialT11E (15:51 28.01.20)

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatUtil {

    public static void sendCommandMessage(ProxiedPlayer player, String message, String command) {
        TextComponent component = new TextComponent(message);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        player.sendMessage(component);
    }
}