package me.spawningchests.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.spawningchests.SpawningChests;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChestExpansion extends PlaceholderExpansion {

    private final SpawningChests plugin;

    public ChestExpansion(SpawningChests plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "chests";
    }

    @Override
    public @NotNull String getAuthor() {
        return "frags";
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        if (player == null) {
            return "";
        }

        if (params.equals("amount")) {
            if (plugin.data.getConfig().getInt(player.getDisplayName() + ".ChestsSpawned") == 0) {
                return "0";
            } else {
                return String.valueOf(plugin.data.getConfig().getInt(player.getDisplayName() + ".ChestsSpawned"));
            }
        }


        return null;
    }
}
