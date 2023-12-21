package jgor.heartstealplugin.CraftingSpecialItemsGUI;

import jgor.heartstealplugin.HeartItem;
import jgor.heartstealplugin.HeartStealPlugin;
import jgor.heartstealplugin.RedemptionItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nullable;

public class CraftingSpecialItemsGUI implements CommandExecutor {

    @Override
    public boolean onCommand(@Nullable CommandSender commandSender, @Nullable Command command, @Nullable String s, @Nullable String[] strings) {

        if(!(commandSender instanceof Player)) return true;

        Player player = ((Player) commandSender).getPlayer();

        mainCraftingGui(player);

        return true;
    }

    public static void mainCraftingGui(Player player) {
        Inventory inventory = Bukkit.createInventory(player,9, ChatColor.GOLD + "Sprawdź craftingi !");

        inventory.setItem(3, HeartItem.createHeartItem());
        inventory.setItem(5, RedemptionItem.createRedemptionItem());

        player.openInventory(inventory);
        player.setMetadata("OpenedMenu", new FixedMetadataValue(HeartStealPlugin.getInstance(),inventory));
    }
}
