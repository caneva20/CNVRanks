package me.caneva20.CNVRanks.Configs;


import me.caneva20.CNVCore.CNVConfig;
import me.caneva20.CNVRanks.CNVRanks;

@SuppressWarnings({"unused", "EmptyMethod"})
public class Configuration {
    private static CNVRanks plugin;
    private static CNVConfig config;

    public static void setup (CNVRanks plugin) {
        Configuration.plugin = plugin;
        config = new CNVConfig(plugin, "Config", "Config");
        CNVRanks.getVault().registerPermission(getHideTagPermission());
    }

    public static void reload () {
        config.reload();
    }

    public static void disable () {}

    public static String getMySqlHost () {
        return config.getString("MYSQL.HOST");
    }

    public static String getMySqlDb () {
        return config.getString("MYSQL.DB");
    }

    public static String getMySqlUser () {
        return config.getString("MYSQL.USER");
    }

    public static String getMySqlPassword () {
        return config.getString("MYSQL.PASSWORD");
    }

    public static int getMySqlPort () {
        return config.getInt("MYSQL.PORT");
    }

    public static long getDbLoadDelay() {
        return config.getLong("DB_LOAD_DELAY");
    }

    public static long getDbSaveDelay() {
        return config.getLong("DB_SAVE_DELAY");
    }

    public static boolean hideTag () {
        return config.getBoolean("HIDE_CHAT_TAG.ENABLED");
    }

    public static String getHideTagPermission() {
        return config.getString("HIDE_CHAT_TAG.PERMISSION");
    }
}


























