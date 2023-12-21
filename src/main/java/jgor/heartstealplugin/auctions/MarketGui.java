package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class MarketGui {

    private static final ArrayList<HashMap<UUID, ItemStack>> playerSellItemList = new ArrayList<>();

    private static final ArrayList<HashMap<UUID, ArrayList<ItemStack>>> playerSellItemPrice = new ArrayList<>();

    public static HashMap<UUID, ArrayList<ItemStack>> sellingPlayerItems = new HashMap<>();

    public static HashMap<UUID, ArrayList<ItemStack>> buyingPlayerItems = new HashMap<>();

    private static int lastPage;

    public static void openMarketInventory(Player player, int page) {

        countPages();

        if (page == 1 && lastPage == 1) {
            Inventory marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GRAY + " Strona - " + page + "/" + lastPage);

            if (!playerSellItemPrice.isEmpty() && !playerSellItemList.isEmpty()) {
                int startIndex = 0;
                int endIndex = 45;

                ArrayList<HashMap<UUID, ItemStack>> itemsForPage = getItemsForPage(startIndex, endIndex);

                ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage);
            }


            schematicBottomMarketMenu(marketPlace, player, true, false, false);


            player.openInventory(marketPlace);
            player.setMetadata(AuctionListeners.MARKET_DEAFAULT, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));
        } else if (page > 1 && page != lastPage) {
            Inventory marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GRAY + " Strona - " + page + "/" + lastPage);

            if (!playerSellItemPrice.isEmpty() && !playerSellItemList.isEmpty()) {
                int startIndex = (page - 1) * 45;
                int endIndex = (page * 45);

                ArrayList<HashMap<UUID, ItemStack>> itemsForPage = getItemsForPage(startIndex, endIndex);

                ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage);
            }


            schematicBottomMarketMenu(marketPlace, player, true, true, true);


            player.openInventory(marketPlace);
            player.setMetadata(AuctionListeners.MARKET_DEAFAULT, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));
        } else if (page == lastPage) {
            Inventory marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GRAY + " Strona - " + page + "/" + lastPage);

            if (!playerSellItemPrice.isEmpty() && !playerSellItemList.isEmpty()) {
                int startIndex = (page - 1) * 45;
                int endIndex = (page * 45);

                ArrayList<HashMap<UUID, ItemStack>> itemsForPage = getItemsForPage(startIndex, endIndex);

                ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage);
            }


            schematicBottomMarketMenu(marketPlace, player, true, true, false);


            player.openInventory(marketPlace);
            player.setMetadata(AuctionListeners.MARKET_DEAFAULT, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));
        } else if (page == 1 && lastPage > page) { //TODO DOkoncz
            Inventory marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GRAY + " Strona - " + page + "/" + lastPage);

            if (!playerSellItemPrice.isEmpty() && !playerSellItemList.isEmpty()) {
                int startIndex = 0;
                int endIndex = (page * 45);

                ArrayList<HashMap<UUID, ItemStack>> itemsForPage = getItemsForPage(startIndex, endIndex);

                ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage);
            }


            schematicBottomMarketMenu(marketPlace, player, true, false, true);


            player.openInventory(marketPlace);
            player.setMetadata(AuctionListeners.MARKET_DEAFAULT, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));
        }


    }

    private static ArrayList<HashMap<UUID, ArrayList<ItemStack>>> getItemsPricesForPage(int startIndex, int endIndex) {
        ArrayList<HashMap<UUID, ArrayList<ItemStack>>> allItems = new ArrayList<>();

        for (HashMap<UUID, ArrayList<ItemStack>> list : playerSellItemPrice) {
            allItems.add(new HashMap<>(list));
        }

        List<HashMap<UUID, ArrayList<ItemStack>>> subList = allItems.subList(startIndex, Math.min(endIndex, allItems.size()));

        return new ArrayList<>(subList);
    }

    private static ArrayList<HashMap<UUID, ItemStack>> getItemsForPage(int startIndex, int endIndex) {
        ArrayList<HashMap<UUID, ItemStack>> allItems = new ArrayList<>();

        for (HashMap<UUID, ItemStack> list : playerSellItemList) {
            allItems.add(new HashMap<>(list));
        }

        List<HashMap<UUID, ItemStack>> subList = allItems.subList(startIndex, Math.min(endIndex, allItems.size()));

        return new ArrayList<>(subList);

    }

    private static void countPages() {
        int size = playerSellItemList.stream().mapToInt(list -> list.size()).sum();
        if (size == 0) lastPage = 1;
        else lastPage = (size / 45) + ((size % 45 != 0) ? 1 : 0);

    }


    private static void schematicBottomMarketMenu(Inventory menu, Player player, boolean flagToBoughtItems, boolean flagToPreviousPage, boolean flagToNextPage) {
        ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta whiteMeta = white.getItemMeta();
        whiteMeta.setDisplayName(" ");
        white.setItemMeta(whiteMeta);

        menu.setItem(46, white);
        menu.setItem(52, white);

        ItemStack blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta blueMeta = blue.getItemMeta();
        blueMeta.setDisplayName(" ");
        blue.setItemMeta(blueMeta);

        menu.setItem(47, blue);
        menu.setItem(51, blue);
        menu.setItem(53, blue);


        ItemStack playerMenu = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerMenu.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(ChatColor.GOLD + "❈ Sprawdź swoje wygasłe oferty ❈");
        playerMenu.setItemMeta(skullMeta);
        menu.setItem(49, playerMenu);

        if (flagToBoughtItems) {
            ItemStack boughtItems = new ItemStack(Material.CHEST);
            ItemMeta meta = boughtItems.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "✦ Odbierz zakupione przedmioty ✦");
            boughtItems.setItemMeta(meta);
            menu.setItem(45, boughtItems);
        } else {
            menu.setItem(45, blue);
        }

        if (flagToPreviousPage) {
            ItemStack previous = new ItemStack(Material.RED_CANDLE);
            ItemMeta meta = previous.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "← Poprzednia strona");
            previous.setItemMeta(meta);
            menu.setItem(48, previous);
        } else {
            menu.setItem(48, white);
        }

        if (flagToNextPage) {
            ItemStack next = new ItemStack(Material.LIME_CANDLE);
            ItemMeta meta = next.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Następna strona →");
            next.setItemMeta(meta);
            menu.setItem(50, next);
        } else {
            menu.setItem(50, white);
        }


    }


    public static void placeItemsInMarket(Inventory menu, ArrayList<HashMap<UUID, ItemStack>> itemsForPage, ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage) {

        ArrayList<UUID> sellingPlayer = new ArrayList<>();

        for (HashMap<UUID, ItemStack> listOfItems : itemsForPage) {
            sellingPlayer.addAll(listOfItems.keySet());
        }

        int menuIndex = 0; // Use a separate index for menu items
        for (HashMap<UUID, ItemStack> listOfItems : playerSellItemList) {
            if (menuIndex != itemsForPage.size()) {
                for (ItemStack item : listOfItems.values()) {
                    ItemStack k = item.clone();
                    ItemMeta meta = k.getItemMeta();
                    UUID sellingPlayerUUID = sellingPlayer.get(menuIndex);
                    int amount = itemsForPage.get(menuIndex).get(sellingPlayerUUID).getAmount();
                    k.setItemMeta(createMeta(meta, sellingPlayer.get(menuIndex), itemsForPage.get(menuIndex).get(sellingPlayerUUID), itemsPricesForPage.get(menuIndex).get(sellingPlayer.get(menuIndex))));
                    menu.setItem(menuIndex, k);
                    // Increment the menu index
                }
                menuIndex++;
            }
        }
    }

    private static ItemMeta createMeta(ItemMeta meta, UUID playerUUID, ItemStack itemAmount, ArrayList<ItemStack> priceList) {
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        Player player = Bukkit.getPlayer(playerUUID);
        String playerName = player.getName();

        lore.add(" ");
        lore.add(ChatColor.DARK_PURPLE + "✿ " + ChatColor.RESET + ChatColor.GRAY + "Wystawiony przez: " + ChatColor.RESET + ChatColor.AQUA + playerName + ChatColor.RESET + ChatColor.DARK_PURPLE + " ✿");
        lore.add(ChatColor.DARK_PURPLE + "❈ " + ChatColor.RESET + ChatColor.GRAY + "W ilości → " + ChatColor.RESET + ChatColor.AQUA + itemAmount.getAmount() + ChatColor.RESET + ChatColor.DARK_PURPLE + "  ❈");

        // Pobierz ItemMeta z itemAmount


        lore.add(" ");
        lore.add(ChatColor.GRAY + "Wygasa za: " + ChatColor.RESET + ChatColor.AQUA + Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
        lore.add(" ");
        lore.add(ChatColor.DARK_PURPLE + "☄ " + ChatColor.RESET + ChatColor.GRAY + "Wystawiony za: " + ChatColor.RESET + ChatColor.DARK_PURPLE + " ☄");

        for (ItemStack price : priceList) {
            lore.add(ChatColor.GREEN + "  ☛ " + ChatColor.RESET + ChatColor.AQUA + SellGui.getDisplayName(price) + ChatColor.RESET + ChatColor.GRAY + " w ilości " + ChatColor.RESET + ChatColor.AQUA + price.getAmount());
        }

        lore.add(" ");

        meta.setLore(lore);

        return meta;
    }

    public static void placeItemIntoMarket(Player player) {

        UUID playerUUID = player.getUniqueId();

        ArrayList<ItemStack> sellingItems = AuctionCommand.getPlayerSellingItems().get(playerUUID);
        ArrayList<Integer> wishedAmounts = AuctionCommand.getPlayerWishedAmountToRemove().get(playerUUID);

        ArrayList<ItemStack> priceItems = AuctionListeners.playerPriceItemList.get(playerUUID);


        int lastIndex = wishedAmounts.size() - 1;

        //TODO add playersSellList
        HashMap<UUID, ItemStack> newItemToSell = new HashMap<>();

        ItemStack itemToAdd = sellingItems.get(lastIndex).clone();
        int amountPlayerWished = wishedAmounts.get(lastIndex);
        itemToAdd.setAmount(amountPlayerWished);

        newItemToSell.put(playerUUID, itemToAdd);
        playerSellItemList.add(newItemToSell);

        //TODO add playersSellList


        //TODO add playerPriceList
        HashMap<UUID, ArrayList<ItemStack>> newPriceToAdd = new HashMap<>();
        newPriceToAdd.put(playerUUID, priceItems);

        playerSellItemPrice.add(newPriceToAdd);


        //TODO add playerPriceList

    }

    public static boolean hasPlayerEnoughToBuy(Player player, int slot, int page) {
        int startIndex = (page - 1) * 45;
        int endIndex = page * 45;

        ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

        HashMap<UUID, ArrayList<ItemStack>> currentItemToBuy = itemsPricesForPage.get(slot);

        ArrayList<UUID> sellerUUIDs = new ArrayList<>(currentItemToBuy.keySet());

        if (currentItemToBuy.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Nie możesz kupić swojego przedmiotu!");
            return false;
        }

        ArrayList<ItemStack> priceList = currentItemToBuy.get(sellerUUIDs.get(0));

        int sumAmountPlayerShouldHaveToBuy = priceList.stream().mapToInt(ItemStack::getAmount).sum();

        Bukkit.getLogger().info("Potrzebna wartosc " + sumAmountPlayerShouldHaveToBuy);

        int amount = 0;
        for (ItemStack itemInInventory : player.getInventory().getContents()) {
            if (itemInInventory != null) {
                for (ItemStack priceItem : priceList) {
                    if (itemInInventory.isSimilar(priceItem)) {
                        amount += itemInInventory.getAmount();
                    }
                }
            }
        }

        Bukkit.getLogger().info("Ile masz " + amount);

        if (sumAmountPlayerShouldHaveToBuy > amount) {
            player.sendMessage(ChatColor.RED + "Nie masz wystarczającej ilości przedmiotów, aby to kupić!");
            return false;
        }

        return true;
    }


    public static void openConfirmBuyInventory(Player player) {
        Inventory marketPlace = Bukkit.createInventory(player, 9 * 3, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GREEN + " Potwierdzenie zakupu");

        ItemStack finalConfirmButton = new ItemStack(Material.LIME_SHULKER_BOX);
        ItemMeta confirmMeta = finalConfirmButton.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "✔ " + ChatColor.RESET + ChatColor.GRAY + "Jestem pewien " + ChatColor.RESET + ChatColor.GREEN + "Potwierdzam ✔");
        finalConfirmButton.setItemMeta(confirmMeta);

        marketPlace.setItem(14, finalConfirmButton);

        ItemStack finalExitButton = new ItemStack(Material.RED_SHULKER_BOX);
        ItemMeta exitMeta = finalConfirmButton.getItemMeta();
        exitMeta.setDisplayName(ChatColor.RED + "✘ " + ChatColor.RESET + ChatColor.GRAY + "Jescze się zastanawiam, " + ChatColor.RESET + ChatColor.RED + "Anuluj ✘");
        finalExitButton.setItemMeta(exitMeta);

        marketPlace.setItem(12, finalExitButton);

        player.openInventory(marketPlace);
        player.setMetadata(AuctionListeners.MARKET_BUY_CONFIRM, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));
    }

    public static void removeNeddedItems(Player player, int slot, int page) {

        UUID playerUUID = player.getUniqueId();

        int startIndex = (page - 1) * 45;
        int endIndex = page * 45;

        ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

        HashMap<UUID, ArrayList<ItemStack>> currentItemToBuy = itemsPricesForPage.get(slot);

        ArrayList<UUID> sellerUUIDs = new ArrayList<>(currentItemToBuy.keySet());

        ArrayList<ItemStack> priceList = currentItemToBuy.get(sellerUUIDs.get(0));

        Player sellingPlayer = Bukkit.getPlayer(sellerUUIDs.get(0));
        sellingPlayer.sendTitle(ChatColor.GREEN + "Ktoś kupił twój przedmiot", ChatColor.GOLD + "Odbierz itemy na /rynek", 10, 35, 25);

        for (ItemStack priceItem : priceList) {
            int amountToRemove = priceItem.getAmount();

            // Iterate through the player's inventory to find and remove the item
            for (ItemStack itemInInventory : player.getInventory().getContents()) {
                if (itemInInventory != null && itemInInventory.isSimilar(priceItem)) {
                    int amountInInventory = itemInInventory.getAmount();

                    if (amountInInventory > amountToRemove) {
                        itemInInventory.setAmount(amountInInventory - amountToRemove);
                        break; // Item removed, exit the loop
                    } else {
                        // Remove the entire stack if needed
                        player.getInventory().removeItem(itemInInventory);
                        amountToRemove -= amountInInventory;

                        if (amountToRemove <= 0) {
                            break; // All items removed, exit the loop
                        }
                    }
                }
            }
        }


        splitItemsForPlayers(player, slot, page);

    }

    private static void splitItemsForPlayers(Player player, int slot, int page) {

        int targetIndex = ((page - 1) * 45) + slot;

        HashMap<UUID, ItemStack> buyingPlayer = playerSellItemList.get(targetIndex);

        HashMap<UUID, ArrayList<ItemStack>> sellingPlayer = playerSellItemPrice.get(targetIndex);

        UUID uuidToRemove = new ArrayList<>(buyingPlayer.keySet()).get(0);


        sellingPlayerItems.put(uuidToRemove, splitSellingItems(sellingPlayer.get(uuidToRemove))); //Sprzedawca

        buyingPlayerItems.put(player.getUniqueId(), splitItems(buyingPlayer.get(uuidToRemove))); //Kupiciel


        playerSellItemPrice.remove(targetIndex);
        playerSellItemList.remove(targetIndex);

    }

    private static ArrayList<ItemStack> splitSellingItems(ArrayList<ItemStack> original) {
        ArrayList<ItemStack> result = new ArrayList<>();

        for (ItemStack itemStack : original) {
            int amount = itemStack.getAmount();

            if (itemStack.getMaxStackSize() == 1) {
                for (int i = 0; i < amount; i++) {
                    ItemStack one = itemStack.clone();
                    one.setAmount(1);
                    result.add(one);
                }
            } else if (itemStack.getMaxStackSize() == 16) {
                if (amount <= 16) {
                    result.add(itemStack.clone());
                } else {
                    int fullStacks = amount / 16;
                    int remainingItems = amount % 16;

                    for (int i = 0; i < fullStacks; i++) {
                        ItemStack stack = itemStack.clone();
                        stack.setAmount(16);
                        result.add(stack);
                    }

                    if (remainingItems > 0) {
                        ItemStack remainingStack = itemStack.clone();
                        remainingStack.setAmount(remainingItems);
                        result.add(remainingStack);
                    }
                }
            } else {
                if (amount <= 64) {
                    result.add(itemStack.clone());
                } else {
                    int fullStacks = amount / 64;
                    int remainingItems = amount % 64;

                    for (int i = 0; i < fullStacks; i++) {
                        ItemStack stack = itemStack.clone();
                        stack.setAmount(64);
                        result.add(stack);
                    }

                    if (remainingItems > 0) {
                        ItemStack remainingStack = itemStack.clone();
                        remainingStack.setAmount(remainingItems);
                        result.add(remainingStack);
                    }
                }
            }
        }

        return result;
    }


    private static ArrayList<ItemStack> splitItems(ItemStack original) {
        ArrayList<ItemStack> result = new ArrayList<>();

        int amount = original.getAmount();

        if (original.getMaxStackSize() == 1) {

            for (int i = 0; i < original.getAmount(); i++) {
                ItemStack one = original.clone();
                one.setAmount(1);
                result.add(one);
            }

        } else if (original.getMaxStackSize() == 16) {
            if (amount <= 16) {
                result.add(original.clone());
            } else {
                int fullStacks = amount / 16;
                int remainingItems = amount % 16;

                for (int i = 0; i < fullStacks; i++) {
                    ItemStack stack = original.clone();
                    stack.setAmount(16);
                    result.add(stack);
                }

                if (remainingItems > 0) {
                    ItemStack remainingStack = original.clone();
                    remainingStack.setAmount(remainingItems);
                    result.add(remainingStack);
                }
            }
        } else {
            if (amount <= 64) {
                result.add(original.clone());
            } else {
                int fullStacks = amount / 64;
                int remainingItems = amount % 64;

                for (int i = 0; i < fullStacks; i++) {
                    ItemStack stack = original.clone();
                    stack.setAmount(64);
                    result.add(stack);
                }

                if (remainingItems > 0) {
                    ItemStack remainingStack = original.clone();
                    remainingStack.setAmount(remainingItems);
                    result.add(remainingStack);
                }
            }
        }


        return result;
    }


    public static void openCollectItemsInventory(Player player, boolean which) {

        Inventory marketPlace;

        if (which) {
            marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GOLD + " Odbierz swoje zakupione przedmioty");
            putItemForSellingPlayerItems(marketPlace, player);
        } else {
            marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GOLD + " Odbierz swoje przedmioty za sprzedaż");
            putItemForBuingPlayerItems(marketPlace, player);
        }


        player.openInventory(marketPlace);
        player.setMetadata(AuctionListeners.MARKET_COLLECT_ITEMS, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));


    }

    private static void putItemForBuingPlayerItems(Inventory marketPlace, Player player) {
        ArrayList<ItemStack> itemsToPutOnCollect = buyingPlayerItems.get(player.getUniqueId());

        int index = 0;
        for (ItemStack item : itemsToPutOnCollect) {
            marketPlace.setItem(index, item.clone());
            index++;
        }
    }

    private static void putItemForSellingPlayerItems(Inventory marketPlace, Player player) {

        ArrayList<ItemStack> itemsToPutOnCollect = sellingPlayerItems.get(player.getUniqueId());

        int index = 0;
        for (ItemStack item : itemsToPutOnCollect) {
            marketPlace.setItem(index, item.clone());
            index++;
        }

    }

    public static void removeItemsToCollect(UUID uniqueId, int slot) {
        if (buyingPlayerItems.containsKey(uniqueId)) {
            ArrayList<ItemStack> items = buyingPlayerItems.get(uniqueId);
            if (slot >= 0 && slot < items.size()) {
                items.remove(slot);
                if (items.isEmpty()) {
                    buyingPlayerItems.remove(uniqueId);
                }
            }
        }

        if (sellingPlayerItems.containsKey(uniqueId)) {
            ArrayList<ItemStack> items = sellingPlayerItems.get(uniqueId);
            if (slot >= 0 && slot < items.size()) {
                items.remove(slot);
                if (items.isEmpty()) {
                    sellingPlayerItems.remove(uniqueId);
                }
            }
        }
    }


}
