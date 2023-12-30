package jgor.heartstealplugin.toggle.teleport.end;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;


public class ToggleEndTeleport implements Listener {

    LocalDateTime blockedEndDate = LocalDateTime.of(2024, Month.JANUARY,1,18,0);


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock.getType().equals(Material.END_PORTAL_FRAME) && event.getItem().isSimilar(new ItemStack(Material.ENDER_EYE))) {
                Player player = event.getPlayer();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm ");
                if(LocalDateTime.now().isBefore(blockedEndDate)) {
                    event.setCancelled(true);
                    String formattedDateTime = blockedEndDate.format(formatter);
                    player.sendTitle(ChatColor.RED + "Dostęp do endu jest zablokowany!",  ChatColor.GRAY + "do " + formattedDateTime,10,35,20);
                }
            }
        }
    }

}
