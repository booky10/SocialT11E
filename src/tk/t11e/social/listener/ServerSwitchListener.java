package tk.t11e.social.listener;
// Created by booky10 in SocialT11E (18:08 28.01.20)

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.t11e.social.main.Main;
import tk.t11e.social.manager.PartyManager;

import java.util.Objects;
import java.util.UUID;

public class ServerSwitchListener implements Listener {

    private final Main main = Main.main;

    @EventHandler
    public void onSwitch(ServerConnectedEvent event) {
        if (PartyManager.isPlayerInParty(event.getPlayer()))
            if (PartyManager.isPlayerPartyOwner(event.getPlayer()))
                    for (UUID UUID : Objects.requireNonNull(PartyManager
                            .getPlayerUUIDsInParty(event.getPlayer()))) {
                        ProxiedPlayer player = main.getProxy().getPlayer(UUID);
                        if (player != null)
                            if (!player.getUniqueId().equals(event.getPlayer().getUniqueId()))
                                if (!player.getServer().equals(event.getServer()))
                                    player.connect(event.getServer().getInfo(),
                                            ServerConnectEvent.Reason.PLUGIN);
                    }
    }
}