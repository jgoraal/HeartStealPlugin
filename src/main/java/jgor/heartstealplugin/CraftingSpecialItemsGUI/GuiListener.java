package jgor.heartstealplugin.CraftingSpecialItemsGUI;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;


import java.util.HashSet;

public class GuiListener implements Listener {

    private static final HashSet<Integer> borderSlots = new HashSet<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();


        if (player.hasMetadata("OpenedMenu")) {
            event.setCancelled(true);
            if (event.getSlot() == 3) {
                player.closeInventory();
                createCraftingRecipe(player, "Heart");
            } else if (event.getSlot() == 5) {
                player.closeInventory();
                createCraftingRecipe(player, "Redemption");
            }
        } else if (player.hasMetadata("CraftingMenu")) {
            event.setCancelled(true);

            if(event.getSlot() == 10 || event.getSlot() == 16) {
                player.closeInventory();
                CraftingSpecialItemsGUI.mainCraftingGui(player);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata("OpenedMenu")) {
            player.removeMetadata("OpenedMenu", HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata("CraftingMenu")) {
            player.removeMetadata("CraftingMenu", HeartStealPlugin.getInstance());
        }
    }

    private void createCraftingRecipe(Player player, String craftingName) {

        for (int i = 0; i < 9*3; i++) {
            if((i % 9 == 3) || (i % 9 == 4) || (i % 9 == 5) || (i == 10) || (i == 16)) {
                continue;
            }
            borderSlots.add(i);
        }

        if (craftingName.equals("Heart")) {
            Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColor.GOLD + "Crafting Dodatkowego Serca");
            inventory.setItem(3, new ItemStack(Material.DIAMOND));
            inventory.setItem(4, new ItemStack(Material.GOLDEN_APPLE));
            inventory.setItem(5, new ItemStack(Material.DIAMOND));
            inventory.setItem(12, new ItemStack(Material.GOLDEN_APPLE));
            inventory.setItem(13, new ItemStack(Material.RED_DYE));
            inventory.setItem(14, new ItemStack(Material.GOLDEN_APPLE));
            inventory.setItem(21, new ItemStack(Material.DIAMOND));
            inventory.setItem(22, new ItemStack(Material.GOLDEN_APPLE));
            inventory.setItem(23, new ItemStack(Material.DIAMOND));
            inventory.setItem(10, returnItem());
            inventory.setItem(16, returnItem());

            for(int i : borderSlots) {
                inventory.setItem(i,new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
            }

            player.openInventory(inventory);
            player.setMetadata("CraftingMenu", new FixedMetadataValue(HeartStealPlugin.getInstance(), inventory));
        } else if (craftingName.equals("Redemption")) {
            Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColor.GOLD + "Crafting Itemu Odkupienia");
            inventory.setItem(3, new ItemStack(Material.DIAMOND_BLOCK));
            inventory.setItem(4, new ItemStack(Material.GOLDEN_APPLE));
            inventory.setItem(5, new ItemStack(Material.DIAMOND_BLOCK));
            inventory.setItem(12, new ItemStack(Material.TOTEM_OF_UNDYING));
            inventory.setItem(13, new ItemStack(Material.NETHER_STAR));
            inventory.setItem(14, new ItemStack(Material.TOTEM_OF_UNDYING));
            inventory.setItem(21, new ItemStack(Material.DIAMOND_BLOCK));
            inventory.setItem(22, new ItemStack(Material.NETHERITE_INGOT));
            inventory.setItem(23, new ItemStack(Material.DIAMOND_BLOCK));
            inventory.setItem(10, returnItem());
            inventory.setItem(16, returnItem());

            for(int i : borderSlots) {
                inventory.setItem(i,new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
            }

            player.openInventory(inventory);
            player.setMetadata("CraftingMenu", new FixedMetadataValue(HeartStealPlugin.getInstance(), inventory));
        }
    }


    public ItemStack returnItem() {
        ItemStack returnItem = new ItemStack(Material.BARRIER);

        ItemMeta returnItemMeta = returnItem.getItemMeta();

        returnItemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Powrót");
        returnItemMeta.addEnchant(Enchantment.DURABILITY,3,true);
        returnItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        returnItem.setItemMeta(returnItemMeta);

        return returnItem;
    }
}
