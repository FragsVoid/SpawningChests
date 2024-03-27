package me.spawningchests.events;

import me.spawningchests.SpawningChests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EnterEvent implements Listener {

    private final SpawningChests plugin;

    public EnterEvent(SpawningChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOp() && !(plugin.getVersion().equals(plugin.latestversion))) {
            player.sendMessage(ChatColor.RED +"(SpawningChests) There is a new version available. "+ChatColor.YELLOW+
                    "("+ChatColor.GRAY+plugin.getLatestversion()+ChatColor.YELLOW+")");
            player.sendMessage(ChatColor.RED+"You can download it at: "
                    +ChatColor.WHITE+"https://www.spigotmc.org/resources/spawningchests.115263/");
        }
    }
}
