package jgor.heartstealplugin.restore.heart.command;

import jgor.heartstealplugin.HeartStealListener;
import jgor.heartstealplugin.HeartStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestoreHeartCommand implements CommandExecutor {

    private HeartStealListener heartStealListener;

    public RestoreHeartCommand(HeartStealPlugin plugin) {
        this.heartStealListener = plugin.getHeartStealListener();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            if (strings.length == 0) {
                commandSender.sendMessage("Podaj jakiemu graczu!");
            } else if (strings.length == 1) {

                commandSender.sendMessage("Podaj ile serc dodać/odjąć!");


            } else if (strings.length == 2) {
                Player player = Bukkit.getPlayer(strings[0]);
                double hearts = 0;

                try {
                    hearts = Double.parseDouble(strings[1]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("jesteś debilem!");
                }


                if (!player.isOnline()) {
                    commandSender.sendMessage("Nie odnaleziona takiego gracza!");
                    return true;
                }

                if (hearts <= 0 || hearts > 20) {
                    commandSender.sendMessage("Nie możesz ustawić takiej wartości serc!");
                    return true;
                }

                if (heartStealListener.getPlayersHearts().containsKey(player.getUniqueId())) {
                    heartStealListener.getPlayersHearts().replace(player.getUniqueId(), hearts * 2);
                } else {
                    heartStealListener.getPlayersHearts().put(player.getUniqueId(), hearts * 2);
                }

                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hearts * 2);
                HeartStealPlugin.getInstance().getConfig().set("PlayerHealth." + player.getUniqueId(), hearts * 2);
                HeartStealPlugin.getInstance().saveConfig();

                player.setHealth(hearts*2);

                commandSender.sendMessage("Pomyślnie ustawiłeś ilość serc graczowi: " +player.getName());

                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage(ChatColor.GOLD + "Konsola zmieniła ilość serc graczowi: " + player.getName());
                }

            } else commandSender.sendMessage("Jestes chyba chory");

        }


        return true;
    }
}
