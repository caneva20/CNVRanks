package me.caneva20.CNVRanks.Configs;

import me.caneva20.CNVCore.CNVConfig;
import me.caneva20.CNVCore.CNVLogger;
import me.caneva20.CNVCore.MessageLevel;
import me.caneva20.CNVCore.UnicodeChars;
import me.caneva20.CNVCore.Util.Util;
import me.caneva20.CNVRanks.CNVRanks;
import me.caneva20.CNVRanks.Rank;
import me.caneva20.CNVRanks.RankManager;
import me.caneva20.CNVRanks.RankPlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused", "EmptyMethod"})
public class Lang {
    private static CNVRanks plugin;
    private static CNVLogger logger;
    private static CNVConfig language;

    private Lang () {}

    public static void setup(CNVRanks plugin) {
        Lang.plugin = plugin;
        logger = CNVRanks.getMainLogger();

        language = new CNVConfig(plugin, "Language", "Language");
    }

    public static void disable () {}

    public static void reload () {
        language.reload();
    }

    public static void sendNotEnoughMoney (CommandSender to, RankPlayer rank) {
        String msg = getNoMoney(rank);
        logger.warn(to, msg, true);
    }

    public static void sendNoPermission (CommandSender to) {
        String msg = getNoPermission();
        logger.warn(to, msg, true);
    }

    public static void sendHighestRank (CommandSender to) {
        String msg = getHighest();
        logger.info(to, msg, true);
    }

    public static void sendNoRank (CommandSender to) {
        String msg = getNoRank();
        logger.warn(to, msg, true);
    }

    public static void sendPlayerRankedup (CommandSender to, Rank rank) {
        String msg = getPlayerRankedup(rank);
        logger.info(to, msg, true);
    }

    public static void sendRanksListingHeader (CommandSender to) {
        String msg = getRankListHeader();
        logger.info(to, msg, false);
    }

    public static void sendRanksListingFooter (CommandSender to) {
        String msg = getRankListFooter();
        logger.info(to, msg, false);
    }

    public static void sendRanksListingBody(CommandSender to, Rank rank, int order, boolean purchased, boolean isActual) {
        String msg = getRankListBody(rank, order, purchased, isActual);
        logger.info(to, msg, false);
    }

    public static void sendRankupOtherSuccess (CommandSender to, String playerName) {
        String msg = getRankupOtherSuccess(playerName);
        logger.success(to, msg, true);
    }

    public static void sendInvalidPlayer(CommandSender to, String playerName) {
        String msg = getInvalidPlayer(playerName);
        logger.error(to, msg, true);
    }

    public static void sendRankupOtherPlayerCantRankup (CommandSender to, String playerName) {
        String msg = getRankupOtherPlayerCantRankup(playerName);
        logger.warn(to, msg, true);
    }

    public static void sendRankInfo (CommandSender to, RankPlayer player) {
        String[] rankInfoBody = getRankInfoBody(player);

        for (String s : rankInfoBody) {
            logger.info(to, s, false);
        }
    }

    public static void sendInvalidNumber (CommandSender to, String arg) {
        String msg = getInvalidNumber(arg);
        logger.warn(to, msg, true);
    }

    public static void sendNumberMustBeMoreThan (CommandSender to, String arg, String more) {
        String msg = getNumberMustMoreThan(arg, more);
        logger.warn(to, msg, true);
    }

    public static void sendInvalidNumber (CommandSender to, String arg, String less) {
        String msg = getNumberMustBeLessThan(arg, less);
        logger.warn(to, msg, true);
    }

    public static void sendExpGiven (CommandSender to, String player, double amount) {
        String msg = getExpGiven(player, amount);
        logger.info(to, msg, true);
    }

    public static void sendExpReceived (CommandSender to, String player, double amount) {
        String msg = getExpReceived(player, amount);
        logger.info(to, msg, true);
    }

    public static void sendExpBottleGiven (CommandSender to, String player, double amount) {
        String msg = getExpBottleGiven(player, amount);
        logger.info(to, msg, true);
    }

    public static void sendExpBottleReceived (CommandSender to, String player, double amount) {
        String msg = getExpBottleReceived(player, amount);
        logger.info(to, msg, true);
    }

    public static void sendCantRankup (CommandSender to) {
        logger.error(to, getCantRankup());
    }

    public static void sendPluginReloaded (CommandSender to) {
        logger.info(to, getPluginReloaded());
    }

    // Messages
    public static String getNoMoney(RankPlayer rankPlayer) {
        Rank rank = rankPlayer.getRank();
        double moneyNeeded = RankManager.getExpPrice(rankPlayer.getPlayer()) * rankPlayer.getRemainingExp() - CNVRanks.getVault().getPlayerMoney(rankPlayer.getPlayer());
        double rankPrice = rank.getNeededExp();

        return language.getString("PLAYER_NOT_ENOUGH_MONEY")
                .replace("{{RANK}}", rank.getTag())
                .replace("{{RANK_NAME}}", rank.getDisplayName())
                .replace("{{RANK_PRICE}}", Util.formatNumber(rankPrice))
                .replace("{{MONEY_NEEDED}}", Util.formatNumber(moneyNeeded));
    }

    public static String getNoPermission() {
        return language.getString("YOU_DONT_HAVE_PERMISSION");
    }

    public static String getHighest() {
        return language.getString("HIGHEST_RANK_POSSIBLE");
    }

    public static String getNoRank() {
        return language.getString("NO_RANK");
    }

    public static String getPlayerRankedup(Rank rank) {
        return language.getString("PLAYER_ON_RANKUP")
                .replace("{{RANK}}", rank.getTag())
                .replace("{{RANK_NAME}}", rank.getDisplayName());
    }

    public static String getRankListHeader() {
        return language.getString("RANK_LIST_HEADER");
    }

    public static String getRankListFooter() {
        return language.getString("RANK_LIST_FOOTER");
    }

    public static String getRankListBody(Rank rank, int order, boolean purchased, boolean isActual) {
        return language.getString("RANK_LIST_BODY")
                .replace("{{RANK}}", rank.getTag())
                .replace("{{RANK_NAME}}", rank.getDisplayName())
                .replace("{{RANK_PRICE}}", Util.formatNumber(rank.getNeededExp()))
                .replace("{{RANK_ORDER}}", order + "")
                .replace("{{RANK_STATUS}}", purchased ? getRankStatusPuchased() : getRankStatusNotPuchased())
                .replace("{{IS_ACTUAL_RANK}}", isActual ? getRankIsActual() : "");
    }

    public static String getRankStatusPuchased () {
        return language.getString("RANK_LIST_STATUS_PURCHASED")
                .replace("{{CHECK_MARK}}", UnicodeChars.CHECK_MARK + "")
                .replace("{{CROSS_CHECK_MARK}}", UnicodeChars.CROSS_MARK + "");
    }

    public static String getRankStatusNotPuchased () {
        return language.getString("RANK_LIST_STATUS_NOT_PURCHASED")
                .replace("{{CHECK_MARK}}", UnicodeChars.CHECK_MARK + "")
                .replace("{{CROSS_CHECK_MARK}}", UnicodeChars.CROSS_MARK + "");
    }

    public static String getRankIsActual () {
        return language.getString("RANK_LIST_ACTUAL");
    }

    public static String getPluginReloaded () {
        return language.getString("PLUGIN_RELOADED");
    }

    public static String getPluginTag () {
        return language.getString("PLUGIN_TAG");
    }

    public static String getRankupOtherSuccess (String playerName) {
        return language.getString("RANKUP_OTHER_SUCCESS")
                .replace("{{PLAYER_NAME}}", playerName);
    }

    public static String getInvalidPlayer(String playerName) {
        return language.getString("INVALID_PLAYER")
                .replace("{{PLAYER_NAME}}", playerName);
    }

    public static String getRankupOtherPlayerCantRankup (String playerName) {
        return language.getString("RANKUP_OTHER_PLAYER_CANT_RANKUP")
                .replace("{{PLAYER_NAME}}", playerName);
    }

    public static String getInvalidNumber (String arg) {
        return language.getString("INVALID_NUMBER")
                .replace("{{ARG}}", arg);
    }

    public static String getNumberMustMoreThan (String arg, String more) {
        return language.getString("INVALID_NUMBER")
                .replace("{{ARG}}", arg)
                .replace("{{MORE}}", more);
    }

    public static String getNumberMustBeLessThan (String arg, String less) {
        return language.getString("INVALID_NUMBER")
                .replace("{{ARG}}", arg)
                .replace("{{LESS}}", less);
    }

    public static String getExpGiven (String to, double amount) {
        return language.getString("EXP_GIVEN")
                .replace("{{PLAYER_NAME}}", to)
                .replace("{{EXP}}", Util.formatNumber(amount));
    }

    public static String getExpReceived (String from, double amount) {
        return language.getString("EXP_RECEIVED")
                .replace("{{PLAYER_NAME}}", from)
                .replace("{{EXP}}", Util.formatNumber(amount));
    }

    public static String getExpBottleGiven (String to, double amount) {
        return language.getString("EXP_BOTTLE_GIVEN")
                .replace("{{PLAYER_NAME}}", to)
                .replace("{{EXP}}", Util.formatNumber(amount));
    }

    public static String getExpBottleReceived (String from, double amount) {
        return language.getString("EXP_BOTTLE_RECEIVED")
                .replace("{{PLAYER_NAME}}", from)
                .replace("{{EXP}}", Util.formatNumber(amount));
    }

    public static String getCantRankup () {
        return language.getString("CANT_RANKUP");
    }

    //Rank info
    public static int getRankInfoProgressBarLength () {
        return language.getInt("RANK_INFO_PROGRESS_BAR_LENGTH");
    }

    public static String getRankInfoFillCharActive () {
        return language.getString("RANK_INFO_PROGRESS_BAR_FILL_CHAR_ACTIVE");
    }

    public static String getRankInfoFillCharDeactive () {
        return language.getString("RANK_INFO_PROGRESS_BAR_FILL_CHAR_DEACTIVE");
    }

    public static String[] getRankInfoBody (RankPlayer player) {
        List<String> infoBody = new ArrayList<>();

        int barLength = getRankInfoProgressBarLength();
        String active = getRankInfoFillCharActive();
        String deactive = getRankInfoFillCharDeactive();
        double currentExp = player.getCurrentExp();
        double neededExp = player.getRank().getNeededExp();

        double percent = (currentExp / neededExp * 100);

        String progressBar = creteProgressBar(barLength, percent, active, deactive);
        double moneyNeeded = RankManager.getExpPrice(player.getPlayer()) * player.getRemainingExp() - CNVRanks.getVault().getPlayerMoney(player.getPlayer());

        moneyNeeded = Math.max(0, moneyNeeded);

        for (String bodyLine : language.getStringList("RANK_INFO_BODY")) {
            infoBody.add(bodyLine
                    .replace("{{RANK_NAME}}", player.getRank().getDisplayName())
                    .replace("{{RANK_TAG}}", player.getRank().getTag())
                    .replace("{{PROGRESS_BAR}}", progressBar)
                    .replace("{{PROGRESS_PERCENTAGE}}", ((int) percent) + "")
                    .replace("{{CURRENT_EXP}}", Util.formatNumber(currentExp))
                    .replace("{{NEEDED_EXP}}", Util.formatNumber(neededExp))
                    .replace("{{EXP_REMAINING}}", Util.formatNumber(player.getRemainingExp()))
                    .replace("{{MONEY_NEEDED}}", Util.formatNumber(moneyNeeded))
            );
        }

        return infoBody.toArray(new String[0]);
    }

    public static String creteProgressBar (int length, double fillPercent, String activeFillChar, String deactiveFillChar) {
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ((double) i / length * 100 < fillPercent) {
                bar.append(activeFillChar);
            } else {
                bar.append(deactiveFillChar);
            }
        }

        return bar.toString();
    }

    //EXP Bottle
    public static List<String> getExpBottleDescription (double amount) {
        List<String> lore = new ArrayList<>();

        for (String description : language.getStringList("EXP_BOTTLE_DESCRIPTION")) {
            String replace = description.replace("{{AMOUNT}}", Util.formatNumber(amount));

            lore.add(logger.format("<begin>" + replace, MessageLevel.INFO));
        }

        return lore;
    }

    public static String getExpBottleName () {
        return logger.format(language.getString("EXP_BOTTLE_NAME"), MessageLevel.INFO);
    }
}

























