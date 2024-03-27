package me.spawningchests;

import me.spawningchests.chests.LootItem;
import me.spawningchests.events.ChestManager;
import me.spawningchests.events.EnterEvent;
import me.spawningchests.events.onBreak;
import me.spawningchests.events.onPlayerInteraction;
import me.spawningchests.expansions.ChestExpansion;
import me.spawningchests.files.DataManager;
import me.spawningchests.utils.Utility;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpawningChests extends JavaPlugin {

    private static SpawningChests plugin;

    public Economy eco;


    FileConfiguration config;
    File cFile;

    public Map<Material, Material> drops = new HashMap<>();

    public List<Location> specificChests = new ArrayList<>();

    public HashMap<Location, String> chestsOwners = new HashMap<>();

    public LootItem item;

    public String version = getDescription().getVersion();
    public String latestversion;

    public String configRoute;

    private final List<LootItem> lootItems = new ArrayList<>();

    public DataManager data;


    @Override
    public void onEnable() {

        plugin = this;

        if (!setupEconomy()) {
            System.out.println("(SpawningChests) You don't have any economy plugin installed!");
        }

        ConfigurationSection itemsSection = getConfig().getConfigurationSection("LootTable");
        if (itemsSection == null)
            System.out.println("LootTable is null, it wont work if you delete it!");
        for (String key : itemsSection.getKeys(false)){
            ConfigurationSection section = itemsSection.getConfigurationSection(key);
            lootItems.add(new LootItem(section));
            this.item = new LootItem(section);
        }

        Utility utils = new Utility(this);
        this.config = getConfig();
        this.config.options().copyDefaults(true);
        this.cFile = new File(getDataFolder(), "config.yml");
        configRoute = getConfig().getCurrentPath();
        saveDefaultConfig();

        this.data = new DataManager(this);

        new ChestExpansion(this).register();
        updateChecker();

        utils.materials();
        getServer().getPluginManager().registerEvents(new onBreak(this), this);
        getServer().getPluginManager().registerEvents(new ChestManager(this, getConfig()), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteraction(this), this);
        getServer().getPluginManager().registerEvents(new EnterEvent(this), this);

        getServer().broadcastMessage(ChatColor.AQUA + "SpawningChests has been enabled.");
    }

    @Override
    public void onDisable() {
        plugin = null;
        Location loc;
        for (int i = 0; i < specificChests.size(); i++) {
            loc = specificChests.get(i);
            loc.getBlock().setType(Material.AIR);
        }
        drops.clear();
        specificChests.clear();
        chestsOwners.clear();
        data.saveConfig();
    }


    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().
                getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economy != null)
            eco = economy.getProvider();
        return (eco != null);
    }

    public Economy getEconomy() {
        return eco;
    }




    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("chestreload")){
            config = YamlConfiguration.loadConfiguration(this.cFile);
            reloadConfig();
            data.saveConfig();
            data.reloadConfig();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("(ChestsReload) The config has been reloaded");
            } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                System.out.println("(ChestsReload) The config has been reloaded");
            }
        }
        return true;
    }

    public void updateChecker(){
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=115263").openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (latestversion.length() <= 7) {
                if(!version.equals(latestversion)){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"There is a new version available. "+ChatColor.YELLOW+
                            "("+ChatColor.GRAY+latestversion+ChatColor.YELLOW+")");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"You can download it at: "
                            +ChatColor.WHITE+"https://www.spigotmc.org/resources/spawningchests.115263/");
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("(SpawningChests)" + ChatColor.RED +"Error while checking update.");
        }
    }


    public void checkConfig() {
        Path path = Paths.get(configRoute);
        try {
            String text = new String(Files.readAllBytes(path));

            if (!(text.contains("a:"))) {
                //getConfig().set("a", 1);
                //saveConfig();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getVersion() {
        return version;
    }

    public String getLatestversion() {
        return latestversion;
    }

    public static SpawningChests getPlugin() {
        return plugin;
    }
}
