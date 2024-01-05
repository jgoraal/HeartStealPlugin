package jgor.heartstealplugin.UnstuckCommand;

import StuckCommand.StuckCommand;
import jgor.heartstealplugin.HeartStealPlugin;
import jgor.heartstealplugin.start.protection.StartProtection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class UnStuckCommand implements CommandExecutor {

    private BukkitTask unstuckTask;
    private StartProtection startProtection;



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            if(StuckCommand.getStuckPlayers().isEmpty()) {
                commandSender.sendMessage("Nie ma kogo odmrozic");
                return true;
            }

            if (unstuckTask != null && !unstuckTask.isCancelled()) {
                unstuckTask.cancel();
            }

            unstuckTask = unstuckPlayers(StuckCommand.getStuckPlayers());
        }

        Player player = ((Player) commandSender).getPlayer();

        if(!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You can't use that command!");
            return true;
        }

        if(StuckCommand.getStuckPlayers().isEmpty()) {
            player.sendMessage("Nie ma kogo odmrozic");
            return true;
        }

        if (unstuckTask != null && !unstuckTask.isCancelled()) {
            unstuckTask.cancel();
        }

        unstuckTask = unstuckPlayers(StuckCommand.getStuckPlayers());


        return true;
    }

    private BukkitTask unstuckPlayers(HashMap<UUID, ArmorStand> players) {
        return Bukkit.getScheduler().runTaskTimer(HeartStealPlugin.getInstance(), new Runnable() {
            int timer = 10;

            @Override
            public void run() {
                if (timer > 0) {
                    for (UUID playerUUID : players.keySet()) {
                        Player player = Bukkit.getPlayer(playerUUID);
                        player.sendTitle(ChatColor.BOLD + "" + ChatColor.GOLD + "Odmrożenie za " + timer, "", 1, 10, 20);
                    }
                    timer -= 1;
                } else {
                    Iterator<UUID> iterator = players.keySet().iterator();
                    while (iterator.hasNext()) {
                        UUID playerUUID = iterator.next();
                        Player player = Bukkit.getPlayer(playerUUID);
                        player.sendTitle(ChatColor.BOLD + "" + ChatColor.GREEN + "Start!", "Good luck :)", 1, 10, 20);
                        StuckCommand.getStuckPlayers().get(playerUUID).removePassenger(player);
                        StuckCommand.getStuckPlayers().get(playerUUID).remove();
                        Bukkit.getPlayer(playerUUID).setAllowFlight(false);
                        //startProtection.getPlayerProtectionMap().put(playerUUID,startProtection.protectionTask(player,300));
                        iterator.remove(); // Usunięcie elementu za pomocą iteratora
                    }
                    unstuckTask.cancel();

                }
            }
        }, 1L, 20L);
    }



}
