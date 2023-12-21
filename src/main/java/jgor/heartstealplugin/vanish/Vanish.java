package jgor.heartstealplugin.vanish;

import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Vanish implements CommandExecutor {

    private static final Set<UUID> vanishedPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player targetPlayer = null;

        if (strings.length == 0) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("Jakiego gracza ukryć!");

                return true;
            }

            targetPlayer = (Player) commandSender;

            if(!targetPlayer.isOp()) {
                targetPlayer.sendMessage(ChatColor.RED + "Nie możesz tego użyć!");
                return true;
            }



        } else {

            if(!commandSender.isOp()) {
                targetPlayer.sendMessage(ChatColor.RED + "Nie możesz tego użyć!");
                return true;
            }

            targetPlayer = Bukkit.getPlayer(strings[0]);

            if (targetPlayer == null) {
                commandSender.sendMessage("Nie odnaleziono takiego gracza");
                return true;
            }

        }

        targetPlayer.sendMessage("Udalo sie");

        toggleVanish(targetPlayer);

        return true;
    }


    private static void toggleVanish(Player target) {
        boolean isVanished = vanishedPlayers.contains(target.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.equals(target)) continue;

            if (isVanished) {
                online.showPlayer(HeartStealPlugin.getInstance(), target);
            } else {
                online.hidePlayer(HeartStealPlugin.getInstance(), target);
            }
        }

        if (isVanished)
            vanishedPlayers.remove(target.getUniqueId());
        else
            vanishedPlayers.add(target.getUniqueId());

    }

    public static Set<UUID> getVanishedPlayers() {
        return vanishedPlayers;
    }
}
