package me.spawningchests.events;

import me.spawningchests.SpawningChests;
import me.spawningchests.chests.LootItem;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ChestManager implements Listener {

    private final SpawningChests plugin;

    private final Set<Location> openedChests = new HashSet<>();
    private final List<LootItem> lootItems = new ArrayList<>();

    public ChestManager(SpawningChests plugin, FileConfiguration lootConfig) {
        this.plugin = plugin;

        ConfigurationSection itemsSection = lootConfig.getConfigurationSection("LootTable");

        if (itemsSection == null)
            System.out.println("LootTable is null, it wont work if you delete it!");
        for (String key : itemsSection.getKeys(false)){
            ConfigurationSection section = itemsSection.getConfigurationSection(key);
            lootItems.add(new LootItem(section));
        }
    }



    @EventHandler
    private void onChestOpen(InventoryOpenEvent e){
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            if (hasBeenOpened(chest.getLocation())) return;
            markAsOpened(chest.getLocation());
            fill(chest.getInventory(), ((Chest) holder).getLocation(), (Player) e.getPlayer());
        }
    }

    public void fill(Inventory inventory, Location loc, Player player){

        if (!plugin.specificChests.contains(loc)) return;

        inventory.clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> used = new HashSet<>();

        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
            LootItem randomItem = lootItems.get(random.nextInt(lootItems.size()));
            if (used.contains(randomItem)) continue;
            used.add(randomItem);

            if (randomItem.shouldFill(random)) {
                ItemStack itemStack = randomItem.make(random);
                inventory.setItem(slotIndex, itemStack);
            }
        }
        if (plugin.getConfig().getDouble("Money.maxAmount") != 0) {
            plugin.item.makeMoney(player, random);
        }
    }

    public void markAsOpened(Location loc) {
        openedChests.add(loc);
    }

    public boolean hasBeenOpened(Location loc) {
        return openedChests.contains(loc);
    }

    public void resetChests() {
        openedChests.clear();
    }

}
