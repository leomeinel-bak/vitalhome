/*
 * VitalHome is a Spigot Plugin that gives players the ability to set homes and teleport to them.
 * Copyright © 2022 Leopold Meinel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalHome/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalhome.storage;

import com.tamrielnetwork.vitalhome.utils.Chat;
import com.tamrielnetwork.vitalhome.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class HomeStorageYaml extends HomeStorage {

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

		if (homeConf.getString("home." + playerUUID + "." + arg + ".world") == null) {
			return null;
		}
		World world = Bukkit.getWorld(Objects.requireNonNull(homeConf.getString("home." + playerUUID + "." + arg + ".world")));
		int x = homeConf.getInt("home." + playerUUID + "." + arg + ".x");
		int y = homeConf.getInt("home." + playerUUID + "." + arg + ".y");
		int z = homeConf.getInt("home." + playerUUID + "." + arg + ".z");
		int yaw = homeConf.getInt("home." + playerUUID + "." + arg + ".yaw");
		int pitch = homeConf.getInt("home." + playerUUID + "." + arg + ".pitch");

		return new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public Set<String> listHome(@NotNull Player player) {

		String playerUUID = player.getUniqueId().toString();
		Set<String> homes;

		if (homeConf.getString("home." + playerUUID) == null) {
			return null;
		}
		homes = Objects.requireNonNull(homeConf.getConfigurationSection("home." + playerUUID)).getKeys(false);

		return homes;
	}

	@Override
	public void saveHome(@NotNull Player player, @NotNull String arg) {

		String playerUUID = player.getUniqueId().toString();
		Location location = player.getLocation();

		if (homeConf.getConfigurationSection("home." + playerUUID) != null) {
			@NotNull Set<String> keys = Objects.requireNonNull(homeConf.getConfigurationSection("home." + playerUUID)).getKeys(false);

			if (keys.size() >= CmdSpec.getAllowedHomes(player, 2) && !keys.contains(arg)) {
				Chat.sendMessage(player, "max-homes");
				return;
			}
		}
		Chat.sendMessage(player, "home-set");

		clear(playerUUID, arg);

		homeConf.set("home." + playerUUID + "." + arg + ".world", location.getWorld().getName());
		homeConf.set("home." + playerUUID + "." + arg + ".x", (int) location.getX());
		homeConf.set("home." + playerUUID + "." + arg + ".y", (int) location.getY());
		homeConf.set("home." + playerUUID + "." + arg + ".z", (int) location.getZ());
		homeConf.set("home." + playerUUID + "." + arg + ".yaw", (int) location.getYaw());
		homeConf.set("home." + playerUUID + "." + arg + ".pitch", (int) location.getPitch());

		save();
	}

	@Override
	public void clear(@NotNull String playerUUID, @NotNull String arg) {

		if (homeConf.getConfigurationSection("home." + playerUUID) == null) {
			return;
		}
		for (String key : Objects.requireNonNull(homeConf.getConfigurationSection("home." + playerUUID)).getKeys(false)) {
			if (Objects.equals(key, arg)) {
				homeConf.set("home." + playerUUID + "." + key, null);
			}
		}
		save();
	}

	public void save() {

		try {
			homeConf.save(homeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}