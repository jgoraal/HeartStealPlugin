package jgor.heartstealplugin.auctions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ItemDeserialiser {
    public static ItemStack deserialise(Map<String, Object> map) {
        if (map.containsKey("type") && map.containsKey("meta")) {
            Object typeObject = map.get("type");
            if (typeObject instanceof String) {
                Material material = Material.matchMaterial((String) typeObject);
                if (material != null) {
                    ItemStack itemStack = new ItemStack(material);

                    Object metaObject = map.get("meta");
                    if (metaObject instanceof Map) {
                        Map<String, Object> metaSerialized = (Map<String, Object>) metaObject;
                        metaSerialized.put("meta-type", "UNSPECIFIC");

                        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);
                        itemMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(metaSerialized, ConfigurationSerialization.getClassByAlias("ItemMeta"));

                        if (metaSerialized.containsKey("displayName")) {

                            Bukkit.getLogger().info("Co wyswietla displayname: " + displayname.get("extra"));

                            itemMeta.setDisplayName("Debil");
                        }



                        if (metaSerialized.containsKey("enchants")) {
                            Map<String, Object> enchantMap = (Map<String, Object>) metaSerialized.get("enchants");
                            for (Map.Entry<String, Object> enchantEntry : enchantMap.entrySet()) {
                                Enchantment enchant = Enchantment.getByName(enchantEntry.getKey());
                                if (enchant != null && enchantEntry.getValue() instanceof Integer) {
                                    itemMeta.addEnchant(enchant, (Integer) enchantEntry.getValue(), true);
                                }
                            }
                        }

                        itemStack.setItemMeta(itemMeta);
                        return itemStack;
                    }
                }
            }
        }
        return null; // or throw an exception if needed
    }




}
