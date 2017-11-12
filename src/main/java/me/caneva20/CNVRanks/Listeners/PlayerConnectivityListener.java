package me.caneva20.CNVRanks.Listeners;

import me.caneva20.CNVRanks.RankManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectivityListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin (PlayerJoinEvent e) {
        RankManager.addPlayer(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerQuit (PlayerQuitEvent e) {
        RankManager.removePlayer(e.getPlayer());
    }
}
