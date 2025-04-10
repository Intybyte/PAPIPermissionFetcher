package me.vaan.papiPermissionFetcher;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PAPIPermissionFetcher extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "permissionfetch";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Vaan1310";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean register() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(PlaceholderAPIPlugin.getInstance(), PluginScanner::load, 1L);
        return super.register();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] split = params.split("_");
        if (split.length != 3) {
            return null;
        }

        try {
            int position = Integer.parseInt(split[2]);
            Permission permission = PluginScanner.getPermission(split[1], position);
            if (permission == null) {
                return null;
            }

            return switch (split[0]) {
                case "fetch" -> permission.getName();
                case "default" -> permission.getDefault().toString();
                case "description" -> permission.getDescription();
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
