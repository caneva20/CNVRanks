package me.caneva20.CNVRanks;

import me.caneva20.CNVCore.CNVConfig;
import me.caneva20.CNVCore.CNVLogger;
import me.caneva20.CNVCore.Vault;
import me.caneva20.CNVRanks.Configs.Configuration;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.Configs.Storage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "UnusedReturnValue", "unused"})
public class RankManager {
    private static CNVRanks plugin;
    private static CNVLogger logger;
    private static CNVConfig configuration;
    private static List<Rank> ranks;
    private static List<RankPlayer> players;

    //Async things
    private static Set<Player> playersToLoad;
    private static Set<RankPlayer> playersToSave;
    private static BukkitTask loadTask;
    private static BukkitTask saveTask;

    private RankManager () {}

    public static void setup (CNVRanks plugin) {
        RankManager.plugin = plugin;
        logger = CNVRanks.getMainLogger();

        configuration = new CNVConfig(plugin, "Ranks", "Ranks");
        players = new ArrayList<>();
        playersToLoad = new HashSet<>();
        playersToSave = new HashSet<>();
        loadRanks();
        reloadPlayers();
        routine();
    }

    public static void disable () {
        savePlayers();

        ranks = new ArrayList<>();
        players = new ArrayList<>();
        loadTask.cancel();

        playersToSave = new HashSet<>();
        playersToLoad = new HashSet<>();
    }

    public static void reload () {
        configuration.reloadCustomConfig();
    }

    private static void reloadPlayers () {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            addPlayer(player);
        }
    }

    private static void routine () {
        loadTask = new BukkitRunnable() {
            @Override
            public void run() {
                loadPlayers();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, Configuration.getDbLoadDelay());

        saveTask = new BukkitRunnable() {
            @Override
            public void run() {
                savePlayers();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, Configuration.getDbSaveDelay());
    }

    private static void savePlayers() {
        for (RankPlayer player : new ArrayList<>(playersToSave)) {
            Storage.savePlayer(player);

            playersToSave.remove(player);
        }
    }

    private static void loadRanks () {
        ranks = new ArrayList<>();

        int order = 1;
        for (String rank : configuration.getSection("RANKS")) {
            Rank newRank = new Rank(
                    rank,
                    configuration.getString("RANKS." + rank + ".DISPLAY_NAME"),
                    order++,
                    configuration.getLong("RANKS." + rank + ".PRICE"),
                    configuration.getString("RANKS." + rank + ".TAG"),
                    configuration.getString("RANKS." + rank + ".PERMISSION"),
                    configuration.getBoolean("RANKS." + rank + ".LISTABLE", true),
                    configuration.getStringList("RANKS." + rank + ".PERMISSIONS.PERMANENT"),
                    configuration.getStringList("RANKS." + rank + ".PERMISSIONS.TEMPORARY")
            );
            ranks.add(newRank);
        }
    }

    public static void savePlayer (Player player) {
        RankPlayer playerRank = getPlayerRank(player.getName());

        if (playerRank != null) {
            savePlayer(playerRank);
        }
    }

    private static void loadPlayers() {
        for (Player player : new HashSet<>(playersToLoad)) {
            RankPlayer playerRank = Storage.loadPlayer(player);

            if (playerRank != null) {
                players.add(playerRank);
            } else {
                Rank firstRank = getFirstRank();

                if (firstRank != null) {
                    RankPlayer rankPlayer = new RankPlayer(player, firstRank);
                    Storage.addPlayer(rankPlayer);

                    players.add(rankPlayer);
                }
            }

            playersToLoad.remove(player);
        }
    }

    public static void savePlayer (RankPlayer player) {
        playersToSave.add(player);
    }

    public static void addPlayer (Player player) {
        if (!playersToLoad.contains(player)) {
            playersToLoad.add(player);
        }
    }

    public static void removePlayer (Player player) {
        if (hasPlayer(player)) {
            savePlayer(player);
            players.remove(getPlayerRank(player.getName()));
        } else {
            CNVRanks.getMainLogger().warnConsole("Trying to remove an invalid player");
        }
    }

    public static boolean hasPlayer (Player player) {
        return hasPlayer(player.getName());
    }

    public static boolean hasPlayer (String playerName) {
        for (RankPlayer player : players) {
            if (player.getPlayer().getName().equals(playerName)) {
                return true;
            }
        }

        return false;
    }

    public static Rank getRank (String rankName) {
        for (Rank rank : ranks) {
            if (rank.getName().equals(rankName)) {
                return rank;
            }
        }

        return null;
    }

    public static Rank getRank (int order) {
        for (Rank rank : ranks) {
            if (rank.getOrder() == order) {
                return rank;
            }
        }

        return null;
    }

    public static List<Rank> getRanks () {
        return ranks;
    }

    public static RankPlayer getPlayerRank (String playerName) {
        for (RankPlayer player : players) {
            if (player.getPlayer().getName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

    public static Rank getNextRank (String playerName) {
        RankPlayer playerRank = getPlayerRank(playerName);

        if (playerRank != null) {
            int order = playerRank.getRank().getOrder();

            return getRank(order + 1);
        } else {
            return null;
        }
    }

    public static Rank getFirstRank () {
        if (ranks.size() > 0) {
            return ranks.get(0);
        }

        return null;
    }

    public static Rank getLastRank () {
        if (ranks.size() > 0) {
            return ranks.get(ranks.size() - 1);
        }

        return null;
    }

    public static boolean rankup (Player player) {
        RankPlayer rankPlayer = getPlayerRank(player.getName());

        if (rankPlayer == null) {
            Lang.sendNoRank(player);
            return false;
        }

        Vault vault = CNVRanks.getVault();
        Rank currrentRank = rankPlayer.getRank();
        Rank nextRank = getNextRank(player.getName());

        if (nextRank == null) {
            Lang.sendHighestRank(player);
            return false;
        }

        if (!player.hasPermission(nextRank.getPermission())) {
            Lang.sendNoPermission(player);
            return false;
        }

        if (!buyExp(rankPlayer, rankPlayer.getRemainingExp())) {
            Lang.sendNotEnoughMoney(player, rankPlayer);
            return false;
        }

        rankPlayer.removeExp(currrentRank.getNeededExp());

        for (String permission : currrentRank.getTemporaryPermissions()) {
            vault.removePermission(player, permission);
        }

        for (String permission : nextRank.getPermanentPermissions()) {
            vault.addPermission(player, permission);
        }

        for (String permission : nextRank.getTemporaryPermissions()) {
            vault.addPermission(player, permission);
        }

        rankPlayer.setRank(nextRank);
        playersToSave.add(rankPlayer);
        Lang.sendPlayerRankedup(player, nextRank);

        return true;
    }

    public static boolean forceRankup (Player player) {
        RankPlayer rankPlayer = getPlayerRank(player.getName());

        if (rankPlayer == null) {
            Lang.sendNoRank(player);
            return false;
        }

        Rank playerRank = rankPlayer.getRank();
        Rank nextRank = getNextRank(player.getName());
        Vault vault = CNVRanks.getVault();

        if (nextRank != null) {
            for (String permission : playerRank.getTemporaryPermissions()) {
                vault.removePermission(player, permission);
            }

            for (String permission : nextRank.getPermanentPermissions()) {
                vault.addPermission(player, permission);
            }

            for (String permission : nextRank.getTemporaryPermissions()) {
                vault.addPermission(player, permission);
            }

            rankPlayer.setRank(nextRank);
            playersToSave.add(rankPlayer);
            Lang.sendPlayerRankedup(player, nextRank);
            return true;
        }

        return false;
    }

    public static boolean canRankup (Player player) {
        RankPlayer playerRank = getPlayerRank(player.getName());

        if (playerRank == null) {
            return false;
        }

        Rank rank = playerRank.getRank();

        if (rank == null) {
            return false;
        }

        Vault vault = CNVRanks.getVault();
        return vault.hasEnoughMoney(player, playerRank.getRemainingExp() * getExpPrice(player))
                && player.hasPermission(rank.getPermission());
    }

    public static boolean buyExp (RankPlayer player, double amount) {
        if (player == null) {
            return false;
        }

        double price = amount * getExpPrice(player.getPlayer());

        Vault vault = CNVRanks.getVault();

        if (vault.hasEnoughMoney(player.getPlayer(), price)) {
            vault.withdrawPlayer(player.getPlayer(), price);
            player.addExp(amount);
        } else {
            return false;
        }

        return true;
    }

    public static double getExpPrice (Player player) {
        int lastPriority = 0;
        double lastPrice = 0;
        for (String price : configuration.getSection("RANK_EXP_PRICE")) {
            double expPrice = configuration.getDouble("RANK_EXP_PRICE." + price + ".PRICE");
            String perm = configuration.getString("RANK_EXP_PRICE." + price + ".PERMISSION");
            int priority = configuration.getInt("RANK_EXP_PRICE." + price + ".PRIORITY");

            if (player.hasPermission(perm) && priority >= lastPriority) {
                lastPrice = expPrice;
                lastPriority = priority;
            }
        }

        return lastPrice;
    }
}

















