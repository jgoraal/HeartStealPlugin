package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class SellGui {

    public static final HashSet<String> categoryNames = new HashSet<>();

    public static final String Title = ChatColor.BOLD + "" + ChatColor.GRAY + "["
            + ChatColor.RESET + ChatColor.BOLD + ChatColor.GOLD + "Rynek"
            + ChatColor.RESET + ChatColor.BOLD + ChatColor.GRAY + "]"
            + ChatColor.RESET + ChatColor.BOLD + ChatColor.GOLD + " Sprzedaż";


    public static void openSellGui(Player player) {
        Inventory sellGui = Bukkit.createInventory(player, 9 * 6, Title);

        schematicGuiDesign(sellGui);
        categoryNames.clear();
        setCategoryItems(sellGui);

        createBottomNotPaged(sellGui, false, false);

        if (AuctionListeners.playerPriceItemList.containsKey(player.getUniqueId())) {
            ItemStack playerConfirmSellButton = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta buttonMeta = (SkullMeta) playerConfirmSellButton.getItemMeta();
            buttonMeta.setOwningPlayer(player);


            buttonMeta.setDisplayName(ChatColor.GOLD + "✮ Kliknij mnie, żeby potwierdźić sprzedaż ✮");

            UUID playerUUID = player.getUniqueId();

            ArrayList<ItemStack> sellingItems = AuctionCommand.getPlayerSellingItems().get(playerUUID);
            ArrayList<Integer> wishedAmounts = AuctionCommand.getPlayerWishedAmountToRemove().get(playerUUID);

            int lastIndex = sellingItems.size() - 1;
            ItemStack item = sellingItems.get(lastIndex);
            int amount = wishedAmounts.get(lastIndex);

            buttonMeta.setLore(Arrays.asList(
                    " ",
                    ChatColor.GRAY + "Oferujesz " + ChatColor.RESET + ChatColor.GREEN + getDisplayName(item) + ChatColor.RESET + ChatColor.GRAY + " w ilości " + ChatColor.RESET + ChatColor.GREEN + amountToReadable(amount),
                    " ",
                    ChatColor.GRAY + "❁ Cena ❁"
            ));

            if (AuctionListeners.playerPriceItemList.containsKey(playerUUID)) {
                List<String> lore = buttonMeta.getLore();

                ArrayList<ItemStack> price = AuctionListeners.playerPriceItemList.get(playerUUID);

                for (ItemStack i : price) {
                    lore.add(ChatColor.GREEN + "   ✔ " + ChatColor.RESET + ChatColor.GRAY + "- " + ChatColor.RESET + ChatColor.DARK_PURPLE + getDisplayName(i) + ChatColor.RESET + ChatColor.GRAY + " ilość " + ChatColor.RESET + ChatColor.DARK_PURPLE + amountToReadable(i.getAmount()));
                }

                lore.add(" ");
                int remainSize = 5 - price.size();
                lore.add(ChatColor.GRAY + "Możesz dodać jescze " + ChatColor.RESET + ChatColor.DARK_AQUA + remainSize + ChatColor.RESET + ChatColor.GRAY + " itemy !");
                int remainAmount = AuctionListeners.MAX_ITEM_AMOUNT - AuctionListeners.sumPlayerItemAmount.get(playerUUID);
                lore.add(ChatColor.GRAY + "Nie możesz przekroczyć " + ChatColor.RESET + ChatColor.DARK_AQUA + remainAmount + ChatColor.RESET + ChatColor.GRAY + " ilości itemów !");
                lore.add(" ");
                lore.add(compreserTo64(remainAmount));

                buttonMeta.setLore(lore);
            }

            playerConfirmSellButton.setItemMeta(buttonMeta);
            sellGui.setItem(4, playerConfirmSellButton);

        }

        placeStatusItem(sellGui, player);

        fillEmptySlots(sellGui, fillItem());

        player.openInventory(sellGui);
        player.setMetadata(AuctionListeners.SELL_GUI, new FixedMetadataValue(HeartStealPlugin.getInstance(), sellGui));
    }


    private static String amountToReadable(int amount) {
        String value;

        if (amount <= 64) {
            value = String.valueOf(amount);
        } else if (amount % 64 == 0) {
            int lack = amount / 64;
            value = lack + "x64";
        } else {
            int lack = amount / 64;
            int rest = amount - (lack * 64);

            value = lack + "x64" + ChatColor.RESET + ChatColor.GRAY + " i " + ChatColor.RESET + ChatColor.DARK_PURPLE + rest;
        }

        return value;
    }


    private static String compreserTo64(int amount) {
        String text;
        if (amount <= 64) {
            text = ChatColor.GRAY + "Przelicznik " + ChatColor.RESET + ChatColor.DARK_AQUA + amount + ChatColor.RESET + ChatColor.GRAY + " ► " + ChatColor.RESET + ChatColor.DARK_AQUA + amount;
        } else if (amount % 64 == 0) {
            int lack = amount / 64;
            text = ChatColor.GRAY + "Przelicznik " + ChatColor.RESET + ChatColor.DARK_AQUA + amount + ChatColor.RESET + ChatColor.GRAY + " ► " + ChatColor.RESET + ChatColor.DARK_AQUA + lack + ChatColor.RESET + ChatColor.GRAY + " stacków";
        } else {
            int lack = amount / 64;
            int rest = amount - (lack * 64);

            text = ChatColor.GRAY + "Przelicznik " + ChatColor.RESET + ChatColor.DARK_AQUA + amount + ChatColor.RESET + ChatColor.GRAY + " ► " + ChatColor.RESET + ChatColor.DARK_AQUA + lack + ChatColor.RESET + ChatColor.GRAY + " stacków i " + ChatColor.RESET + ChatColor.DARK_AQUA + rest;
        }

        return text;
    }

    public static void openPagedCategoryMenu(Player player, String categoryName, int page) {
        CategoryMenu.openCategoryMenu(player, categoryName, page);
    }


    public static void createBottomNotPaged(Inventory menu, boolean previousPageFlag, boolean nextPageFlag) {

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Powrót");
        cancel.setItemMeta(cancelMeta);
        menu.setItem(46, cancel);
        menu.setItem(52, cancel);


        ItemStack nothing = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = nothing.getItemMeta();
        meta.setDisplayName(" ");
        nothing.setItemMeta(meta);

        menu.setItem(47, nothing);
        menu.setItem(51, nothing);


        ItemStack previous = getItemStackPrevious(previousPageFlag);
        menu.setItem(48, previous);

        ItemStack next = getItemStackNext(nextPageFlag);
        menu.setItem(50, next);


    }


    private static ItemStack getItemStackNext(boolean previousPageFlag) {
        ItemStack previous;
        if (previousPageFlag) {
            previous = new ItemStack(Material.LIME_CANDLE);
            ItemMeta meta = previous.getItemMeta();
            meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + "Następna strona");
            previous.setItemMeta(meta);
        } else {
            previous = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta meta = previous.getItemMeta();
            meta.setDisplayName(" ");
            previous.setItemMeta(meta);
        }
        return previous;
    }

    private static ItemStack getItemStackPrevious(boolean previousPageFlag) {
        ItemStack previous;
        if (previousPageFlag) {
            previous = new ItemStack(Material.RED_CANDLE);
            ItemMeta meta = previous.getItemMeta();
            meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + "Poprzednia strona");
            previous.setItemMeta(meta);
        } else {
            previous = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta meta = previous.getItemMeta();
            meta.setDisplayName(" ");
            previous.setItemMeta(meta);
        }
        return previous;
    }

    public static void placeStatusItem(Inventory menu, Player player) {
        ItemStack status = new ItemStack(Material.BOOK);
        ItemMeta meta = status.getItemMeta();

        UUID playerUUID = player.getUniqueId();

        if (AuctionCommand.getPlayerSellingItems().containsKey(playerUUID) &&
                AuctionCommand.getPlayerWishedAmountToRemove().containsKey(playerUUID)) {

            ArrayList<ItemStack> sellingItems = AuctionCommand.getPlayerSellingItems().get(playerUUID);
            ArrayList<Integer> wishedAmounts = AuctionCommand.getPlayerWishedAmountToRemove().get(playerUUID);

            if (sellingItems.size() == 1 && wishedAmounts.size() == 1) {
                ItemStack item = sellingItems.get(0);
                int amount = wishedAmounts.get(0);



                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Sprzedajesz: " +
                        getDisplayName(item) + " w ilości " + amount);
            } else {
                int lastIndex = sellingItems.size() - 1;
                ItemStack item = sellingItems.get(lastIndex);
                int amount = wishedAmounts.get(lastIndex);



                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Sprzedajesz: " +
                        getDisplayName(item) + " w ilości " + amount);
            }

            meta.setLore(Arrays.asList(
                    "",
                    "◆ Można wybrać maksymalnie 5 itemów ◆",
                    ChatColor.BOLD + "" + ChatColor.BLUE + "▣ Wybrane itemy nie mogą przekroczyć łącznie 36 stacków ▣"
            ));

            status.setItemMeta(meta);

            menu.setItem(49, status);
        }
    }

    public static String getDisplayName(ItemStack item) {
        // Check for null ItemStack
        if (item == null) {
            return "Unknown Item";
        }

        // Check for null ItemMeta
        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();

            // Check for null DisplayName
            if (itemMeta.hasDisplayName()) {
                return itemMeta.getDisplayName();
            } else {


                // Fallback to default name
                return getDefaultName(item.getType());
            }
        } else {


            // Fallback to default name
            return getDefaultName(item.getType());
        }
    }

    private static String getDefaultName(Material material) {
        // Provide default names for specific materials if needed
        // For now, returning the material name
        return material.toString();
    }


    private static ItemStack fillItem() {
        ItemStack fill = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta meta = fill.getItemMeta();
        meta.setDisplayName(" ");
        fill.setItemMeta(meta);
        return fill;
    }

    private static void fillEmptySlots(Inventory menu, ItemStack fillItem) {
        for (int i = 0; i < menu.getSize(); i++) {
            if (menu.getItem(i) == null || menu.getItem(i).getType() == Material.AIR) {
                menu.setItem(i, fillItem);
            }
        }
    }


    private static ItemStack createCategoryItem(Material material, String name) {
        ItemStack natural = new ItemStack(material);
        ItemMeta nMeta = natural.getItemMeta();
        nMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + name);
        categoryNames.add(ChatColor.BLUE + name);
        natural.setItemMeta(nMeta);
        return natural;
    }

    public static void setCategoryItems(Inventory menu) {
        menu.setItem(12, createCategoryItem(Material.GRASS_BLOCK, "Naturalne Bloki"));
        menu.setItem(14, createCategoryItem(Material.BRICKS, "Budowlane Bloki"));
        menu.setItem(20, createCategoryItem(Material.ENCHANTING_TABLE, "Funkcjonalne Bloki"));
        menu.setItem(22, createCategoryItem(Material.DIAMOND_SWORD, "Walka"));
        menu.setItem(24, createCategoryItem(Material.REDSTONE, "Mechanizmy"));
        menu.setItem(30, createCategoryItem(Material.BEEF, "Jedzenie"));
        menu.setItem(32, createCategoryItem(Material.DIAMOND, "Surowce"));
        menu.setItem(40, createCategoryItem(Material.ORANGE_GLAZED_TERRACOTTA, "Kolorowe Bloki"));
        menu.setItem(43, createCategoryItem(Material.ENCHANTED_GOLDEN_APPLE, "Specjalne Itemy"));
    }

    public static void schematicGuiDesign(Inventory menu) {

        //TODO GRAY GLASS====================
        menu.setItem(0, grayBorderItem());
        menu.setItem(2, grayBorderItem());
        menu.setItem(4, grayBorderItem());
        menu.setItem(6, grayBorderItem());
        menu.setItem(8, grayBorderItem());

        menu.setItem(18, grayBorderItem());
        menu.setItem(36, grayBorderItem());

        menu.setItem(26, grayBorderItem());
        menu.setItem(44, grayBorderItem());
        //TODO GRAY GLASS====================

        //TODO BLUE GLASS====================
        menu.setItem(1, blueBorderItem());
        menu.setItem(3, blueBorderItem());
        menu.setItem(5, blueBorderItem());
        menu.setItem(7, blueBorderItem());

        menu.setItem(9, blueBorderItem());
        menu.setItem(17, blueBorderItem());

        menu.setItem(27, blueBorderItem());
        menu.setItem(35, blueBorderItem());

        menu.setItem(45, blueBorderItem());
        menu.setItem(53, blueBorderItem());
        //TODO BLUE GLASS====================

    }

    private static ItemStack grayBorderItem() {
        ItemStack g = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        ItemMeta meta = g.getItemMeta();
        meta.setDisplayName(" ");
        g.setItemMeta(meta);

        return g;
    }

    private static ItemStack blueBorderItem() {
        ItemStack b = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);

        ItemMeta meta = b.getItemMeta();
        meta.setDisplayName(" ");
        b.setItemMeta(meta);

        return b;
    }


}
