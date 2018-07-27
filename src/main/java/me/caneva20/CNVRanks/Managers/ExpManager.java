package me.caneva20.CNVRanks.Managers;

import me.caneva20.CNVCore.CNVConfig;
import me.caneva20.CNVCore.CNVLogger;
import me.caneva20.CNVCore.MessageLevel;
import me.caneva20.CNVCore.Util.Util;
import me.caneva20.CNVRanks.CNVRanks;
import me.caneva20.CNVRanks.Configs.Lang;
import me.caneva20.CNVRanks.Enums.FightingExpType;
import me.caneva20.CNVRanks.RankManager;
import me.caneva20.CNVRanks.RankPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal", "unused", "FieldCanBeLocal"})
public class ExpManager {
    private CNVRanks plugin;
    private CNVConfig config;
    private CNVLogger logger;
    private Map<String, Double> miningExps = new HashMap<>();
    private Map<FightingExpType, Double> fightingExps = new HashMap<>();

    public ExpManager(CNVRanks plugin) {
        this.plugin = plugin;
        logger = CNVRanks.getMainLogger();
        config = new CNVConfig(plugin, "Ranks", "Ranks");
        loadExps();
    }

    private void loadExps () {
        for (String section : config.getSection("RANK_EXP_EARNING.MINING")) {
            double exp = config.getDouble("RANK_EXP_EARNING.MINING." + section + ".EXP");
            for (String id : config.getStringList("RANK_EXP_EARNING.MINING." + section + ".IDS")) {
                if (id.matches("^\\d+(:\\d+)?$")) {
                    if (id.matches("\\d+")) {
                        id += ":0";
                    }

                    miningExps.put(id, exp);
                }
            }
        }

        //Safety measure, we add possible values with 0, so, no exp, then we load everyone
        //If for some reason some of them is loaded from file, it will have a default value
        //Doing that we don't have to care about Null Pointers
        for (FightingExpType expType : FightingExpType.values()) {
            fightingExps.put(expType, 0D);
        }

        double playerPerDamage = config.getDouble("RANK_EXP_EARNING.FIGHTING.PLAYERS.PER_DAMAGE_DEALT");
        double playerPerKill = config.getDouble("RANK_EXP_EARNING.FIGHTING.PLAYERS.PER_KILL");
        double passivePerDamage = config.getDouble("RANK_EXP_EARNING.FIGHTING.PASSIVE_MOBS.PER_DAMAGE_DEALT");
        double passivePerKill = config.getDouble("RANK_EXP_EARNING.FIGHTING.PASSIVE_MOBS.PER_KILL");
        double aggressivePerDamage = config.getDouble("RANK_EXP_EARNING.FIGHTING.AGGRESSIVE_MOBS.PER_DAMAGE_DEALT");
        double aggressivePerKill = config.getDouble("RANK_EXP_EARNING.FIGHTING.AGGRESSIVE_MOBS.PER_KILL");

        fightingExps.put(FightingExpType.PLAYERS_PER_DAMAGE_DEALT, playerPerDamage);
        fightingExps.put(FightingExpType.PLAYERS_PER_KILL, playerPerKill);
        fightingExps.put(FightingExpType.PASSIVE_MOBS_PER_DAMAGE_DEALT, passivePerDamage);
        fightingExps.put(FightingExpType.PASSIVE_MOBS_PER_KILL, passivePerKill);
        fightingExps.put(FightingExpType.AGGRESSIVE_MOBS_PER_DAMAGE_DEALT, aggressivePerDamage);
        fightingExps.put(FightingExpType.AGGRESSIVE_MOBS_PER_KILL, aggressivePerKill);
    }

    public double getMiningExp (Block block) {
        String id = block.getTypeId() + ":" + block.getData();

        if (miningExps.containsKey(id)) {
            return miningExps.get(id);
        }

        return 0;
    }

    public double getFightingExp (FightingExpType type) {
        return fightingExps.get(type);
    }

    public void giveExp (Player player, double amount) {
        RankPlayer playerRank = RankManager.getPlayerRank(player.getName());

        if (playerRank != null) {
            CNVRanks.getMainLogger().info(player, "You earned <par>" + amount + "</par> XP");
            playerRank.addExp(amount);

            //Safety measure
            int loops = 0;

            while (playerRank.canLevelUp() && loops++ < 100) {
                RankManager.rankup(player);
            }
        }
    }

    public void giveExpBottle (Player player, double amount) {
        ItemStack bottle = new ItemStack(Material.BOOK, 1);

        bottle.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
        ItemMeta meta = bottle.getItemMeta();

        meta.setDisplayName(Lang.getExpBottleName());

        List<String> lore = new ArrayList<>();

        lore.addAll(Lang.getExpBottleDescription(amount));
        lore.add(logger.format("&0&mRANKExpBottle", MessageLevel.ERROR));
        lore.add(logger.format("&0&mEXPValue:" + String.format("%.00f", amount), MessageLevel.ERROR));

        meta.setLore(lore);

        bottle.setItemMeta(meta);
        player.getInventory().addItem(bottle);
    }

    public void useExpBottle (Player player, ItemStack bottle) {
        ItemMeta meta = bottle.getItemMeta();

        double value = 0;
        for (String s : meta.getLore()) {
            if (Util.removeColor(s).matches("^EXPValue:\\d+(?:\\.\\d+)?$")) {
                try {
                    value = Double.parseDouble(Util.removeColor(s).replace("EXPValue:", ""));
                } catch (Exception ignored) {}

                break;
            }
        }

        boolean sneaking = player.isSneaking();
        int amount = bottle.getAmount();

        if (amount == 1 || sneaking) {
            player.getInventory().remove(bottle);
        } else {
            bottle.setAmount(amount - 1);
        }

        giveExp(player, sneaking ? value * amount : value);
    }

    public boolean isExpBottle (ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        boolean validBottle = false;
        for (String s : meta.getLore()) {
            if (Util.removeColor(s).matches("^RANKExpBottle$")) {
                validBottle = true;
                break;
            }
        }

        return validBottle;
    }
}



















