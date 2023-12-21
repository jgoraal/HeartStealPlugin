package jgor.heartstealplugin.auctions;


import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class ConfirmSellGui {


    public static void openConfirmSellGui(Player player, ItemStack chosenItem) {
        Inventory confirmSellMenu = Bukkit.createInventory(player, 9 * 6, SellGui.Title + " - " + ChatColor.RESET + ChatColor.BOLD + ChatColor.GREEN + "Potwierdzenie");

        SellGui.schematicGuiDesign(confirmSellMenu);
        SellGui.placeStatusItem(confirmSellMenu, player);
        createBottomConfirmSellMenu(confirmSellMenu);
        createCustomStackValueItem(confirmSellMenu);

        confirmSellMenu.setItem(13, chosenItem);

        createPlusButtons(confirmSellMenu);

        player.openInventory(confirmSellMenu);
        player.setMetadata(AuctionListeners.SELL_GUI_SELL_CONFIRM, new FixedMetadataValue(HeartStealPlugin.getInstance(), confirmSellMenu));
    }

    private static void createCustomStackValueItem(Inventory confirmSellMenu) {
        ItemStack customValueStackItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = customValueStackItem.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Ustaw wartość w stakach");
        customValueStackItem.setItemMeta(meta);

        confirmSellMenu.setItem(16, customValueStackItem);
    }


    public static void openConfirmSellGuiMinus(Player player, ItemStack item, boolean flag) {
        Inventory confirmSellMenuMinus = Bukkit.createInventory(player, 9 * 6, SellGui.Title + " - " + ChatColor.RESET + ChatColor.BOLD + ChatColor.GREEN + "Potwierdzenie");

        SellGui.schematicGuiDesign(confirmSellMenuMinus);
        SellGui.placeStatusItem(confirmSellMenuMinus, player);
        createBottomConfirmSellMenu(confirmSellMenuMinus);
        createCustomStackValueItem(confirmSellMenuMinus);

        createPlusButtonsWithMinus(player, confirmSellMenuMinus);

        confirmSellMenuMinus.setItem(13, changeItemAmount(player, item));

        player.openInventory(confirmSellMenuMinus);
        player.setMetadata(AuctionListeners.SELL_GUI_SELL_CONFIRM_MINUS, new FixedMetadataValue(HeartStealPlugin.getInstance(), confirmSellMenuMinus));
    }

    private static ItemStack changeItemAmount(Player player, ItemStack item) {
        int amount = item.getAmount();

        item.setAmount(amount + AuctionListeners.sellItemAmount.get(player.getUniqueId()));

        AuctionListeners.playerItemSellAmount.replace(player.getUniqueId(), item);

        return item;
    }


    public static void createBottomConfirmSellMenu(Inventory menu) {
        ItemStack cancel = new ItemStack(Material.RED_CONCRETE);
        ItemMeta cancelMeta = cancel.getItemMeta();

        cancelMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Anuluj");
        cancel.setItemMeta(cancelMeta);

        menu.setItem(48, cancel);


        ItemStack confirm = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta confirmMeta = cancel.getItemMeta();

        confirmMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Potwierdź");
        confirm.setItemMeta(confirmMeta);


        menu.setItem(50, confirm);


        ItemStack w = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = w.getItemMeta();
        meta.setDisplayName(" ");
        w.setItemMeta(meta);

        menu.setItem(46, w);
        menu.setItem(47, w);
        menu.setItem(51, w);
        menu.setItem(52, w);


    }


    public static void createPlusButtons(Inventory menu) {
        ItemStack add = new ItemStack(Material.LIME_BANNER);

        ItemMeta meta = add.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "+ 1");
        add.setItemMeta(meta);
        menu.setItem(30, add);

        meta.setDisplayName(ChatColor.GREEN + "+ 2");
        add.setItemMeta(meta);
        menu.setItem(39, add);

        meta.setDisplayName(ChatColor.GREEN + "+ 8");
        add.setItemMeta(meta);
        menu.setItem(31, add);

        meta.setDisplayName(ChatColor.GREEN + "+ 16");
        add.setItemMeta(meta);
        menu.setItem(40, add);

        meta.setDisplayName(ChatColor.GREEN + "+ 32");
        add.setItemMeta(meta);
        menu.setItem(32, add);

        meta.setDisplayName(ChatColor.GREEN + "+ 48");
        add.setItemMeta(meta);
        menu.setItem(41, add);
    }


    public static void createPlusButtonsWithMinus(Player player, Inventory menu) {
        ItemStack add = new ItemStack(Material.LIME_BANNER);

        ItemMeta meta = add.getItemMeta();

        int amount = AuctionListeners.playerItemSellAmount.get(player.getUniqueId()).getAmount() + AuctionListeners.sellItemAmount.get(player.getUniqueId());


        if (amount + 1 <= 64) {
            meta.setDisplayName(ChatColor.GREEN + "+ 1");
            add.setItemMeta(meta);
            menu.setItem(32, add);
        }


        if (amount + 2 <= 64) {
            meta.setDisplayName(ChatColor.GREEN + "+ 2");
            add.setItemMeta(meta);
            menu.setItem(41, add);
        }


        if (amount + 8 <= 64) {
            meta.setDisplayName(ChatColor.GREEN + "+ 8");
            add.setItemMeta(meta);
            menu.setItem(33, add);
        }


        if (amount + 16 <= 64) {
            meta.setDisplayName(ChatColor.GREEN + "+ 16");
            add.setItemMeta(meta);
            menu.setItem(42, add);
        }


        if (amount + 32 <= 64) {
            meta.setDisplayName(ChatColor.GREEN + "+ 32");
            add.setItemMeta(meta);
            menu.setItem(34, add);
        }


        if (amount + 48 <= 64) {
            meta.setDisplayName(ChatColor.GREEN + "+ 48");
            add.setItemMeta(meta);
            menu.setItem(43, add);
        }


        ItemStack minus = new ItemStack(Material.RED_BANNER);
        ItemMeta meta1 = minus.getItemMeta();


        if (amount - 1 >= 1) {
            meta1.setDisplayName(ChatColor.RED + "- 1");
            minus.setItemMeta(meta1);
            menu.setItem(28, minus);
        }


        if (amount - 2 >= 1) {
            meta1.setDisplayName(ChatColor.RED + "- 2");
            minus.setItemMeta(meta1);
            menu.setItem(37, minus);
        }

        if (amount - 8 >= 1) {
            meta1.setDisplayName(ChatColor.RED + "- 8");
            minus.setItemMeta(meta1);
            menu.setItem(29, minus);
        }


        if (amount - 16 >= 1) {
            meta1.setDisplayName(ChatColor.RED + "- 16");
            minus.setItemMeta(meta1);
            menu.setItem(38, minus);
        }


        if (amount - 32 >= 1) {
            meta1.setDisplayName(ChatColor.RED + "- 32");
            minus.setItemMeta(meta1);
            menu.setItem(30, minus);
        }


        if (amount - 48 >= 1) {
            meta1.setDisplayName(ChatColor.RED + "- 48");
            minus.setItemMeta(meta1);
            menu.setItem(39, minus);
        }


    }

    public static void openStackConfirm(Player player, ItemStack itemStack) {
        Inventory confirmSellMenuStack;
        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) == 0) {
            confirmSellMenuStack = Bukkit.createInventory(player, 9 * 6, SellGui.Title + " - " + ChatColor.RESET + ChatColor.BOLD + ChatColor.GREEN + "Potwierdzenie");
        } else {
            confirmSellMenuStack = Bukkit.createInventory(player, 9 * 6, SellGui.Title + " - " + ChatColor.RESET + ChatColor.BOLD + ChatColor.GREEN + "Potwierdzenie - wybrano " + AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + "x64");

        }

        if (!AuctionListeners.sellItemStack.containsKey(player.getUniqueId())) {
            ItemStack item = AuctionListeners.playerItemSellAmount.get(player.getUniqueId());
            item.setAmount(0);
            AuctionListeners.sellItemStack.put(player.getUniqueId(), item);
        }

        SellGui.schematicGuiDesign(confirmSellMenuStack);
        SellGui.placeStatusItem(confirmSellMenuStack, player);
        createBottomConfirmSellMenu(confirmSellMenuStack);

        itemStack.setAmount(64);

        confirmSellMenuStack.setItem(13, itemStack);

        createStackMenu(confirmSellMenuStack, player);

        player.openInventory(confirmSellMenuStack);
        player.setMetadata(AuctionListeners.SELL_GUI_SELL_CONFIRM_STACK, new FixedMetadataValue(HeartStealPlugin.getInstance(), confirmSellMenuStack));
    }


    public static void createStackMenu(Inventory menu, Player player) {
        ItemStack stack = new ItemStack(Material.HONEYCOMB);

        ItemMeta meta = stack.getItemMeta();
        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 1 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "1 stak");
            stack.setItemMeta(meta);
            menu.setItem(21, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 2 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "2 staki");
            stack.setItemMeta(meta);
            stack.setAmount(2);
            menu.setItem(22, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 3 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "3 staki");
            stack.setItemMeta(meta);
            stack.setAmount(3);
            menu.setItem(23, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 4 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "4 staki");
            stack.setItemMeta(meta);
            stack.setAmount(4);
            menu.setItem(30, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 5 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "5 staków");
            stack.setItemMeta(meta);
            stack.setAmount(5);
            menu.setItem(31, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 6 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "6 staków");
            stack.setItemMeta(meta);
            stack.setAmount(6);
            menu.setItem(32, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 7 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "7 staków");
            stack.setItemMeta(meta);
            stack.setAmount(7);
            menu.setItem(39, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 8 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "8 staków");
            stack.setItemMeta(meta);
            stack.setAmount(8);
            menu.setItem(40, stack);
        }

        if (AuctionListeners.sellItemAmountStack.get(player.getUniqueId()) + 9 <= 36) {
            meta.setDisplayName(ChatColor.AQUA + "9 staków");
            stack.setItemMeta(meta);
            stack.setAmount(9);
            menu.setItem(41, stack);
        }

    }
}
