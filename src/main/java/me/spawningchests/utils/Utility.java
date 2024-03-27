package me.spawningchests.utils;

import me.spawningchests.SpawningChests;
import org.bukkit.Material;

public class Utility {


    private final SpawningChests plugin;

    public Utility(SpawningChests plugin) {
        this.plugin = plugin;
    }

    public void materials () {
        plugin.drops.put(Material.STONE, Material.COBBLESTONE);
        plugin.drops.put(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE);
        plugin.drops.put(Material.IRON_ORE, Material.RAW_IRON);
        plugin.drops.put(Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON);
        plugin.drops.put(Material.LAPIS_ORE, Material.LAPIS_LAZULI);
        plugin.drops.put(Material.DEEPSLATE_LAPIS_ORE, Material.LAPIS_LAZULI);
        plugin.drops.put(Material.COAL_ORE, Material.COAL);
        plugin.drops.put(Material.DEEPSLATE_COAL_ORE, Material.COAL);
        plugin.drops.put(Material.COPPER_ORE, Material.RAW_COPPER);
        plugin.drops.put(Material.DEEPSLATE_COPPER_ORE, Material.RAW_COPPER);
        plugin.drops.put(Material.GOLD_ORE, Material.RAW_GOLD);
        plugin.drops.put(Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD);
        plugin.drops.put(Material.DIAMOND_ORE,Material.DIAMOND);
        plugin.drops.put(Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND);
        plugin.drops.put(Material.EMERALD_ORE, Material.EMERALD);
        plugin.drops.put(Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD);
        plugin.drops.put(Material.REDSTONE_ORE, Material.REDSTONE);
        plugin.drops.put(Material.DEEPSLATE_REDSTONE_ORE, Material.REDSTONE);
        plugin.drops.put(Material.NETHER_GOLD_ORE, Material.GOLD_NUGGET);
        plugin.drops.put(Material.NETHER_QUARTZ_ORE, Material.QUARTZ);
        plugin.drops.put(Material.CLAY, Material.CLAY_BALL);
    }

}
