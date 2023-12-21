package jgor.heartstealplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class HeartItem {

    public static ItemStack createHeartItem() {
        ItemStack heartItem = new ItemStack(Material.RED_DYE);

        ItemMeta heartItemMeta = heartItem.getItemMeta();

        PersistentDataContainer data = heartItemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(HeartStealPlugin.getInstance(),"HeartRestore"), PersistentDataType.STRING,"AdditionalHeart");

        heartItemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Dodatkowe Serce");
        heartItemMeta.addEnchant(Enchantment.DURABILITY,3,true);
        heartItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        heartItemMeta.setLore(Arrays.asList(
                ChatColor.GREEN + "Odnawia 1 serce!",
                ChatColor.YELLOW + "Naciśnij RPM",
                "",
                ChatColor.BOLD + "" + ChatColor.RED + "Szansa na odnowienia serca wynosi ~25% !"
        ));
        heartItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        heartItem.setItemMeta(heartItemMeta);

        return heartItem;
    }


    public static void createRecipe() {


        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(HeartStealPlugin.getInstance(),"additional_heart"),createHeartItem());

        recipe.shape(
                "DGD",
                "GRG",
                "DGD"
        );

        recipe.setIngredient('D',Material.DIAMOND);
        recipe.setIngredient('G',Material.GOLDEN_APPLE);
        recipe.setIngredient('R',Material.RED_DYE);

        Bukkit.addRecipe(recipe);
    }
}
