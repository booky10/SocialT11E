package tk.t11e.social.main;
// Created by booky10 in SocialT11E (18:11 27.01.20)

import com.sun.istack.internal.NotNull;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import tk.t11e.social.commands.Friend;
import tk.t11e.social.commands.Party;
import tk.t11e.social.commands.PartyChat;
import tk.t11e.social.listener.JoinLeaveListener;
import tk.t11e.social.listener.ServerSwitchListener;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Main extends Plugin {

    public static final String PREFIX = "§7[§bT11E§7]§c ", NO_PERMISSION = PREFIX + "You don't have " +
            "the permissions for this!";
    public static Main main;

    @Override
    public void onEnable() {
        long milliseconds = System.currentTimeMillis();
        main = this;

        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdirs();
            if (!getConfigFile().exists())
                getConfigFile().createNewFile();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        init();

        milliseconds = System.currentTimeMillis() - milliseconds;
        System.out.println("[BungeeT11E] It took " + milliseconds + "ms to initialize this plugin!");
    }

    private void init() {
        //Commands
        getProxy().getPluginManager().registerCommand(this, new Friend());
        getProxy().getPluginManager().registerCommand(this, new Party());
        getProxy().getPluginManager().registerCommand(this, new PartyChat());

        //Listener
        getProxy().getPluginManager().registerListener(this,new JoinLeaveListener());
        getProxy().getPluginManager().registerListener(this,new ServerSwitchListener());
    }

    public File getConfigFile() {
        return new File(getDataFolder() + "/config.yml");
    }

    @NotNull
    public Configuration getConfig() {
        try {
            ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            return provider.load(getConfigFile());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public void saveConfig(Configuration config) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, getConfigFile());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}