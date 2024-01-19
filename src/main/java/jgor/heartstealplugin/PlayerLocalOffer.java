package jgor.heartstealplugin;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record PlayerLocalOffer(UUID playerUUID, int offerAmount, int itemAmountInOneOffer, ItemStack item) {
}
