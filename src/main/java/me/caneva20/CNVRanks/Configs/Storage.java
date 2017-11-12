package me.caneva20.CNVRanks.Configs;

import me.caneva20.CNVCore.MySQL;
import me.caneva20.CNVRanks.CNVRanks;
import me.caneva20.CNVRanks.RankManager;
import me.caneva20.CNVRanks.RankPlayer;
import me.caneva20.CNVRanks.SQLS;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings({"UnusedReturnValue", "unused", "EmptyMethod"})
public class Storage {
    private static CNVRanks plugin;
//    private static CNVConfig storage;

    //Db
    private static MySQL mySQL;

    public static void setup (CNVRanks plugin) {
        Storage.plugin = plugin;
//        storage = new CNVConfig(plugin, "Storage", "Storage");

        String host = Configuration.getMySqlHost();
        String db = Configuration.getMySqlDb();
        String user = Configuration.getMySqlUser();
        String password = Configuration.getMySqlPassword();
        int port = Configuration.getMySqlPort();

        mySQL = new MySQL(host, db, user, password, port);

        if (!mySQL.isConnected()) {
            CNVRanks.getMainLogger().warnConsole("SQL Connection failed, see the log for more information");
        } else {
            try {
                mySQL.update(SQLS.createTableCnvRanks);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reload () {

    }

    public static void disable () {}

    public static RankPlayer loadPlayer(Player player) {
        try {
            ResultSet query = mySQL.query("SELECT * FROM cnvranks WHERE player='" + player.getName() + "'");

            if (query.next()) {
                String rank = query.getString("rank");
                double exp = query.getDouble("exp");
                return new RankPlayer(player, RankManager.getRank(rank), exp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean addPlayer (RankPlayer rankPlayer) {
        try {
            if (mySQL.query("SELECT * FROM `cnvranks` WHERE `player`='" + rankPlayer.getPlayer().getName() + "'").next()) {
                savePlayer(rankPlayer);
            } else {
                mySQL.update("INSERT INTO cnvranks(player, rank, exp) VALUES ('" + rankPlayer.getPlayer().getName() + "', '" + rankPlayer.getRank().getName() + "',0)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean savePlayer (RankPlayer rankPlayer) {
        try {
            mySQL.update("UPDATE cnvranks SET rank='" + rankPlayer.getRank().getName() + "',exp='" + rankPlayer.getCurrentExp() + "' WHERE player='" + rankPlayer.getPlayer().getName() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
