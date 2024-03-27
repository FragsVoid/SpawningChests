package me.spawningchests.events;

import me.spawningchests.SpawningChests;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class onBreak implements Listener {

    private final SpawningChests plugin;
    public onBreak(SpawningChests plugin) {
        this.plugin = plugin;
    }

    public List<Material> loadBlocks(@NotNull String path) {
        List<String> blocks = plugin.getConfig().getStringList(path);
        return  blocks.stream().map(Material::valueOf).toList();
    }

    public boolean check(@NotNull Material mat, @NotNull String path) {
        List<Material> materials = loadBlocks(path);
        if (materials.contains(mat))
            return true;
        return false;
    }

    @EventHandler
    public void onBreakEvent(BlockBreakEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location loc = block.getLocation();
        ItemStack brokenBlock = new ItemStack(block.getType());
        Material drop = plugin.drops.get(block.getType());
        Random random = new Random();
        double r = random.nextDouble();


        if (check(block.getType(), "AllowedBlocks")){
            if (r <= plugin.getConfig().getDouble("probability")/100) {
                e.setCancelled(true);
                block.setType(Material.CHEST);
                plugin.data.getConfig().set(player.getDisplayName() + ".ChestsSpawned",
                        plugin.data.getConfig().getInt(player.getDisplayName() + ".ChestsSpawned") + 1);

                plugin.chestsOwners.put(loc, player.getDisplayName());
                plugin.specificChests.add(loc);
                e.setDropItems(false);
                if (player.getGameMode().equals(GameMode.SURVIVAL)){
                    if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                        block.getWorld().dropItemNaturally(block.getLocation(), brokenBlock);
                    } else if (drop != null){
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop));
                    }
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.chestsOwners.remove(player, loc);
                        if (plugin.getConfig().getBoolean("Do-Chests-Disappear"))
                            block.setType(Material.AIR);
                    }
                }, plugin.getConfig().getLong("Disappearing-Time"));
                }
            }
        }

        if(!Objects.equals(plugin.chestsOwners.get(block.getLocation()), player.getDisplayName())) {
            if (player.hasPermission("spawningchest.break"))
                return;
            if (!plugin.getConfig().getBoolean("Break-Chests"))
                return;
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("Break-Message")));
        }
    }
}
