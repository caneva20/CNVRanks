package me.caneva20.CNVRanks;

import org.bukkit.entity.Player;

@SuppressWarnings({"CanBeFinal", "unused"})
public class RankPlayer {
    private Player player;
    private double currentExp;
    private Rank rank;

    public RankPlayer(Player player, Rank rank) {
        this.player = player;
        this.rank = rank;
    }

    public RankPlayer(Player player, Rank rank, double currentExp) {
        this.player = player;
        this.rank = rank;
        this.currentExp = currentExp;
    }

    public void rankup () {
        RankManager.rankup(player);
    }

    public boolean canLevelUp () {
        Rank lastRank = RankManager.getLastRank();

        return currentExp >= rank.getNeededExp() && lastRank != null && rank.getOrder() < lastRank.getOrder();
    }

    public double getRemainingExp() {
        return Math.max(rank.getNeededExp() - currentExp, 0);
    }

    public void addExp (double amount) {
        if (amount != 0) {
            currentExp += amount;
            RankManager.savePlayer(this);
        }
    }

    public void removeExp (double amount) {
        currentExp -= amount;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public double getCurrentExp() {
        return currentExp;
    }
}
