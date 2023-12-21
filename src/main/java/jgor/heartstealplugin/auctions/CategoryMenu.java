package jgor.heartstealplugin.auctions;


import jgor.heartstealplugin.HeartItem;
import jgor.heartstealplugin.HeartStealPlugin;
import jgor.heartstealplugin.RedemptionItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;


import java.util.HashMap;



public class CategoryMenu {

    public static final HashMap<String, Integer> maxPageCount = new HashMap<>();


    public static void openCategoryMenu(Player player, String categoryName, int page) {
        setMaxPageCount();
        Inventory categoryMenu = Bukkit.createInventory(player, 9 * 6, SellGui.Title + " - " + categoryName + " " + ChatColor.RESET + ChatColor.GRAY + page + " / " + maxPageCount.get(categoryName));

        if (page == 1) {
            putItemsInCorrectCategory(categoryName, categoryMenu, page);

            SellGui.schematicGuiDesign(categoryMenu);
            SellGui.createBottomNotPaged(categoryMenu, false, true);
            SellGui.placeStatusItem(categoryMenu, player);

            player.openInventory(categoryMenu);
            player.setMetadata(AuctionListeners.SELL_GUI_CATEGORY, new FixedMetadataValue(HeartStealPlugin.getInstance(), categoryMenu));
        } else if (page > 1 && page < maxPageCount.get(categoryName)) {
            putItemsInCorrectCategory(categoryName, categoryMenu, page);

            SellGui.schematicGuiDesign(categoryMenu);
            SellGui.createBottomNotPaged(categoryMenu, true, true);
            SellGui.placeStatusItem(categoryMenu, player);

            player.openInventory(categoryMenu);
            player.setMetadata(AuctionListeners.SELL_GUI_CATEGORY, new FixedMetadataValue(HeartStealPlugin.getInstance(), categoryMenu));
        } else if (page == maxPageCount.get(categoryName)) {
            putItemsInCorrectCategory(categoryName, categoryMenu, page);

            SellGui.schematicGuiDesign(categoryMenu);
            SellGui.createBottomNotPaged(categoryMenu, true, false);
            SellGui.placeStatusItem(categoryMenu, player);

            player.openInventory(categoryMenu);
            player.setMetadata(AuctionListeners.SELL_GUI_CATEGORY, new FixedMetadataValue(HeartStealPlugin.getInstance(), AuctionListeners.SELL_GUI_CATEGORY));
        }

    }


    public static void putItemsInCorrectCategory(String category, Inventory menu, int page) {

        if (category.equals(ChatColor.BLUE + "Naturalne Bloki")) {
            if (page == 1) {
                naturalFirstPage(menu);
            } else if (page == 2) {
                naturalSecondPage(menu);
            } else if (page == 3) {
                naturalThridPage(menu);
            } else if (page == 4) {
                naturalFourthPage(menu);
            } else if (page == 5) {
                naturalFifthPage(menu);
            } else if (page == 6) {
                naturalSixthhPage(menu);
            } else if (page == 7) {
                naturalSeventhPage(menu);
            }
        } else if (category.equals(ChatColor.BLUE + "Budowlane Bloki")) {
            if (page == 1) {
                buildFirstPage(menu);
            } else if (page == 2) {
                buildSecondPage(menu);
            } else if (page == 3) {
                buildThirdPage(menu);
            } else if (page == 4) {
                buildFouthPage(menu);
            } else if (page == 5) {
                buildFifthPage(menu);
            } else if (page == 6) {
                buildSixthPage(menu);
            } else if (page == 7) {
                buildSeventhPage(menu);
            } else if (page == 8) {
                buildEighthPage(menu);
            } else if (page == 9) {
                buildNinthPage(menu);
            } else if (page == 10) {
                buildTenthPage(menu);
            } else if (page == 11) {
                buildEleventhPage(menu);
            } else if (page == 12) {
                buildTwelfthPage(menu);
            }
        } else if (category.equals(ChatColor.BLUE + "Walka")) { //TODO WALKA !!!!!!
            if (page == 1) {
                combatFirstPage(menu);
            } else if (page == 2) {
                combatSecondPage(menu);
            } else if (page == 3) {
                combatThirdPage(menu);
            } else if (page == 4) {
                combatFourthPage(menu);
            } else if (page == 5) {
                combatFifthPage(menu);
            }
        } else if (category.equals(ChatColor.BLUE + "Mechanizmy")) {
            if (page == 1) {
                redstoneFirstPage(menu);
            } else if (page == 2) {
                redstoneSecondPage(menu);
            }
        } else if (category.equals(ChatColor.BLUE + "Jedzenie")) {
            if (page == 1) {
                foodFirstPage(menu);
            } else if (page == 2) {
                foodSecondPage(menu);
            }

        } else if (category.equals(ChatColor.BLUE + "Surowce")) {
            if (page == 1) {
                materialsFirstPage(menu);
            } else if (page == 2) {
                materialsSecondPage(menu);
            } else if (page == 3) {
                materialsThirdPage(menu);
            } else if (page == 4) {
                materialsFouthPage(menu);
            } else if (page == 5) {
                materialsFifthPage(menu);
            } else if (page == 6) {
                materialsSixthPage(menu);
            } else if (page == 7) {
                materialsSeventhPage(menu);
            }
        } else if (category.equals(ChatColor.BLUE + "Kolorowe Bloki")) {
            if (page == 1) {
                colorFirstPage(menu);
            } else if (page == 2) {
                colorSecondPage(menu);
            } else if (page == 3) {
                colorThirdPage(menu);
            } else if (page == 4) {
                colorFouthPage(menu);
            } else if (page == 5) {
                colorFifthPage(menu);
            } else if (page == 6) {
                colorSixthPage(menu);
            } else if (page == 7) {
                colorSeventhPage(menu);
            } else if (page == 8) {
                colorEightPage(menu);
            } else if (page == 9) {
                colorNinthPage(menu);
            }
        } else if (category.equals(ChatColor.BLUE + "Specjalne Itemy")) {
            if (page == 1) {
                specialFirstPage(menu);
            }

        } else if (category.equals(ChatColor.BLUE + "Funkcjonalne Bloki")) {
            if (page == 1) {
                funcFirstPage(menu);
            } else if (page == 2) {
                funcSecondPage(menu);
            } else if (page == 3) {
                funcThirdPage(menu);
            } else if (page == 4) {
                funcFourthPage(menu);
            }
        }


    }



    private static void specialFirstPage(Inventory menu) {
        menu.setItem(10, HeartItem.createHeartItem());
        menu.setItem(11, RedemptionItem.createRedemptionItem());
    }

    private static void colorNinthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.WHITE_BANNER));
        menu.setItem(11, new ItemStack(Material.LIGHT_GRAY_BANNER));
        menu.setItem(12, new ItemStack(Material.GRAY_BANNER));
        menu.setItem(13, new ItemStack(Material.BLACK_BANNER));
        menu.setItem(14, new ItemStack(Material.BROWN_BANNER));
        menu.setItem(15, new ItemStack(Material.RED_BANNER));
        menu.setItem(16, new ItemStack(Material.ORANGE_BANNER));

        menu.setItem(19, new ItemStack(Material.YELLOW_BANNER));
        menu.setItem(20, new ItemStack(Material.LIME_BANNER));
        menu.setItem(21, new ItemStack(Material.GREEN_BANNER));
        menu.setItem(22, new ItemStack(Material.CYAN_BANNER));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_BANNER));
        menu.setItem(24, new ItemStack(Material.BLUE_BANNER));
        menu.setItem(25, new ItemStack(Material.PURPLE_BANNER));

        menu.setItem(28, new ItemStack(Material.MAGENTA_BANNER));
        menu.setItem(29, new ItemStack(Material.PINK_BANNER));
    }

    private static void colorEightPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.CANDLE));
        menu.setItem(11, new ItemStack(Material.WHITE_CANDLE));
        menu.setItem(12, new ItemStack(Material.LIGHT_GRAY_CANDLE));
        menu.setItem(13, new ItemStack(Material.GRAY_CANDLE));
        menu.setItem(14, new ItemStack(Material.BLACK_CANDLE));
        menu.setItem(15, new ItemStack(Material.BROWN_CANDLE));
        menu.setItem(16, new ItemStack(Material.RED_CANDLE));

        menu.setItem(19, new ItemStack(Material.ORANGE_CANDLE));
        menu.setItem(20, new ItemStack(Material.YELLOW_CANDLE));
        menu.setItem(21, new ItemStack(Material.LIME_CANDLE));
        menu.setItem(22, new ItemStack(Material.GREEN_CANDLE));
        menu.setItem(23, new ItemStack(Material.CYAN_CANDLE));
        menu.setItem(24, new ItemStack(Material.LIGHT_BLUE_CANDLE));
        menu.setItem(25, new ItemStack(Material.BLUE_CANDLE));

        menu.setItem(28, new ItemStack(Material.PURPLE_CANDLE));
        menu.setItem(29, new ItemStack(Material.MAGENTA_CANDLE));
        menu.setItem(29, new ItemStack(Material.PINK_CANDLE));
    }

    private static void colorSeventhPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.GLASS_PANE));
        menu.setItem(11, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        menu.setItem(12, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        menu.setItem(13, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        menu.setItem(14, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        menu.setItem(15, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        menu.setItem(16, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE));

        menu.setItem(19, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
        menu.setItem(20, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        menu.setItem(21, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
        menu.setItem(22, new ItemStack(Material.CYAN_STAINED_GLASS_PANE));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        menu.setItem(24, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
        menu.setItem(25, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));

        menu.setItem(28, new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        menu.setItem(29, new ItemStack(Material.PINK_STAINED_GLASS_PANE));
        menu.setItem(30, new ItemStack(Material.BROWN_STAINED_GLASS_PANE));
    }

    private static void colorSixthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.GLASS));
        menu.setItem(11, new ItemStack(Material.TINTED_GLASS));
        menu.setItem(12, new ItemStack(Material.WHITE_STAINED_GLASS));
        menu.setItem(13, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS));
        menu.setItem(14, new ItemStack(Material.GRAY_STAINED_GLASS));
        menu.setItem(15, new ItemStack(Material.BLACK_STAINED_GLASS));
        menu.setItem(16, new ItemStack(Material.BROWN_STAINED_GLASS));

        menu.setItem(19, new ItemStack(Material.RED_STAINED_GLASS));
        menu.setItem(20, new ItemStack(Material.ORANGE_STAINED_GLASS));
        menu.setItem(21, new ItemStack(Material.YELLOW_STAINED_GLASS));
        menu.setItem(22, new ItemStack(Material.CYAN_STAINED_GLASS));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));
        menu.setItem(24, new ItemStack(Material.BLUE_STAINED_GLASS));
        menu.setItem(25, new ItemStack(Material.LIME_STAINED_GLASS));

        menu.setItem(28, new ItemStack(Material.GREEN_STAINED_GLASS));
        menu.setItem(29, new ItemStack(Material.PURPLE_STAINED_GLASS));
        menu.setItem(30, new ItemStack(Material.MAGENTA_STAINED_GLASS));
        menu.setItem(31, new ItemStack(Material.PINK_STAINED_GLASS));
    }

    private static void colorFifthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.WHITE_GLAZED_TERRACOTTA));
        menu.setItem(11, new ItemStack(Material.LIGHT_GRAY_GLAZED_TERRACOTTA));
        menu.setItem(12, new ItemStack(Material.GRAY_GLAZED_TERRACOTTA));
        menu.setItem(13, new ItemStack(Material.BLACK_GLAZED_TERRACOTTA));
        menu.setItem(14, new ItemStack(Material.BROWN_GLAZED_TERRACOTTA));
        menu.setItem(15, new ItemStack(Material.RED_GLAZED_TERRACOTTA));
        menu.setItem(16, new ItemStack(Material.ORANGE_GLAZED_TERRACOTTA));

        menu.setItem(19, new ItemStack(Material.YELLOW_GLAZED_TERRACOTTA));
        menu.setItem(20, new ItemStack(Material.LIME_GLAZED_TERRACOTTA));
        menu.setItem(21, new ItemStack(Material.GREEN_GLAZED_TERRACOTTA));
        menu.setItem(22, new ItemStack(Material.CYAN_GLAZED_TERRACOTTA));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_GLAZED_TERRACOTTA));
        menu.setItem(24, new ItemStack(Material.BLUE_GLAZED_TERRACOTTA));
        menu.setItem(25, new ItemStack(Material.PURPLE_GLAZED_TERRACOTTA));

        menu.setItem(28, new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA));
        menu.setItem(29, new ItemStack(Material.PINK_GLAZED_TERRACOTTA));
    }

    private static void colorFouthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.WHITE_CONCRETE_POWDER));
        menu.setItem(11, new ItemStack(Material.LIGHT_GRAY_CONCRETE_POWDER));
        menu.setItem(12, new ItemStack(Material.GRAY_CONCRETE_POWDER));
        menu.setItem(13, new ItemStack(Material.BLACK_CONCRETE_POWDER));
        menu.setItem(14, new ItemStack(Material.BROWN_CONCRETE_POWDER));
        menu.setItem(15, new ItemStack(Material.RED_CONCRETE_POWDER));
        menu.setItem(16, new ItemStack(Material.ORANGE_CONCRETE_POWDER));

        menu.setItem(19, new ItemStack(Material.YELLOW_CONCRETE_POWDER));
        menu.setItem(20, new ItemStack(Material.LIME_CONCRETE_POWDER));
        menu.setItem(21, new ItemStack(Material.GREEN_CONCRETE_POWDER));
        menu.setItem(22, new ItemStack(Material.CYAN_CONCRETE_POWDER));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_CONCRETE_POWDER));
        menu.setItem(24, new ItemStack(Material.BLUE_CONCRETE_POWDER));
        menu.setItem(25, new ItemStack(Material.PURPLE_CONCRETE_POWDER));

        menu.setItem(28, new ItemStack(Material.MAGENTA_CONCRETE_POWDER));
        menu.setItem(29, new ItemStack(Material.PINK_CONCRETE_POWDER));
    }

    private static void colorThirdPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.WHITE_CONCRETE));
        menu.setItem(11, new ItemStack(Material.LIGHT_GRAY_CONCRETE));
        menu.setItem(12, new ItemStack(Material.GRAY_CONCRETE));
        menu.setItem(13, new ItemStack(Material.BLACK_CONCRETE));
        menu.setItem(14, new ItemStack(Material.BROWN_CONCRETE));
        menu.setItem(15, new ItemStack(Material.RED_CONCRETE));
        menu.setItem(16, new ItemStack(Material.ORANGE_CONCRETE));

        menu.setItem(19, new ItemStack(Material.YELLOW_CONCRETE));
        menu.setItem(20, new ItemStack(Material.LIME_CONCRETE));
        menu.setItem(21, new ItemStack(Material.GREEN_CONCRETE));
        menu.setItem(22, new ItemStack(Material.CYAN_CONCRETE));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_CONCRETE));
        menu.setItem(24, new ItemStack(Material.BLUE_CONCRETE));
        menu.setItem(25, new ItemStack(Material.PURPLE_CONCRETE));

        menu.setItem(28, new ItemStack(Material.MAGENTA_CONCRETE));
        menu.setItem(29, new ItemStack(Material.PINK_CONCRETE));
    }

    private static void colorSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.PURPLE_CARPET));
        menu.setItem(11, new ItemStack(Material.MAGENTA_CARPET));
        menu.setItem(12, new ItemStack(Material.PINK_CARPET));
        menu.setItem(13, new ItemStack(Material.TERRACOTTA));
        menu.setItem(14, new ItemStack(Material.WHITE_TERRACOTTA));
        menu.setItem(15, new ItemStack(Material.LIGHT_GRAY_TERRACOTTA));
        menu.setItem(16, new ItemStack(Material.GRAY_TERRACOTTA));

        menu.setItem(19, new ItemStack(Material.BLACK_TERRACOTTA));
        menu.setItem(20, new ItemStack(Material.BROWN_TERRACOTTA));
        menu.setItem(21, new ItemStack(Material.RED_TERRACOTTA));
        menu.setItem(22, new ItemStack(Material.ORANGE_TERRACOTTA));
        menu.setItem(23, new ItemStack(Material.YELLOW_TERRACOTTA));
        menu.setItem(24, new ItemStack(Material.LIME_TERRACOTTA));
        menu.setItem(25, new ItemStack(Material.GREEN_TERRACOTTA));

        menu.setItem(28, new ItemStack(Material.CYAN_TERRACOTTA));
        menu.setItem(29, new ItemStack(Material.LIGHT_BLUE_TERRACOTTA));
        menu.setItem(30, new ItemStack(Material.BLUE_TERRACOTTA));
        menu.setItem(31, new ItemStack(Material.PURPLE_TERRACOTTA));
        menu.setItem(32, new ItemStack(Material.MAGENTA_TERRACOTTA));
        menu.setItem(33, new ItemStack(Material.PINK_TERRACOTTA));
    }

    private static void colorFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.WHITE_WOOL));
        menu.setItem(11, new ItemStack(Material.LIGHT_GRAY_WOOL));
        menu.setItem(12, new ItemStack(Material.GRAY_WOOL));
        menu.setItem(13, new ItemStack(Material.BLACK_WOOL));
        menu.setItem(14, new ItemStack(Material.BROWN_WOOL));
        menu.setItem(15, new ItemStack(Material.RED_WOOL));
        menu.setItem(16, new ItemStack(Material.ORANGE_WOOL));

        menu.setItem(19, new ItemStack(Material.YELLOW_WOOL));
        menu.setItem(20, new ItemStack(Material.LIME_WOOL));
        menu.setItem(21, new ItemStack(Material.GREEN_WOOL));
        menu.setItem(22, new ItemStack(Material.CYAN_WOOL));
        menu.setItem(23, new ItemStack(Material.LIGHT_BLUE_WOOL));
        menu.setItem(24, new ItemStack(Material.BLUE_WOOL));
        menu.setItem(25, new ItemStack(Material.PURPLE_WOOL));

        menu.setItem(28, new ItemStack(Material.MAGENTA_WOOL));
        menu.setItem(29, new ItemStack(Material.WHITE_CARPET));
        menu.setItem(30, new ItemStack(Material.LIGHT_GRAY_CARPET));
        menu.setItem(31, new ItemStack(Material.GRAY_CARPET));
        menu.setItem(32, new ItemStack(Material.BLACK_CARPET));
        menu.setItem(33, new ItemStack(Material.BROWN_CARPET));
        menu.setItem(34, new ItemStack(Material.RED_CARPET));

        menu.setItem(37, new ItemStack(Material.ORANGE_CARPET));
        menu.setItem(38, new ItemStack(Material.YELLOW_CARPET));
        menu.setItem(39, new ItemStack(Material.LIME_CARPET));
        menu.setItem(40, new ItemStack(Material.GREEN_CARPET));
        menu.setItem(41, new ItemStack(Material.CYAN_CARPET));
        menu.setItem(42, new ItemStack(Material.LIGHT_BLUE_CARPET));
        menu.setItem(43, new ItemStack(Material.BLUE_CARPET));
    }

    private static void materialsSeventhPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.ENCHANTED_BOOK));
        menu.setItem(11, new ItemStack(Material.QUARTZ));
        menu.setItem(12, new ItemStack(Material.AMETHYST_SHARD));
        menu.setItem(13, new ItemStack(Material.STICK));
        menu.setItem(14, new ItemStack(Material.FLINT));
        menu.setItem(15, new ItemStack(Material.BONE));
        menu.setItem(16, new ItemStack(Material.BONE_MEAL));

        menu.setItem(19, new ItemStack(Material.STRING));
        menu.setItem(20, new ItemStack(Material.FEATHER));
        menu.setItem(21, new ItemStack(Material.SNOWBALL));
        menu.setItem(22, new ItemStack(Material.EGG));
        menu.setItem(23, new ItemStack(Material.LEATHER));
        menu.setItem(24, new ItemStack(Material.RABBIT_HIDE));
        menu.setItem(25, new ItemStack(Material.HONEYCOMB));

        menu.setItem(28, new ItemStack(Material.INK_SAC));
        menu.setItem(29, new ItemStack(Material.GLOW_INK_SAC));
        menu.setItem(30, new ItemStack(Material.SCUTE));
        menu.setItem(31, new ItemStack(Material.SLIME_BALL));
        menu.setItem(32, new ItemStack(Material.CLAY_BALL));
        menu.setItem(33, new ItemStack(Material.PRISMARINE_SHARD));
        menu.setItem(34, new ItemStack(Material.PRISMARINE_CRYSTALS));

        menu.setItem(37, new ItemStack(Material.NAUTILUS_SHELL));
        menu.setItem(38, new ItemStack(Material.HEART_OF_THE_SEA));
        menu.setItem(39, new ItemStack(Material.FIRE_CHARGE));
        menu.setItem(40, new ItemStack(Material.BLAZE_ROD));
        menu.setItem(41, new ItemStack(Material.NETHER_STAR));
        menu.setItem(42, new ItemStack(Material.ENDER_PEARL));
        menu.setItem(43, new ItemStack(Material.ENDER_EYE));
    }

    private static void materialsSixthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
        menu.setItem(11, new ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(12, new ItemStack(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(13, new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(14, new ItemStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(15, new ItemStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(16, new ItemStack(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE));

        menu.setItem(19, new ItemStack(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(20, new ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(21, new ItemStack(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(22, new ItemStack(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(23, new ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(24, new ItemStack(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(25, new ItemStack(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE));

        menu.setItem(28, new ItemStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(29, new ItemStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE));
        menu.setItem(30, new ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE));
    }

    private static void materialsFifthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.ANGLER_POTTERY_SHERD));
        menu.setItem(11, new ItemStack(Material.ARCHER_POTTERY_SHERD));
        menu.setItem(12, new ItemStack(Material.ARMS_UP_POTTERY_SHERD));
        menu.setItem(13, new ItemStack(Material.BLADE_POTTERY_SHERD));
        menu.setItem(14, new ItemStack(Material.BREWER_POTTERY_SHERD));
        menu.setItem(15, new ItemStack(Material.BURN_POTTERY_SHERD));
        menu.setItem(16, new ItemStack(Material.DANGER_POTTERY_SHERD));

        menu.setItem(19, new ItemStack(Material.EXPLORER_POTTERY_SHERD));
        menu.setItem(20, new ItemStack(Material.FRIEND_POTTERY_SHERD));
        menu.setItem(21, new ItemStack(Material.HEART_POTTERY_SHERD));
        menu.setItem(22, new ItemStack(Material.HEARTBREAK_POTTERY_SHERD));
        menu.setItem(23, new ItemStack(Material.HOWL_POTTERY_SHERD));
        menu.setItem(24, new ItemStack(Material.MINER_POTTERY_SHERD));
        menu.setItem(25, new ItemStack(Material.MOURNER_POTTERY_SHERD));

        menu.setItem(28, new ItemStack(Material.PLENTY_POTTERY_SHERD));
        menu.setItem(29, new ItemStack(Material.PRIZE_POTTERY_SHERD));
        menu.setItem(30, new ItemStack(Material.SHEAF_POTTERY_SHERD));
        menu.setItem(31, new ItemStack(Material.SHELTER_POTTERY_SHERD));
        menu.setItem(32, new ItemStack(Material.SKULL_POTTERY_SHERD));
        menu.setItem(33, new ItemStack(Material.SNORT_POTTERY_SHERD));
    }

    private static void materialsFouthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.BOWL));
        menu.setItem(11, new ItemStack(Material.BRICK));
        menu.setItem(12, new ItemStack(Material.NETHER_BRICK));
        menu.setItem(13, new ItemStack(Material.PAPER));
        menu.setItem(14, new ItemStack(Material.BOOK));
        menu.setItem(15, new ItemStack(Material.FIREWORK_STAR));
        menu.setItem(16, new ItemStack(Material.GLASS_BOTTLE));

        menu.setItem(19, new ItemStack(Material.GLOWSTONE_DUST));
        menu.setItem(20, new ItemStack(Material.GUNPOWDER));
        menu.setItem(21, new ItemStack(Material.DRAGON_BREATH));
        menu.setItem(22, new ItemStack(Material.FERMENTED_SPIDER_EYE));
        menu.setItem(23, new ItemStack(Material.BLAZE_POWDER));
        menu.setItem(24, new ItemStack(Material.SUGAR));
        menu.setItem(25, new ItemStack(Material.RABBIT_FOOT));

        menu.setItem(28, new ItemStack(Material.GLISTERING_MELON_SLICE));
        menu.setItem(29, new ItemStack(Material.SPIDER_EYE));
        menu.setItem(30, new ItemStack(Material.PUFFERFISH));
        menu.setItem(31, new ItemStack(Material.MAGMA_CREAM));
        menu.setItem(32, new ItemStack(Material.GOLDEN_CARROT));
        menu.setItem(33, new ItemStack(Material.GHAST_TEAR));
        menu.setItem(34, new ItemStack(Material.PHANTOM_MEMBRANE));
    }

    private static void materialsThirdPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.SHULKER_SHELL));
        menu.setItem(11, new ItemStack(Material.POPPED_CHORUS_FRUIT));
        menu.setItem(12, new ItemStack(Material.ECHO_SHARD));
        menu.setItem(13, new ItemStack(Material.DISC_FRAGMENT_5));
        menu.setItem(14, new ItemStack(Material.WHITE_DYE));
        menu.setItem(15, new ItemStack(Material.LIGHT_GRAY_DYE));
        menu.setItem(16, new ItemStack(Material.GRAY_DYE));

        menu.setItem(19, new ItemStack(Material.BLACK_DYE));
        menu.setItem(20, new ItemStack(Material.BROWN_DYE));
        menu.setItem(21, new ItemStack(Material.RED_DYE));
        menu.setItem(22, new ItemStack(Material.ORANGE_DYE));
        menu.setItem(23, new ItemStack(Material.LEATHER));
        menu.setItem(24, new ItemStack(Material.YELLOW_DYE));
        menu.setItem(25, new ItemStack(Material.LIME_DYE));

        menu.setItem(28, new ItemStack(Material.GREEN_DYE));
        menu.setItem(29, new ItemStack(Material.CYAN_DYE));
        menu.setItem(30, new ItemStack(Material.LIGHT_BLUE_DYE));
        menu.setItem(31, new ItemStack(Material.BLUE_DYE));
        menu.setItem(32, new ItemStack(Material.PURPLE_DYE));
        menu.setItem(33, new ItemStack(Material.PRISMARINE_SHARD));
        menu.setItem(34, new ItemStack(Material.MAGENTA_DYE));

        menu.setItem(37, new ItemStack(Material.PINK_DYE));
    }

    private static void materialsSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.NETHER_QUARTZ_ORE));
        menu.setItem(11, new ItemStack(Material.QUARTZ));
        menu.setItem(12, new ItemStack(Material.AMETHYST_SHARD));
        menu.setItem(13, new ItemStack(Material.STICK));
        menu.setItem(14, new ItemStack(Material.FLINT));
        menu.setItem(15, new ItemStack(Material.BONE));
        menu.setItem(16, new ItemStack(Material.BONE_MEAL));

        menu.setItem(19, new ItemStack(Material.STRING));
        menu.setItem(20, new ItemStack(Material.FEATHER));
        menu.setItem(21, new ItemStack(Material.SNOWBALL));
        menu.setItem(22, new ItemStack(Material.EGG));
        menu.setItem(23, new ItemStack(Material.LEATHER));
        menu.setItem(24, new ItemStack(Material.RABBIT_HIDE));
        menu.setItem(25, new ItemStack(Material.HONEYCOMB));

        menu.setItem(28, new ItemStack(Material.INK_SAC));
        menu.setItem(29, new ItemStack(Material.GLOW_INK_SAC));
        menu.setItem(30, new ItemStack(Material.SCUTE));
        menu.setItem(31, new ItemStack(Material.SLIME_BALL));
        menu.setItem(32, new ItemStack(Material.CLAY_BALL));
        menu.setItem(33, new ItemStack(Material.PRISMARINE_SHARD));
        menu.setItem(34, new ItemStack(Material.PRISMARINE_CRYSTALS));

        menu.setItem(37, new ItemStack(Material.NAUTILUS_SHELL));
        menu.setItem(38, new ItemStack(Material.HEART_OF_THE_SEA));
        menu.setItem(39, new ItemStack(Material.FIRE_CHARGE));
        menu.setItem(40, new ItemStack(Material.BLAZE_ROD));
        menu.setItem(41, new ItemStack(Material.NETHER_STAR));
        menu.setItem(42, new ItemStack(Material.ENDER_PEARL));
        menu.setItem(43, new ItemStack(Material.ENDER_EYE));
    }

    private static void materialsFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.RAW_IRON));
        menu.setItem(11, new ItemStack(Material.RAW_COPPER));
        menu.setItem(12, new ItemStack(Material.RAW_GOLD));
        menu.setItem(13, new ItemStack(Material.EMERALD));
        menu.setItem(14, new ItemStack(Material.LAPIS_LAZULI));
        menu.setItem(15, new ItemStack(Material.DIAMOND));
        menu.setItem(16, new ItemStack(Material.NETHERITE_SCRAP));

        menu.setItem(19, new ItemStack(Material.IRON_INGOT));
        menu.setItem(20, new ItemStack(Material.COPPER_INGOT));
        menu.setItem(21, new ItemStack(Material.GOLD_INGOT));
        menu.setItem(22, new ItemStack(Material.EMERALD_BLOCK));
        menu.setItem(23, new ItemStack(Material.LAPIS_BLOCK));
        menu.setItem(24, new ItemStack(Material.DIAMOND_BLOCK));
        menu.setItem(25, new ItemStack(Material.NETHERITE_INGOT));

        menu.setItem(28, new ItemStack(Material.IRON_BLOCK));
        menu.setItem(29, new ItemStack(Material.COPPER_BLOCK));
        menu.setItem(30, new ItemStack(Material.GOLD_BLOCK));
        menu.setItem(31, new ItemStack(Material.RAW_IRON_BLOCK));
        menu.setItem(32, new ItemStack(Material.RAW_COPPER_BLOCK));
        menu.setItem(33, new ItemStack(Material.RAW_GOLD_BLOCK));
        menu.setItem(34, new ItemStack(Material.NETHERITE_BLOCK));


        menu.setItem(37, new ItemStack(Material.IRON_ORE));
        menu.setItem(38, new ItemStack(Material.COPPER_ORE));
        menu.setItem(39, new ItemStack(Material.GOLD_ORE));
        menu.setItem(40, new ItemStack(Material.EMERALD_ORE));
        menu.setItem(41, new ItemStack(Material.LAPIS_ORE));
        menu.setItem(42, new ItemStack(Material.DIAMOND_ORE));
        menu.setItem(43, new ItemStack(Material.ANCIENT_DEBRIS));
    }


    private static void foodSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.TROPICAL_FISH));
        menu.setItem(11, new ItemStack(Material.PUFFERFISH));
        menu.setItem(12, new ItemStack(Material.BREAD));
        menu.setItem(13, new ItemStack(Material.COOKIE));
        menu.setItem(14, new ItemStack(Material.CAKE));
        menu.setItem(15, new ItemStack(Material.PUMPKIN_PIE));
        menu.setItem(16, new ItemStack(Material.ROTTEN_FLESH));

        menu.setItem(19, new ItemStack(Material.SPIDER_EYE));
        menu.setItem(20, new ItemStack(Material.MUSHROOM_STEW));
        menu.setItem(21, new ItemStack(Material.BEETROOT_SOUP));
        menu.setItem(22, new ItemStack(Material.BAKED_POTATO));
        menu.setItem(23, new ItemStack(Material.RABBIT_STEW));
        menu.setItem(24, new ItemStack(Material.SUSPICIOUS_STEW));
        menu.setItem(25, new ItemStack(Material.MILK_BUCKET));

        menu.setItem(28, new ItemStack(Material.HONEY_BOTTLE));
        menu.setItem(29, new ItemStack(Material.WHEAT_SEEDS));
        menu.setItem(30, new ItemStack(Material.COCOA_BEANS));
        menu.setItem(31, new ItemStack(Material.PUMPKIN_SEEDS));
        menu.setItem(32, new ItemStack(Material.MELON_SEEDS));
        menu.setItem(33, new ItemStack(Material.BEETROOT_SEEDS));
        menu.setItem(34, new ItemStack(Material.TORCHFLOWER_SEEDS));

        menu.setItem(37, new ItemStack(Material.PITCHER_POD));
        menu.setItem(38, new ItemStack(Material.NETHER_WART));
        menu.setItem(39, new ItemStack(Material.BAMBOO));
        menu.setItem(40, new ItemStack(Material.SUGAR_CANE));
        menu.setItem(41, new ItemStack(Material.CACTUS));
    }

    private static void foodFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.APPLE));
        menu.setItem(11, new ItemStack(Material.GOLDEN_APPLE));
        menu.setItem(12, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        menu.setItem(13, new ItemStack(Material.MELON_SLICE));
        menu.setItem(14, new ItemStack(Material.SWEET_BERRIES));
        menu.setItem(15, new ItemStack(Material.GLOW_BERRIES));
        menu.setItem(16, new ItemStack(Material.CHORUS_FRUIT));

        menu.setItem(19, new ItemStack(Material.CARROT));
        menu.setItem(20, new ItemStack(Material.GOLDEN_CARROT));
        menu.setItem(21, new ItemStack(Material.POTATO));
        menu.setItem(22, new ItemStack(Material.BAKED_POTATO));
        menu.setItem(23, new ItemStack(Material.POISONOUS_POTATO));
        menu.setItem(24, new ItemStack(Material.BEETROOT));
        menu.setItem(25, new ItemStack(Material.DRIED_KELP));

        menu.setItem(28, new ItemStack(Material.BEEF));
        menu.setItem(29, new ItemStack(Material.COOKED_BEEF));
        menu.setItem(30, new ItemStack(Material.PORKCHOP));
        menu.setItem(31, new ItemStack(Material.COOKED_PORKCHOP));
        menu.setItem(32, new ItemStack(Material.MUTTON));
        menu.setItem(33, new ItemStack(Material.COOKED_MUTTON));
        menu.setItem(34, new ItemStack(Material.CHICKEN));

        menu.setItem(37, new ItemStack(Material.COOKED_CHICKEN));
        menu.setItem(38, new ItemStack(Material.RABBIT));
        menu.setItem(39, new ItemStack(Material.COOKED_RABBIT));
        menu.setItem(40, new ItemStack(Material.COD));
        menu.setItem(41, new ItemStack(Material.COOKED_COD));
        menu.setItem(42, new ItemStack(Material.SALMON));
        menu.setItem(43, new ItemStack(Material.COOKED_SALMON));
    }


    private static void redstoneSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.DROPPER));
        menu.setItem(11, new ItemStack(Material.HOPPER));
        menu.setItem(12, new ItemStack(Material.CHEST));
        menu.setItem(13, new ItemStack(Material.BARREL));
        menu.setItem(14, new ItemStack(Material.CHISELED_BOOKSHELF));
        menu.setItem(15, new ItemStack(Material.FURNACE));
        menu.setItem(16, new ItemStack(Material.TRAPPED_CHEST));

        menu.setItem(19, new ItemStack(Material.JUKEBOX));
        menu.setItem(20, new ItemStack(Material.OBSERVER));
        menu.setItem(21, new ItemStack(Material.NOTE_BLOCK));
        menu.setItem(22, new ItemStack(Material.COMPOSTER));
        menu.setItem(23, new ItemStack(Material.CAULDRON));
        menu.setItem(24, new ItemStack(Material.RAIL));
        menu.setItem(25, new ItemStack(Material.POWERED_RAIL));

        menu.setItem(28, new ItemStack(Material.DETECTOR_RAIL));
        menu.setItem(29, new ItemStack(Material.ACTIVATOR_RAIL));
        menu.setItem(30, new ItemStack(Material.MINECART));
        menu.setItem(31, new ItemStack(Material.HOPPER_MINECART));
        menu.setItem(32, new ItemStack(Material.CHEST_MINECART));
        menu.setItem(33, new ItemStack(Material.FURNACE_MINECART));
        menu.setItem(34, new ItemStack(Material.TNT_MINECART));

        menu.setItem(37, new ItemStack(Material.OAK_CHEST_BOAT));
        menu.setItem(38, new ItemStack(Material.BAMBOO_CHEST_RAFT));
        menu.setItem(39, new ItemStack(Material.TNT));
        menu.setItem(40, new ItemStack(Material.REDSTONE_LAMP));
        menu.setItem(41, new ItemStack(Material.BELL));
        menu.setItem(42, new ItemStack(Material.BIG_DRIPLEAF));
        menu.setItem(43, new ItemStack(Material.ARMOR_STAND));
    }

    private static void redstoneFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.REDSTONE));
        menu.setItem(11, new ItemStack(Material.REDSTONE_TORCH));
        menu.setItem(12, new ItemStack(Material.REDSTONE_BLOCK));
        menu.setItem(13, new ItemStack(Material.REPEATER));
        menu.setItem(14, new ItemStack(Material.COMPARATOR));
        menu.setItem(15, new ItemStack(Material.TARGET));
        menu.setItem(16, new ItemStack(Material.LEVER));

        menu.setItem(19, new ItemStack(Material.OAK_BUTTON));
        menu.setItem(20, new ItemStack(Material.STONE_BUTTON));
        menu.setItem(21, new ItemStack(Material.OAK_PRESSURE_PLATE));
        menu.setItem(22, new ItemStack(Material.STONE_PRESSURE_PLATE));
        menu.setItem(23, new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE));
        menu.setItem(24, new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
        menu.setItem(25, new ItemStack(Material.SCULK_SENSOR));

        menu.setItem(28, new ItemStack(Material.CALIBRATED_SCULK_SENSOR));
        menu.setItem(29, new ItemStack(Material.SCULK_SHRIEKER));
        menu.setItem(30, new ItemStack(Material.AMETHYST_BLOCK));
        menu.setItem(31, new ItemStack(Material.WHITE_WOOL));
        menu.setItem(32, new ItemStack(Material.TRIPWIRE_HOOK));
        menu.setItem(33, new ItemStack(Material.STRING));
        menu.setItem(34, new ItemStack(Material.LECTERN));

        menu.setItem(37, new ItemStack(Material.DAYLIGHT_DETECTOR));
        menu.setItem(38, new ItemStack(Material.LIGHTNING_ROD));
        menu.setItem(39, new ItemStack(Material.PISTON));
        menu.setItem(40, new ItemStack(Material.STICKY_PISTON));
        menu.setItem(41, new ItemStack(Material.SLIME_BLOCK));
        menu.setItem(42, new ItemStack(Material.HONEY_BLOCK));
        menu.setItem(43, new ItemStack(Material.DISPENSER));
    }



    private static void combatFifthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.MINECART));
        menu.setItem(11, new ItemStack(Material.HOPPER_MINECART));
        menu.setItem(12, new ItemStack(Material.CHEST_MINECART));
        menu.setItem(13, new ItemStack(Material.FURNACE_MINECART));
        menu.setItem(14, new ItemStack(Material.TNT_MINECART));

        menu.setItem(19, new ItemStack(Material.GOAT_HORN));
        menu.setItem(20, new ItemStack(Material.MUSIC_DISC_13));
        menu.setItem(21, new ItemStack(Material.MUSIC_DISC_CAT));
        menu.setItem(22, new ItemStack(Material.MUSIC_DISC_BLOCKS));
        menu.setItem(23, new ItemStack(Material.MUSIC_DISC_CHIRP));
        menu.setItem(24, new ItemStack(Material.MUSIC_DISC_FAR));
        menu.setItem(25, new ItemStack(Material.MUSIC_DISC_MALL));

        menu.setItem(28, new ItemStack(Material.MUSIC_DISC_MELLOHI));
        menu.setItem(29, new ItemStack(Material.MUSIC_DISC_STAL));
        menu.setItem(30, new ItemStack(Material.MUSIC_DISC_STRAD));
        menu.setItem(31, new ItemStack(Material.MUSIC_DISC_WARD));
        menu.setItem(32, new ItemStack(Material.MUSIC_DISC_11));
        menu.setItem(33, new ItemStack(Material.MUSIC_DISC_WAIT));
        menu.setItem(34, new ItemStack(Material.MUSIC_DISC_OTHERSIDE));

        menu.setItem(37, new ItemStack(Material.MUSIC_DISC_RELIC));
        menu.setItem(38, new ItemStack(Material.MUSIC_DISC_5));
        menu.setItem(39, new ItemStack(Material.MUSIC_DISC_PIGSTEP));
    }
    private static void combatFourthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.BUCKET));
        menu.setItem(11, new ItemStack(Material.WATER_BUCKET));
        menu.setItem(12, new ItemStack(Material.COD_BUCKET));
        menu.setItem(13, new ItemStack(Material.SALMON_BUCKET));
        menu.setItem(14, new ItemStack(Material.TROPICAL_FISH_BUCKET));
        menu.setItem(15, new ItemStack(Material.PUFFERFISH_BUCKET));
        menu.setItem(16, new ItemStack(Material.AXOLOTL_BUCKET));

        menu.setItem(19, new ItemStack(Material.TADPOLE_BUCKET));
        menu.setItem(20, new ItemStack(Material.LAVA_BUCKET));
        menu.setItem(21, new ItemStack(Material.POWDER_SNOW_BUCKET));
        menu.setItem(22, new ItemStack(Material.FISHING_ROD));
        menu.setItem(23, new ItemStack(Material.FLINT_AND_STEEL));
        menu.setItem(24, new ItemStack(Material.SHEARS));
        menu.setItem(25, new ItemStack(Material.BRUSH));

        menu.setItem(28, new ItemStack(Material.NAME_TAG));
        menu.setItem(29, new ItemStack(Material.LEAD));
        menu.setItem(30, new ItemStack(Material.COMPASS));
        menu.setItem(31, new ItemStack(Material.RECOVERY_COMPASS));
        menu.setItem(32, new ItemStack(Material.CLOCK));
        menu.setItem(33, new ItemStack(Material.SPYGLASS));
        menu.setItem(34, new ItemStack(Material.MAP));

        menu.setItem(37, new ItemStack(Material.WRITABLE_BOOK));
        menu.setItem(38, new ItemStack(Material.ELYTRA));
        menu.setItem(39, new ItemStack(Material.SADDLE));
        menu.setItem(40, new ItemStack(Material.CARROT_ON_A_STICK));
        menu.setItem(41, new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK));
    }
    private static void combatThirdPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.NETHERITE_SHOVEL));
        menu.setItem(11, new ItemStack(Material.NETHERITE_PICKAXE));
        menu.setItem(12, new ItemStack(Material.NETHERITE_AXE));
        menu.setItem(13, new ItemStack(Material.NETHERITE_HOE));

        menu.setItem(19, new ItemStack(Material.DIAMOND_SHOVEL));
        menu.setItem(20, new ItemStack(Material.DIAMOND_PICKAXE));
        menu.setItem(21, new ItemStack(Material.DIAMOND_AXE));
        menu.setItem(22, new ItemStack(Material.DIAMOND_HOE));

        menu.setItem(28, new ItemStack(Material.IRON_SHOVEL));
        menu.setItem(29, new ItemStack(Material.IRON_PICKAXE));
        menu.setItem(30, new ItemStack(Material.IRON_AXE));
        menu.setItem(31, new ItemStack(Material.IRON_HOE));

        menu.setItem(37, new ItemStack(Material.GOLDEN_SHOVEL));
        menu.setItem(38, new ItemStack(Material.GOLDEN_PICKAXE));
        menu.setItem(39, new ItemStack(Material.GOLDEN_AXE));
        menu.setItem(40, new ItemStack(Material.GOLDEN_HOE));
    }

    private static void combatSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.DIAMOND_HELMET));
        menu.setItem(11, new ItemStack(Material.DIAMOND_CHESTPLATE));
        menu.setItem(12, new ItemStack(Material.DIAMOND_LEGGINGS));
        menu.setItem(13, new ItemStack(Material.DIAMOND_BOOTS));
        menu.setItem(14, new ItemStack(Material.DIAMOND_SWORD));
        menu.setItem(15, new ItemStack(Material.DIAMOND_AXE));

        menu.setItem(19, new ItemStack(Material.NETHERITE_HELMET));
        menu.setItem(20, new ItemStack(Material.NETHERITE_CHESTPLATE));
        menu.setItem(21, new ItemStack(Material.NETHERITE_LEGGINGS));
        menu.setItem(22, new ItemStack(Material.NETHERITE_BOOTS));
        menu.setItem(23, new ItemStack(Material.NETHERITE_SWORD));
        menu.setItem(24, new ItemStack(Material.NETHERITE_AXE));

        menu.setItem(28, new ItemStack(Material.TRIDENT));
        menu.setItem(29, new ItemStack(Material.SHIELD));
        menu.setItem(30, new ItemStack(Material.TURTLE_HELMET));
        menu.setItem(31, new ItemStack(Material.TOTEM_OF_UNDYING));
        menu.setItem(32, new ItemStack(Material.TNT));
        menu.setItem(33, new ItemStack(Material.END_CRYSTAL));
        menu.setItem(34, new ItemStack(Material.GOLDEN_APPLE));

        menu.setItem(37, new ItemStack(Material.SNOWBALL));
        menu.setItem(38, new ItemStack(Material.EGG));
        menu.setItem(39, new ItemStack(Material.BOW));
        menu.setItem(40, new ItemStack(Material.CROSSBOW));
        menu.setItem(41, new ItemStack(Material.FIREWORK_ROCKET));
        menu.setItem(42, new ItemStack(Material.ARROW));
        menu.setItem(43, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
    }

    private static void combatFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.LEATHER_HELMET));
        menu.setItem(11, new ItemStack(Material.LEATHER_CHESTPLATE));
        menu.setItem(12, new ItemStack(Material.LEATHER_LEGGINGS));
        menu.setItem(13, new ItemStack(Material.LEATHER_BOOTS));
        menu.setItem(14, new ItemStack(Material.WOODEN_SWORD));
        menu.setItem(15, new ItemStack(Material.WOODEN_AXE));

        menu.setItem(19, new ItemStack(Material.CHAINMAIL_HELMET));
        menu.setItem(20, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        menu.setItem(21, new ItemStack(Material.CHAINMAIL_LEGGINGS));
        menu.setItem(22, new ItemStack(Material.CHAINMAIL_BOOTS));
        menu.setItem(23, new ItemStack(Material.STONE_SWORD));
        menu.setItem(24, new ItemStack(Material.STONE_AXE));

        menu.setItem(28, new ItemStack(Material.IRON_HELMET));
        menu.setItem(29, new ItemStack(Material.IRON_CHESTPLATE));
        menu.setItem(30, new ItemStack(Material.IRON_LEGGINGS));
        menu.setItem(31, new ItemStack(Material.IRON_BOOTS));
        menu.setItem(32, new ItemStack(Material.IRON_SWORD));
        menu.setItem(33, new ItemStack(Material.IRON_AXE));

        menu.setItem(37, new ItemStack(Material.GOLDEN_HELMET));
        menu.setItem(38, new ItemStack(Material.GOLDEN_CHESTPLATE));
        menu.setItem(39, new ItemStack(Material.GOLDEN_LEGGINGS));
        menu.setItem(40, new ItemStack(Material.GOLDEN_BOOTS));
        menu.setItem(41, new ItemStack(Material.GOLDEN_SWORD));
        menu.setItem(42, new ItemStack(Material.GOLDEN_AXE));
    }


    private static void funcFourthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.SHULKER_BOX));
        menu.setItem(11, new ItemStack(Material.WHITE_SHULKER_BOX));
        menu.setItem(12, new ItemStack(Material.LIGHT_GRAY_SHULKER_BOX));
        menu.setItem(13, new ItemStack(Material.GRAY_SHULKER_BOX));
        menu.setItem(14, new ItemStack(Material.BLACK_SHULKER_BOX));
        menu.setItem(15, new ItemStack(Material.BROWN_SHULKER_BOX));
        menu.setItem(16, new ItemStack(Material.RED_SHULKER_BOX));

        menu.setItem(19, new ItemStack(Material.ORANGE_SHULKER_BOX));
        menu.setItem(20, new ItemStack(Material.YELLOW_SHULKER_BOX));
        menu.setItem(21, new ItemStack(Material.LIME_SHULKER_BOX));
        menu.setItem(22, new ItemStack(Material.GREEN_SHULKER_BOX));
        menu.setItem(23, new ItemStack(Material.CYAN_SHULKER_BOX));
        menu.setItem(24, new ItemStack(Material.LIGHT_BLUE_SHULKER_BOX));
        menu.setItem(25, new ItemStack(Material.BLUE_SHULKER_BOX));

        menu.setItem(28, new ItemStack(Material.PURPLE_SHULKER_BOX));
        menu.setItem(29, new ItemStack(Material.MAGENTA_SHULKER_BOX));
        menu.setItem(30, new ItemStack(Material.PINK_SHULKER_BOX));
        menu.setItem(31, new ItemStack(Material.SKELETON_SKULL));
        menu.setItem(32, new ItemStack(Material.WITHER_SKELETON_SKULL));
        menu.setItem(33, new ItemStack(Material.ZOMBIE_HEAD));
        menu.setItem(34, new ItemStack(Material.CREEPER_HEAD));

        menu.setItem(37, new ItemStack(Material.PIGLIN_HEAD));
        menu.setItem(38, new ItemStack(Material.DRAGON_HEAD));
    }

    private static void funcThirdPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.TINTED_GLASS));
        menu.setItem(11, new ItemStack(Material.OAK_SIGN));
        menu.setItem(12, new ItemStack(Material.OAK_HANGING_SIGN));
        menu.setItem(13, new ItemStack(Material.SPRUCE_SIGN));
        menu.setItem(14, new ItemStack(Material.SPRUCE_HANGING_SIGN));
        menu.setItem(15, new ItemStack(Material.BIRCH_SIGN));
        menu.setItem(16, new ItemStack(Material.BIRCH_HANGING_SIGN));

        menu.setItem(19, new ItemStack(Material.JUNGLE_SIGN));
        menu.setItem(20, new ItemStack(Material.JUNGLE_HANGING_SIGN));
        menu.setItem(21, new ItemStack(Material.ACACIA_SIGN));
        menu.setItem(22, new ItemStack(Material.ACACIA_HANGING_SIGN));
        menu.setItem(23, new ItemStack(Material.DARK_OAK_SIGN));
        menu.setItem(24, new ItemStack(Material.DARK_OAK_HANGING_SIGN));
        menu.setItem(25, new ItemStack(Material.MANGROVE_SIGN));

        menu.setItem(28, new ItemStack(Material.MANGROVE_HANGING_SIGN));
        menu.setItem(29, new ItemStack(Material.CHERRY_SIGN));
        menu.setItem(30, new ItemStack(Material.CHERRY_HANGING_SIGN));
        menu.setItem(31, new ItemStack(Material.BAMBOO_SIGN));
        menu.setItem(32, new ItemStack(Material.CRIMSON_SIGN));
        menu.setItem(33, new ItemStack(Material.CRIMSON_HANGING_SIGN));
        menu.setItem(34, new ItemStack(Material.WARPED_SIGN));

        menu.setItem(37, new ItemStack(Material.WARPED_HANGING_SIGN));
        menu.setItem(38, new ItemStack(Material.CHEST));
        menu.setItem(39, new ItemStack(Material.BARREL));
        menu.setItem(40, new ItemStack(Material.ENDER_CHEST));
        menu.setItem(41, new ItemStack(Material.RESPAWN_ANCHOR));
        menu.setItem(42, new ItemStack(Material.DRAGON_EGG));
        menu.setItem(43, new ItemStack(Material.ENDER_EYE));
    }

    private static void funcSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.SOUL_CAMPFIRE));
        menu.setItem(11, new ItemStack(Material.ANVIL));
        menu.setItem(12, new ItemStack(Material.COMPOSTER));
        menu.setItem(13, new ItemStack(Material.NOTE_BLOCK));
        menu.setItem(14, new ItemStack(Material.JUKEBOX));
        menu.setItem(15, new ItemStack(Material.ENCHANTING_TABLE));
        menu.setItem(16, new ItemStack(Material.END_CRYSTAL));

        menu.setItem(19, new ItemStack(Material.BREWING_STAND));
        menu.setItem(20, new ItemStack(Material.CAULDRON));
        menu.setItem(21, new ItemStack(Material.BELL));
        menu.setItem(22, new ItemStack(Material.BEACON));
        menu.setItem(23, new ItemStack(Material.CONDUIT));
        menu.setItem(24, new ItemStack(Material.LODESTONE));
        menu.setItem(25, new ItemStack(Material.LADDER));

        menu.setItem(28, new ItemStack(Material.SCAFFOLDING));
        menu.setItem(29, new ItemStack(Material.BEE_NEST));
        menu.setItem(30, new ItemStack(Material.BEEHIVE));
        menu.setItem(31, new ItemStack(Material.SUSPICIOUS_SAND));
        menu.setItem(32, new ItemStack(Material.SUSPICIOUS_GRAVEL));
        menu.setItem(33, new ItemStack(Material.LIGHTNING_ROD));
        menu.setItem(34, new ItemStack(Material.FLOWER_POT));

        menu.setItem(37, new ItemStack(Material.DECORATED_POT));
        menu.setItem(38, new ItemStack(Material.ARMOR_STAND));
        menu.setItem(39, new ItemStack(Material.ITEM_FRAME));
        menu.setItem(40, new ItemStack(Material.GLOW_ITEM_FRAME));
        menu.setItem(41, new ItemStack(Material.BOOKSHELF));
        menu.setItem(42, new ItemStack(Material.CHISELED_BOOKSHELF));
        menu.setItem(43, new ItemStack(Material.LECTERN));
    }

    private static void funcFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.TORCH));
        menu.setItem(11, new ItemStack(Material.SOUL_TORCH));
        menu.setItem(12, new ItemStack(Material.REDSTONE_TORCH));
        menu.setItem(13, new ItemStack(Material.LANTERN));
        menu.setItem(14, new ItemStack(Material.SOUL_LANTERN));
        menu.setItem(15, new ItemStack(Material.CHAIN));
        menu.setItem(16, new ItemStack(Material.END_ROD));

        menu.setItem(19, new ItemStack(Material.SEA_LANTERN));
        menu.setItem(20, new ItemStack(Material.REDSTONE_LAMP));
        menu.setItem(21, new ItemStack(Material.GLOWSTONE));
        menu.setItem(22, new ItemStack(Material.SHROOMLIGHT));
        menu.setItem(23, new ItemStack(Material.OCHRE_FROGLIGHT));
        menu.setItem(24, new ItemStack(Material.VERDANT_FROGLIGHT));
        menu.setItem(25, new ItemStack(Material.PEARLESCENT_FROGLIGHT));

        menu.setItem(28, new ItemStack(Material.CRYING_OBSIDIAN));
        menu.setItem(29, new ItemStack(Material.GLOW_LICHEN));
        menu.setItem(30, new ItemStack(Material.MAGMA_BLOCK));
        menu.setItem(31, new ItemStack(Material.CRAFTING_TABLE));
        menu.setItem(32, new ItemStack(Material.STONECUTTER));
        menu.setItem(33, new ItemStack(Material.CARTOGRAPHY_TABLE));
        menu.setItem(34, new ItemStack(Material.FLETCHING_TABLE));

        menu.setItem(37, new ItemStack(Material.SMITHING_TABLE));
        menu.setItem(38, new ItemStack(Material.GRINDSTONE));
        menu.setItem(39, new ItemStack(Material.LOOM));
        menu.setItem(40, new ItemStack(Material.FURNACE));
        menu.setItem(41, new ItemStack(Material.SMOKER));
        menu.setItem(42, new ItemStack(Material.BLAST_FURNACE));
        menu.setItem(43, new ItemStack(Material.CAMPFIRE));
    }


    private static void buildTwelfthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.WAXED_EXPOSED_CUT_COPPER));
        menu.setItem(11, new ItemStack(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS));
        menu.setItem(12, new ItemStack(Material.WAXED_EXPOSED_CUT_COPPER_SLAB));
        menu.setItem(13, new ItemStack(Material.WAXED_WEATHERED_COPPER));
        menu.setItem(14, new ItemStack(Material.WAXED_WEATHERED_CUT_COPPER));
        menu.setItem(15, new ItemStack(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS));
        menu.setItem(16, new ItemStack(Material.WAXED_WEATHERED_CUT_COPPER_SLAB));

        menu.setItem(19, new ItemStack(Material.WAXED_OXIDIZED_COPPER));
        menu.setItem(20, new ItemStack(Material.WAXED_OXIDIZED_CUT_COPPER));
        menu.setItem(21, new ItemStack(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS));
        menu.setItem(22, new ItemStack(Material.WAXED_OXIDIZED_CUT_COPPER_SLAB));
    }

    private static void buildEleventhPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.CHISELED_QUARTZ_BLOCK));
        menu.setItem(11, new ItemStack(Material.QUARTZ_BRICKS));
        menu.setItem(12, new ItemStack(Material.QUARTZ_PILLAR));
        menu.setItem(13, new ItemStack(Material.SMOOTH_QUARTZ));
        menu.setItem(14, new ItemStack(Material.SMOOTH_QUARTZ_STAIRS));
        menu.setItem(15, new ItemStack(Material.SMOOTH_QUARTZ_SLAB));
        menu.setItem(16, new ItemStack(Material.AMETHYST_BLOCK));

        menu.setItem(19, new ItemStack(Material.COPPER_BLOCK));
        menu.setItem(20, new ItemStack(Material.CUT_COPPER));
        menu.setItem(21, new ItemStack(Material.CUT_COPPER_STAIRS));
        menu.setItem(22, new ItemStack(Material.CUT_COPPER_SLAB));
        menu.setItem(23, new ItemStack(Material.EXPOSED_COPPER));
        menu.setItem(24, new ItemStack(Material.EXPOSED_CUT_COPPER));
        menu.setItem(25, new ItemStack(Material.EXPOSED_CUT_COPPER_STAIRS));

        menu.setItem(28, new ItemStack(Material.EXPOSED_CUT_COPPER_SLAB));
        menu.setItem(29, new ItemStack(Material.WEATHERED_COPPER));
        menu.setItem(30, new ItemStack(Material.WEATHERED_CUT_COPPER));
        menu.setItem(31, new ItemStack(Material.WEATHERED_CUT_COPPER_STAIRS));
        menu.setItem(32, new ItemStack(Material.WEATHERED_CUT_COPPER_SLAB));
        menu.setItem(33, new ItemStack(Material.OXIDIZED_COPPER));
        menu.setItem(34, new ItemStack(Material.OXIDIZED_CUT_COPPER));

        menu.setItem(37, new ItemStack(Material.OXIDIZED_CUT_COPPER_STAIRS));
        menu.setItem(38, new ItemStack(Material.OXIDIZED_CUT_COPPER_SLAB));
        menu.setItem(39, new ItemStack(Material.WAXED_COPPER_BLOCK));
        menu.setItem(40, new ItemStack(Material.WAXED_CUT_COPPER));
        menu.setItem(41, new ItemStack(Material.WAXED_CUT_COPPER_STAIRS));
        menu.setItem(42, new ItemStack(Material.WAXED_CUT_COPPER_SLAB));
        menu.setItem(43, new ItemStack(Material.WAXED_EXPOSED_COPPER));
    }


    private static void buildTenthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.POLISHED_BLACKSTONE_STAIRS));
        menu.setItem(11, new ItemStack(Material.POLISHED_BLACKSTONE_SLAB));
        menu.setItem(12, new ItemStack(Material.POLISHED_BLACKSTONE_WALL));
        menu.setItem(13, new ItemStack(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE));
        menu.setItem(14, new ItemStack(Material.POLISHED_BLACKSTONE_BUTTON));
        menu.setItem(15, new ItemStack(Material.POLISHED_BLACKSTONE_BRICKS));
        menu.setItem(16, new ItemStack(Material.POLISHED_BLACKSTONE_BRICK_STAIRS));

        menu.setItem(19, new ItemStack(Material.POLISHED_BLACKSTONE_BRICK_SLAB));
        menu.setItem(20, new ItemStack(Material.POLISHED_BLACKSTONE_BRICK_WALL));
        menu.setItem(21, new ItemStack(Material.END_STONE));
        menu.setItem(22, new ItemStack(Material.END_STONE_BRICKS));
        menu.setItem(23, new ItemStack(Material.END_STONE_BRICK_STAIRS));
        menu.setItem(24, new ItemStack(Material.END_STONE_BRICK_SLAB));
        menu.setItem(25, new ItemStack(Material.END_STONE_BRICK_WALL));

        menu.setItem(28, new ItemStack(Material.PURPUR_BLOCK));
        menu.setItem(29, new ItemStack(Material.PURPUR_PILLAR));
        menu.setItem(30, new ItemStack(Material.PURPUR_STAIRS));
        menu.setItem(31, new ItemStack(Material.PURPUR_SLAB));
        menu.setItem(32, new ItemStack(Material.IRON_BARS));
        menu.setItem(33, new ItemStack(Material.IRON_DOOR));
        menu.setItem(34, new ItemStack(Material.IRON_TRAPDOOR));

        menu.setItem(37, new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
        menu.setItem(38, new ItemStack(Material.CHAIN));
        menu.setItem(39, new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE));
        menu.setItem(40, new ItemStack(Material.QUARTZ_BLOCK));
        menu.setItem(41, new ItemStack(Material.QUARTZ_STAIRS));
        menu.setItem(42, new ItemStack(Material.QUARTZ_SLAB));
        menu.setItem(43, new ItemStack(Material.CHISELED_QUARTZ_BLOCK));
    }

    private static void buildNinthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.PRISMARINE_BRICK_STAIRS));
        menu.setItem(11, new ItemStack(Material.PRISMARINE_BRICK_SLAB));
        menu.setItem(12, new ItemStack(Material.DARK_PRISMARINE));
        menu.setItem(13, new ItemStack(Material.DARK_PRISMARINE_STAIRS));
        menu.setItem(14, new ItemStack(Material.DARK_PRISMARINE_SLAB));
        menu.setItem(15, new ItemStack(Material.NETHERRACK));
        menu.setItem(16, new ItemStack(Material.NETHER_BRICKS));

        menu.setItem(19, new ItemStack(Material.CRACKED_NETHER_BRICKS));
        menu.setItem(20, new ItemStack(Material.NETHER_BRICK_STAIRS));
        menu.setItem(21, new ItemStack(Material.NETHER_BRICK_SLAB));
        menu.setItem(22, new ItemStack(Material.NETHER_BRICK_WALL));
        menu.setItem(23, new ItemStack(Material.NETHER_BRICK_FENCE));
        menu.setItem(24, new ItemStack(Material.CHISELED_NETHER_BRICKS));
        menu.setItem(25, new ItemStack(Material.RED_NETHER_BRICKS));

        menu.setItem(28, new ItemStack(Material.RED_NETHER_BRICK_STAIRS));
        menu.setItem(29, new ItemStack(Material.RED_NETHER_BRICK_SLAB));
        menu.setItem(30, new ItemStack(Material.RED_NETHER_BRICK_WALL));
        menu.setItem(31, new ItemStack(Material.BASALT));
        menu.setItem(32, new ItemStack(Material.SMOOTH_BASALT));
        menu.setItem(33, new ItemStack(Material.POLISHED_BASALT));
        menu.setItem(34, new ItemStack(Material.BLACKSTONE));

        menu.setItem(37, new ItemStack(Material.GILDED_BLACKSTONE));
        menu.setItem(38, new ItemStack(Material.BLACKSTONE_STAIRS));
        menu.setItem(39, new ItemStack(Material.BLACKSTONE_SLAB));
        menu.setItem(40, new ItemStack(Material.BLACKSTONE_WALL));
        menu.setItem(41, new ItemStack(Material.CHISELED_POLISHED_BLACKSTONE));
        menu.setItem(42, new ItemStack(Material.POLISHED_BLACKSTONE));
        menu.setItem(43, new ItemStack(Material.POLISHED_BLACKSTONE_STAIRS));
    }


    private static void buildEighthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.MUD_BRICK_SLAB));
        menu.setItem(11, new ItemStack(Material.MUD_BRICK_WALL));
        menu.setItem(12, new ItemStack(Material.SANDSTONE));
        menu.setItem(13, new ItemStack(Material.SANDSTONE_STAIRS));
        menu.setItem(14, new ItemStack(Material.SANDSTONE_SLAB));
        menu.setItem(15, new ItemStack(Material.SANDSTONE_WALL));
        menu.setItem(16, new ItemStack(Material.CHISELED_SANDSTONE));

        menu.setItem(19, new ItemStack(Material.SMOOTH_SANDSTONE));
        menu.setItem(20, new ItemStack(Material.SMOOTH_SANDSTONE_STAIRS));
        menu.setItem(21, new ItemStack(Material.SMOOTH_SANDSTONE_SLAB));
        menu.setItem(22, new ItemStack(Material.CUT_SANDSTONE));
        menu.setItem(23, new ItemStack(Material.CUT_SANDSTONE_SLAB));
        menu.setItem(24, new ItemStack(Material.RED_SANDSTONE));
        menu.setItem(25, new ItemStack(Material.RED_SANDSTONE_STAIRS));

        menu.setItem(28, new ItemStack(Material.RED_SANDSTONE_SLAB));
        menu.setItem(29, new ItemStack(Material.RED_SANDSTONE_WALL));
        menu.setItem(30, new ItemStack(Material.CHISELED_RED_SANDSTONE));
        menu.setItem(31, new ItemStack(Material.SMOOTH_RED_SANDSTONE));
        menu.setItem(32, new ItemStack(Material.SMOOTH_RED_SANDSTONE_STAIRS));
        menu.setItem(33, new ItemStack(Material.SMOOTH_RED_SANDSTONE_SLAB));
        menu.setItem(34, new ItemStack(Material.CUT_RED_SANDSTONE));

        menu.setItem(37, new ItemStack(Material.CUT_RED_SANDSTONE_SLAB));
        menu.setItem(38, new ItemStack(Material.SEA_LANTERN));
        menu.setItem(39, new ItemStack(Material.PRISMARINE));
        menu.setItem(40, new ItemStack(Material.PRISMARINE_STAIRS));
        menu.setItem(41, new ItemStack(Material.PRISMARINE_SLAB));
        menu.setItem(42, new ItemStack(Material.PRISMARINE_WALL));
        menu.setItem(43, new ItemStack(Material.PRISMARINE_BRICKS));
    }

    private static void buildSeventhPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.POLISHED_ANDESITE_SLAB));
        menu.setItem(11, new ItemStack(Material.DEEPSLATE));
        menu.setItem(12, new ItemStack(Material.COBBLED_DEEPSLATE));
        menu.setItem(13, new ItemStack(Material.COBBLED_DEEPSLATE_STAIRS));
        menu.setItem(14, new ItemStack(Material.COBBLED_DEEPSLATE_SLAB));
        menu.setItem(15, new ItemStack(Material.COBBLED_DEEPSLATE_WALL));
        menu.setItem(16, new ItemStack(Material.CHISELED_DEEPSLATE));

        menu.setItem(19, new ItemStack(Material.POLISHED_DEEPSLATE));
        menu.setItem(20, new ItemStack(Material.POLISHED_DEEPSLATE_STAIRS));
        menu.setItem(21, new ItemStack(Material.POLISHED_DEEPSLATE_SLAB));
        menu.setItem(22, new ItemStack(Material.POLISHED_DEEPSLATE_WALL));
        menu.setItem(23, new ItemStack(Material.DEEPSLATE_BRICKS));
        menu.setItem(24, new ItemStack(Material.CRACKED_DEEPSLATE_BRICKS));
        menu.setItem(25, new ItemStack(Material.DEEPSLATE_BRICK_STAIRS));

        menu.setItem(28, new ItemStack(Material.DEEPSLATE_BRICK_SLAB));
        menu.setItem(29, new ItemStack(Material.DEEPSLATE_BRICK_WALL));
        menu.setItem(30, new ItemStack(Material.DEEPSLATE_TILES));
        menu.setItem(31, new ItemStack(Material.CRACKED_DEEPSLATE_TILES));
        menu.setItem(32, new ItemStack(Material.DEEPSLATE_TILE_STAIRS));
        menu.setItem(33, new ItemStack(Material.DEEPSLATE_TILE_SLAB));
        menu.setItem(34, new ItemStack(Material.DEEPSLATE_TILE_WALL));

        menu.setItem(37, new ItemStack(Material.BRICKS));
        menu.setItem(38, new ItemStack(Material.BRICK_STAIRS));
        menu.setItem(39, new ItemStack(Material.BRICK_SLAB));
        menu.setItem(40, new ItemStack(Material.BRICK_WALL));
        menu.setItem(41, new ItemStack(Material.PACKED_MUD));
        menu.setItem(42, new ItemStack(Material.MUD_BRICKS));
        menu.setItem(43, new ItemStack(Material.MUD_BRICK_STAIRS));
    }

    private static void buildSixthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.STONE_BRICK_STAIRS));
        menu.setItem(11, new ItemStack(Material.STONE_BRICK_SLAB));
        menu.setItem(12, new ItemStack(Material.STONE_BRICK_WALL));
        menu.setItem(13, new ItemStack(Material.CHISELED_STONE_BRICKS));
        menu.setItem(14, new ItemStack(Material.MOSSY_STONE_BRICKS));
        menu.setItem(15, new ItemStack(Material.MOSSY_STONE_BRICK_STAIRS));
        menu.setItem(16, new ItemStack(Material.MOSSY_STONE_BRICK_SLAB));

        menu.setItem(19, new ItemStack(Material.MOSSY_STONE_BRICK_WALL));
        menu.setItem(20, new ItemStack(Material.GRANITE));
        menu.setItem(21, new ItemStack(Material.GRANITE_STAIRS));
        menu.setItem(22, new ItemStack(Material.GRANITE_SLAB));
        menu.setItem(23, new ItemStack(Material.GRANITE_WALL));
        menu.setItem(24, new ItemStack(Material.POLISHED_GRANITE));
        menu.setItem(25, new ItemStack(Material.POLISHED_GRANITE_STAIRS));

        menu.setItem(28, new ItemStack(Material.POLISHED_GRANITE_SLAB));
        menu.setItem(29, new ItemStack(Material.DIORITE));
        menu.setItem(30, new ItemStack(Material.DIORITE_STAIRS));
        menu.setItem(31, new ItemStack(Material.DIORITE_SLAB));
        menu.setItem(32, new ItemStack(Material.DIORITE_WALL));
        menu.setItem(33, new ItemStack(Material.POLISHED_DIORITE));
        menu.setItem(34, new ItemStack(Material.POLISHED_DIORITE_STAIRS));

        menu.setItem(37, new ItemStack(Material.POLISHED_DIORITE_SLAB));
        menu.setItem(38, new ItemStack(Material.ANDESITE));
        menu.setItem(39, new ItemStack(Material.ANDESITE_STAIRS));
        menu.setItem(40, new ItemStack(Material.ANDESITE_SLAB));
        menu.setItem(41, new ItemStack(Material.ANDESITE_WALL));
        menu.setItem(42, new ItemStack(Material.POLISHED_ANDESITE));
        menu.setItem(43, new ItemStack(Material.POLISHED_ANDESITE_STAIRS));
    }

    private static void buildFifthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.CRIMSON_BUTTON));
        menu.setItem(11, new ItemStack(Material.WARPED_STEM));
        menu.setItem(12, new ItemStack(Material.STRIPPED_WARPED_STEM));
        menu.setItem(13, new ItemStack(Material.WARPED_PLANKS));
        menu.setItem(14, new ItemStack(Material.WARPED_STAIRS));
        menu.setItem(15, new ItemStack(Material.WARPED_SLAB));
        menu.setItem(16, new ItemStack(Material.WARPED_FENCE));

        menu.setItem(19, new ItemStack(Material.WARPED_FENCE_GATE));
        menu.setItem(20, new ItemStack(Material.WARPED_DOOR));
        menu.setItem(21, new ItemStack(Material.WARPED_TRAPDOOR));
        menu.setItem(22, new ItemStack(Material.WARPED_PRESSURE_PLATE));
        menu.setItem(23, new ItemStack(Material.WARPED_BUTTON));
        menu.setItem(24, new ItemStack(Material.STONE));
        menu.setItem(25, new ItemStack(Material.STONE_STAIRS));

        menu.setItem(28, new ItemStack(Material.STONE_SLAB));
        menu.setItem(29, new ItemStack(Material.STONE_PRESSURE_PLATE));
        menu.setItem(30, new ItemStack(Material.COBBLESTONE));
        menu.setItem(31, new ItemStack(Material.COBBLESTONE_STAIRS));
        menu.setItem(32, new ItemStack(Material.COBBLESTONE_SLAB));
        menu.setItem(33, new ItemStack(Material.COBBLESTONE_WALL));
        menu.setItem(34, new ItemStack(Material.MOSSY_COBBLESTONE));

        menu.setItem(37, new ItemStack(Material.MOSSY_COBBLESTONE_STAIRS));
        menu.setItem(38, new ItemStack(Material.MOSSY_COBBLESTONE_SLAB));
        menu.setItem(39, new ItemStack(Material.MOSSY_COBBLESTONE_WALL));
        menu.setItem(40, new ItemStack(Material.SMOOTH_STONE));
        menu.setItem(41, new ItemStack(Material.SMOOTH_STONE_SLAB));
        menu.setItem(42, new ItemStack(Material.STONE_BRICKS));
        menu.setItem(43, new ItemStack(Material.CRACKED_STONE_BRICKS));
    }

    private static void buildFouthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.CHERRY_DOOR));
        menu.setItem(11, new ItemStack(Material.CHERRY_TRAPDOOR));
        menu.setItem(12, new ItemStack(Material.CHERRY_PRESSURE_PLATE));
        menu.setItem(13, new ItemStack(Material.CHERRY_BUTTON));
        menu.setItem(14, new ItemStack(Material.BAMBOO_BLOCK));
        menu.setItem(15, new ItemStack(Material.STRIPPED_BAMBOO_BLOCK));
        menu.setItem(16, new ItemStack(Material.BAMBOO_PLANKS));

        menu.setItem(19, new ItemStack(Material.BAMBOO_MOSAIC));
        menu.setItem(20, new ItemStack(Material.BAMBOO_STAIRS));
        menu.setItem(21, new ItemStack(Material.BAMBOO_MOSAIC_STAIRS));
        menu.setItem(22, new ItemStack(Material.BAMBOO_SLAB));
        menu.setItem(23, new ItemStack(Material.BAMBOO_MOSAIC_SLAB));
        menu.setItem(24, new ItemStack(Material.BAMBOO_FENCE));
        menu.setItem(25, new ItemStack(Material.BAMBOO_FENCE_GATE));

        menu.setItem(28, new ItemStack(Material.BAMBOO_DOOR));
        menu.setItem(29, new ItemStack(Material.BAMBOO_TRAPDOOR));
        menu.setItem(30, new ItemStack(Material.BAMBOO_PRESSURE_PLATE));
        menu.setItem(31, new ItemStack(Material.BAMBOO_BUTTON));
        menu.setItem(32, new ItemStack(Material.CRIMSON_STEM));
        menu.setItem(33, new ItemStack(Material.STRIPPED_CRIMSON_STEM));
        menu.setItem(34, new ItemStack(Material.CRIMSON_PLANKS));

        menu.setItem(37, new ItemStack(Material.CRIMSON_STAIRS));
        menu.setItem(38, new ItemStack(Material.CRIMSON_SLAB));
        menu.setItem(39, new ItemStack(Material.CRIMSON_FENCE));
        menu.setItem(40, new ItemStack(Material.CRIMSON_FENCE_GATE));
        menu.setItem(41, new ItemStack(Material.CRIMSON_DOOR));
        menu.setItem(42, new ItemStack(Material.CRIMSON_TRAPDOOR));
        menu.setItem(43, new ItemStack(Material.CRIMSON_PRESSURE_PLATE));
    }

    private static void buildThirdPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.DARK_OAK_LOG));
        menu.setItem(11, new ItemStack(Material.STRIPPED_DARK_OAK_LOG));
        menu.setItem(12, new ItemStack(Material.DARK_OAK_PLANKS));
        menu.setItem(13, new ItemStack(Material.DARK_OAK_STAIRS));
        menu.setItem(14, new ItemStack(Material.DARK_OAK_SLAB));
        menu.setItem(15, new ItemStack(Material.DARK_OAK_FENCE));
        menu.setItem(16, new ItemStack(Material.DARK_OAK_FENCE_GATE));

        menu.setItem(19, new ItemStack(Material.DARK_OAK_DOOR));
        menu.setItem(20, new ItemStack(Material.DARK_OAK_TRAPDOOR));
        menu.setItem(21, new ItemStack(Material.DARK_OAK_PRESSURE_PLATE));
        menu.setItem(22, new ItemStack(Material.DARK_OAK_BUTTON));
        menu.setItem(23, new ItemStack(Material.MANGROVE_LOG));
        menu.setItem(24, new ItemStack(Material.STRIPPED_MANGROVE_LOG));
        menu.setItem(25, new ItemStack(Material.MANGROVE_PLANKS));

        menu.setItem(28, new ItemStack(Material.MANGROVE_STAIRS));
        menu.setItem(29, new ItemStack(Material.MANGROVE_SLAB));
        menu.setItem(30, new ItemStack(Material.MANGROVE_FENCE));
        menu.setItem(31, new ItemStack(Material.MANGROVE_FENCE_GATE));
        menu.setItem(32, new ItemStack(Material.MANGROVE_DOOR));
        menu.setItem(33, new ItemStack(Material.MANGROVE_TRAPDOOR));
        menu.setItem(34, new ItemStack(Material.MANGROVE_PRESSURE_PLATE));

        menu.setItem(37, new ItemStack(Material.MANGROVE_BUTTON));
        menu.setItem(38, new ItemStack(Material.CHERRY_LOG));
        menu.setItem(39, new ItemStack(Material.STRIPPED_CHERRY_LOG));
        menu.setItem(40, new ItemStack(Material.CHERRY_STAIRS));
        menu.setItem(41, new ItemStack(Material.CHERRY_SLAB));
        menu.setItem(42, new ItemStack(Material.CHERRY_FENCE));
        menu.setItem(43, new ItemStack(Material.CHERRY_FENCE_GATE));
    }

    private static void buildSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.BIRCH_FENCE_GATE));
        menu.setItem(11, new ItemStack(Material.BIRCH_DOOR));
        menu.setItem(12, new ItemStack(Material.BIRCH_TRAPDOOR));
        menu.setItem(13, new ItemStack(Material.BIRCH_PRESSURE_PLATE));
        menu.setItem(14, new ItemStack(Material.BIRCH_BUTTON));
        menu.setItem(15, new ItemStack(Material.JUNGLE_LOG));
        menu.setItem(16, new ItemStack(Material.OAK_FENCE_GATE));

        menu.setItem(19, new ItemStack(Material.STRIPPED_JUNGLE_LOG));
        menu.setItem(20, new ItemStack(Material.JUNGLE_PLANKS));
        menu.setItem(21, new ItemStack(Material.JUNGLE_STAIRS));
        menu.setItem(22, new ItemStack(Material.JUNGLE_SLAB));
        menu.setItem(23, new ItemStack(Material.JUNGLE_FENCE));
        menu.setItem(24, new ItemStack(Material.JUNGLE_FENCE_GATE));
        menu.setItem(25, new ItemStack(Material.JUNGLE_DOOR));

        menu.setItem(28, new ItemStack(Material.JUNGLE_TRAPDOOR));
        menu.setItem(29, new ItemStack(Material.JUNGLE_PRESSURE_PLATE));
        menu.setItem(30, new ItemStack(Material.JUNGLE_BUTTON));
        menu.setItem(31, new ItemStack(Material.ACACIA_LOG));
        menu.setItem(32, new ItemStack(Material.STRIPPED_ACACIA_LOG));
        menu.setItem(33, new ItemStack(Material.ACACIA_PLANKS));
        menu.setItem(34, new ItemStack(Material.ACACIA_STAIRS));

        menu.setItem(37, new ItemStack(Material.ACACIA_SLAB));
        menu.setItem(38, new ItemStack(Material.ACACIA_FENCE));
        menu.setItem(39, new ItemStack(Material.ACACIA_FENCE_GATE));
        menu.setItem(40, new ItemStack(Material.ACACIA_DOOR));
        menu.setItem(41, new ItemStack(Material.ACACIA_TRAPDOOR));
        menu.setItem(42, new ItemStack(Material.ACACIA_PRESSURE_PLATE));
        menu.setItem(43, new ItemStack(Material.ACACIA_BUTTON));
    }

    private static void buildFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.OAK_LOG));
        menu.setItem(11, new ItemStack(Material.STRIPPED_OAK_LOG));
        menu.setItem(12, new ItemStack(Material.OAK_PLANKS));
        menu.setItem(13, new ItemStack(Material.OAK_STAIRS));
        menu.setItem(14, new ItemStack(Material.OAK_SLAB));
        menu.setItem(15, new ItemStack(Material.OAK_FENCE));
        menu.setItem(16, new ItemStack(Material.OAK_FENCE_GATE));

        menu.setItem(19, new ItemStack(Material.OAK_DOOR));
        menu.setItem(20, new ItemStack(Material.OAK_TRAPDOOR));
        menu.setItem(21, new ItemStack(Material.OAK_PRESSURE_PLATE));
        menu.setItem(22, new ItemStack(Material.OAK_BUTTON));
        menu.setItem(23, new ItemStack(Material.SPRUCE_LOG));
        menu.setItem(24, new ItemStack(Material.STRIPPED_SPRUCE_LOG));
        menu.setItem(25, new ItemStack(Material.SPRUCE_PLANKS));

        menu.setItem(28, new ItemStack(Material.SPRUCE_STAIRS));
        menu.setItem(29, new ItemStack(Material.SPRUCE_SLAB));
        menu.setItem(30, new ItemStack(Material.SPRUCE_FENCE));
        menu.setItem(31, new ItemStack(Material.SPRUCE_FENCE_GATE));
        menu.setItem(32, new ItemStack(Material.SPRUCE_DOOR));
        menu.setItem(33, new ItemStack(Material.SPRUCE_TRAPDOOR));
        menu.setItem(34, new ItemStack(Material.SPRUCE_PRESSURE_PLATE));

        menu.setItem(37, new ItemStack(Material.SPRUCE_BUTTON));
        menu.setItem(38, new ItemStack(Material.BIRCH_LOG));
        menu.setItem(39, new ItemStack(Material.STRIPPED_BIRCH_LOG));
        menu.setItem(40, new ItemStack(Material.BIRCH_PLANKS));
        menu.setItem(41, new ItemStack(Material.BIRCH_STAIRS));
        menu.setItem(42, new ItemStack(Material.BIRCH_SLAB));
        menu.setItem(43, new ItemStack(Material.BIRCH_FENCE));
    }


    private static void naturalSeventhPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.SLIME_BLOCK));
        menu.setItem(11, new ItemStack(Material.HONEYCOMB_BLOCK));
        menu.setItem(12, new ItemStack(Material.OCHRE_FROGLIGHT));
        menu.setItem(13, new ItemStack(Material.VERDANT_FROGLIGHT));
        menu.setItem(14, new ItemStack(Material.PEARLESCENT_FROGLIGHT));
        menu.setItem(15, new ItemStack(Material.SCULK));
        menu.setItem(16, new ItemStack(Material.SCULK_VEIN));

        menu.setItem(19, new ItemStack(Material.SCULK_CATALYST));
        menu.setItem(20, new ItemStack(Material.SCULK_SHRIEKER));
        menu.setItem(21, new ItemStack(Material.SCULK_SENSOR));
        menu.setItem(22, new ItemStack(Material.COBWEB));
    }


    private static void naturalSixthhPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.BRAIN_CORAL));
        menu.setItem(11, new ItemStack(Material.BUBBLE_CORAL));
        menu.setItem(12, new ItemStack(Material.FIRE_CORAL));
        menu.setItem(13, new ItemStack(Material.HORN_CORAL));
        menu.setItem(14, new ItemStack(Material.DEAD_TUBE_CORAL));
        menu.setItem(15, new ItemStack(Material.DEAD_BRAIN_CORAL));
        menu.setItem(16, new ItemStack(Material.DEAD_BUBBLE_CORAL));

        menu.setItem(19, new ItemStack(Material.DEAD_FIRE_CORAL));
        menu.setItem(20, new ItemStack(Material.DEAD_HORN_CORAL));
        menu.setItem(21, new ItemStack(Material.TUBE_CORAL_FAN));
        menu.setItem(22, new ItemStack(Material.BRAIN_CORAL_FAN));
        menu.setItem(23, new ItemStack(Material.BUBBLE_CORAL_FAN));
        menu.setItem(24, new ItemStack(Material.FIRE_CORAL_FAN));
        menu.setItem(25, new ItemStack(Material.HORN_CORAL_FAN));

        menu.setItem(28, new ItemStack(Material.DEAD_TUBE_CORAL_FAN));
        menu.setItem(29, new ItemStack(Material.DEAD_BRAIN_CORAL_FAN));
        menu.setItem(30, new ItemStack(Material.DEAD_BUBBLE_CORAL_FAN));
        menu.setItem(31, new ItemStack(Material.DEAD_FIRE_CORAL_FAN));
        menu.setItem(32, new ItemStack(Material.DEAD_HORN_CORAL_FAN));
        menu.setItem(33, new ItemStack(Material.SPONGE));
        menu.setItem(34, new ItemStack(Material.WET_SPONGE));

        menu.setItem(37, new ItemStack(Material.MELON));
        menu.setItem(38, new ItemStack(Material.PUMPKIN));
        menu.setItem(39, new ItemStack(Material.CARVED_PUMPKIN));
        menu.setItem(40, new ItemStack(Material.JACK_O_LANTERN));
        menu.setItem(41, new ItemStack(Material.HAY_BLOCK));
        menu.setItem(42, new ItemStack(Material.BEE_NEST));
        menu.setItem(43, new ItemStack(Material.HONEYCOMB_BLOCK));
    }


    private static void naturalFifthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.PEONY));
        menu.setItem(11, new ItemStack(Material.PITCHER_PLANT));
        menu.setItem(12, new ItemStack(Material.AZURE_BLUET));
        menu.setItem(13, new ItemStack(Material.BIG_DRIPLEAF));
        menu.setItem(14, new ItemStack(Material.SMALL_DRIPLEAF));
        menu.setItem(15, new ItemStack(Material.CHORUS_PLANT));
        menu.setItem(16, new ItemStack(Material.CHORUS_FLOWER));

        menu.setItem(19, new ItemStack(Material.GLOW_LICHEN));
        menu.setItem(20, new ItemStack(Material.HANGING_ROOTS));
        menu.setItem(21, new ItemStack(Material.FROGSPAWN));
        menu.setItem(22, new ItemStack(Material.TURTLE_EGG));
        menu.setItem(23, new ItemStack(Material.SNIFFER_EGG));
        menu.setItem(24, new ItemStack(Material.LILY_PAD));
        menu.setItem(25, new ItemStack(Material.SEAGRASS));

        menu.setItem(28, new ItemStack(Material.SEA_PICKLE));
        menu.setItem(29, new ItemStack(Material.KELP));
        menu.setItem(30, new ItemStack(Material.DRIED_KELP_BLOCK));
        menu.setItem(31, new ItemStack(Material.TUBE_CORAL_BLOCK));
        menu.setItem(32, new ItemStack(Material.BRAIN_CORAL_BLOCK));
        menu.setItem(33, new ItemStack(Material.BUBBLE_CORAL_BLOCK));
        menu.setItem(34, new ItemStack(Material.FIRE_CORAL_BLOCK));

        menu.setItem(37, new ItemStack(Material.HORN_CORAL_BLOCK));
        menu.setItem(38, new ItemStack(Material.DEAD_TUBE_CORAL_BLOCK));
        menu.setItem(39, new ItemStack(Material.DEAD_BRAIN_CORAL_BLOCK));
        menu.setItem(40, new ItemStack(Material.DEAD_BUBBLE_CORAL_BLOCK));
        menu.setItem(41, new ItemStack(Material.DEAD_FIRE_CORAL_BLOCK));
        menu.setItem(42, new ItemStack(Material.DEAD_HORN_CORAL_BLOCK));
        menu.setItem(43, new ItemStack(Material.TUBE_CORAL));
    }


    private static void naturalFourthPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.BLUE_ORCHID));
        menu.setItem(11, new ItemStack(Material.ALLIUM));
        menu.setItem(12, new ItemStack(Material.AZURE_BLUET));
        menu.setItem(13, new ItemStack(Material.RED_TULIP));
        menu.setItem(14, new ItemStack(Material.ORANGE_TULIP));
        menu.setItem(15, new ItemStack(Material.WHITE_TULIP));
        menu.setItem(16, new ItemStack(Material.PINK_TULIP));

        menu.setItem(19, new ItemStack(Material.OXEYE_DAISY));
        menu.setItem(20, new ItemStack(Material.CORNFLOWER));
        menu.setItem(21, new ItemStack(Material.LILY_OF_THE_VALLEY));
        menu.setItem(22, new ItemStack(Material.TORCHFLOWER));
        menu.setItem(23, new ItemStack(Material.WITHER_ROSE));
        menu.setItem(24, new ItemStack(Material.PINK_PETALS));
        menu.setItem(25, new ItemStack(Material.SPORE_BLOSSOM));

        menu.setItem(28, new ItemStack(Material.BAMBOO));
        menu.setItem(29, new ItemStack(Material.SUGAR_CANE));
        menu.setItem(30, new ItemStack(Material.CACTUS));
        menu.setItem(31, new ItemStack(Material.CRIMSON_ROOTS));
        menu.setItem(32, new ItemStack(Material.WARPED_ROOTS));
        menu.setItem(33, new ItemStack(Material.NETHER_SPROUTS));
        menu.setItem(34, new ItemStack(Material.WEEPING_VINES));

        menu.setItem(37, new ItemStack(Material.TWISTING_VINES));
        menu.setItem(38, new ItemStack(Material.VINE));
        menu.setItem(39, new ItemStack(Material.TALL_GRASS));
        menu.setItem(40, new ItemStack(Material.LARGE_FERN));
        menu.setItem(41, new ItemStack(Material.SUNFLOWER));
        menu.setItem(42, new ItemStack(Material.LILAC));
        menu.setItem(43, new ItemStack(Material.ROSE_BUSH));
    }


    private static void naturalThridPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.MANGROVE_LEAVES));
        menu.setItem(11, new ItemStack(Material.CHERRY_LEAVES));
        menu.setItem(12, new ItemStack(Material.AZALEA_LEAVES));
        menu.setItem(13, new ItemStack(Material.FLOWERING_AZALEA_LEAVES));
        menu.setItem(14, new ItemStack(Material.BROWN_MUSHROOM_BLOCK));
        menu.setItem(15, new ItemStack(Material.RED_MUSHROOM_BLOCK));
        menu.setItem(16, new ItemStack(Material.NETHER_WART_BLOCK));

        menu.setItem(19, new ItemStack(Material.WARPED_WART_BLOCK));
        menu.setItem(20, new ItemStack(Material.SHROOMLIGHT));
        menu.setItem(21, new ItemStack(Material.OAK_SAPLING));
        menu.setItem(22, new ItemStack(Material.SPRUCE_SAPLING));
        menu.setItem(23, new ItemStack(Material.BIRCH_SAPLING));
        menu.setItem(24, new ItemStack(Material.JUNGLE_SAPLING));
        menu.setItem(25, new ItemStack(Material.ACACIA_SAPLING));

        menu.setItem(28, new ItemStack(Material.DARK_OAK_SAPLING));
        menu.setItem(29, new ItemStack(Material.MANGROVE_PROPAGULE));
        menu.setItem(30, new ItemStack(Material.CHERRY_SAPLING));
        menu.setItem(31, new ItemStack(Material.AZALEA));
        menu.setItem(32, new ItemStack(Material.FLOWERING_AZALEA));
        menu.setItem(33, new ItemStack(Material.BROWN_MUSHROOM));
        menu.setItem(34, new ItemStack(Material.RED_MUSHROOM));

        menu.setItem(37, new ItemStack(Material.CRIMSON_FUNGUS));
        menu.setItem(38, new ItemStack(Material.WARPED_FUNGUS));
        menu.setItem(39, new ItemStack(Material.GRASS));
        menu.setItem(40, new ItemStack(Material.FERN));
        menu.setItem(41, new ItemStack(Material.DEAD_BUSH));
        menu.setItem(42, new ItemStack(Material.DANDELION));
        menu.setItem(43, new ItemStack(Material.POPPY));
    }


    private static void naturalSecondPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.TUFF));
        menu.setItem(11, new ItemStack(Material.DRIPSTONE_BLOCK));
        menu.setItem(12, new ItemStack(Material.POINTED_DRIPSTONE));
        menu.setItem(13, new ItemStack(Material.PRISMARINE));
        menu.setItem(14, new ItemStack(Material.MAGMA_BLOCK));
        menu.setItem(15, new ItemStack(Material.OBSIDIAN));
        menu.setItem(16, new ItemStack(Material.CRYING_OBSIDIAN));

        menu.setItem(19, new ItemStack(Material.NETHERRACK));
        menu.setItem(20, new ItemStack(Material.CRIMSON_NYLIUM));
        menu.setItem(21, new ItemStack(Material.WARPED_NYLIUM));
        menu.setItem(22, new ItemStack(Material.SOUL_SAND));
        menu.setItem(23, new ItemStack(Material.SOUL_SOIL));
        menu.setItem(24, new ItemStack(Material.BONE_BLOCK));
        menu.setItem(25, new ItemStack(Material.BLACKSTONE));

        menu.setItem(28, new ItemStack(Material.BASALT));
        menu.setItem(29, new ItemStack(Material.SMOOTH_BASALT));
        menu.setItem(30, new ItemStack(Material.END_STONE));
        menu.setItem(31, new ItemStack(Material.GLOWSTONE));
        menu.setItem(32, new ItemStack(Material.AMETHYST_BLOCK));
        menu.setItem(33, new ItemStack(Material.SMALL_AMETHYST_BUD));
        menu.setItem(34, new ItemStack(Material.MEDIUM_AMETHYST_BUD));

        menu.setItem(37, new ItemStack(Material.LARGE_AMETHYST_BUD));
        menu.setItem(38, new ItemStack(Material.OAK_LEAVES));
        menu.setItem(39, new ItemStack(Material.SPRUCE_LEAVES));
        menu.setItem(40, new ItemStack(Material.BIRCH_LEAVES));
        menu.setItem(41, new ItemStack(Material.JUNGLE_LEAVES));
        menu.setItem(42, new ItemStack(Material.ACACIA_LEAVES));
        menu.setItem(43, new ItemStack(Material.DARK_OAK_LEAVES));
    }

    private static void naturalFirstPage(Inventory menu) {
        menu.setItem(10, new ItemStack(Material.GRASS_BLOCK));
        menu.setItem(11, new ItemStack(Material.PODZOL));
        menu.setItem(12, new ItemStack(Material.MYCELIUM));
        menu.setItem(13, new ItemStack(Material.DIRT_PATH));
        menu.setItem(14, new ItemStack(Material.DIRT));
        menu.setItem(15, new ItemStack(Material.COARSE_DIRT));
        menu.setItem(16, new ItemStack(Material.ROOTED_DIRT));

        menu.setItem(19, new ItemStack(Material.FARMLAND));
        menu.setItem(20, new ItemStack(Material.MUD));
        menu.setItem(21, new ItemStack(Material.CLAY));
        menu.setItem(22, new ItemStack(Material.GRAVEL));
        menu.setItem(23, new ItemStack(Material.SAND));
        menu.setItem(24, new ItemStack(Material.SANDSTONE));
        menu.setItem(25, new ItemStack(Material.RED_SAND));

        menu.setItem(28, new ItemStack(Material.RED_SANDSTONE));
        menu.setItem(29, new ItemStack(Material.ICE));
        menu.setItem(30, new ItemStack(Material.PACKED_ICE));
        menu.setItem(31, new ItemStack(Material.BLUE_ICE));
        menu.setItem(32, new ItemStack(Material.SNOW_BLOCK));
        menu.setItem(33, new ItemStack(Material.SNOW));
        menu.setItem(34, new ItemStack(Material.MOSS_BLOCK));

        menu.setItem(37, new ItemStack(Material.MOSS_CARPET));
        menu.setItem(38, new ItemStack(Material.STONE));
        menu.setItem(39, new ItemStack(Material.DEEPSLATE));
        menu.setItem(40, new ItemStack(Material.GRANITE));
        menu.setItem(41, new ItemStack(Material.DIORITE));
        menu.setItem(42, new ItemStack(Material.ANDESITE));
        menu.setItem(43, new ItemStack(Material.CALCITE));
    }

    public static void setMaxPageCount() {

        for (String category : SellGui.categoryNames) {
            if (category.equalsIgnoreCase(ChatColor.BLUE + "Naturalne Bloki")) {
                maxPageCount.put(category, 7);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Budowlane Bloki")) {
                maxPageCount.put(category, 12);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Funkcjonalne Bloki")) {
                maxPageCount.put(category, 4);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Walka")) {
                maxPageCount.put(category, 5);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Mechanizmy")) {
                maxPageCount.put(category, 2);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Jedzenie")) {
                maxPageCount.put(category, 2);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Surowce")) {
                maxPageCount.put(category, 6);
            } else if (category.equalsIgnoreCase(ChatColor.BLUE + "Kolorowe Bloki")) {
                maxPageCount.put(category, 9);
            } else {
                maxPageCount.put(category, 1);
            }
        }
    }
}
