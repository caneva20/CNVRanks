package me.caneva20.CNVRanks.Commands;

import me.caneva20.CNVCore.Command.CommandBase;
import me.caneva20.CNVCore.Command.ICommand;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.Rank;
import me.caneva20.CNVRanks.RankManager;
import me.caneva20.CNVRanks.RankPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class RanksCommand extends CommandBase implements ICommand {
    private String command = "";
    private String permission = "cnv.ranks.command.ranks";
    private String usage = "/ranks";
    private List<String> alias = new ArrayList<>();
    private boolean onlyPlayers = false;
    private String description = "List all ranks";

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
        Lang.sendRanksListingHeader(sender);

        RankPlayer actualRank = null;
        if (sender instanceof Player) {
            actualRank = RankManager.getPlayerRank(sender.getName());
        }

        int i = 1;
        for (Rank rank : RankManager.getRanks()) {
            if (rank.isListable()) {
                if (actualRank != null) {
                    boolean purchased = rank.getOrder() <= actualRank.getRank().getOrder();
                    boolean isActual = actualRank.getRank().getOrder() == rank.getOrder();

                    Lang.sendRanksListingBody(sender, rank, i++, purchased, isActual);
                } else {
                    Lang.sendRanksListingBody(sender, rank, i++, false, false);
                }
            }
        }

        Lang.sendRanksListingFooter(sender);
    }
}
