package me.caneva20.CNVRanks.Commands;

import me.caneva20.CNVCore.Command.CommandBase;
import me.caneva20.CNVCore.Command.ICommand;
import me.caneva20.CNVRanks.Configs.Configuration;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.Configs.Storage;
import me.caneva20.CNVRanks.RankManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class ReloadCommand extends CommandBase implements ICommand {
    private String command = "reload";
    private String permission = "cnv.ranks.command.reload";
    private String usage = "/rankup reload";
    private List<String> alias = new ArrayList<>();
    private boolean onlyPlayers = false;
    private String description = "Reloads the plugin";

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public boolean getOnlyPlayers() {
        return onlyPlayers;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public List<String> getAlias() {
        return alias;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args, JavaPlugin plugin) {
        RankManager.reload();
        Lang.reload();
        Configuration.reload();
        Storage.reload();

        Lang.sendPluginReloaded(sender);
    }
}
