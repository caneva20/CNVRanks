package me.caneva20.CNVRanks.Commands;

import me.caneva20.CNVCore.Command.CommandBase;
import me.caneva20.CNVCore.Command.ICommand;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.RankManager;
import me.caneva20.CNVRanks.RankPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class RankCommand extends CommandBase implements ICommand {
    private String command = "";
    private String permission = "cnv.ranks.command.rank";
    private String usage = "/rank";
    private List<String> alias = new ArrayList<>();
    private boolean onlyPlayers = true;
    private String description = "Shows info about your current rank";

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
    public void onCommand(CommandSender sender, String[] args, JavaPlugin javaPlugin) {
        if (args.length != 0) {
            sendUsage(sender, getUsage(), true);
            return;
        }

        Player player = ((Player) sender);

        RankPlayer playerRank = RankManager.getPlayerRank(player.getName());

        if (playerRank == null) {
            return;
        }

        Lang.sendRankInfo(sender, playerRank);
    }
}
























