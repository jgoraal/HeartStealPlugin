//package jgor.heartstealplugin.auctions;
//
//import jgor.heartstealplugin.HeartStealPlugin;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.metadata.FixedMetadataValue;
//
//public class MarketAddGui {
//
//    public static void openMarketAddInventory(Player player) {
//        Inventory marketAddInventory = Bukkit.createInventory(player, 9 * 3, ChatColor.BOLD + "" + ChatColor.GOLD + "Dodaj item do rynku");
//
//        for (int i = 0; i < 27; i++) {
//            if (i % 9 == 3 || i % 9 == 4 || i % 9 == 5) continue;
//            marketAddInventory.setItem(i, createEmptySlot());
//        }
//
//        marketAddInventory.setItem(3, createBorderItemAddMarket());
//        marketAddInventory.setItem(4, createBorderItemAddMarket());
//        marketAddInventory.setItem(5, createBorderItemAddMarket());
//        marketAddInventory.setItem(12, createBorderItemAddMarket());
//        marketAddInventory.setItem(14, createBorderItemAddMarket());
//        marketAddInventory.setItem(21, createBorderItemAddMarket());
//        marketAddInventory.setItem(22, createBorderItemAddMarket());
//        marketAddInventory.setItem(23, createBorderItemAddMarket());
//
//
//        player.openInventory(marketAddInventory);
//        player.setMetadata(AuctionListeners.MARKET_ADD, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketAddInventory));
//    }
//
//    private static ItemStack createBorderItemAddMarket() {
//        ItemStack border = new ItemStack(Material.RED_STAINED_GLASS_PANE);
//
//        ItemMeta borderMeta = border.getItemMeta();
//
//        borderMeta.addEnchant(Enchantment.DURABILITY, 3, true);
//        borderMeta.setDisplayName(ChatColor.RED + "Wstaw item na środek!");
//        borderMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//
//        border.setItemMeta(borderMeta);
//
//        return border;
//    }
//
//    private static ItemStack createEmptySlot() {
//        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
//
//        ItemMeta emptyItemMeta = empty.getItemMeta();
//
//        emptyItemMeta.setDisplayName(" ");
//
//        empty.setItemMeta(emptyItemMeta);
//
//        return empty;
//    }
//
//}
