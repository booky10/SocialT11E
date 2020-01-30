package tk.t11e.social.commands;
// Created by booky10 in SocialT11E (19:35 27.01.20)

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import tk.t11e.social.main.Main;
import tk.t11e.social.manager.FriendManager;
import tk.t11e.social.util.TabCompleter;
import tk.t11e.social.util.UUIDFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class Friend extends Command implements TabExecutor {

    private final Main main = Main.main;

    public Friend() {
        super("friend", "", "f");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 1)
                if (args[0].equalsIgnoreCase("list")) {
                    player.sendMessage("§e--------§6[Friends]§e--------");
                    for (String name : FriendManager.getFriendNames(player))
                        if (FriendManager.getOnlineFriendNames(player).contains(name))
                            player.sendMessage("  §e- " + name + "§e is online at " + main.getProxy()
                                    .getPlayer(name).getServer().getInfo().getName());
                        else
                            player.sendMessage("  §e- " + name + "§e is offline");
                } else
                    help(player);
            else if (args.length == 2)
                if (args[0].equalsIgnoreCase("check"))
                    if (FriendManager.getFriendNames(player).contains(args[1]))
                        if (FriendManager.getOnlineFriendNames(player).contains(args[1]))
                            player.sendMessage(Main.PREFIX + "§aThat player is online!");
                        else
                            player.sendMessage(Main.PREFIX + "That player is offline!");
                    else
                        player.sendMessage(Main.PREFIX + "That player is not your friend!");
                else if (args[0].equalsIgnoreCase("add")
                        ||args[0].equalsIgnoreCase("invite"))
                    if (!FriendManager.getFriendNames(player).contains(args[1])) {
                        UUID target= UUIDFetcher.getUUID(args[1]);
                            FriendManager.addFriend(player, target);
                            if(main.getProxy().getPlayer(target)!=null)
                                main.getProxy().getPlayer(target).sendMessage(Main.PREFIX + player.getName() + " has added you as a " +
                                    "friend!");
                            player.sendMessage(Main.PREFIX + "§aSuccessfully added player!");
                            System.out.println("[Friend] " + player.getName() + " has added " +
                                    args[1]);
                    } else
                        player.sendMessage(Main.PREFIX + "You already have that player as a friend!");
                else if (args[0].equalsIgnoreCase("remove"))
                    if (FriendManager.getFriendNames(player).contains(args[1])) {
                        ProxiedPlayer target = main.getProxy().getPlayer(args[1]);
                        if (target != null) {
                            FriendManager.removeFriend(player, target);
                            target.sendMessage(Main.PREFIX + player.getName() + " has removed you" +
                                    " as a friend!");
                            player.sendMessage(Main.PREFIX + "§aSuccessfully removed player!");
                            System.out.println("[Friend] " + player.getName() + " has removed " +
                                    target.getName());
                        } else
                            player.sendMessage(Main.PREFIX + "Unknown player!");
                    } else
                        player.sendMessage(Main.PREFIX + "You already don't have that player as a " +
                                "friend!");
                else
                    help(player);
        } else
            sender.sendMessage("You must execute this command as a player!");
    }

    private void help(CommandSender sender) {
        sender.sendMessage("Usage: /friend <list|add|remove|check> <player>");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length == 1) {
                list.add("add");
                list.add("remove");
                list.add("check");
                list.add("list");
            } else if (args.length == 2)
                if (args[0].equalsIgnoreCase("remove"))
                    list.addAll(FriendManager.getFriendNames(player));
                else if (args[0].equalsIgnoreCase("check"))
                    list.addAll(FriendManager.getFriendNames(player));
                else if (args[0].equalsIgnoreCase("add"))
                    for (ProxiedPlayer player2 : main.getProxy().getPlayers())
                        list.add(player2.getName());
        }
        return TabCompleter.convert(args,list);
    }
}