package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class AuctionListeners implements Listener {

    public static final String MARKET_DEAFAULT = "MarketPlace";

    public static final String SELL_GUI = "MarketSell";

    public static final String MARKET_SELL_FINAL_CONFIRM = "MarketSellFinalConfirm";
    public static final String SELL_GUI_CATEGORY = "MarketCategory";
    public static final String SELL_GUI_SELL_CONFIRM = "MarketSellConfirm";
    public static final String SELL_GUI_SELL_CONFIRM_MINUS = "MarketSellConfirmMinus";
    public static final String MARKET_BUY_CONFIRM = "MarketBuyConfirm";
    public static final String SELL_GUI_SELL_CONFIRM_STACK = "MarketSellConfirmStack";
    public static final String MARKET_COLLECT_EXPIRED_ITEMS = "MarketCollectExpiredItems";
    public static final String MARKET_COLLECT_ITEMS = "MarketCollectItems";



    public static final int MAX_ITEM_AMOUNT = 2304;

    public static final HashMap<UUID, Integer> playerCurrentPage = new HashMap<>();

    public static final HashMap<UUID, String> playerCurrentCategory = new HashMap<>();

    public static final HashMap<UUID, ItemStack> playerItemSellAmount = new HashMap<>();

    public static final HashMap<UUID, Integer> sellItemAmount = new HashMap<>();

    public static final HashMap<UUID, Integer> sumPlayerItemAmount = new HashMap<>();

    public static final HashMap<UUID, Integer> sellItemAmountStack = new HashMap<>();
    public static final HashMap<UUID, ItemStack> sellItemStack = new HashMap<>();
    public static final HashMap<UUID, ArrayList<ItemStack>> playerPriceItemList = new HashMap<>();

    public static final HashMap<UUID, Integer> playerCurrentMarketPage = new HashMap<>();

    public static final HashMap<UUID, Integer> playerSlotWantToBuy = new HashMap<>();




    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();


        if (player.hasMetadata(MARKET_DEAFAULT)) {
            event.setCancelled(true);

            UUID playerUniqueId = player.getUniqueId();

            // Sprawdź, czy playerUniqueId znajduje się w mapie
            if (!playerCurrentMarketPage.containsKey(playerUniqueId)) {
                // Dodaj domyślną wartość, jeśli nie istnieje
                playerCurrentMarketPage.put(playerUniqueId, 1);
            }

            if (event.getSlot() == 48) {
                if (!event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
                    player.closeInventory();

                    // Sprawdź, czy wartość dla danego gracza jest większa od 1, aby uniknąć ujemnych wartości
                    if (playerCurrentMarketPage.get(playerUniqueId) > 1) {
                        playerCurrentMarketPage.replace(playerUniqueId, playerCurrentMarketPage.get(playerUniqueId) - 1);
                        MarketGui.openMarketInventory(player, playerCurrentMarketPage.get(playerUniqueId));
                    }
                }
            }

            if (event.getSlot() == 50) {
                if (!event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
                    player.closeInventory();
                    playerCurrentMarketPage.replace(playerUniqueId, playerCurrentMarketPage.get(playerUniqueId) + 1);
                    MarketGui.openMarketInventory(player, playerCurrentMarketPage.get(playerUniqueId));
                }
            }

            if (event.getSlot() == 45) {
                if (event.getCurrentItem() != null) {

                    boolean hasSellingItems = MarketGui.sellingPlayerItems.containsKey(player.getUniqueId());
                    boolean hasBuyingItems = MarketGui.buyingPlayerItems.containsKey(player.getUniqueId());

                    if (!hasSellingItems && !hasBuyingItems) {
                        player.sendMessage(ChatColor.RED + "Nie masz czego odebrać!");
                    } else {
                        player.closeInventory();

                        if (hasSellingItems && hasBuyingItems) {
                            MarketGui.openCollectItemsInventory(player, false);
                        } else if (hasBuyingItems) {
                            MarketGui.openCollectItemsInventory(player, false);
                        } else {
                            MarketGui.openCollectItemsInventory(player, true);
                        }
                    }
                }
            }

            if (event.getSlot() == 49) {
                if (event.getCurrentItem() != null) {
                    player.closeInventory();
                    MarketGui.openExpiredItemsIncentory(player);
                }
            }


            if (event.getSlot() >= 0 && event.getSlot() < 45) {
                if (event.getCurrentItem() != null) {
                    if (MarketGui.hasTimeExpiredForItem(player, event.getSlot(), playerCurrentMarketPage.get(player.getUniqueId()))) {
                        if (MarketGui.hasPlayerEnoughToBuy(player, event.getSlot(), playerCurrentMarketPage.get(player.getUniqueId()))) {
                            if (MarketGui.buyingPlayerItems.containsKey(player.getUniqueId()) || MarketGui.sellingPlayerItems.containsKey(player.getUniqueId())) {
                                player.sendMessage(ChatColor.RED + "Odbierz najpierw przedmioty, żeby dalej kupować");
                            } else {
                                playerSlotWantToBuy.put(player.getUniqueId(), event.getSlot());

                                player.closeInventory();

                                MarketGui.openConfirmBuyInventory(player);
                            }
                        }
                    }
                }
            }


        } else if (player.hasMetadata(MARKET_BUY_CONFIRM)) {
            event.setCancelled(true);

            if (event.getSlot() == 14) {
                player.closeInventory();
                MarketGui.removeNeddedItems(player, playerSlotWantToBuy.get(player.getUniqueId()), playerCurrentMarketPage.get(player.getUniqueId()));
                player.sendTitle(ChatColor.GREEN + "Pomyślnie zakupiłeś przedmiot/y", ChatColor.GOLD + "Odbierz swoje itemy na /rynek", 10, 35, 20);
            }

            if (event.getSlot() == 12) {
                playerSlotWantToBuy.remove(player.getUniqueId());
                playerCurrentMarketPage.replace(player.getUniqueId(), 1);
                MarketGui.openMarketInventory(player, playerCurrentMarketPage.get(player.getUniqueId()));
            }

        } else if (player.hasMetadata(MARKET_COLLECT_ITEMS)) {

            if (event.getCurrentItem() != null) {
                boolean hasSellingItems = MarketGui.sellingPlayerItems.containsKey(player.getUniqueId());
                boolean hasBuyingItems = MarketGui.buyingPlayerItems.containsKey(player.getUniqueId());
                if (hasSellingItems && hasBuyingItems) {
                    MarketGui.removeBuyingItems(player.getUniqueId(),event.getSlot());
                } else if (hasSellingItems) {
                    MarketGui.removeSellingItems(player.getUniqueId(),event.getSlot());
                } else if (hasBuyingItems) {
                    MarketGui.removeBuyingItems(player.getUniqueId(),event.getSlot());
                }


            }


        } else if (player.hasMetadata(MARKET_COLLECT_EXPIRED_ITEMS)) {
            if (event.getCurrentItem() != null) {
                MarketGui.removeExpiredItemsToCollect(player.getUniqueId(),event.getSlot());
            }

        } else if (player.hasMetadata(SELL_GUI)) {
            event.setCancelled(true);

            if (isCategorySlot(event.getSlot())) {
                if (event.getClickedInventory() != null && !event.getClickedInventory().getType().name().equals("PLAYER") && event.getCurrentItem() != null) {
                    if (SellGui.categoryNames.contains(event.getCurrentItem().getItemMeta().getDisplayName())) {
                        player.closeInventory();
                        playerCurrentPage.put(player.getUniqueId(), 1);
                        playerCurrentCategory.put(player.getUniqueId(), event.getCurrentItem().getItemMeta().getDisplayName());

                        SellGui.openPagedCategoryMenu(player, event.getCurrentItem().getItemMeta().getDisplayName(), 1);
                    }
                }

            } else if (event.getSlot() == 46 || event.getSlot() == 52) {
                player.closeInventory();
                playerCurrentCategory.remove(player.getUniqueId());
                playerCurrentPage.remove(player.getUniqueId());

            } else if (event.getSlot() == 4) {
                if (!event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE))
                    FinalConfirmGUI.openFinalConfirmGui(player);
            }

        } else if (player.hasMetadata(SELL_GUI_CATEGORY)) {
            event.setCancelled(true);
            if (event.getSlot() == 46 || event.getSlot() == 52) {
                player.closeInventory();
                playerCurrentCategory.remove(player.getUniqueId());
                playerCurrentPage.remove(player.getUniqueId());
                SellGui.openSellGui(player);
            } else if (event.getSlot() == 48) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(" "))
                    return;
                playerCurrentPage.replace(player.getUniqueId(), playerCurrentPage.get(player.getUniqueId()) - 1);
                player.closeInventory();
                SellGui.openPagedCategoryMenu(player, playerCurrentCategory.get(player.getUniqueId()), playerCurrentPage.get(player.getUniqueId()));
            } else if (event.getSlot() == 50) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(" "))
                    return;
                playerCurrentPage.replace(player.getUniqueId(), playerCurrentPage.get(player.getUniqueId()) + 1);
                player.closeInventory();
                SellGui.openPagedCategoryMenu(player, playerCurrentCategory.get(player.getUniqueId()), playerCurrentPage.get(player.getUniqueId()));
            } else if (ConstantSlots.itemSlot.contains(event.getSlot())) {
                player.closeInventory();
                playerCurrentCategory.remove(player.getUniqueId());
                playerCurrentPage.remove(player.getUniqueId());
                if (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR))) {
                    playerItemSellAmount.put(player.getUniqueId(), event.getCurrentItem());//TODO TUTAJ!!!!!!!!!!!!!!!!!!!!!!!!!!
                    ConfirmSellGui.openConfirmSellGui(player, event.getCurrentItem());
                }

            }
        } else if (player.hasMetadata(SELL_GUI_SELL_CONFIRM)) {
            event.setCancelled(true);

            if (event.getSlot() == 16) {
                player.closeInventory();
                sellItemAmountStack.put(player.getUniqueId(), 0);
                ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
            }

            if (event.getSlot() == 50) {

                ArrayList<ItemStack> itemList;

                if (!playerPriceItemList.containsKey(player.getUniqueId())) {
                    itemList = new ArrayList<>();
                } else {
                    itemList = playerPriceItemList.get(player.getUniqueId());

                    if (itemList.size() == 5) {
                        player.sendMessage(ChatColor.RED + "Osiągnieto limit cennika, potwierdz sprzedaż!");
                        return;
                    }
                }

                int amount = playerItemSellAmount.get(player.getUniqueId()).getAmount();

                if (amount == 0) player.sendMessage(ChatColor.RED + "Nie wybrano żadnej wartości");
                else {

                    UUID playerUUID = player.getUniqueId();

                    if (!sumPlayerItemAmount.containsKey(player.getUniqueId()))
                        sumPlayerItemAmount.put(playerUUID, amount);
                    else
                        sumPlayerItemAmount.replace(playerUUID, sumPlayerItemAmount.get(playerUUID) + amount);

                    int currentSumAmount = sumPlayerItemAmount.get(playerUUID);

                    if (sumPlayerItemAmount.get(playerUUID) == MAX_ITEM_AMOUNT) {

                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN + "Poprawnie dodałeś item na rynek :)");

                        itemList.add(playerItemSellAmount.get(playerUUID));
                        if (playerPriceItemList.containsKey(playerUUID)) {
                            playerPriceItemList.replace(playerUUID, itemList);
                        } else playerPriceItemList.put(playerUUID, itemList);

                        SellGui.openSellGui(player);

                    } else if (sumPlayerItemAmount.get(playerUUID) < MAX_ITEM_AMOUNT) {

                        player.sendMessage(ChatColor.YELLOW + "Dodałeś jakaś część na rynek");
                        player.closeInventory();

                        itemList.add(playerItemSellAmount.get(playerUUID));
                        if (playerPriceItemList.containsKey(playerUUID)) {
                            playerPriceItemList.replace(playerUUID, itemList);
                        } else playerPriceItemList.put(playerUUID, itemList);

                        SellGui.openSellGui(player);

                    } else {

                        int tooMuchAmount = currentSumAmount - MAX_ITEM_AMOUNT;
                        player.sendMessage(ChatColor.RED + "Przekroczono limit o " + tooMuchAmount);
                        sumPlayerItemAmount.replace(playerUUID, currentSumAmount - amount);
                        player.closeInventory();
                        SellGui.openSellGui(player);

                    }

                }


            } //TODO BUTTON CONFIRM

            if (event.getSlot() == 48) {
                player.closeInventory();
                playerItemSellAmount.remove(player.getUniqueId());
                SellGui.openSellGui(player);
            } else if (event.getSlot() == 30 || event.getSlot() == 31 || event.getSlot() == 32 ||
                    event.getSlot() == 39 || event.getSlot() == 40 || event.getSlot() == 41) {
                //TODO ZMIANA EKWIPUNKU NA DODAWANIE I ODEJMOWANIEe
                if (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR))) {
                    player.closeInventory();


                    if (event.getSlot() == 30) {
                        if (!sellItemAmount.containsKey(player.getUniqueId()))
                            sellItemAmount.put(player.getUniqueId(), 1);
                        else
                            sellItemAmount.replace(player.getUniqueId(), 1);
                    }

                    if (event.getSlot() == 31) {
                        if (!sellItemAmount.containsKey(player.getUniqueId()))
                            sellItemAmount.put(player.getUniqueId(), 8);
                        else
                            sellItemAmount.replace(player.getUniqueId(), 8);
                    }

                    if (event.getSlot() == 32) {
                        if (!sellItemAmount.containsKey(player.getUniqueId()))
                            sellItemAmount.put(player.getUniqueId(), 32);
                        else
                            sellItemAmount.replace(player.getUniqueId(), 32);
                    }

                    if (event.getSlot() == 39) {
                        if (!sellItemAmount.containsKey(player.getUniqueId()))
                            sellItemAmount.put(player.getUniqueId(), 2);
                        else
                            sellItemAmount.replace(player.getUniqueId(), 2);
                    }

                    if (event.getSlot() == 40) {
                        if (!sellItemAmount.containsKey(player.getUniqueId()))
                            sellItemAmount.put(player.getUniqueId(), 16);
                        else
                            sellItemAmount.replace(player.getUniqueId(), 16);
                    }

                    if (event.getSlot() == 41) {
                        if (!sellItemAmount.containsKey(player.getUniqueId()))
                            sellItemAmount.put(player.getUniqueId(), 48);
                        else
                            sellItemAmount.replace(player.getUniqueId(), 48);
                    }

                    ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), true);
                }
            }


        } else if (player.hasMetadata(SELL_GUI_SELL_CONFIRM_MINUS)) { //TODO AFTER FIRST CLICK ADD
            event.setCancelled(true);

            if (event.getSlot() == 48) {
                player.closeInventory();
                playerItemSellAmount.remove(player.getUniqueId());
                sellItemAmount.remove(player.getUniqueId());
                SellGui.openSellGui(player);
            }

            if (event.getSlot() == 50) {

                ArrayList<ItemStack> itemList;

                if (!playerPriceItemList.containsKey(player.getUniqueId())) {
                    itemList = new ArrayList<>();
                } else {
                    itemList = playerPriceItemList.get(player.getUniqueId());

                    if (itemList.size() == 5) {
                        player.sendMessage(ChatColor.RED + "Osiągnieto limit cennika, potwierdz sprzedaż!");
                        return;
                    }
                }

                int amount = playerItemSellAmount.get(player.getUniqueId()).getAmount();

                if (amount == 0) player.sendMessage(ChatColor.RED + "Nie wybrano żadnej wartości");
                else {

                    UUID playerUUID = player.getUniqueId();

                    if (!sumPlayerItemAmount.containsKey(player.getUniqueId()))
                        sumPlayerItemAmount.put(playerUUID, amount);
                    else
                        sumPlayerItemAmount.replace(playerUUID, sumPlayerItemAmount.get(playerUUID) + amount);

                    int currentSumAmount = sumPlayerItemAmount.get(playerUUID);

                    if (sumPlayerItemAmount.get(playerUUID) == MAX_ITEM_AMOUNT) {

                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN + "Poprawnie dodałeś item na rynek :)");

                        itemList.add(playerItemSellAmount.get(playerUUID));

                        if (playerPriceItemList.containsKey(playerUUID)) {
                            playerPriceItemList.replace(playerUUID, itemList);
                        } else playerPriceItemList.put(playerUUID, itemList);


                        SellGui.openSellGui(player);

                    } else if (sumPlayerItemAmount.get(playerUUID) < MAX_ITEM_AMOUNT) {

                        player.sendMessage(ChatColor.YELLOW + "Dodałeś jakaś część na rynek");
                        player.closeInventory();

                        itemList.add(playerItemSellAmount.get(playerUUID));
                        if (playerPriceItemList.containsKey(playerUUID)) {
                            playerPriceItemList.replace(playerUUID, itemList);
                        } else playerPriceItemList.put(playerUUID, itemList);

                        SellGui.openSellGui(player);

                    } else {

                        int tooMuchAmount = currentSumAmount - MAX_ITEM_AMOUNT;
                        player.sendMessage(ChatColor.RED + "Przekroczono limit o " + tooMuchAmount);
                        sumPlayerItemAmount.replace(playerUUID, currentSumAmount - amount);
                        player.closeInventory();
                        SellGui.openSellGui(player);

                    }

                }


            } //TODO BUTTON CONFIRM

            if (event.getSlot() == 16) {
                player.closeInventory();
                sellItemAmountStack.put(player.getUniqueId(), 0);
                ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
            }

            //TODO MINUS ----------------------------------------------------------------------------------------------------------------------
            if (event.getSlot() == 28 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), -1);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 29 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), -8);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }


            if (event.getSlot() == 30 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), -32);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }


            if (event.getSlot() == 37 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), -2);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 38 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), -16);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 39 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), -48);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }
            //TODO END MINUS ----------------------------------------------------------------------------------------------------------------------

            //TODO PLUS PLUS ----------------------------------------------------------------------------------------------------------------------
            if (event.getSlot() == 32 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), 1);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 41 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), 2);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 33 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), 8);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 42 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), 16);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 34 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), 32);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }

            if (event.getSlot() == 43 && (event.getCurrentItem() != null || !event.getCurrentItem().isSimilar(new ItemStack(Material.AIR)))) {
                player.closeInventory();
                sellItemAmount.replace(player.getUniqueId(), 48);
                ConfirmSellGui.openConfirmSellGuiMinus(player, playerItemSellAmount.get(player.getUniqueId()), false);
            }
            //TODO END PLUS ----------------------------------------------------------------------------------------------------------------------


        } else if (player.hasMetadata(SELL_GUI_SELL_CONFIRM_STACK)) { //TODO STACKS !!!!!!!!!
            event.setCancelled(true);

            if (event.getSlot() == 48) {
                player.closeInventory();
                playerItemSellAmount.remove(player.getUniqueId());
                sellItemAmount.remove(player.getUniqueId());
                sellItemAmountStack.remove(player.getUniqueId());
                SellGui.openSellGui(player);
            }

            if (event.getSlot() == 50) {

                ArrayList<ItemStack> itemList;

                if (!playerPriceItemList.containsKey(player.getUniqueId())) {
                    itemList = new ArrayList<>();
                } else {
                    itemList = playerPriceItemList.get(player.getUniqueId());

                    if (itemList.size() == 5) {
                        player.sendMessage(ChatColor.RED + "Osiągnieto limit cennika, potwierdz sprzedaż!");
                        return;
                    }
                }

                UUID playerUUID = player.getUniqueId();

                if (sellItemAmountStack.getOrDefault(playerUUID, 0) == 0) {
                    player.sendMessage(ChatColor.RED + "Nie wybrano żadnej wartości!");
                } else {
                    int amount = sellItemAmountStack.get(playerUUID) * 64;

                    if (!sumPlayerItemAmount.containsKey(playerUUID)) {
                        sumPlayerItemAmount.put(playerUUID, amount);

                    } else {
                        sumPlayerItemAmount.replace(playerUUID, sumPlayerItemAmount.get(playerUUID) + amount);
                    }

                    int currentSumAmount = sumPlayerItemAmount.get(playerUUID);

                    if (sumPlayerItemAmount.get(playerUUID) == MAX_ITEM_AMOUNT) {

                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN + "Poprawnie dodałeś item na rynek :)");

                        if (sellItemStack.containsKey(playerUUID)) {
                            ItemStack itemToReplace = sellItemStack.get(playerUUID);
                            itemToReplace.setAmount(amount);

                            itemList.add(itemToReplace);

                            if (playerPriceItemList.containsKey(playerUUID)) {
                                playerPriceItemList.replace(playerUUID, itemList);
                            } else playerPriceItemList.put(playerUUID, itemList);
                            sellItemStack.remove(player.getUniqueId());
                        }


                        //sumPlayerItemAmount.remove(playerUUID);
                        SellGui.openSellGui(player);

                    } else if (sumPlayerItemAmount.get(playerUUID) < MAX_ITEM_AMOUNT) {

                        player.sendMessage(ChatColor.YELLOW + "Dodałeś jakaś część na rynek");
                        player.closeInventory();

                        if (sellItemStack.containsKey(playerUUID)) {
                            ItemStack itemToReplace = sellItemStack.get(playerUUID);
                            itemToReplace.setAmount(amount);

                            itemList.add(itemToReplace);

                            if (playerPriceItemList.containsKey(playerUUID)) {
                                playerPriceItemList.replace(playerUUID, itemList);
                            } else playerPriceItemList.put(playerUUID, itemList);
                            sellItemStack.remove(player.getUniqueId());
                        }

                        SellGui.openSellGui(player);

                    } else {

                        int tooMuchAmount = currentSumAmount - MAX_ITEM_AMOUNT;
                        player.sendMessage(ChatColor.RED + "Przekroczono limit o " + tooMuchAmount);
                        sumPlayerItemAmount.replace(playerUUID, currentSumAmount - amount);
                        player.closeInventory();
                        SellGui.openSellGui(player);

                    }


                }

            } //TODO BUTTON CONFIRM


            if (event.getSlot() == 21) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 1);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }

            if (event.getSlot() == 22) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 2);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }

            if (event.getSlot() == 23) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 3);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }


            if (event.getSlot() == 30) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 4);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }

            if (event.getSlot() == 31) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 5);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }

            if (event.getSlot() == 32) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 6);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }


            if (event.getSlot() == 39) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 7);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }
            }

            if (event.getSlot() == 40) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 8);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }

            }

            if (event.getSlot() == 41) {
                if (event.getCurrentItem() != null) {
                    sellItemAmountStack.replace(player.getUniqueId(), sellItemAmountStack.get(player.getUniqueId()) + 9);
                    ConfirmSellGui.openStackConfirm(player, playerItemSellAmount.get(player.getUniqueId()));
                }
            }


        } else if (player.hasMetadata(MARKET_SELL_FINAL_CONFIRM)) {
            event.setCancelled(true);

            if (event.getSlot() == 12) {
                player.closeInventory();
                SellGui.openSellGui(player);
            }

            if (event.getSlot() == 14) {
                player.closeInventory();

                player.sendTitle(ChatColor.GREEN + "Pomyślnie dodałeś item na rynek", "", 10, 35, 20);

                MarketGui.placeItemIntoMarket(player);

                Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> {
                    FinalConfirmGUI.removeExactAmountOfPutedItem(player);
                }, 1L);

            }
        }

    }

    public boolean isCategorySlot(int slot) {
        return ConstantSlots.categorySlot.contains(slot);
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata(MARKET_DEAFAULT)) {
            player.removeMetadata(MARKET_DEAFAULT, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(SELL_GUI)) {
            player.removeMetadata(SELL_GUI, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(SELL_GUI_CATEGORY)) {
            player.removeMetadata(SELL_GUI_CATEGORY, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(SELL_GUI_SELL_CONFIRM)) {
            player.removeMetadata(SELL_GUI_SELL_CONFIRM, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(SELL_GUI_SELL_CONFIRM_MINUS)) {
            player.removeMetadata(SELL_GUI_SELL_CONFIRM_MINUS, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(SELL_GUI_SELL_CONFIRM_STACK)) {
            player.removeMetadata(SELL_GUI_SELL_CONFIRM_STACK, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(MARKET_SELL_FINAL_CONFIRM)) {
            player.removeMetadata(MARKET_SELL_FINAL_CONFIRM, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(MARKET_BUY_CONFIRM)) {
            player.removeMetadata(MARKET_BUY_CONFIRM, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(MARKET_COLLECT_ITEMS)) {
            player.removeMetadata(MARKET_COLLECT_ITEMS, HeartStealPlugin.getInstance());
        }

        if (player.hasMetadata(MARKET_COLLECT_EXPIRED_ITEMS)) {
            player.removeMetadata(MARKET_COLLECT_EXPIRED_ITEMS, HeartStealPlugin.getInstance());
        }



        Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> {
            if (!hasPlayerAnyMetaData(player)) {

                playerPriceItemList.remove(player.getUniqueId());
                sumPlayerItemAmount.remove(player.getUniqueId());
                playerCurrentMarketPage.remove(player.getUniqueId());
                playerSlotWantToBuy.remove(player.getUniqueId());
            }

        }, 5L);


    }

    private boolean hasPlayerAnyMetaData(Player player) {
        return Arrays.stream(new String[]{MARKET_DEAFAULT,MARKET_COLLECT_EXPIRED_ITEMS, MARKET_COLLECT_ITEMS, MARKET_BUY_CONFIRM, MARKET_SELL_FINAL_CONFIRM, SELL_GUI, SELL_GUI_CATEGORY, SELL_GUI_SELL_CONFIRM, SELL_GUI_SELL_CONFIRM_MINUS, SELL_GUI_SELL_CONFIRM_STACK})
                .anyMatch(player::hasMetadata);
    }


    public static ArrayList<Integer> generateItemSlots() {
        ArrayList<Integer> slots = new ArrayList<>();

        for (int i = 10; i < 17; i++) {
            slots.add(i);
        }

        for (int i = 19; i < 26; i++) {
            slots.add(i);
        }

        for (int i = 28; i < 35; i++) {
            slots.add(i);
        }

        for (int i = 37; i < 44; i++) {
            slots.add(i);
        }


        return slots;
    }


}
