package tk.t11e.social.manager;
// Created by booky10 in SocialT11E (18:22 27.01.20)

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import tk.t11e.social.main.Main;
import tk.t11e.social.util.UUIDFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnusedReturnValue")
public class FriendManager {

    private final static Main main = Main.main;

    public static Boolean addFriend(ProxiedPlayer player, UUID target) {
        Configuration config = main.getConfig();
        List<String> friends = config.getStringList("Friends." + player.getUniqueId().toString());

        if (friends.contains(target.toString()))
            return false;

        friends.add(target.toString());
        config.set("Friends." + player.getUniqueId().toString(), friends);

        main.saveConfig(config);
        return true;
    }

    public static Boolean removeFriend(ProxiedPlayer player, ProxiedPlayer target) {
        Configuration config = main.getConfig();
        List<String> friends = config.getStringList("Friends." + player.getUniqueId().toString());

        if (!friends.contains(target.getUniqueId().toString()))
            return false;

        friends.remove(target.getUniqueId().toString());
        config.set("Friends." + player.getUniqueId().toString(), friends);

        main.saveConfig(config);
        return true;
    }

    public static List<UUID> getFriendUUIDs(ProxiedPlayer player) {
        Configuration config = main.getConfig();
        List<String> UUIDStrings = config.getStringList("Friends." + player
                .getUniqueId().toString());
        List<UUID> UUIDs = new ArrayList<>();

        for (String UUID:UUIDStrings)
            UUIDs.add(java.util.UUID.fromString(UUID));

        return UUIDs;
    }

    public static List<String> getFriendNames(ProxiedPlayer player) {
        List<String> names = new ArrayList<>();
        for (UUID UUID:getFriendUUIDs(player))
            names.add(UUIDFetcher.getName(UUID));

        return names;
    }

    public static List<UUID> getOnlineFriendUUIDs(ProxiedPlayer player) {
        List<UUID> UUIDs = new ArrayList<>();
        for (ProxiedPlayer player2 : main.getProxy().getPlayers())
            if (getFriendUUIDs(player).contains(player2.getUniqueId()))
                UUIDs.add(player2.getUniqueId());

        return UUIDs;
    }

    public static List<String> getOnlineFriendNames(ProxiedPlayer player) {
        List<String> names = new ArrayList<>();
        for (UUID UUID:getOnlineFriendUUIDs(player))
            names.add(UUIDFetcher.getName(UUID));

        return names;
    }
}