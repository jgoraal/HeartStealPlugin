package StuckCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class StuckCommand implements CommandExecutor, Listener {

    private static final HashMap<UUID, ArmorStand> stuckPlayers = new HashMap<>();

    @Override
    public boolean onCommand(@Nullable CommandSender commandSender, @Nullable Command command, @Nullable String label, @Nullable String[] args) {
        if (!(commandSender instanceof Player)) return true;

        Player admin = (Player) commandSender;

        if (!admin.isOp()) {
            admin.sendMessage(ChatColor.RED + "You can't use that command!");
            return true;
        }


        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.isOp()) {
                Location loc = online.getLocation();
                loc.setY(loc.getY() - 1.0);
                ArmorStand armorStand = online.getWorld().spawn(loc, ArmorStand.class);
                armorStand.setVisible(false);
                armorStand.setGravity(false);
                armorStand.addPassenger(online);
                stuckPlayers.put(online.getUniqueId(), armorStand);

            }
        }

        return true;
    }

    @EventHandler
    public void onDismountEvent(EntityDismountEvent event) {

        if (event.getEntity() instanceof Player && event.getDismounted() instanceof ArmorStand) {
            Player player = (Player) event.getEntity();
            ArmorStand armorStand = (ArmorStand) event.getDismounted();

            if (stuckPlayers.containsKey(player.getUniqueId()) && stuckPlayers.get(player.getUniqueId()).equals(armorStand)) {
                // Cancel the dismount event
                event.setCancelled(true);

                // Remove the player from the existing ArmorStand
                armorStand.eject();

                if (armorStand.getPassengers().isEmpty()) {
                    // Remove the existing ArmorStand
                    armorStand.remove();
                    stuckPlayers.remove(player.getUniqueId());
                }
            } else {
                // Create a new ArmorStand and add the player to it
                Location loc = player.getLocation().subtract(0, 1, 0);
                ArmorStand newArmorStand = player.getWorld().spawn(loc, ArmorStand.class);
                newArmorStand.setVisible(false);
                newArmorStand.setGravity(false);
                newArmorStand.addPassenger(player);
                stuckPlayers.put(player.getUniqueId(), newArmorStand);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore() && !player.isOp()) {
            event.setJoinMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Powitajmy nowego gracza " + player.getDisplayName() + " !");
            player.sendTitle(
                    ChatColor.BOLD + "" + ChatColor.GOLD + "Witaj " + ChatColor.RESET + "" + ChatColor.GREEN + player.getDisplayName(),
                    ChatColor.GRAY + "Poczekaj chwilę zaraz zaczynamy!",
                    10, 80, 20
            );

            player.setAllowFlight(true);

            randomSpawnLocation(player);

            Location loc = player.getLocation();
            loc.setY(loc.getY() - 1.0);
            ArmorStand armorStand = player.getWorld().spawn(loc, ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.addPassenger(player);
            stuckPlayers.put(player.getUniqueId(), armorStand);
        } else {
            event.setJoinMessage(ChatColor.BOLD + "" + ChatColor.GREEN + player.getDisplayName() + " wbił :)");
            player.sendTitle(
                    ChatColor.BOLD + "" + ChatColor.GOLD + "Witaj " + ChatColor.RESET + ChatColor.GREEN + player.getDisplayName(),
                    ChatColor.GRAY + "Miłej zabawy!",
                    10, 40, 20
            );
        }
    }

    private void randomSpawnLocation(Player player) {
        double radiusX = 1000.0;
        double radiusZ = 1000.0;

        Random random = new Random();

        int offsetX = random.nextInt(900) + 100;
        int offsetZ = random.nextInt(900) + 100;

        double newLocationX = (random.nextBoolean() ? Math.round(player.getLocation().getX()) + offsetX + 0.5 : -1 * (Math.round(player.getLocation().getX()) + offsetX + 0.5));
        double newLocationZ = (random.nextBoolean() ? Math.round(player.getLocation().getZ()) + offsetZ + 0.5 : -1 * (Math.round(player.getLocation().getZ()) + offsetZ + 0.5));

        double newY = player.getWorld().getHighestBlockYAt((int) newLocationX, (int) newLocationZ) + 2.0;

        Location newLocation = new Location(player.getWorld(), newLocationX, newY, newLocationZ);

        if (isLocationOnWater(newLocation)) {
            newLocation = movePlayerToSurfave(newLocation);
        }

        player.teleport(newLocation);

        player.setBedSpawnLocation(newLocation,true);

        player.sendMessage(ChatColor.GOLD + "New location: " + "x = " + newLocationX + " y = " + newY + " z = " + newLocationZ);
    }

    private boolean isLocationOnWater(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block above = location.getWorld().getBlockAt(x, y-2, z);


        return above.isLiquid();
    }

    private Location movePlayerToSurfave(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);



        while (above.isLiquid()) {
            y++;
            above = location.getWorld().getBlockAt(x, y + 1, z);
        }

        return new Location(location.getWorld(), x, y + 1, z);
    }

    public static HashMap<UUID, ArmorStand> getStuckPlayers() {
        return stuckPlayers;
    }
}
