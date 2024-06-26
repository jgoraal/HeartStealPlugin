package jgor.heartstealplugin;


import jgor.heartstealplugin.start.protection.StartProtection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HeartStealListener implements Listener {
    private final Map<UUID, Double> playersHearts = new HashMap<>();
    private static final Map<UUID, Boolean> bannedPlayers = new HashMap<>();
    private final Map<UUID, BukkitTask> combatTasks = new HashMap<>();
    private final Map<UUID, Boolean> playersInCombat = new HashMap<>();

    private static final double BASIC_HEARTS_COUNT = 20.0;
    private static final double MIN_HEART_AMOUNT = 2.0;
    private static final double MAX_HEARTS_ADDED = 40.0;
    private static final double HEART_LOSS_AMOUNT = 2.0;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        if (player == null) return;

        player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Straciłeś jedno serce!");

        UUID playerUUID = player.getUniqueId();

        if (playersInCombat.containsKey(playerUUID) && player.getKiller() instanceof Player) {
            Player killer = player.getKiller();

            if (killer.isOnline()) {
                List<ItemStack> playerDrops = event.getDrops();

                Inventory killerInventory = killer.getInventory();
                killer.getInventory();

                if(hasKillerFullInventory(killer)) {
                    Iterator<ItemStack> iterator = playerDrops.iterator();
                    while (iterator.hasNext()) {
                        ItemStack remainingItem = iterator.next();
                        killer.getWorld().dropItem(killer.getLocation(), remainingItem);
                        iterator.remove();
                    }

                } else {
                    int index = 0;
                    for (ItemStack item : killerInventory.getStorageContents()) {
                        if(item == null && !playerDrops.isEmpty()) {
                            killerInventory.setItem(index,playerDrops.getFirst());
                            playerDrops.removeFirst();
                        }
                        index++;
                    }

                    if(!playerDrops.isEmpty()) {
                        for (ItemStack remainingItem : playerDrops) {
                            killer.getWorld().dropItem(killer.getLocation(), remainingItem);
                            playerDrops.remove(remainingItem);
                        }
                    }
                }
            }
        }

        if (combatTasks.containsKey(playerUUID)) combatTasks.get(playerUUID).cancel();

        if (hasLastHeart(playerUUID)) banPlayer(playerUUID); //TODO FINISH THAT !!!!!!!!!!
        else removePlayerHeart(playerUUID);

    }

    private boolean hasKillerFullInventory(Player killer) {
        Inventory killerInventory = killer.getInventory();


        for (ItemStack item : killerInventory.getStorageContents()) {
            if (item == null) {
                return false;
            }
        }

        return true;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        UUID playerUUID = player.getUniqueId();

        if (playersHearts.containsKey(playerUUID)) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(playersHearts.get(playerUUID));
        }
    }

    @EventHandler
    public void onHeartItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if ((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && handItem.isSimilar(HeartItem.createHeartItem())) {
            PersistentDataContainer dataContainer = Objects.requireNonNull(handItem.getItemMeta()).getPersistentDataContainer();

            if (playersInCombat.getOrDefault(player.getUniqueId(), false)) {
                player.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "Nie możesz tego zrobić!", ChatColor.WHITE + "Poczekaj aż zakończy się walka", 10, 30, 20);
                event.setCancelled(true);
                return;
            }

            if (dataContainer.has(new NamespacedKey(HeartStealPlugin.getInstance(), "HeartRestore"), PersistentDataType.STRING)) {

                if (!hasPlayerHasMaxHearts(player.getUniqueId())) {

                    Random rand = new Random();

                    int chance = rand.nextInt(100) + 1;

                    if (chance <= 25) {
                        player.sendTitle(ChatColor.GREEN + "Serce dodane!", "", 10, 20, 25);
                        UUID playerUUID = player.getUniqueId();
                        addPlayerOneHeart(playerUUID);

                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 4, false, false, false));

                    } else {
                        player.sendTitle(ChatColor.RED + "Serce spalone!", "", 10, 20, 25);
                    }
                    if (handItem.getAmount() > 1) handItem.setAmount(handItem.getAmount() - 1);
                    else player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                } else {
                    player.sendTitle(
                            ChatColor.BOLD + "" + ChatColor.RED + "Posiadasz już max serc!",
                            "",
                            10,
                            20,
                            25);
                }

            }
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {


        if (event.getEntity() instanceof Player && event.getDamager() instanceof Creeper) return;

        if (!(event.getEntity() instanceof Player) ||
                !(event.getDamager() instanceof Player)) return;

        Player damagedPlayer = (Player) event.getEntity();
        Player damagerPlayer = (Player) event.getDamager();

        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getType() == EntityType.ENDER_PEARL) {
                // Jeśli przyczyną obrażeń jest ender pearl, przerwij dalsze działanie
                return;
            }
        }

        if (!StartProtection.playerProtectionMap.containsKey(damagedPlayer) || !StartProtection.playerProtectionMap.containsKey(damagerPlayer))
            startCombatCountDown(damagedPlayer, damagerPlayer);
    }

    @EventHandler
    public void onCrystalExplosion(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                || event.getCause() == EntityDamageEvent.DamageCause.DRAGON_BREATH || event.getCause() == EntityDamageEvent.DamageCause.WITHER
                || event.getCause() == EntityDamageEvent.DamageCause.POISON
        ) {
            //TODO NOT BLAZE NOT GHAST AND CREEPER
            if (!(event.getEntity() instanceof Blaze) || !(event.getEntity() instanceof Ghast) || !(event.getEntity() instanceof Creeper)) {
                if (event.getEntity() instanceof Player) {
                    Player damagedPlayer = (Player) event.getEntity();

                    startCombatFromExplosion(damagedPlayer);


                }
            }
        }
    }


    private void startCombatFromExplosion(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (combatTasks.containsKey(playerUUID)) combatTasks.get(playerUUID).cancel();

        playersInCombat.put(playerUUID, true);

        BukkitTask task = startCountDown(player);

        combatTasks.put(playerUUID, task);

    }


    @EventHandler
    public void onPlayerHitUsingWeapon(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player) || !(event.getHitEntity() instanceof Player)) return;

        Player damagerPlayer = (Player) event.getEntity().getShooter();
        Player damagedPlayer = ((Player) event.getHitEntity()).getPlayer();

        if (event.getEntityType() == EntityType.ENDER_PEARL) {
            // Jeśli ender pearl trafiła gracza, przerwij dalsze działanie
            return;
        }


        if (damagedPlayer != null) {
            startCombatCountDown(damagedPlayer, damagerPlayer);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        UUID playerUUID = player.getUniqueId();

        if (playersInCombat.getOrDefault(player.getUniqueId(), false)) {
            player.setHealth(0.0);
            playersInCombat.replace(player.getUniqueId(), false);
            event.setQuitMessage(ChatColor.RED + player.getDisplayName() + " wylogował się podczas walki!");
        } else {
            event.setQuitMessage(ChatColor.RED + player.getDisplayName() + " wyszedł :(");
        }

        if (combatTasks.containsKey(playerUUID)) {
            combatTasks.get(playerUUID).cancel();
            combatTasks.remove(playerUUID);
        }
    }



    private void startCombatCountDown(Player defender, Player attacker) {
        UUID attackerUUID = attacker.getUniqueId();
        UUID defenderUUID = defender.getUniqueId();

        // Anuluj poprzednie zadania odliczania dla obu graczy
        if (combatTasks.containsKey(attackerUUID)) combatTasks.get(attackerUUID).cancel();
        if (combatTasks.containsKey(defenderUUID)) combatTasks.get(defenderUUID).cancel();

        // Ustaw obu graczy jako zaangażowanych w walkę
        playersInCombat.put(attackerUUID, true);
        playersInCombat.put(defenderUUID, true);

        // Rozpocznij nowe zadanie odliczania dla obu graczy
        BukkitTask attackerTask = startCountDown(attacker);
        BukkitTask defenderTask = startCountDown(defender);

        // Dodaj zadania do mapy combatTasks
        combatTasks.put(attackerUUID, attackerTask);
        combatTasks.put(defenderUUID, defenderTask);
    }

    private BukkitTask startCountDown(Player player) {
        return Bukkit.getScheduler().runTaskTimer(HeartStealPlugin.getInstance(), new Runnable() {
            double timeLeft = 20.0;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Pozostały czas walki: " + castPrecisionTimer(timeLeft) + "s"));
                    timeLeft -= 0.1;
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Jesteś bezpieczny możesz się wylogować"));
                    playersInCombat.replace(player.getUniqueId(), false);
                    combatTasks.get(player.getUniqueId()).cancel();
                }
            }
        }, 0L, 2L);
    }

    private double castPrecisionTimer(double timer) {
        BigDecimal bigDecimal = new BigDecimal(timer);
        bigDecimal = bigDecimal.setScale(1, RoundingMode.FLOOR);
        return bigDecimal.doubleValue();
    }


    private void addPlayerOneHeart(UUID playerUUID) {
        double currentPlayerHeartAmount = playersHearts.get(playerUUID);

        playersHearts.replace(playerUUID, currentPlayerHeartAmount + HEART_LOSS_AMOUNT);

        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null && player.isOnline()) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(playersHearts.get(playerUUID));
            HeartStealPlugin.getInstance().getConfig().set("PlayerHealth." + playerUUID, playersHearts.get(playerUUID));
            HeartStealPlugin.getInstance().getConfig().set("PlayerBanned." + playerUUID, false);
            HeartStealPlugin.getInstance().saveConfig();
        }
    }

    private boolean hasPlayerHasMaxHearts(UUID playerUUID) {
        return playersHearts.containsKey(playerUUID) && playersHearts.get(playerUUID) == MAX_HEARTS_ADDED;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (HeartStealPlugin.getInstance().getConfig().contains("PlayerHealth." + playerUUID)) {
            double savedPlayerHealth = HeartStealPlugin.getInstance().getConfig().getDouble("PlayerHealth." + playerUUID);
            playersHearts.put(playerUUID, savedPlayerHealth);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(savedPlayerHealth);
        } else {
            playersHearts.put(playerUUID, BASIC_HEARTS_COUNT);
        }

        if (HeartStealPlugin.getInstance().getConfig().contains("PlayerBanned." + playerUUID)) {
            boolean isPlayerBanned = HeartStealPlugin.getInstance().getConfig().getBoolean("PlayerBanned." + playerUUID);
            bannedPlayers.put(playerUUID, isPlayerBanned);
        } else {
            bannedPlayers.put(playerUUID, false);
        }

        if (bannedPlayers.containsKey(playerUUID) && bannedPlayers.get(playerUUID) && !Bukkit.getBanList(BanList.Type.NAME).getBanEntries().contains(Bukkit.getOfflinePlayer(playerUUID).getName())) {
            unbanPlayer(playerUUID);
            playersHearts.replace(playerUUID, BASIC_HEARTS_COUNT);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BASIC_HEARTS_COUNT);
            player.setHealth(BASIC_HEARTS_COUNT);
            HeartStealPlugin.getInstance().getConfig().set("PlayerHealth." + playerUUID, playersHearts.get(playerUUID));
            HeartStealPlugin.getInstance().saveConfig();
        }


        if (!playersHearts.containsKey(playerUUID) && !bannedPlayers.containsKey(playerUUID)) {
            playersHearts.put(playerUUID, BASIC_HEARTS_COUNT);
            bannedPlayers.put(playerUUID, false);
            HeartStealPlugin.getInstance().getConfig().set("PlayerHealth." + playerUUID, playersHearts.get(playerUUID));

            HeartStealPlugin.getInstance().getConfig().set("PlayerBanned." + playerUUID, false);
            HeartStealPlugin.getInstance().saveConfig();
        }

    }

    public void banPlayer(UUID playerUUID) {
        playersInCombat.replace(playerUUID,false);
        bannedPlayers.replace(playerUUID, true);
        HeartStealPlugin.getInstance().getConfig().set("PlayerBanned." + playerUUID, bannedPlayers.get(playerUUID));
        HeartStealPlugin.getInstance().saveConfig();
        String banReason = ChatColor.BOLD + "" + ChatColor.RED + "Straciłeś wszystkie serca!";
        String playerName = Bukkit.getPlayer(playerUUID).getName();
        Bukkit.getBanList(BanList.Type.NAME).addBan(playerName, banReason, Date.from(Instant.now().plus(1, ChronoUnit.DAYS)), null);
        Bukkit.getPlayer(playerUUID).kickPlayer(banReason);
    }

    public static void unbanPlayer(UUID playerUUID) {
        bannedPlayers.replace(playerUUID, false);
        HeartStealPlugin.getInstance().getConfig().set("PlayerBanned." + playerUUID, false);
        HeartStealPlugin.getInstance().saveConfig();
    }

    private void removePlayerHeart(UUID playerUUID) {
        double currentPlayerHeartAmount = playersHearts.get(playerUUID);

        playersHearts.replace(playerUUID, currentPlayerHeartAmount - HEART_LOSS_AMOUNT);

        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null && player.isOnline()) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(playersHearts.get(playerUUID));
            HeartStealPlugin.getInstance().getConfig().set("PlayerHealth." + playerUUID, playersHearts.get(playerUUID));
            HeartStealPlugin.getInstance().getConfig().set("PlayerBanned." + playerUUID, false);
            HeartStealPlugin.getInstance().saveConfig();
        }
    }

    private boolean hasLastHeart(UUID playerUUID) {
        return playersHearts.containsKey(playerUUID) && playersHearts.get(playerUUID) == MIN_HEART_AMOUNT;
    }

    public void loadConfigData() {
        HeartStealPlugin plugin = HeartStealPlugin.getInstance();
        for (String key : plugin.getConfig().getKeys(true)) {
            if (key.startsWith("PlayerHealth.")) {
                UUID playerUUID = UUID.fromString(key.substring(13));
                double health = plugin.getConfig().getDouble(key);
                playersHearts.put(playerUUID, health);
            } else if (key.startsWith("PlayerBanned.")) {
                UUID playerUUID = UUID.fromString(key.substring(13));
                boolean isBanned = plugin.getConfig().getBoolean(key);
                bannedPlayers.put(playerUUID, isBanned);
            }
        }
    }

    public void saveConfigData() {
        HeartStealPlugin plugin = HeartStealPlugin.getInstance();
        for (Map.Entry<UUID, Double> entry : playersHearts.entrySet()) {
            plugin.getConfig().set("PlayerHealth." + entry.getKey(), entry.getValue());
        }
        for (Map.Entry<UUID, Boolean> entry : bannedPlayers.entrySet()) {
            plugin.getConfig().set("PlayerBanned." + entry.getKey(), entry.getValue());
        }

        plugin.saveConfig();
    }

    public Map<UUID, BukkitTask> getCombatTasks() {
        return combatTasks;
    }

    public Map<UUID, Boolean> getPlayersInCombat() {
        return playersInCombat;
    }

    public Map<UUID, Double> getPlayersHearts() {
        return playersHearts;
    }

}

