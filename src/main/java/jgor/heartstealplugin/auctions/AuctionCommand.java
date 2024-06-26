package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.HeartStealListener;
import jgor.heartstealplugin.HeartStealPlugin;
import jgor.heartstealplugin.PlayerLocalOffer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.StringUtil;

import java.util.*;


public class AuctionCommand implements CommandExecutor, TabCompleter {

    private static final HashMap<UUID, ArrayList<Integer>> playerItemAmount = new HashMap<>();

    private static final HashMap<UUID, ArrayList<ItemStack>> playerSellingItem = new HashMap<>();

    private static final HashMap<UUID, ArrayList<Integer>> playerWishedAmountToRemove = new HashMap<>();

    private static final HashMap<UUID,ArrayList<PlayerLocalOffer>> playerLocalOffers = new HashMap<>();
    private static final int MAX_ITEM_AMOUNT = 2304;

    private final HeartStealListener heartStealListener;

    private static final String[] COMMANDS = {"wystaw"};
    private static final String[] COMMANDS2 = {"lokalnie","stak","<ilość>"};

    private static final String[] COMMANDS3 = {"<ilość ofert>"};
    private static final String[] COMMANDS31 = {"<ilość staków>"};
    private static final String[] COMMANDS4 = {"<ilość w jednej ofercie>"};

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        // create new array
        final List<String> completions = new ArrayList<>();

        // Check if strings array has at least one element
        if (strings.length == 1) {
            // copy matches of first argument from list
            StringUtil.copyPartialMatches(strings[0], Arrays.asList(COMMANDS), completions);
        } else if (strings.length == 2) {
            StringUtil.copyPartialMatches(strings[1], Arrays.asList(COMMANDS2), completions);
        } else if (strings.length == 3) {
            if (strings[1].equalsIgnoreCase("lokalnie")) {
                StringUtil.copyPartialMatches(strings[2], Arrays.asList(COMMANDS3), completions);
            } else if (strings[1].equalsIgnoreCase("stak")) {
                StringUtil.copyPartialMatches(strings[2], Arrays.asList(COMMANDS31), completions);
            }
        } else if (strings.length == 4) {
            if (strings[1].equalsIgnoreCase("lokalnie"))
                StringUtil.copyPartialMatches(strings[3], Arrays.asList(COMMANDS4), completions);
        }

        // Check if strings array has at least two elements
//        if (strings.length > 1 && strings.length < 3) {
//            StringUtil.copyPartialMatches(strings[1], Arrays.asList(COMMANDS2), completions);
//        }
//
//        // Check if strings array has at least three elements
//        if (strings.length > 2) {
//            if (strings[1].equalsIgnoreCase("lokalnie")) {
//                StringUtil.copyPartialMatches(strings[2], Arrays.asList(COMMANDS3), completions);
//            } else if (strings[1].equalsIgnoreCase("stak")) {
//                StringUtil.copyPartialMatches(strings[2], Arrays.asList(COMMANDS31), completions);
//            }
//
//        }
//
//        if (strings.length > 3) {
//            if (strings[1].equalsIgnoreCase("lokalnie"))
//                StringUtil.copyPartialMatches(strings[3], Arrays.asList(COMMANDS4), completions);
//        }

        // sort the list
        Collections.sort(completions);
        return completions;
    }


    public AuctionCommand(HeartStealPlugin plugin) {
        this.heartStealListener = plugin.getHeartStealListener();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Tylko dla graczy!");
            return true;
        }

        if (heartStealListener.getPlayersInCombat().getOrDefault(player.getUniqueId(), false)) {
            player.sendMessage(ChatColor.RED + "Nie masz dostępu do rynku podczas walki");
            return true;
        }


        if (strings.length == 0) {

            MarketGui.openMarketInventory(player, 1);

        } else if (strings.length == 2 && strings[0].equals("wystaw")) {

            checkTypedAmountCommand(strings, player);


        } else if (strings.length == 3 && strings[0].equalsIgnoreCase("wystaw") && strings[1].equalsIgnoreCase("stak")) {

            checkTypedStackCommand(strings, player);


        } else if (strings.length == 4 && strings[0].equalsIgnoreCase("wystaw") && strings[1].equalsIgnoreCase("lokalnie")) {

            checkTypedLocalMarketItemAmount(player, strings);

        }


        return true;
    }


    //FIXME LOCAL
    private void checkTypedLocalMarketItemAmount(Player player, String[] strings) {
        try {

            int number_of_offers = Integer.parseInt(strings[2]);
            int item_amount_in_one_offer = Integer.parseInt(strings[3]);

            if (checkLocalPossibility(player, number_of_offers, item_amount_in_one_offer)) {
                SellGui.openSellGui(player,"lokalnie");
            }


        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Poprawne użycie: /rynek wystaw lokalnie <ilość ofert> <ilość>");
        } catch (LocalException e) {
            e.sendErrorMessageToPlayer(player);
        }

    }

    private boolean checkLocalPossibility(Player player, int number_of_offers, int item_amount_in_one_offer) throws LocalException {

        PlayerInventory playerInventory = player.getInventory();
        ItemStack playerItemInMainHand = playerInventory.getItemInMainHand();

        if (playerItemInMainHand.isSimilar(new ItemStack(Material.AIR))) {
            throw new LocalException("Null");
        }


        if (playerItemInMainHand.getMaxStackSize() < item_amount_in_one_offer) {
            throw new LocalException("AboveStack");
        }

        int player_wanted_amount = number_of_offers * item_amount_in_one_offer;

        if (player_wanted_amount > MAX_ITEM_AMOUNT) {
            throw new LocalException("Max");
        }


        int currentAmount = 0;
        for (ItemStack item : playerInventory.getStorageContents()) {
            if (item != null && item.isSimilar(playerItemInMainHand)) {
                currentAmount += item.getAmount();
            }
        }

        if (currentAmount < player_wanted_amount) throw new LocalException("NotEnough");
        else {
            PlayerLocalOffer offer = new PlayerLocalOffer(player.getUniqueId(),number_of_offers,item_amount_in_one_offer,playerItemInMainHand.clone());
            //TODO TO BE CONTINUE.....

            if (playerLocalOffers.containsKey(player.getUniqueId())) {
                ArrayList<PlayerLocalOffer> playerOfferList = playerLocalOffers.get(player.getUniqueId());
                playerOfferList.add(offer);
                playerLocalOffers.put(player.getUniqueId(),playerOfferList);
            } else {
                ArrayList<PlayerLocalOffer> playerLocalOffer = new ArrayList<>();

                playerLocalOffer.add(offer);

                playerLocalOffers.put(player.getUniqueId(),playerLocalOffer);
            }


            return true;
        }


    }

    public static void removeLastOffer(UUID playerUUID) {
        playerLocalOffers.get(playerUUID).removeLast();
    }
    //FIXME END LOCAL

    //TODO VALUES
    private void checkTypedAmountCommand(String[] strings, Player player) {
        try {
            int itemAmount = Integer.parseInt(strings[1]);

            int amountToRemove = checkValue(player, itemAmount);

            if (amountToRemove != -1) {
                if (!playerWishedAmountToRemove.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> wishedAmountToRemove = new ArrayList<>();
                    wishedAmountToRemove.add(itemAmount);
                    playerWishedAmountToRemove.put(player.getUniqueId(), wishedAmountToRemove);
                } else {
                    ArrayList<Integer> wishedAmountToRemove = playerWishedAmountToRemove.get(player.getUniqueId());
                    wishedAmountToRemove.add(itemAmount);
                    playerWishedAmountToRemove.replace(player.getUniqueId(), wishedAmountToRemove);
                }

                if (!playerItemAmount.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> amountToRemoveArray = new ArrayList<>();
                    amountToRemoveArray.add(amountToRemove);
                    playerItemAmount.put(player.getUniqueId(), amountToRemoveArray);
                } else {
                    ArrayList<Integer> amountToRemoveArray = playerItemAmount.get(player.getUniqueId());
                    amountToRemoveArray.add(amountToRemove);
                    playerItemAmount.replace(player.getUniqueId(), amountToRemoveArray);
                }


                SellGui.openSellGui(player);
            }


        } catch (NumberFormatException e) {

            player.sendMessage(ChatColor.RED + "Poprawne użycie: /rynek wystaw <ilość 1-64>");
        }
    }

    private int checkValue(Player player, int expectedAmount) {
        if (expectedAmount <= 0 || expectedAmount > 64) throw new NumberFormatException("Idiot");

        int currentValue = 0;

        ItemStack itemInMain = player.getInventory().getItemInMainHand();

        if (itemInMain.isSimilar(new ItemStack(Material.AIR))) {
            player.sendMessage(ChatColor.RED + "Nie możesz tego wystawić");
            return -1;
        }

        ItemStack[] inventory = player.getInventory().getContents();

        for (ItemStack check : inventory) {
            if (check != null && check.isSimilar(itemInMain)) {
                currentValue += check.getAmount();
            }
        }

        if (currentValue >= expectedAmount) {

            if (!playerSellingItem.containsKey(player.getUniqueId())) {
                ArrayList<ItemStack> itemList = new ArrayList<>();
                itemList.add(itemInMain);
                playerSellingItem.put(player.getUniqueId(), itemList);
            } else {
                ArrayList<ItemStack> itemList = playerSellingItem.get(player.getUniqueId());
                itemList.add(itemInMain);
                playerSellingItem.replace(player.getUniqueId(), itemList);
            }

            return currentValue - expectedAmount;
        } else
            player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + (expectedAmount - currentValue));


        return -1;
    }

    //TODO END VALUES

    //TODO STACKS
    private void checkTypedStackCommand(String[] strings, Player player) {
        try {
            int stackAmount = Integer.parseInt(strings[2]);
            int stacksToRemove = checkStackValue(player, stackAmount);

            if (stacksToRemove != -1) {
                if (!playerWishedAmountToRemove.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> wishedAmountToRemove = new ArrayList<>();
                    wishedAmountToRemove.add(stackAmount * 64);
                    playerWishedAmountToRemove.put(player.getUniqueId(), wishedAmountToRemove);
                } else {
                    ArrayList<Integer> wishedAmountToRemove = playerWishedAmountToRemove.get(player.getUniqueId());
                    wishedAmountToRemove.add(stackAmount * 64);
                    playerWishedAmountToRemove.replace(player.getUniqueId(), wishedAmountToRemove);
                }

                if (!playerItemAmount.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> amountToRemoveArray = new ArrayList<>();
                    amountToRemoveArray.add(stacksToRemove);
                    playerItemAmount.put(player.getUniqueId(), amountToRemoveArray);
                } else {
                    ArrayList<Integer> amountToRemoveArray = playerItemAmount.get(player.getUniqueId());
                    amountToRemoveArray.add(stacksToRemove);
                    playerItemAmount.replace(player.getUniqueId(), amountToRemoveArray);
                }

                SellGui.openSellGui(player);
            }

        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Poprawne użycie: /rynek wystaw stak <ilość w stakach 1-36>");
        }

    }

    private int checkStackValue(Player player, int stack) {
        if (stack <= 0 || stack > 36) throw new NumberFormatException("Idiot");

        int expectedValue = stack * 64;
        int currentValue = 0;

        ItemStack itemInMain = player.getInventory().getItemInMainHand();

        if (itemInMain.isSimilar(new ItemStack(Material.AIR))) {
            player.sendMessage(ChatColor.RED + "Nie możesz tego wystawić");
            return -1;
        }

        ItemStack[] inventory = player.getInventory().getContents();

        for (ItemStack check : inventory) {
            if (check != null && check.isSimilar(itemInMain)) {
                currentValue += check.getAmount();
            }
        }

        if (currentValue >= expectedValue) {

            if (!playerSellingItem.containsKey(player.getUniqueId())) {
                ArrayList<ItemStack> itemList = new ArrayList<>();
                itemList.add(itemInMain);
                playerSellingItem.put(player.getUniqueId(), itemList);
            } else {
                ArrayList<ItemStack> itemList = playerSellingItem.get(player.getUniqueId());
                itemList.add(itemInMain);
                playerSellingItem.replace(player.getUniqueId(), itemList);
            }

            return currentValue - expectedValue;
        } else {
            int lackValue = expectedValue - currentValue;
            if (lackValue < 64) player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + lackValue);
            else {
                int stacks;
                if (lackValue % 64 == 0) {
                    stacks = lackValue / 64;
                    player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + stacks + "x64");
                } else {
                    stacks = lackValue / 64;
                    int a = lackValue - (stacks * 64);
                    player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + stacks + "x64" + " i " + a);
                }
            }
        }

        return -1;
    }

    //TODO END STACKS


    public static HashMap<UUID, ArrayList<Integer>> getPlayerItemAmount() {
        return playerItemAmount;
    }

    public static HashMap<UUID, ArrayList<Integer>> getPlayerWishedAmountToRemove() {
        return playerWishedAmountToRemove;
    }

    public static HashMap<UUID, ArrayList<ItemStack>> getPlayerSellingItems() {
        return playerSellingItem;
    }

    public static HashMap<UUID,ArrayList<PlayerLocalOffer>> getPlayerLocalOffers() {return playerLocalOffers;}
}

class LocalException extends Exception {
    private final String message;

    public LocalException(String message) {
        super(message);
        this.message = message;
    }

    public void sendErrorMessageToPlayer(Player player) {
        switch (message) {
            case "Null" : {
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hej! " + ChatColor.RESET + ChatColor.GRAY + "Nie możesz tego wystawić");
                break;
            }
            case "AboveStack": {
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hej! " + ChatColor.RESET + ChatColor.GRAY + "Nie możesz wystawić tego przedmiotu w takiej ilości");
                break;
            }
            case "Max": {
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hej! " + ChatColor.RESET + ChatColor.GRAY + "Przekroczono maksymalną ilość!");
                break;
            }
            case "NotEnough": {
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hej! " + ChatColor.RESET + ChatColor.GRAY + "Posiadasz nie wystarczającą ilość!");
                break;
            }
            default:
                player.sendMessage(ChatColor.RED + "Poprawne użycie /rynek wystaw lokalnie <ilość ofert> <ilość>");
        }
    }
}

