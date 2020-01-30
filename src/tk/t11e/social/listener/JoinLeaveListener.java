package tk.t11e.social.listener;
// Created by booky10 in SocialT11E (16:50 28.01.20)

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.t11e.social.main.Main;
import tk.t11e.social.manager.FriendManager;

@SuppressWarnings("deprecation")
public class JoinLeaveListener implements Listener {

    private final Main main = Main.main;
    private final String FRIEND_PREFIX = "§7[§bFriend§7]§e ";

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        for (String name : FriendManager.getOnlineFriendNames(player))
            main.getProxy().getPlayer(name).sendMessage(FRIEND_PREFIX + player
                    .getDisplayName() + " joined!");
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        for (String name : FriendManager.getOnlineFriendNames(player))
            main.getProxy().getPlayer(name).sendMessage(FRIEND_PREFIX + player
                    .getDisplayName() + " left!");
    }
}