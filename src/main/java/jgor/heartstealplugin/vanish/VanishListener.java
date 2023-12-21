package jgor.heartstealplugin.vanish;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VanishListener implements Listener {

    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event) {
        if (Vanish.getVanishedPlayers().isEmpty()) return;

        Player player = event.getPlayer();

        if(Vanish.getVanishedPlayers().contains(player.getUniqueId())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if(online.equals(player)) continue;

                online.hidePlayer(HeartStealPlugin.getInstance(),player);
            }

        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if(Vanish.getVanishedPlayers().contains(online.getUniqueId())) {
                    player.hidePlayer(HeartStealPlugin.getInstance(),online);
                }
            }
        }



    }
}
