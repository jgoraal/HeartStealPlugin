package jgor.heartstealplugin.start.protection;

import com.nametagedit.plugin.NametagEdit;
import jgor.heartstealplugin.HeartStealPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StartProtection implements Listener {

    private final String prefix = "&#ffcf04[&#ffb807O&#ffa20ac&#ff8b0ch&#ff750fr&#ff5e12o&#ff4715n&#ff3117a&#ff1a1a] ";
    private final Map<UUID, BukkitTask> playerProtectionMap = new HashMap<>();

    private final Map<UUID, Integer> playerProtectionTimeMap = new HashMap<>();
    private static final int FULL_PROTECTION_TIME = 100;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Player player = event.getPlayer();
        
        //Criteria protection = Criteria.create("START_PROTECTION");

        //Objective objective = board.registerNewObjective(HeartStealPlugin.getInstance() + "_start_protection", "");
        //objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        NametagEdit.getApi().setPrefix(player, prefix + ChatColor.RESET);

        if (playerProtectionTimeMap.containsKey(player.getUniqueId())) {
            int remainingProtectionTime = playerProtectionTimeMap.get(player.getUniqueId());

            //playerProtectionMap.put(player.getUniqueId(),protectionTask(player,objective,board,remainingProtectionTime));
            playerProtectionMap.put(player.getUniqueId(), protectionTask(player, remainingProtectionTime));
        } else {
            playerProtectionTimeMap.put(player.getUniqueId(), FULL_PROTECTION_TIME);
            //playerProtectionMap.put(player.getUniqueId(), protectionTask(player,objective,board, FULL_PROTECTION_TIME));
            playerProtectionMap.put(player.getUniqueId(), protectionTask(player, FULL_PROTECTION_TIME));
        }

    }


    public BukkitTask protectionTask(Player player, int remainingTime) {
        return Bukkit.getScheduler().runTaskTimer(HeartStealPlugin.getInstance(), new Runnable() {
            int timer = remainingTime;
            boolean toggle = true;

            @Override
            public void run() {
                if (timer >= 0) {
                    int minutes = timer / 60;
                    int seconds = timer % 60;

                    final String textBeforeMinute = ChatColor.GOLD + "" + ChatColor.BOLD + "Twoja ochrona trwa jeszcze przez: " + ChatColor.RESET + ChatColor.WHITE + ChatColor.BOLD + String.format("%01dm %02ds", minutes, seconds);
                    final String textAfterMinute = ChatColor.GOLD + "" + ChatColor.BOLD + "Twoja ochrona trwa jeszcze przez: " + ChatColor.RESET + ChatColor.WHITE + ChatColor.BOLD + String.format("%02ds", seconds);
                    final String textAfter10SecondsWhite = ChatColor.GOLD + "" + ChatColor.BOLD + "Twoja ochrona trwa jeszcze przez: " + ChatColor.RESET + ChatColor.WHITE + ChatColor.BOLD + String.format("%01ds", seconds);
                    final String textAfter10SecondsRed = ChatColor.GOLD + "" + ChatColor.BOLD + "Twoja ochrona trwa jeszcze przez: " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + String.format("%01ds", seconds);

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(textBeforeMinute));

                    if (timer < 60 && timer >= 10)
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(textAfterMinute));
                    else if (timer < 10) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(textAfter10SecondsWhite));

                        if (timer <= 5) {
                            String text = (toggle ? textAfter10SecondsWhite : textAfter10SecondsRed);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
                            toggle = !toggle;
                        }
                    }

                    timer--;
                    playerProtectionTimeMap.put(player.getUniqueId(), timer);
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + "Twoja ochrona się skonczyła"));
                    NametagEdit.getApi().clearNametag(player);// Usuń prefix po zakończeniu ochrony
                    playerProtectionMap.get(player.getUniqueId()).cancel();
                    playerProtectionTimeMap.remove(player.getUniqueId());
                    playerProtectionMap.remove(player.getUniqueId());
                }

            }


        }, 0L, 20L);
    }


    private BukkitTask protectionTask(Player player, Objective objective, Scoreboard board, int remainingTime) {
        return Bukkit.getScheduler().runTaskTimer(HeartStealPlugin.getInstance(), new Runnable() {
            int timer = remainingTime; // 5 minut w sekundach

            @Override
            public void run() {
                if (timer >= 0) {
                    int minutes = timer / 60;
                    int seconds = timer % 60;
                    objective.setDisplayName(String.format("%01dm %02ds", minutes, seconds));
                    if (timer < 60 && timer >= 10)
                        objective.setDisplayName(String.format("%02ds", seconds));
                    else if (timer < 10) {
                        objective.setDisplayName(String.format("%01ds", seconds));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "Twoja ochrona trwa jeszcze przez: " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + String.format("%01ds", seconds)));
                    }
                    player.setScoreboard(board);
                    timer--;
                    playerProtectionTimeMap.put(player.getUniqueId(), timer);
                } else {
                    // Po zakończeniu ochrony usuń gracza z scoreboarda
                    board.resetScores(player.getName());
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    NametagEdit.getApi().clearNametag(player);// Usuń prefix po zakończeniu ochrony

                    playerProtectionMap.get(player.getUniqueId()).cancel();
                    playerProtectionTimeMap.remove(player.getUniqueId());
                    playerProtectionMap.remove(player.getUniqueId());
                }
                Bukkit.getLogger().info("W WATKU -> " + playerProtectionTimeMap);
            }


        }, 0L, 20L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (playerProtectionMap.containsKey(player.getUniqueId())) {

            playerProtectionMap.get(player.getUniqueId()).cancel();
            playerProtectionMap.remove(player.getUniqueId());
        }


    }


    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player target) {


            if (playerProtectionMap.containsKey(damager.getUniqueId()) && playerProtectionMap.containsKey(target.getUniqueId())) {
                event.setCancelled(true);
                damager.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Posiadasz ochronę!", ChatColor.WHITE + "Poczekaj aż się skończy", 10, 20, 10);
            } else if (playerProtectionMap.containsKey(damager.getUniqueId())) {
                event.setCancelled(true);
                damager.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Posiadasz ochronę!", ChatColor.WHITE + "Poczekaj aż się skończy", 10, 20, 10);
            } else if (playerProtectionMap.containsKey(target.getUniqueId())) {
                event.setCancelled(true);
                damager.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Ten gracz posiada ochronę!", ChatColor.WHITE + "Poczekaj aż się skończy", 10, 20, 10);
            } else {
                event.setCancelled(false);
            }
        }
    }

    public Map<UUID, BukkitTask> getPlayerProtectionMap() {
        return playerProtectionMap;
    }

    public Map<UUID, Integer> getPlayerProtectionTimeMap() {
        return playerProtectionTimeMap;
    }
}
