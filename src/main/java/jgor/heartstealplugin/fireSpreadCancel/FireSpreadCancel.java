package jgor.heartstealplugin.fireSpreadCancel;

import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Fire;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class FireSpreadCancel implements Listener {

    @EventHandler
    public void onFireSpread(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            Bukkit.getLogger().info("pali ale cos nie moze");
            event.setCancelled(true);
        }

        if (event.getCause() == BlockIgniteEvent.IgniteCause.LAVA) {
            Bukkit.getLogger().info("lawa cos probuje ale cos nie moze");
            event.setCancelled(true);
        }

    }


    @EventHandler
    public void onBlockBurn(BlockSpreadEvent event) {

        if (event.getBlock() instanceof Fire) event.setCancelled(true);

        if (event.getSource() instanceof Fire) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }
}
