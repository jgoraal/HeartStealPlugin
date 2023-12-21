package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.UUID;

public class FinalConfirmGUI {

    public static final String Title = ChatColor.BOLD + "" + ChatColor.GRAY + "["
            + ChatColor.RESET + ChatColor.BOLD + ChatColor.GOLD + "Rynek"
            + ChatColor.RESET + ChatColor.BOLD + ChatColor.GRAY + "]"
            + ChatColor.RESET + ChatColor.BOLD + ChatColor.GOLD + " Potwierdzenie wystawienia";


    public static void openFinalConfirmGui(Player player) {
        Inventory finalConfirmGui = Bukkit.createInventory(player, 9 * 3, Title);

        ItemStack finalConfirmButton = new ItemStack(Material.LIME_SHULKER_BOX);
        ItemMeta confirmMeta = finalConfirmButton.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "✔ " + ChatColor.RESET + ChatColor.GRAY + "Jestem pewien " + ChatColor.RESET + ChatColor.GREEN + "Potwierdzam ✔");
        finalConfirmButton.setItemMeta(confirmMeta);

        finalConfirmGui.setItem(14, finalConfirmButton);

        ItemStack finalExitButton = new ItemStack(Material.RED_SHULKER_BOX);
        ItemMeta exitMeta = finalConfirmButton.getItemMeta();
        exitMeta.setDisplayName(ChatColor.RED + "✘ " + ChatColor.RESET + ChatColor.GRAY + "Jescze się zastanawiam, " + ChatColor.RESET + ChatColor.RED + "Anuluj ✘");
        finalExitButton.setItemMeta(exitMeta);

        finalConfirmGui.setItem(12, finalExitButton);

        player.openInventory(finalConfirmGui);
        player.setMetadata(AuctionListeners.MARKET_SELL_FINAL_CONFIRM, new FixedMetadataValue(HeartStealPlugin.getInstance(), finalConfirmGui));
    }


    public static void removeExactAmountOfPutedItem(Player player) {
        UUID playerUUID = player.getUniqueId();

        ArrayList<ItemStack> sellingItems = AuctionCommand.getPlayerSellingItems().get(playerUUID);
        ArrayList<Integer> wishedAmounts = AuctionCommand.getPlayerWishedAmountToRemove().get(playerUUID);



        int lastIndex = sellingItems.size() - 1;
        ItemStack itemToRemove = sellingItems.get(lastIndex);
        int amountToRemove = wishedAmounts.get(lastIndex);

        PlayerInventory inventory = player.getInventory();

        // Spróbuj najpierw usunąć z off-handa
        if (inventory.getItemInOffHand().isSimilar(itemToRemove)) {
            ItemStack offHandItem = inventory.getItemInOffHand();
            if (offHandItem.getAmount() <= amountToRemove) {
                inventory.setItemInOffHand(new ItemStack(Material.AIR));
                amountToRemove -= offHandItem.getAmount();
            } else {
                offHandItem.setAmount(offHandItem.getAmount() - amountToRemove);
                amountToRemove = 0;
            }
        }

        // Następnie usuń z głównego ekwipunku
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.isSimilar(itemToRemove) && amountToRemove > 0) {
                if (itemStack.getAmount() <= amountToRemove) {
                    inventory.removeItem(itemStack);
                    amountToRemove -= itemStack.getAmount();
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amountToRemove);
                    amountToRemove = 0;
                }
            }
        }

    }

}
