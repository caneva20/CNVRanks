package me.caneva20.CNVRanks.Commands;

import me.caneva20.CNVCore.Command.CommandBase;
import me.caneva20.CNVCore.Command.ICommand;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.RankManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class RankupCommand extends CommandBase implements ICommand{
    private String command = " [par]";
    private String permission = "cnv.ranks.command.rankup";
    private String usage = "/rankup";
    private List<String> alias = new ArrayList<>();
    private boolean onlyPlayers = false;
    private String description = "Ranks you up";

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
        if (args.length == 1 && sender.hasPermission("cnv.ranks.rankup.others")) {
            Player other = javaPlugin.getServer().getPlayerExact(args[0]);

            if (other != null) {
                if (RankManager.forceRankup(other)) {
                    Lang.sendRankupOtherSuccess(sender, other.getName());
                } else {
                    Lang.sendRankupOtherPlayerCantRankup(sender, other.getName());
                }
                return;
            } else {
                Lang.sendInvalidPlayer(sender, args[0]);
                return;
            }
        }

        if (args.length != 0) {
            sendUsage(sender, getUsage(), true);
            return;
        }

        if (!(sender instanceof Player)) {
            Lang.sendCantRankup(sender);
            return;
        }

        Player player = (Player) sender;

        RankManager.rankup(player);
    }
}
