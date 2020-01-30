package tk.t11e.social.commands;
// Created by booky10 in SocialT11E (17:27 29.01.20)

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import tk.t11e.social.main.Main;
import tk.t11e.social.manager.PartyManager;

import java.util.Collections;

@SuppressWarnings("deprecation")
public class PartyChat extends Command implements TabExecutor {

    private Main main = Main.main;

    public PartyChat() {
        super("partyChat", "", "pc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(args.length>0) {
                if(PartyManager.isPlayerInParty(player)){
                    if(args.length==1)
                        PartyManager.sendPartyMessage(player,args[0]);
                    else {
                        StringBuilder message= new StringBuilder();
                        for (String arg : args)
                            message.append(arg).append(" ");
                        PartyManager.sendPartyMessage(player, message.toString());
                    }
                }else
                    player.sendMessage(Main.PREFIX+"You must be in a party, to chat!");
            }else
                help(player);
        } else
            sender.sendMessage("You must execute this command as a player!");
    }

    private void help(CommandSender sender) {
        sender.sendMessage("Usage: /partyChat <message>");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}