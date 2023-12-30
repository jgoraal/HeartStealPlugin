package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.HeartStealListener;
import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class AuctionCommand implements CommandExecutor {

    private static final HashMap<UUID, ArrayList<Integer>> playerItemAmount = new HashMap<>();

    private static final HashMap<UUID, ArrayList<ItemStack>> playerSellingItem = new HashMap<>();

    private static final HashMap<UUID, ArrayList<Integer>> playerWishedAmountToRemove = new HashMap<>();

    private HeartStealListener heartStealListener;

    public AuctionCommand(HeartStealPlugin plugin) {
        this.heartStealListener = plugin.getHeartStealListener();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Tylko dla graczy!");
            return true;
        }

        Player player = (Player) commandSender;


            if (strings.length == 0) {
                if(heartStealListener.getPlayersInCombat().getOrDefault(player.getUniqueId(),false)) {
                    player.sendMessage(ChatColor.RED + "Nie masz dostępu do rynku podczas walki");
                } else {
                    MarketGui.openMarketInventory(player, 1);
                }
            } else if (strings.length == 2 && strings[0].equals("wystaw")) {
                if(heartStealListener.getPlayersInCombat().getOrDefault(player.getUniqueId(),false)) {
                    player.sendMessage(ChatColor.RED + "Nie masz dostępu do rynku podczas walki");
                } else {
                    checkTypedAmountCommand(strings, player);
                }

            } else if (strings.length == 3 && strings[0].equalsIgnoreCase("wystaw") && strings[1].equalsIgnoreCase("stak")) {
                if(heartStealListener.getPlayersInCombat().getOrDefault(player.getUniqueId(),false)) {
                    player.sendMessage(ChatColor.RED + "Nie masz dostępu do rynku podczas walki");
                } else {
                    checkTypedStackCommand(strings, player);
                }


            }




        return true;
    }

    //TODO VALUES
    private void checkTypedAmountCommand(String[] strings, Player player) {
        try {
            int itemAmount = Integer.parseInt(strings[1]);

            int amountToRemove = checkValue(player, itemAmount);

            if(amountToRemove != -1) {
                if(!playerWishedAmountToRemove.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> wishedAmountToRemove = new ArrayList<>();
                    wishedAmountToRemove.add(itemAmount);
                    playerWishedAmountToRemove.put(player.getUniqueId(),wishedAmountToRemove);
                } else {
                    ArrayList<Integer> wishedAmountToRemove = playerWishedAmountToRemove.get(player.getUniqueId());
                    wishedAmountToRemove.add(itemAmount);
                    playerWishedAmountToRemove.replace(player.getUniqueId(),wishedAmountToRemove);
                }

                if(!playerItemAmount.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> amountToRemoveArray = new ArrayList<>();
                    amountToRemoveArray.add(amountToRemove);
                    playerItemAmount.put(player.getUniqueId(),amountToRemoveArray);
                } else {
                    ArrayList<Integer> amountToRemoveArray = playerItemAmount.get(player.getUniqueId());
                    amountToRemoveArray.add(amountToRemove);
                    playerItemAmount.replace(player.getUniqueId(),amountToRemoveArray);
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

        if(itemInMain.isSimilar(new ItemStack(Material.AIR))) {
            player.sendMessage(ChatColor.RED + "Nie możesz tego wystawić");
            return -1;
        }

        ItemStack[] inventory = player.getInventory().getContents();

        for (ItemStack check : inventory) {
            if (check != null && check.isSimilar(itemInMain)) {
                currentValue += check.getAmount();
            }
        }

        if(currentValue >= expectedAmount) {

            if(!playerSellingItem.containsKey(player.getUniqueId())) {
                ArrayList<ItemStack> itemList = new ArrayList<>();
                itemList.add(itemInMain);
                playerSellingItem.put(player.getUniqueId(), itemList);
            } else {
                ArrayList<ItemStack> itemList = playerSellingItem.get(player.getUniqueId());
                itemList.add(itemInMain);
                playerSellingItem.replace(player.getUniqueId(), itemList);
            }

            return currentValue - expectedAmount;
        }
        else player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + (expectedAmount - currentValue));


        return -1;
    }

    //TODO END VALUES

    //TODO STACKS
    private void checkTypedStackCommand(String[] strings, Player player) {
        try {
            int stackAmount = Integer.parseInt(strings[2]);
            int stacksToRemove = checkStackValue(player, stackAmount);

            if(stacksToRemove != -1) {
                if(!playerWishedAmountToRemove.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> wishedAmountToRemove = new ArrayList<>();
                    wishedAmountToRemove.add(stackAmount * 64);
                    playerWishedAmountToRemove.put(player.getUniqueId(),wishedAmountToRemove);
                } else {
                    ArrayList<Integer> wishedAmountToRemove = playerWishedAmountToRemove.get(player.getUniqueId());
                    wishedAmountToRemove.add(stackAmount * 64);
                    playerWishedAmountToRemove.replace(player.getUniqueId(),wishedAmountToRemove);
                }

                if(!playerItemAmount.containsKey(player.getUniqueId())) {
                    ArrayList<Integer> amountToRemoveArray = new ArrayList<>();
                    amountToRemoveArray.add(stacksToRemove);
                    playerItemAmount.put(player.getUniqueId(),amountToRemoveArray);
                } else {
                    ArrayList<Integer> amountToRemoveArray = playerItemAmount.get(player.getUniqueId());
                    amountToRemoveArray.add(stacksToRemove);
                    playerItemAmount.replace(player.getUniqueId(),amountToRemoveArray);
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

        if(itemInMain.isSimilar(new ItemStack(Material.AIR))) {
            player.sendMessage(ChatColor.RED + "Nie możesz tego wystawić");
            return -1;
        }

        ItemStack[] inventory = player.getInventory().getContents();

        for (ItemStack check : inventory) {
            if (check != null && check.isSimilar(itemInMain)) {
                currentValue += check.getAmount();
            }
        }

        if(currentValue >= expectedValue) {

            if(!playerSellingItem.containsKey(player.getUniqueId())) {
                ArrayList<ItemStack> itemList = new ArrayList<>();
                itemList.add(itemInMain);
                playerSellingItem.put(player.getUniqueId(), itemList);
            } else {
                ArrayList<ItemStack> itemList = playerSellingItem.get(player.getUniqueId());
                itemList.add(itemInMain);
                playerSellingItem.replace(player.getUniqueId(), itemList);
            }

            return currentValue - expectedValue;
        }
        else {
            int lackValue = expectedValue - currentValue;
            if(lackValue < 64) player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + lackValue);
            else {
                int stacks;
                if(lackValue % 64 == 0) {
                    stacks = lackValue/64;
                    player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + stacks + "x64");
                } else {
                    stacks = lackValue/64;
                    int a = lackValue - (stacks * 64);
                    player.sendMessage(ChatColor.RED + "Masz za mało tego itemu, brakuje ci " + stacks + "x64" +" i " + a);
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


}
