package jgor.heartstealplugin.auctions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;


import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

public class MarketGui implements Serializable {

    public static ArrayList<HashMap<UUID, ItemStack>> playerSellItemList = new ArrayList<>();

    public static ArrayList<HashMap<UUID, ArrayList<ItemStack>>> playerSellItemPrice = new ArrayList<>();

    public static ArrayList<HashMap<UUID, Date>> timeItemExpired = new ArrayList<>();

    public static HashMap<UUID, ArrayList<ItemStack>> sellingPlayerItems = new HashMap<>();

    public static HashMap<UUID, ArrayList<ItemStack>> buyingPlayerItems = new HashMap<>();

    public static HashMap<UUID, ArrayList<ItemStack>> expiredItems = new HashMap<>();

    private static int lastPage;

    public static int taskID;


    public static void openMarketInventory(Player player, int page) {

        countPages();

        if (page == 1 && lastPage == 1) {
            Inventory marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GRAY + " Strona - " + page + "/" + lastPage);

            if (!playerSellItemPrice.isEmpty() && !playerSellItemList.isEmpty()) {
                int startIndex = 0;
                int endIndex = 45;

                ArrayList<HashMap<UUID, ItemStack>> itemsForPage = getItemsForPage(startIndex, endIndex);

                ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

                ArrayList<HashMap<UUID, Date>> itemsTimeExpired = getExpiredTime(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage, itemsTimeExpired);
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

                ArrayList<HashMap<UUID, Date>> itemsTimeExpired = getExpiredTime(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage, itemsTimeExpired);
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

                ArrayList<HashMap<UUID, Date>> itemsTimeExpired = getExpiredTime(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage, itemsTimeExpired);
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

                ArrayList<HashMap<UUID, Date>> itemsTimeExpired = getExpiredTime(startIndex, endIndex);

                placeItemsInMarket(marketPlace, itemsForPage, itemsPricesForPage, itemsTimeExpired);
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

    private static ArrayList<HashMap<UUID, Date>> getExpiredTime(int startIndex, int endIndex) {
        ArrayList<HashMap<UUID, Date>> allTimes = new ArrayList<>();

        for (HashMap<UUID, Date> map : timeItemExpired) {
            allTimes.add(new HashMap<>(map));
        }

        List<HashMap<UUID, Date>> subList = allTimes.subList(startIndex, Math.min(endIndex, allTimes.size()));

        return new ArrayList<>(subList);
    }

    private static void countPages() {
        int size = playerSellItemList.stream().mapToInt(HashMap::size).sum();
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


    public static void placeItemsInMarket(Inventory menu, ArrayList<HashMap<UUID, ItemStack>> itemsForPage, ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage, ArrayList<HashMap<UUID, Date>> itemsTimeExpired) {

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
                    Date expiredTime = itemsTimeExpired.get(menuIndex).get(sellingPlayerUUID);
                    k.setItemMeta(createMeta(meta, sellingPlayer.get(menuIndex), itemsForPage.get(menuIndex).get(sellingPlayerUUID), itemsPricesForPage.get(menuIndex).get(sellingPlayer.get(menuIndex)), expiredTime));
                    menu.setItem(menuIndex, k);
                    // Increment the menu index
                }
                menuIndex++;
            }
        }
    }

    private static ItemMeta createMeta(ItemMeta meta, UUID playerUUID, ItemStack itemAmount, ArrayList<ItemStack> priceList, Date expiredTime) {
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        Player player = Bukkit.getPlayer(playerUUID);
        String playerName = player.getName();

        lore.add(" ");
        lore.add(ChatColor.DARK_PURPLE + "✿ " + ChatColor.RESET + ChatColor.GRAY + "Wystawiony przez: " + ChatColor.RESET + ChatColor.AQUA + playerName + ChatColor.RESET + ChatColor.DARK_PURPLE + " ✿");
        lore.add(ChatColor.DARK_PURPLE + "❈ " + ChatColor.RESET + ChatColor.GRAY + "W ilości → " + ChatColor.RESET + ChatColor.AQUA + itemAmount.getAmount() + ChatColor.RESET + ChatColor.DARK_PURPLE + "  ❈");

        lore.add(" ");
        lore.add(ChatColor.GRAY + "Wygasa za: " + ChatColor.RESET + ChatColor.AQUA + formatExpirationTime(expiredTime));
        lore.add(" ");
        lore.add(ChatColor.DARK_PURPLE + "☄ " + ChatColor.RESET + ChatColor.GRAY + "Wystawiony za: " + ChatColor.RESET + ChatColor.DARK_PURPLE + " ☄");

        for (ItemStack price : priceList) {
            lore.add(ChatColor.GREEN + "  ☛ " + ChatColor.RESET + ChatColor.AQUA + SellGui.getDisplayName(price) + ChatColor.RESET + ChatColor.GRAY + " w ilości " + ChatColor.RESET + ChatColor.AQUA + price.getAmount());
        }

        lore.add(" ");

        meta.setLore(lore);

        return meta;
    }

    private static String formatExpirationTime(Date expirationDate) {
        if (expirationDate == null) {
            return "N/A"; // or any default value or message
        }

        long timeDiffInMillis = expirationDate.getTime() - System.currentTimeMillis();

        // Check if the expiration time is negative (in the past)
        if (timeDiffInMillis < 0) {
            return "Wygasł";
        }

        long hours = TimeUnit.MILLISECONDS.toHours(timeDiffInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiffInMillis - TimeUnit.HOURS.toMillis(hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiffInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
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


        //TODO Expire item time
        HashMap<UUID, Date> expiredTime = new HashMap<>();


        expiredTime.put(playerUUID, Date.from(Instant.now().plus(24, ChronoUnit.HOURS)));


        timeItemExpired.add(expiredTime);
        //TODO Expire item time


    }

    public static void cleanExpiredItems() {
        Bukkit.getLogger().info("---> Usuwam wygasłe item <---");

        /*Iterator<HashMap<UUID, Date>> iterator = timeItemExpired.iterator();

        while (iterator.hasNext()) {
            HashMap<UUID, Date> itemTimeMap = iterator.next();
            UUID playerUUID = itemTimeMap.keySet().iterator().next();
            Date expirationDate = itemTimeMap.get(playerUUID);

            if (isExpired(expirationDate)) {

                // Remove the expired item from playerSellItemList, playerSellItemPrice, and timeItemExpired
                removeExpiredItem(playerUUID);
                iterator.remove(); // Remove the entry from the iterator
            }
        }*/

        for (HashMap<UUID, Date> map : timeItemExpired) {
            for (UUID playerUUID : map.keySet()) {
                Date expirationDate = map.get(playerUUID);

                if (isExpired(expirationDate)) {
                    removeExpiredItem(playerUUID);
                    map.remove(playerUUID);
                }
            }
        }

    }

    private static void removeExpiredItem(UUID playerUUID) {
        int indexToRemove = -1;

        // Find the index of the expired item
        for (int i = 0; i < playerSellItemList.size(); i++) {
            HashMap<UUID, ItemStack> itemMap = playerSellItemList.get(i);
            if (itemMap.containsKey(playerUUID)) {
                indexToRemove = i;

                break;
            }
        }

        // Remove the item from playerSellItemList and playerSellItemPrice
        if (indexToRemove != -1) {
            Player playerTosendMessage = Bukkit.getPlayer(playerUUID);


            if (playerTosendMessage != null) {
                playerTosendMessage.sendTitle(ChatColor.RED + "Twój przedmiot na rynku wygasł", ChatColor.GOLD + "Odbierz przedmioty na /rynek", 10, 35, 20);
            }



            if (expiredItems.containsKey(playerUUID)) {
                ItemStack itemToRemove = playerSellItemList.get(indexToRemove).get(playerUUID).clone();

                expiredItems.get(playerUUID).add(itemToRemove);
            } else {
                ArrayList<ItemStack> list = new ArrayList<>();
                ItemStack itemToRemove = playerSellItemList.get(indexToRemove).get(playerUUID).clone();
                list.add(itemToRemove);
                expiredItems.put(playerUUID, list);
            }

            playerSellItemList.remove(indexToRemove);
            playerSellItemPrice.remove(indexToRemove);
        }
    }

    private static boolean isExpired(Date expirationDate) {
        return expirationDate.before(new Date());
    }

    public static void scheduleItemExpirationCheck() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(HeartStealPlugin.getInstance(), MarketGui::cleanExpiredItems, 1L, 20L * 60L * 5L); // Sprawdza co 2 minuty (20 * 60 * 2 ticków)

        // Możesz zachować ID zadania, aby je później anulować, jeśli to konieczne
        // HeartStealPlugin.getInstance().setExpirationCheckTaskId(taskId);
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

        ArrayList<ItemStack> priceList = currentItemToBuy.get(sellerUUIDs.getFirst());

        int sumAmountPlayerShouldHaveToBuy = priceList.stream().mapToInt(ItemStack::getAmount).sum();


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


        if (sumAmountPlayerShouldHaveToBuy > amount) {
            player.sendMessage(ChatColor.RED + "Nie masz wystarczającej ilości przedmiotów, aby to kupić!");
            return false;
        }

        return true;
    }

    public static boolean hasTimeExpiredForItem(Player player, int slot, int page) {
        int startIndex = (page - 1) * 45;
        int endIndex = page * 45;

        ArrayList<HashMap<UUID, Date>> timeForItemPage = getExpiredTime(startIndex, endIndex);

        if (timeForItemPage.isEmpty() || slot < 0 || slot >= timeForItemPage.size()) {
            // Handle the case when the list is empty or the slot is out of bounds.
            // You may want to send an appropriate message or take necessary actions.
            player.sendMessage(ChatColor.RED + "Invalid slot or no items on this page.");
            openMarketInventory(player, AuctionListeners.playerCurrentMarketPage.get(player.getUniqueId()));
            return false;
        }

        HashMap<UUID, Date> currentTimeToCheck = timeForItemPage.get(slot);
        ArrayList<UUID> sellerUUIDs = new ArrayList<>(currentTimeToCheck.keySet());
        UUID sellerUUID = sellerUUIDs.getFirst();

        Date dateToCheck = currentTimeToCheck.get(sellerUUID);

        if (isExpired(dateToCheck)) {
            player.sendMessage(ChatColor.RED + "Nie możesz kupić tego przedmiotu, ponieważ wygasł :(");
            cleanExpiredItems();
            openMarketInventory(player, AuctionListeners.playerCurrentMarketPage.get(player.getUniqueId()));
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

        //UUID playerUUID = player.getUniqueId();

        int startIndex = (page - 1) * 45;
        int endIndex = page * 45;

        ArrayList<HashMap<UUID, ArrayList<ItemStack>>> itemsPricesForPage = getItemsPricesForPage(startIndex, endIndex);

        HashMap<UUID, ArrayList<ItemStack>> currentItemToBuy = itemsPricesForPage.get(slot);

        ArrayList<UUID> sellerUUIDs = new ArrayList<>(currentItemToBuy.keySet());

        ArrayList<ItemStack> priceList = currentItemToBuy.get(sellerUUIDs.getFirst());

        Player sellingPlayer = Bukkit.getPlayer(sellerUUIDs.getFirst());
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


        UUID uuidToRemove = new ArrayList<>(buyingPlayer.keySet()).getFirst();


        sellingPlayerItems.put(uuidToRemove, splitSellingItems(sellingPlayer.get(uuidToRemove))); //Sprzedawca

        buyingPlayerItems.put(player.getUniqueId(), splitItems(buyingPlayer.get(uuidToRemove))); //Kupiciel


        playerSellItemPrice.remove(targetIndex);
        playerSellItemList.remove(targetIndex);
        timeItemExpired.remove(targetIndex);

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

    public static void openExpiredItemsIncentory(Player player) {
        if (!expiredItems.isEmpty()) {
            Inventory marketPlace = Bukkit.createInventory(player, 9 * 6, ChatColor.BOLD + "" + ChatColor.GOLD + "Rynek" + ChatColor.RESET + ChatColor.GOLD + " Odbierz swoje wygasłe przedmioty");

            ArrayList<ItemStack> putExpiredItems = splitSellingItems(expiredItems.get(player.getUniqueId()));


            int index = 0;
            for (ItemStack item : putExpiredItems) {
                marketPlace.setItem(index, item);
                index++;
            }

            player.openInventory(marketPlace);
            player.setMetadata(AuctionListeners.MARKET_COLLECT_EXPIRED_ITEMS, new FixedMetadataValue(HeartStealPlugin.getInstance(), marketPlace));
        } else {
            player.sendMessage(ChatColor.RED + "Nie posiadasz jeszcze wygasłych itemów");
        }

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

    public static void removeBuyingItems(UUID uniqueId, int slot) {
        if (buyingPlayerItems.containsKey(uniqueId)) {
            ArrayList<ItemStack> items = buyingPlayerItems.get(uniqueId);
            if (slot >= 0 && slot < items.size()) {
                items.remove(slot);
                if (items.isEmpty()) {
                    buyingPlayerItems.remove(uniqueId);
                }
            }
        }
    }

    public static void removeSellingItems(UUID uniqueId, int slot) {

        if (!buyingPlayerItems.containsKey(uniqueId)) {
            ArrayList<ItemStack> items = sellingPlayerItems.get(uniqueId);
            if (slot >= 0 && slot < items.size()) {
                items.remove(slot);
                if (items.isEmpty()) {
                    sellingPlayerItems.remove(uniqueId);
                }
            }
        }

    }

    public static void removeExpiredItemsToCollect(UUID uniqueId, int slot) {
        if (expiredItems.containsKey(uniqueId)) {
            ArrayList<ItemStack> items = expiredItems.get(uniqueId);
            if (slot >= 0 && slot < items.size()) {
                items.remove(slot);
                if (items.isEmpty()) {
                    expiredItems.remove(uniqueId);
                }
            }
        }
    }


    private static String itemTo64(ItemStack stack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    private static ItemStack itemFrom64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                return (ItemStack) dataInput.readObject();
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }


    public static void saveData_SellingPlayerItemList() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                .create();

        Type type = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();

        ArrayList<HashMap<String, String>> serializedPlayerSellItemList = new ArrayList<>();

        for (HashMap<UUID, ItemStack> map : playerSellItemList) {
            for (Map.Entry<UUID, ItemStack> entry : map.entrySet()) {
                String serializedItemStack = itemTo64(entry.getValue());
                HashMap<String, String> serializedMap = new HashMap<>();
                serializedMap.put(entry.getKey().toString(), serializedItemStack);
                serializedPlayerSellItemList.add(serializedMap);
            }

        }

        try (FileWriter writer = new FileWriter(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/player_sell_item_list_data.json")) {
            gson.toJson(serializedPlayerSellItemList, type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData_SellingPlayerItemList() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                .create();
        Type type = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();

        try (FileReader reader = new FileReader(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/player_sell_item_list_data.json")) {
            ArrayList<HashMap<String, String>> serializedPlayerSellList = gson.fromJson(reader, type);

            for (HashMap<String, String> map : serializedPlayerSellList) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String serializedItem = entry.getValue();
                    ItemStack item = itemFrom64(serializedItem);
                    HashMap<UUID, ItemStack> deserialized = new HashMap<>();
                    deserialized.put(UUID.fromString(entry.getKey()), item);
                    playerSellItemList.add(deserialized);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveData_SellingPlayerPriceList() {
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<HashMap<String, List<String>>>>() {
        }.getType();

        ArrayList<HashMap<String, List<String>>> serializedPlayerPriceList = new ArrayList<>();

        for (HashMap<UUID, ArrayList<ItemStack>> map : playerSellItemPrice) {
            for (Map.Entry<UUID, ArrayList<ItemStack>> entry : map.entrySet()) {
                List<String> serializedItemStackList = new ArrayList<>();
                for (ItemStack item : entry.getValue()) {
                    serializedItemStackList.add(itemTo64(item));
                }
                HashMap<String, List<String>> serialMap = new HashMap<>();
                serialMap.put(entry.getKey().toString(), serializedItemStackList);

                serializedPlayerPriceList.add(serialMap);
            }
        }

        try (FileWriter writer = new FileWriter(HeartStealPlugin.getInstance().getDataFolder() + "/player_sell_price_item_list.json")) {
            gson.toJson(serializedPlayerPriceList, type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData_SellingPlayerPriceList() {
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<HashMap<String, List<String>>>>() {
        }.getType();

        try (FileReader reader = new FileReader(HeartStealPlugin.getInstance().getDataFolder() + "/player_sell_price_item_list.json")) {
            ArrayList<HashMap<String, List<String>>> serializedPlayerPriceList = gson.fromJson(reader, type);

            for (HashMap<String, List<String>> map : serializedPlayerPriceList) {
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    ArrayList<ItemStack> deserializedItemStackList = new ArrayList<>();
                    for (String serializedItem : entry.getValue()) {
                        deserializedItemStackList.add(itemFrom64(serializedItem));
                    }
                    HashMap<UUID, ArrayList<ItemStack>> deserializeMap = new HashMap<>();
                    deserializeMap.put(UUID.fromString(entry.getKey()), deserializedItemStackList);
                    playerSellItemPrice.add(deserializeMap);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveData_TimeItemExpired() {
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<HashMap<String, Long>>>() {
        }.getType();

        ArrayList<HashMap<String, Long>> serializedTimeItemExpired = new ArrayList<>();

        for (HashMap<UUID, Date> map : timeItemExpired) {
            for (Map.Entry<UUID, Date> entry : map.entrySet()) {
                HashMap<String, Long> serialMap = new HashMap<>();
                serialMap.put(entry.getKey().toString(), entry.getValue().getTime());
                serializedTimeItemExpired.add(serialMap);
            }
        }

        try (FileWriter writer = new FileWriter(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/player_item_time_expire_data.json")) {
            gson.toJson(serializedTimeItemExpired, type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData_TimeItemExpire() {
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<HashMap<String, Long>>>() {
        }.getType();

        try (FileReader reader = new FileReader(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/player_item_time_expire_data.json")) {
            ArrayList<HashMap<String, Long>> serializedTimeItemExpire = gson.fromJson(reader, type);

            for (HashMap<String, Long> map : serializedTimeItemExpire) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    UUID playerUUId = UUID.fromString(entry.getKey());
                    Date date = new Date(entry.getValue());
                    HashMap<UUID, Date> deserializedMap = new HashMap<>();
                    deserializedMap.put(playerUUId, date);
                    timeItemExpired.add(deserializedMap);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveData_SellingPlayerList() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        HashMap<String, List<String>> serializedSellingPlayerItems = new HashMap<>();

        for (Map.Entry<UUID, ArrayList<ItemStack>> entry : sellingPlayerItems.entrySet()) {
            List<String> serializedItemStackList = new ArrayList<>();
            for (ItemStack item : entry.getValue()) {
                serializedItemStackList.add(itemTo64(item));
            }
            serializedSellingPlayerItems.put(entry.getKey().toString(), serializedItemStackList);
        }

        try (FileWriter writer = new FileWriter(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/selling_player_item_list_data.json")) {
            gson.toJson(serializedSellingPlayerItems, type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData_SellingPlayerList() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        try (FileReader reader = new FileReader(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/selling_player_item_list_data.json")) {
            HashMap<String, List<String>> serializedSellingPlayerItems = gson.fromJson(reader, type);

            for (Map.Entry<String, List<String>> entry : serializedSellingPlayerItems.entrySet()) {
                ArrayList<ItemStack> deserializedItemStackList = new ArrayList<>();
                for (String serializedItem : entry.getValue()) {
                    deserializedItemStackList.add(itemFrom64(serializedItem));
                }
                sellingPlayerItems.put(UUID.fromString(entry.getKey()), deserializedItemStackList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData_BuyingPlayerList() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        HashMap<String, List<String>> serializedSellingPlayerItems = new HashMap<>();

        for (Map.Entry<UUID, ArrayList<ItemStack>> entry : buyingPlayerItems.entrySet()) {
            List<String> serializedItemStackList = new ArrayList<>();
            for (ItemStack item : entry.getValue()) {
                serializedItemStackList.add(itemTo64(item));
            }
            serializedSellingPlayerItems.put(entry.getKey().toString(), serializedItemStackList);
        }

        try (FileWriter writer = new FileWriter(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/buying_player_item_list_data.json")) {
            gson.toJson(serializedSellingPlayerItems, type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData_BuyingPlayerList() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        try (FileReader reader = new FileReader(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/buying_player_item_list_data.json")) {
            HashMap<String, List<String>> serializedSellingPlayerItems = gson.fromJson(reader, type);

            for (Map.Entry<String, List<String>> entry : serializedSellingPlayerItems.entrySet()) {
                ArrayList<ItemStack> deserializedItemStackList = new ArrayList<>();
                for (String serializedItem : entry.getValue()) {
                    deserializedItemStackList.add(itemFrom64(serializedItem));
                }
                buyingPlayerItems.put(UUID.fromString(entry.getKey()), deserializedItemStackList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveData_ExpiredItemsList() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        HashMap<String, List<String>> serializedSellingPlayerItems = new HashMap<>();

        for (Map.Entry<UUID, ArrayList<ItemStack>> entry : expiredItems.entrySet()) {
            List<String> serializedItemStackList = new ArrayList<>();
            for (ItemStack item : entry.getValue()) {
                serializedItemStackList.add(itemTo64(item));
            }
            serializedSellingPlayerItems.put(entry.getKey().toString(), serializedItemStackList);
        }

        try (FileWriter writer = new FileWriter(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/expired_player_item_list_data.json")) {
            gson.toJson(serializedSellingPlayerItems, type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData_ExpiredItemsList() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>() {
        }.getType();

        try (FileReader reader = new FileReader(HeartStealPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/expired_player_item_list_data.json")) {
            HashMap<String, List<String>> serializedSellingPlayerItems = gson.fromJson(reader, type);

            for (Map.Entry<String, List<String>> entry : serializedSellingPlayerItems.entrySet()) {
                ArrayList<ItemStack> deserializedItemStackList = new ArrayList<>();
                for (String serializedItem : entry.getValue()) {
                    deserializedItemStackList.add(itemFrom64(serializedItem));
                }
                expiredItems.put(UUID.fromString(entry.getKey()), deserializedItemStackList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveAllData() {
        saveData_SellingPlayerList();
        saveData_BuyingPlayerList();
        saveData_ExpiredItemsList();
        saveData_SellingPlayerItemList();
        saveData_SellingPlayerPriceList();
        saveData_TimeItemExpired();
    }

    public static void loadAllData() {
        loadData_SellingPlayerList();
        loadData_BuyingPlayerList();
        loadData_ExpiredItemsList();
        loadData_SellingPlayerItemList();
        loadData_SellingPlayerPriceList();
        loadData_TimeItemExpire();
    }

}
