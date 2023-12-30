package jgor.heartstealplugin.stone.drop;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StoneDrop implements Listener {

    private final Random random = new Random();
    private final Map<Material, Double> oreDropChances = new HashMap<>();

    public StoneDrop() {
        oreDropChances.put(Material.COAL, 2.25);
        oreDropChances.put(Material.RAW_COPPER, 2.1);
        oreDropChances.put(Material.RAW_IRON, 1.25);
        oreDropChances.put(Material.EMERALD, 0.1);
        oreDropChances.put(Material.RAW_GOLD, 1.0);
        oreDropChances.put(Material.LAPIS_LAZULI, 1.4);
        oreDropChances.put(Material.REDSTONE, 1.72);
        oreDropChances.put(Material.SLIME_BALL, 0.07);
        oreDropChances.put(Material.ENDER_PEARL, 0.01);
        oreDropChances.put(Material.DIAMOND, 0.63);
        oreDropChances.put(Material.BONE, 0.29);
    }

    @EventHandler
    public void onStoneBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();

        ItemStack itemUsed = player.getInventory().getItemInMainHand();

        if (!itemUsed.containsEnchantment(Enchantment.SILK_TOUCH) && isTool(itemUsed)) {
            if (isBlockStoneType(block)) {

                switchDropCategory(player, itemUsed, block);

            } else if (isBlockOre(block)) {
                event.setDropItems(false);
                event.setExpToDrop(0);
                player.sendMessage(ChatColor.RED + "Surowce wypadają jedynie z kamienia!");
            }
        }

    }

    private boolean isTool(ItemStack itemUsed) {
        Material itemMaterial = itemUsed.getType();
        return itemMaterial.equals(Material.NETHERITE_PICKAXE) ||
                itemMaterial.equals(Material.DIAMOND_PICKAXE) ||
                itemMaterial.equals(Material.IRON_PICKAXE) ||
                itemMaterial.equals(Material.GOLDEN_PICKAXE) ||
                itemMaterial.equals(Material.STONE_PICKAXE) ||
                itemMaterial.equals(Material.WOODEN_PICKAXE);
    }

    private void switchDropCategory(Player player, ItemStack itemUsed, Block block) {


        if (itemUsed.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            Bukkit.getScheduler().runTaskLater(HeartStealPlugin.getInstance(), () -> fortuneOreDrop(player, itemUsed, block), 3L);
        } else {
            randomOreDrop(player, itemUsed, block);
        }


    }

    private void fortuneOreDrop(Player player, ItemStack itemUsed, Block block) {

        Location locationToDrop = block.getLocation();

        for (Map.Entry<Material, Double> entry : oreDropChances.entrySet()) {
            Material oreMaterial = entry.getKey();
            double chance = entry.getValue();

            // Sprawdź czy gracz ma enchant Fortune
            int fortuneLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

            // Wylicz szansę na wypadnięcie surowca z uwzględnieniem enchantu Fortune
            double modifiedChance = chance * (1 + (0.1 * fortuneLevel));

            // Losuj szansę na wypadnięcie surowca
            double playerChance = ThreadLocalRandom.current().nextDouble(100.0); // 0-100

            if (playerChance <= modifiedChance) {
                if (itemUsed.getType().equals(Material.GOLDEN_PICKAXE) || itemUsed.getType().equals(Material.STONE_PICKAXE)) {
                    if (oreMaterial.equals(Material.EMERALD) || oreMaterial.equals(Material.RAW_GOLD) || oreMaterial.equals(Material.DIAMOND) || oreMaterial.equals(Material.ENDER_PEARL) || oreMaterial.equals(Material.SLIME_BALL))
                        continue;
                    else
                        dropOreWithFortune(locationToDrop, oreMaterial, fortuneLevel);
                } else if (itemUsed.getType().equals(Material.WOODEN_PICKAXE)) {
                    if (oreMaterial.equals(Material.COAL) || oreMaterial.equals(Material.RAW_COPPER)) {
                        dropOreWithFortune(locationToDrop, oreMaterial, fortuneLevel);
                    }
                } else if (itemUsed.getType().equals(Material.DIAMOND_PICKAXE) || itemUsed.getType().equals(Material.NETHERITE_PICKAXE) || itemUsed.getType().equals(Material.IRON_PICKAXE)) {
                    dropOreWithFortune(locationToDrop, oreMaterial, fortuneLevel);
                }
            }
        }
        player.giveExp(2);

    }

    private void dropOreWithFortune(Location locationToDrop, Material oreMaterial, int fortuneLevel) {
        ItemStack item;

        if (!oreMaterial.equals(Material.ENDER_PEARL)) {
            int dropAmount = 1 + random.nextInt(fortuneLevel + 1);

            item = new ItemStack(oreMaterial, dropAmount);
        } else {
            int dropAmount = 1;
            if (fortuneLevel == 3) {
                dropAmount = 1 + random.nextInt(3);
            }

            item = new ItemStack(Material.ENDER_PEARL, dropAmount);
        }


        locationToDrop.getWorld().dropItemNaturally(locationToDrop, item);
    }

    private void randomOreDrop(Player player, ItemStack itemUsed, Block block) {
        Location locationToDrop = block.getLocation();

        for (Map.Entry<Material, Double> entry : oreDropChances.entrySet()) {
            Material oreMaterial = entry.getKey();
            double dropChance = entry.getValue();

            double playerChance = ThreadLocalRandom.current().nextDouble(100.0);

            if (playerChance <= dropChance) {
                if (itemUsed.getType().equals(Material.GOLDEN_PICKAXE) || itemUsed.getType().equals(Material.STONE_PICKAXE)) {
                    if (oreMaterial.equals(Material.EMERALD) || oreMaterial.equals(Material.RAW_GOLD) || oreMaterial.equals(Material.DIAMOND) || oreMaterial.equals(Material.ENDER_PEARL) || oreMaterial.equals(Material.SLIME_BALL))
                        continue;
                    else
                        dropOre(locationToDrop, oreMaterial);

                } else if (itemUsed.getType().equals(Material.WOODEN_PICKAXE)) {
                    if (oreMaterial.equals(Material.COAL) || oreMaterial.equals(Material.RAW_COPPER)) {
                        dropOre(locationToDrop, oreMaterial);
                    } else
                        continue;
                } else
                    dropOre(locationToDrop, oreMaterial);
            }

            player.giveExp(1);

        }
    }

    private void dropOre(Location locationToDrop, Material oreDropMaterial) {
        locationToDrop.getWorld().dropItemNaturally(locationToDrop, new ItemStack(oreDropMaterial));
    }

    private boolean isBlockStoneType(Block block) {
        return block.getType().equals(Material.STONE) ||
                block.getType().equals(Material.DEEPSLATE) ||
                block.getType().equals(Material.GRANITE) ||
                block.getType().equals(Material.DIORITE) ||
                block.getType().equals(Material.ANDESITE) ||
                block.getType().equals(Material.CALCITE);
    }

    private boolean isBlockOre(Block block) {
        return block.getType().equals(Material.COAL_ORE) ||
                block.getType().equals(Material.DEEPSLATE_COAL_ORE) ||
                block.getType().equals(Material.IRON_ORE) ||
                block.getType().equals(Material.DEEPSLATE_IRON_ORE) ||
                block.getType().equals(Material.COPPER_ORE) ||
                block.getType().equals(Material.DEEPSLATE_COPPER_ORE) ||
                block.getType().equals(Material.GOLD_ORE) ||
                block.getType().equals(Material.DEEPSLATE_GOLD_ORE) ||
                block.getType().equals(Material.REDSTONE_ORE) ||
                block.getType().equals(Material.DEEPSLATE_REDSTONE_ORE) ||
                block.getType().equals(Material.EMERALD_ORE) ||
                block.getType().equals(Material.DEEPSLATE_EMERALD_ORE) ||
                block.getType().equals(Material.LAPIS_ORE) ||
                block.getType().equals(Material.DEEPSLATE_LAPIS_ORE) ||
                block.getType().equals(Material.DIAMOND_ORE) ||
                block.getType().equals(Material.DEEPSLATE_DIAMOND_ORE);

    }
}
