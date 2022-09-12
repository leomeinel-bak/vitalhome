/*
 * File: HomeStorage.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.storage;

import dev.meinel.leo.vitalhome.VitalHome;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class HomeStorage {

    protected final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

    public abstract Location loadHome(@NotNull Player player, @NotNull String arg);

    public abstract Set<String> listHome(@NotNull Player player);

    public abstract void saveHome(@NotNull Player player, @NotNull String arg);

    public abstract void clear(@NotNull String playerUUID, @NotNull String arg);
}