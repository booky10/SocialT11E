package tk.t11e.social.commands;
// Created by booky10 in SocialT11E (20:52 27.01.20)

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import tk.t11e.social.main.Main;
import tk.t11e.social.manager.PartyManager;
import tk.t11e.social.util.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class Party extends Command implements TabExecutor {

    private final Main main = Main.main;

    public Party() {
        super("party", "", "p");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (PartyManager.isPlayerInParty(player))
                        PartyManager.listPlayers(player);
                    else
                        player.sendMessage(Main.PREFIX + "You must be in a party to use this command!");
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (PartyManager.isPlayerInParty(player))
                        PartyManager.remove(player);
                    else
                        player.sendMessage(Main.PREFIX + "You must be in a party to use this command!");
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (PartyManager.isPlayerInParty(player))
                        PartyManager.remove(player);
                    PartyManager.createParty(player);
                    player.sendMessage(Main.PREFIX + "§aSuccessfully created party!");
                } else
                    help(player);
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("invite")) {
                    if (PartyManager.isPlayerInParty(player))
                        if (PartyManager.isPlayerPartyOwner(player)) {
                            ProxiedPlayer target = main.getProxy().getPlayer(args[1]);
                            if (target != null)
                                PartyManager.invite(player, target);
                            else
                                player.sendMessage(Main.PREFIX + "Unknown player!");
                        } else
                            player.sendMessage(Main.PREFIX + "You must be the owner of the party!");
                    else
                        player.sendMessage(Main.PREFIX + "You must be in a party, to do that!");
                } else if (args[0].equalsIgnoreCase("promote")) {
                    if (PartyManager.isPlayerInParty(player))
                        if (PartyManager.isPlayerPartyOwner(player)) {
                            ProxiedPlayer target = main.getProxy().getPlayer(args[1]);
                            if (target != null)
                                if (PartyManager.isPlayerInParty(target) && Objects.equals(PartyManager.getPlayerUUIDsInParty(target),
                                        PartyManager.getPlayerUUIDsInParty(player))) {
                                    PartyManager.promote(target);
                                    PartyManager.sendRawPartyMessage(player, target
                                            .getDisplayName() + "§e has been promoted to the new " +
                                            "owner of the party!");
                                } else
                                    player.sendMessage(Main.PREFIX + "That player is not in your party!");
                            else
                                player.sendMessage(Main.PREFIX + "Unknown player!");
                        } else
                            player.sendMessage(Main.PREFIX + "You must be the owner of the party!");
                } else if (args[0].equalsIgnoreCase("accept")) {
                    ProxiedPlayer target = main.getProxy().getPlayer(args[1]);
                    if (target != null) {
                        if (PartyManager.isPlayerInParty(player))
                            PartyManager.remove(player);
                        PartyManager.accept(player, target);
                    } else
                        player.sendMessage(Main.PREFIX + "Unknown player!");
                } else if (args[0].equalsIgnoreCase("chat")
                        || args[0].equalsIgnoreCase("c"))
                    if (PartyManager.isPlayerInParty(player))
                        PartyManager.sendPartyMessage(player, args[1]);
                    else
                        player.sendMessage(Main.PREFIX + "You must be in a party to chat!");
                else
                    help(player);
            } else if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("c")
                        || args[0].equalsIgnoreCase("chat"))
                    if (PartyManager.isPlayerInParty(player)) {
                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i < args.length; i++)
                            message.append(args[i]).append(" ");
                        PartyManager.sendPartyMessage(player, message.toString());
                    } else
                        player.sendMessage(Main.PREFIX + "You must be in a party to chat!");
                else
                    help(player);
            } else
                help(player);
        } else
            sender.sendMessage("You must execute this command as a player!");
    }

    private void help(CommandSender sender) {
        sender.sendMessage("Usage: /party <promote|list|chat|leave|invite|accept> <player|message>");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 1) {
                if (PartyManager.isPlayerInParty(player)) {
                    list.add("list");
                    list.add("chat");
                    list.add("leave");
                }
                if (PartyManager.isPlayerPartyOwner(player)) {
                    list.add("promote");
                    list.add("invite");
                }
                list.add("accept");
                list.add("create");
            } else if (args.length == 2)
                if (args[0].equalsIgnoreCase("promote"))
                    list.addAll(PartyManager.getPlayerNamesInParty(player));
                else if (args[0].equalsIgnoreCase("invite")) {
                    List<String> names = new ArrayList<>();

                    for (ProxiedPlayer player2 : main.getProxy().getPlayers())
                        names.add(player2.getName());
                    names.removeAll(PartyManager.getPlayerNamesInParty(player));

                    list.addAll(names);
                } else if (args[0].equalsIgnoreCase("accept"))
                    list.addAll(PartyManager.getInviteNames(player));
        }
        return TabCompleter.convert(args,list);
    }
}