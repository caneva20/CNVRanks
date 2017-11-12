package me.caneva20.CNVRanks.Listeners;

import me.caneva20.CNVCore.Util.Metadata;
import me.caneva20.CNVRanks.CNVRanks;
import me.caneva20.CNVRanks.Enums.FightingExpType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

@SuppressWarnings("CanBeFinal")
public class PlayerExpListener implements Listener {
    private CNVRanks plugin;

    public PlayerExpListener(CNVRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlaceBlock (BlockPlaceEvent e) {
        Block block = e.getBlock();

        block.setMetadata("Owner", new FixedMetadataValue(plugin, e.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntitySpawn (CreatureSpawnEvent e) {
        CreatureSpawnEvent.SpawnReason spawnReason = e.getSpawnReason();

        if (spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER
                || spawnReason == CreatureSpawnEvent.SpawnReason.EGG
                || spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            Metadata.addMetadata(plugin, e.getEntity(), "CNVNotNaturalSpawn", "");
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBreakBlock (BlockBreakEvent e) {
        if (Metadata.hasMetadata(e.getBlock(), "Owner")) {
            return;
        }

        double miningExp = CNVRanks.getExpManager().getMiningExp(e.getBlock());
        CNVRanks.getExpManager().giveExp(e.getPlayer(), miningExp);
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityDie (EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("CNVNotNaturalSpawn")) {
            return;
        }

        Player killer = e.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        if (e.getEntity() instanceof Player) { //Players
            CNVRanks.getExpManager().giveExp(killer, CNVRanks.getExpManager().getFightingExp(FightingExpType.PLAYERS_PER_KILL));
        } else if (e.getEntity() instanceof Slime || e.getEntity() instanceof Monster){ //Aggressive mobs
            CNVRanks.getExpManager().giveExp(killer, CNVRanks.getExpManager().getFightingExp(FightingExpType.AGGRESSIVE_MOBS_PER_KILL));
        } else { //Passive mobs
            CNVRanks.getExpManager().giveExp(killer, CNVRanks.getExpManager().getFightingExp(FightingExpType.PASSIVE_MOBS_PER_KILL));
        }
    }

    @EventHandler()
    private void useExpBottle (PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand().getType() != Material.BOOK) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        CNVRanks.getExpManager().useExpBottle(e.getPlayer(), e.getPlayer().getItemInHand());
    }
    //DISABLED FEATURE, Maybe for the future
    // It will be too vulnerable for afk farming
//    @EventHandler(ignoreCancelled = true)
//    private void onEntityDamage (EntityDamageByEntityEvent e) {
//        if (e.getEntity().hasMetadata("CNVNotNaturalSpawn")) {
//            return;
//        }
//
//        if (!(e.getEntity() instanceof LivingEntity)) {
//            return;
//        }
//
//        if (e.getDamage() <= 0) {
//            return;
//        }
//
//        Entity damager = e.getDamager();
//
//        //Was a player the damage cause?
//        if (!(damager instanceof Player)) {
//            return;
//        }
//
//        Player player = (Player) damager;
//        int damage = Math.min(e.getDamage(), ((LivingEntity) e.getEntity()).getMaxHealth());
//
//        if (e.getEntity() instanceof Player) { //Players
//            Player damaged = ((Player) e.getEntity());
//            int hits = 0;
//
//            if (damaged.hasMetadata("CNVHitBy:" + player.getName())) {
//                hits = Metadata.getMetadata(damaged, "CNVHitBy:" + player.getName());
//            }
//
//            hits++;
//            Metadata.addMetadata(plugin, damaged, "CNVHitBy:" + player.getName(), hits);
//
//            if (hits > 20) {
//                return;
//            }
//
//            RankManager.giveExp(player, CNVRanks.getExpManager().getFightingExp(FightingExpType.PLAYERS_PER_DAMAGE_DEALT) * damage);
//        } else if (e.getEntity() instanceof Slime || e.getEntity() instanceof Monster){ //Aggressive mobs
//            RankManager.giveExp(player, CNVRanks.getExpManager().getFightingExp(FightingExpType.AGGRESSIVE_MOBS_PER_DAMAGE_DEALT) * damage);
//        } else { //Passive mobs
//            RankManager.giveExp(player, CNVRanks.getExpManager().getFightingExp(FightingExpType.PASSIVE_MOBS_PER_DAMAGE_DEALT) * damage);
//        }
//    }
}


























