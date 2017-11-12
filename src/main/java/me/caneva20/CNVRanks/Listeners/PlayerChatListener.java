package me.caneva20.CNVRanks.Listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.caneva20.CNVRanks.Configs.Configuration;
import me.caneva20.CNVRanks.Rank;
import me.caneva20.CNVRanks.RankManager;
import me.caneva20.CNVRanks.RankPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerChat (ChatMessageEvent e) {
        if (Configuration.hideTag() && e.getSender().hasPermission(Configuration.getHideTagPermission())) {
            return;
        }

        if (e.getTags().contains("cnvrank")) {
            RankPlayer playerRank = RankManager.getPlayerRank(e.getSender().getName());

            if (playerRank != null) {
                Rank rank = playerRank.getRank();
                if (rank != null) {
                    e.setTagValue("cnvrank", rank.getTag());
                }
            }
        }
    }
}
