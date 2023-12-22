package jgor.heartstealplugin;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;


public class RedemptionItem implements Listener {

    @EventHandler
    public void onRedemptionItemUse(BlockPlaceEvent event) {
        ItemStack handItem = event.getItemInHand();
        Player player = event.getPlayer();

        PersistentDataContainer data = handItem.getItemMeta().getPersistentDataContainer();

        if (handItem.isSimilar(createRedemptionItem())) {
            if (data.has(new NamespacedKey(HeartStealPlugin.getInstance(), "RedemptionItem"), PersistentDataType.STRING)) {
                // Anuluj zdarzenie, aby blok nie został postawiony


                // Usuń przedmiot "Odkupienie" z ekwipunku gracza
                if (handItem.getAmount() > 1) {
                    handItem.setAmount(handItem.getAmount() - 1);
                } else {
                    player.getInventory().removeItem(handItem);
                }

                event.setCancelled(true);

                // Postaw tabliczkę na miejscu gracza
                // Stwórz książkę
                ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
                BookMeta bookMeta = (BookMeta) book.getItemMeta();
                bookMeta.setTitle(ChatColor.GREEN + "Wpisz nazwę gracza, którego chcesz ocalić");
                bookMeta.setDisplayName(ChatColor.GREEN + "Notes Odkupienia");
                bookMeta.setPages(Arrays.asList(ChatColor.GREEN + "Tutaj napisz nick gracza: "));
                book.setItemMeta(bookMeta);

                // Dodaj książkę do ekwipunku gracza
                player.getInventory().addItem(book);
            }
        }
    }

    @EventHandler
    public void onBookEdit(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        BookMeta bookMeta = event.getPreviousBookMeta();
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        book.setItemMeta(bookMeta);

        // Sprawdź, czy gracz wpisał nick gracza po odpowiednim tekście
        String text = event.getNewBookMeta().getPage(1).trim(); // Usuń białe znaki na początku i na końcu
        String initialText = ChatColor.GREEN + "Tutaj napisz nick gracza: ";

        if (text.startsWith(initialText)) {
            String playerName = text.substring(initialText.length()).trim(); // Pobierz nick gracza
            if (playerName.contains(" ")) {
                // Jeśli nick gracza zawiera spacje, wyślij wiadomość do gracza
                player.sendMessage(ChatColor.RED + "Nick gracza nie może zawierać spacji!");
                Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().removeItem(player.getInventory().getItemInMainHand()), 2L);
                Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().setItemInMainHand(book), 2L);

            } else {
                // Jeśli gracz wpisał tylko jeden nick, usuń książkę z ekwipunku gracza po pewnym czasie
                Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().removeItem(player.getInventory().getItemInMainHand()), 2L);
                // Tutaj możesz dodać logikę do obsługi nicku gracza
                Bukkit.getConsoleSender().sendMessage(playerName);
                if (Bukkit.getBanList(BanList.Type.NAME).isBanned(playerName)) {
                    Bukkit.getConsoleSender().sendMessage("znalazl");
                    Bukkit.getBanList(BanList.Type.NAME).pardon(playerName); //TODO CONTINUEEE !!!!!!!!!
                    //HeartStealListener.unbanPlayer(playerUUID);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.sendTitle(ChatColor.GOLD + "!UWAGA! Wskrzeszono gracza !Uwaga!",ChatColor.GRAY + playerName + " może teraz wrócić",10,25,35);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Nie znaleziono takiego gracza!");
                    Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().removeItem(player.getInventory().getItemInMainHand()), 1L);
                    Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().setItemInMainHand(book), 1L);
                }
            }
        } else {
            // Jeśli gracz nie wpisał nicku gracza po odpowiednim tekście, wyślij wiadomość do gracza
            player.sendMessage(ChatColor.RED + "Proszę wpisać nick gracza po tekście: '" + initialText + "'");
            Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().removeItem(player.getInventory().getItemInMainHand()), 2L);
            Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> player.getInventory().setItemInMainHand(book), 2L);
        }
    }


    public static ItemStack createRedemptionItem() {
        ItemStack item = new ItemStack(Material.BEACON);

        ItemMeta itemMeta = item.getItemMeta();

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(HeartStealPlugin.getInstance(), "RedemptionItem"), PersistentDataType.STRING, "SecondChance");

        itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "Odkupienie");
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemMeta.setLore(Arrays.asList(
                ChatColor.BOLD + "" + ChatColor.GREEN + "Połóż na ziemi",
                ChatColor.BOLD + "" + ChatColor.GREEN + "Przywróć swojego przyjaciela do żywych!"
        ));

        item.setItemMeta(itemMeta);

        return item;
    }

    public static void createRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(HeartStealPlugin.getInstance(), "RedmptionRecipe"), createRedemptionItem());
        recipe.shape(
                "DGD",
                "TST",
                "DND"
        );

        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GOLDEN_APPLE);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);

        Bukkit.addRecipe(recipe);
    }

}
