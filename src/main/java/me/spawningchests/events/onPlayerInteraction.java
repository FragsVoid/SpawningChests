package me.spawningchests.events;

import me.spawningchests.SpawningChests;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class onPlayerInteraction implements Listener {

    private final SpawningChests plugin;

    public onPlayerInteraction(SpawningChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block == null)
            return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (block.getType() != Material.CHEST)
            return;
        if(!Objects.equals(plugin.chestsOwners.get(block.getLocation()), player.getDisplayName())) {
            if (player.hasPermission("spawningchest.interact"))
                return;
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("Interaction-Message")));
        } else {
            for (String s : plugin.getConfig().getConfigurationSection("LootTable").getKeys(false)) {

            }
        }
    }
}
