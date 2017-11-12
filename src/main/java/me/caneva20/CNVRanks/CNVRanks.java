package me.caneva20.CNVRanks;

import me.caneva20.CNVCore.CNVLogger;
import me.caneva20.CNVCore.Command.CNVCommands;
import me.caneva20.CNVCore.Vault;
import me.caneva20.CNVRanks.Commands.*;
import me.caneva20.CNVRanks.Configs.Configuration;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.Configs.Storage;
import me.caneva20.CNVRanks.Listeners.PlayerChatListener;
import me.caneva20.CNVRanks.Listeners.PlayerConnectivityListener;
import me.caneva20.CNVRanks.Listeners.PlayerExpListener;
import me.caneva20.CNVRanks.Managers.ExpManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CNVRanks extends JavaPlugin {
    private static Vault vault;
    private static ExpManager expManager;
    private static CNVLogger logger;

    private CNVCommands rankupCommand;
    private CNVCommands ranksCommand;
    private CNVCommands rankCommand;
    private CNVCommands mainCommand;


    @Override
    public void onLoad () {
        logger = new CNVLogger(this, true);
    }

    public void onEnable() {
        logger.setLoggerTag("&f[&5CNV&6Ranks&f] ");
        logger.infoConsole("Initializing setup");

        vault = new Vault(this);

        Lang.setup(this);
        Configuration.setup(this);
        RankManager.setup(this);
        Storage.setup(this);

        expManager = new ExpManager(this);

        logger.setLoggerTag(Lang.getPluginTag());

        setupListeners();
        setupCommands();

        logger.infoConsole("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        Lang.disable();
        RankManager.disable();
        Configuration.disable();
        Storage.disable();
    }

    private void setupListeners () {
        logger.infoConsole("Registering listeners");

        getServer().getPluginManager().registerEvents(new PlayerConnectivityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerExpListener(this), this);

        logger.infoConsole("All listeners registered!");
    }

    private void setupCommands () {
        logger.infoConsole("Registering commands");

        rankupCommand = new CNVCommands(this, logger, "rankup");
        ranksCommand = new CNVCommands(this, logger, "ranks");
        rankCommand = new CNVCommands(this, logger, "rank");
        mainCommand = new CNVCommands(this, logger, "cnvranks");

        rankupCommand.registerCommand(new RankupCommand());
        rankupCommand.registerCommand(new ReloadCommand());
        rankupCommand.registerCommand(new GiveExpCommand());
        rankupCommand.registerCommand(new GiveExpBottleCommand());

        ranksCommand.registerCommand(new RanksCommand());

        rankCommand.registerCommand(new RankCommand());

        mainCommand.registerCommand(new InfoCommand());

        logger.infoConsole("All commands registered!");
    }

    public static Vault getVault() {
        return vault;
    }

    public static ExpManager getExpManager() {
        return expManager;
    }

    public static CNVLogger getMainLogger () {
        return logger;
    }
}