/*
 * File: HomeStorageYaml.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.storage;

import dev.meinel.leo.vitalhome.utils.Chat;
import dev.meinel.leo.vitalhome.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class HomeStorageYaml extends HomeStorage {

    private static final String IOEXCEPTION =
            "VitalHome encountered an IOException while executing task";
    private static final String HOME = "home.";
    private static final String WORLD = ".world";
    private final File homeFile;
    private final FileConfiguration homeConf;

    public HomeStorageYaml() {
        homeFile = new File(main.getDataFolder(), "home.yml");
        homeConf = YamlConfiguration.loadConfiguration(homeFile);
        save();
    }

    @Override
    public Location loadHome(@NotNull Player player, @NotNull String arg) {
        String playerUUID = player.getUniqueId().toString();
        if (homeConf.getString(HOME + playerUUID + "." + arg + WORLD) == null) {
            return null;
        }
        World world = Bukkit.getWorld(
                Objects.requireNonNull(homeConf.getString(HOME + playerUUID + "." + arg + WORLD)));
        int x = homeConf.getInt(HOME + playerUUID + "." + arg + ".x");
        int y = homeConf.getInt(HOME + playerUUID + "." + arg + ".y");
        int z = homeConf.getInt(HOME + playerUUID + "." + arg + ".z");
        int yaw = homeConf.getInt(HOME + playerUUID + "." + arg + ".yaw");
        int pitch = homeConf.getInt(HOME + playerUUID + "." + arg + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public Set<String> listHome(@NotNull Player player) {
        String playerUUID = player.getUniqueId().toString();
        Set<String> homes;
        if (homeConf.getString(HOME + playerUUID) == null) {
            return Collections.emptySet();
        }
        homes = Objects.requireNonNull(homeConf.getConfigurationSection(HOME + playerUUID))
                .getKeys(false);
        return homes;
    }

    @Override
    public void saveHome(@NotNull Player player, @NotNull String arg) {
        String playerUUID = player.getUniqueId().toString();
        Location location = player.getLocation();
        if (homeConf.getConfigurationSection(HOME + playerUUID) != null) {
            @NotNull
            Set<String> keys =
                    Objects.requireNonNull(homeConf.getConfigurationSection(HOME + playerUUID))
                            .getKeys(false);
            if (keys.size() >= CmdSpec.getAllowedHomes(player, 1) && !keys.contains(arg)) {
                Chat.sendMessage(player, "max-homes");
                return;
            }
        }
        Chat.sendMessage(player, "home-set");
        clear(playerUUID, arg);
        homeConf.set(HOME + playerUUID + "." + arg + WORLD, location.getWorld().getName());
        homeConf.set(HOME + playerUUID + "." + arg + ".x", (int) location.getX());
        homeConf.set(HOME + playerUUID + "." + arg + ".y", (int) location.getY());
        homeConf.set(HOME + playerUUID + "." + arg + ".z", (int) location.getZ());
        homeConf.set(HOME + playerUUID + "." + arg + ".yaw", (int) location.getYaw());
        homeConf.set(HOME + playerUUID + "." + arg + ".pitch", (int) location.getPitch());
        save();
    }

    @Override
    public void clear(@NotNull String playerUUID, @NotNull String arg) {
        if (homeConf.getConfigurationSection(HOME + playerUUID) == null) {
            return;
        }
        for (String key : Objects
                .requireNonNull(homeConf.getConfigurationSection(HOME + playerUUID))
                .getKeys(false)) {
            if (Objects.equals(key, arg)) {
                homeConf.set(HOME + playerUUID + "." + key, null);
            }
        }
    }

    private void save() {
        try {
            homeConf.save(homeFile);
        } catch (IOException ignored) {
            Bukkit.getLogger().info(IOEXCEPTION);
        }
    }
}
