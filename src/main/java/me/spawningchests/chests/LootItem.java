package me.spawningchests.chests;

import me.spawningchests.SpawningChests;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {

    private final Material material;
    private final String customName;
    private final Map<Enchantment, Integer> enchantmentToLevelMap = new HashMap<>();
    private final double chance, minMoneyAmount, maxMoneyAmount, money;

    private final int minAmount, maxAmount;


    public LootItem(ConfigurationSection section) {
        Material material;
        try {
            material = Material.valueOf(section.getString("material"));
        } catch (Exception e) {
            material = Material.AIR;
        }
        this.material = material;
        this.customName = section.getString("name");
        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
        if (enchantmentsSection != null) {
            for (String enchantmentKey : enchantmentsSection.getKeys(false)){
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey.toLowerCase()));

                if (enchantment != null) {
                    int level = enchantmentsSection.getInt(enchantmentKey);
                    enchantmentToLevelMap.put(enchantment, level);
                }
            }
        }

        this.chance = section.getDouble("chance")/100;
        this.money = section.getDouble("money");
        this.minAmount = section.getInt("minAmount");
        this.maxAmount = section.getInt("maxAmount");
        this.minMoneyAmount = SpawningChests.getPlugin().getConfig().getDouble("Money.minAmount");
        this.maxMoneyAmount = SpawningChests.getPlugin().getConfig().getDouble("Money.maxAmount");
    }

    public boolean shouldFill(Random random) {
        return random.nextDouble() < chance;
    }

    public ItemStack make(ThreadLocalRandom random) {
        int amount = random.nextInt(minAmount, maxAmount + 1);
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return null;

        if (customName != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', customName));
        }

        if(!enchantmentToLevelMap.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> enchantEntry : enchantmentToLevelMap.entrySet()){
                itemMeta.addEnchant(enchantEntry.getKey(),enchantEntry.getValue(),true);
            }
        }
            itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public void makeMoney(Player player, ThreadLocalRandom random) {
        double b = random.nextDouble(minMoneyAmount, maxMoneyAmount + 1);
        double amount = Math.round(b);
        SpawningChests.getPlugin().getEconomy().depositPlayer(player, amount);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                SpawningChests.getPlugin().getConfig().getString("Money_message"))
                .replace("{money}", String.valueOf(amount)));

    }
}
