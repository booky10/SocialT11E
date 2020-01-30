package tk.t11e.social.manager;
// Created by booky10 in SocialT11E (20:50 27.01.20)

import com.sun.istack.internal.NotNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import tk.t11e.social.main.Main;
import tk.t11e.social.util.ChatUtil;
import tk.t11e.social.util.UUIDFetcher;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class PartyManager {

    private final static Main main = Main.main;
    public static final HashMap<UUID, List<UUID>> parties = new HashMap<>();
    public static final HashMap<UUID, List<UUID>> invites = new HashMap<>();

    public static Boolean isPlayerInParty(ProxiedPlayer player) {
        if (parties.containsKey(player.getUniqueId()))
            return true;
        else
            for (List<UUID> UUIDs : parties.values())
                if (UUIDs.contains(player.getUniqueId()))
                    return true;
        return false;
    }

    public static List<UUID> getInviteUUIDs(ProxiedPlayer player) {
        return invites.get(player.getUniqueId());
    }

    public static List<String> getInviteNames(ProxiedPlayer player) {
        List<String> names=new ArrayList<>();
        for (UUID UUID:getInviteUUIDs(player))
            names.add(UUIDFetcher.getName(UUID));
        return names;
    }

    public static void remove(ProxiedPlayer player) {
        if (isPlayerInParty(player))
            if (isPlayerPartyOwner(player)) {
                sendRawPartyMessage(player, player.getDisplayName() + "§e has disbanded this party!");
                parties.remove(player.getUniqueId());
            } else {
                sendRawPartyMessage(player,player.getDisplayName()+"§e has left the party!");
                List<UUID> UUIDs =parties.get(getPartyOwnerUUID(player));
                UUIDs.remove(player.getUniqueId());
                parties.put(getPartyOwnerUUID(player), UUIDs);
            }
    }

    public static void accept(ProxiedPlayer player,ProxiedPlayer target) {
        if (isPlayerInParty(target))
            if (invites.containsKey(player.getUniqueId()))
                if (getInviteUUIDs(player).contains(target.getUniqueId())) {
                    List<UUID> UUIDs = getInviteUUIDs(player);
                    UUIDs.remove(target.getUniqueId());
                    invites.put(player.getUniqueId(), UUIDs);

                    List<UUID> UUIDs2 = parties.get(target.getUniqueId());
                    UUIDs2.add(player.getUniqueId());
                    parties.put(target.getUniqueId(), UUIDs2);
                    sendRawPartyMessage(target, player.getDisplayName() + "§e has joined " +
                            "the party!");
                }
    }

    public static UUID getPartyOwnerUUID(ProxiedPlayer player) {
        if (isPlayerPartyOwner(player)) return player.getUniqueId();

        List<UUID> UUIDs = new ArrayList<>();
        for (List<UUID> UUIDs2 : parties.values())
            if (UUIDs2.contains(player.getUniqueId())) {
                UUIDs = UUIDs2;
                break;
            }

        Set<UUID> UUIDList = getKeysByValue(parties, UUIDs);
        for (UUID UUID : UUIDList)
            return UUID;
        return null;
    }

    public static void promote(ProxiedPlayer player) {
        if(!isPlayerInParty(player)||isPlayerPartyOwner(player)) return;
        List<UUID> UUIDs = parties.get(getPartyOwnerUUID(player));

        parties.remove(getPartyOwnerUUID(player));
        parties.put(player.getUniqueId(),UUIDs);
    }

    public static Boolean isPlayerPartyOwner(ProxiedPlayer player) {
        return parties.containsKey(player.getUniqueId());
    }

    public static void invite(ProxiedPlayer player, ProxiedPlayer target) {
        if (!isPlayerInParty(player))
            createParty(player);
        if (!isPlayerPartyOwner(player)) return;

        sendRawPartyMessage(player, player.getDisplayName() + "§e has invited§r " + target
                .getDisplayName() + "§e to the party!");
        ChatUtil.sendCommandMessage(target, player.getDisplayName() + "§e has invited" +
                " you to join their party!§6 Click here§e to accept this invite!",
                "/party accept " + player.getName());

        List<UUID> UUIDs;
        if (invites.containsKey(target.getUniqueId()))
            UUIDs = invites.get(target.getUniqueId());
        else
            UUIDs = new ArrayList<>();
        UUIDs.add(player.getUniqueId());
        invites.put(target.getUniqueId(), UUIDs);
    }

    @NotNull
    public static List<UUID> getPlayerUUIDsInParty(ProxiedPlayer player) {
        if (isPlayerInParty(player))
            if (isPlayerPartyOwner(player))
                return parties.get(player.getUniqueId());
            else
                for (List<UUID> UUIDs : parties.values())
                    if (UUIDs.contains(player.getUniqueId()))
                        return UUIDs;
        return null;
    }

    public static void sendPartyMessage(ProxiedPlayer player, String message) {
        sendRawPartyMessage(player, "§9Party>§f " + player.getDisplayName() + "§f: " + message);
    }

    public static void sendRawPartyMessage(ProxiedPlayer player, String message) {
        if (isPlayerInParty(player))
            for (UUID UUID : Objects.requireNonNull(getPlayerUUIDsInParty(player)))
                for (ProxiedPlayer player2 : main.getProxy().getPlayers())
                    if (player2.getUniqueId().equals(UUID))
                        player2.sendMessage(message);
    }

    public static void createParty(ProxiedPlayer owner) {
        List<UUID> UUIDs = new ArrayList<>();

        UUIDs.add(owner.getUniqueId());
        parties.put(owner.getUniqueId(), UUIDs);
    }

    public static void listPlayers(ProxiedPlayer player) {
        if(!isPlayerInParty(player)) return;
        player.sendMessage("§e--------§6[Party]§e--------");
        for (String name:getPlayerNamesInParty(player))
            player.sendMessage("§e  - "+name);
    }

    public static List<String> getPlayerNamesInParty(ProxiedPlayer player) {
        List<String> names = new ArrayList<>();
        for (UUID UUID: Objects.requireNonNull(getPlayerUUIDsInParty(player)))
            names.add(UUIDFetcher.getName(UUID));
        return names;
    }
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}