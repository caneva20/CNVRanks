package me.caneva20.CNVRanks.Commands;

import me.caneva20.CNVCore.Command.CommandBase;
import me.caneva20.CNVCore.Command.ICommand;
import me.caneva20.CNVRanks.CNVRanks;
import me.caneva20.CNVRanks.Configs.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class GiveExpBottleCommand extends CommandBase implements ICommand {
    private String command = "givexpbottle";
    private String permission = "cnv.ranks.command.givexpbottle";
    private String usage = "/rankup givexpbottle <player> <amount>";
    private List<String> alias = new ArrayList<>();
    private boolean onlyPlayers = true;
    private String description = "Gives and EXP bottle to a player";

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
        if (args.length != 2) {
            sendUsage(sender, getUsage(), true);
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            Lang.sendInvalidNumber(sender, args[1]);
            sendUsage(sender, getUsage(), true);
            return;
        }

        Player player = javaPlugin.getServer().getPlayerExact(args[0]);

        if (player == null) {
            Lang.sendInvalidPlayer(sender, args[0]);
            return;
        }

        Lang.sendExpBottleGiven(sender, player.getName(), amount);
        Lang.sendExpBottleReceived(player, sender.getName(), amount);
        CNVRanks.getExpManager().giveExpBottle(player, amount);
    }
}




















