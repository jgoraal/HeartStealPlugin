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
//public class MarketConfirmGui {
//    public static void marketConfirmGui(Player player, ItemStack itemToSell, int slotClicked) {
//
//        Inventory marketAddInventory = Bukkit.createInventory(player, 9 * 3, ChatColor.BOLD + "" + ChatColor.GOLD + "[ Rynek ] Dodaj item");
//
//
//        itemToSell.setAmount(1);
//        marketAddInventory.setItem(13,itemToSell);
//
//        createAmountDecreaseSlots(marketAddInventory);
//        createAmountIncreaseSlots(marketAddInventory);
//
//        marketAddInventory.setItem(3, ConfirmButtons());
//        marketAddInventory.setItem(4, ConfirmButtons());
//        marketAddInventory.setItem(5, ConfirmButtons());
//        marketAddInventory.setItem(12, ConfirmButtons());
//        marketAddInventory.setItem(14, ConfirmButtons());
//        marketAddInventory.setItem(21, ConfirmButtons());
//        marketAddInventory.setItem(22, ConfirmButtons());
//        marketAddInventory.setItem(23, ConfirmButtons());
//
//
//        player.openInventory(marketAddInventory);
//        player.setMetadata(AuctionListeners.MARKET_ADD_CONFIRM, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketAddInventory));
//    }
//
//    private static void createAmountIncreaseSlots(Inventory inventory) {
//        inventory.setItem(6, itemToConfirmAddMarket("+ 1",false));
//        inventory.setItem(7, itemToConfirmAddMarket("+ 2",false));
//        inventory.setItem(8, itemToConfirmAddMarket("+ 3",false));
//        inventory.setItem(15, itemToConfirmAddMarket("+ 8",false));
//        inventory.setItem(16, itemToConfirmAddMarket("+ 16",false));
//        inventory.setItem(17, itemToConfirmAddMarket("+ 32",false));
//        inventory.setItem(24, itemToConfirmAddMarket("+ 64",false));
//        inventory.setItem(25, itemToConfirmAddMarket("+ 2 * 64",false));
//        inventory.setItem(26, itemToConfirmAddMarket("+ 3 * 64",false));
//    }
//
//    private static ItemStack ConfirmButtons() {
//        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
//        ItemMeta confirmMeta = confirm.getItemMeta();
//
//        confirmMeta.setDisplayName(ChatColor.GREEN + "Potwierdź ilość");
//        confirmMeta.addEnchant(Enchantment.DURABILITY,3,true);
//        confirmMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//
//        confirm.setItemMeta(confirmMeta);
//
//        return confirm;
//    }
//
//    private static void createAmountDecreaseSlots(Inventory inventory) {
//
//        inventory.setItem(0, itemToConfirmAddMarket("- 1",true));
//        inventory.setItem(1, itemToConfirmAddMarket("- 2",true));
//        inventory.setItem(2, itemToConfirmAddMarket("- 3",true));
//        inventory.setItem(9, itemToConfirmAddMarket("- 8",true));
//        inventory.setItem(10, itemToConfirmAddMarket("- 16",true));
//        inventory.setItem(11, itemToConfirmAddMarket("- 32",true));
//        inventory.setItem(18, itemToConfirmAddMarket("- 64",true));
//        inventory.setItem(19, itemToConfirmAddMarket("- 2 * 64",true));
//        inventory.setItem(20, itemToConfirmAddMarket("- 3 * 64",true));
//
//    }
//
//    private static ItemStack itemToConfirmAddMarket(String amount, boolean flag) {
//        ItemStack decrease;
//
//        if (flag) {
//            decrease = new ItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE));
//            ItemMeta decreaseMeta = decrease.getItemMeta();
//            decreaseMeta.setDisplayName(ChatColor.RED + amount);
//
//            decrease.setItemMeta(decreaseMeta);
//        } else {
//            decrease = new ItemStack(new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
//            ItemMeta decreaseMeta = decrease.getItemMeta();
//            decreaseMeta.setDisplayName(ChatColor.RED + amount);
//
//            decrease.setItemMeta(decreaseMeta);
//        }
//
//
//        return decrease;
//    }
//
//
//}
