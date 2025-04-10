package me.vaan.papiPermissionFetcher;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class PluginScanner {
    // PluginName -> permission list
    private static final Map<String, List<Permission>> permissionMap = new ConcurrentHashMap<>();
    private static final Logger logger = PlaceholderAPIPlugin.getInstance().getLogger();

    public static void load() {
        File folder = Bukkit.getPluginsFolder();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                processJar(file);
            }
        }
    }

    private static void processJar(File file) {
        if (file.isDirectory()) return;
        if (!file.isFile()) return;
        if (!file.getName().endsWith(".jar")) return;

        try (JarFile jar = new JarFile(file)) {
            JarEntry pluginYmlEntry = jar.getJarEntry("plugin.yml");

            if (pluginYmlEntry == null) {
                logger.severe("plugin.yml not found in the jar.");
                return;
            }

            try(InputStream pluginYmlStream = jar.getInputStream(pluginYmlEntry)) {
                if (pluginYmlStream == null) {
                    return;
                }

                try (Reader reader = new InputStreamReader(pluginYmlStream)) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
                    String name = config.getString("name");
                    ConfigurationSection section = config.getConfigurationSection("permissions");
                    if (section == null) {
                        logger.warning("No permissions found for " + file);
                        return;
                    }

                    if (name == null) {
                        logger.severe("Jar doesn't contain plugin name " + file);
                        return;
                    }

                    Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
                    if (plugin == null || !plugin.isEnabled()) {
                        logger.severe("Plugin not found or not enabled: " + file);
                        return;
                    }

                    permissionMap.put(name, plugin.getPluginMeta().getPermissions());
                }
            } catch (IOException e) {
                logger.severe("IOError for plugin.yml of " + file);
            }
        } catch (IOException e) {
            logger.severe("Jar not valid for plugin " + file);
        }
    }

    public static Permission getPermission(String pluginName, int number) {
        var list = permissionMap.get(pluginName);
        if (list == null) {
            return null;
        }

        if (number < list.size()) {
            return list.get(number);
        }

        return null;
    }
}
