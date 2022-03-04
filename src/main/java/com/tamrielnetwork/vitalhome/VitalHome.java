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

package com.tamrielnetwork.vitalhome;

import com.tamrielnetwork.vitalhome.commands.VitalDelHomeCmd;
import com.tamrielnetwork.vitalhome.commands.VitalHomeCmd;
import com.tamrielnetwork.vitalhome.commands.VitalHomesCmd;
import com.tamrielnetwork.vitalhome.commands.VitalSethomeCmd;
import com.tamrielnetwork.vitalhome.files.Messages;
import com.tamrielnetwork.vitalhome.storage.HomeStorage;
import com.tamrielnetwork.vitalhome.storage.HomeStorageSql;
import com.tamrielnetwork.vitalhome.storage.HomeStorageYaml;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VitalHome extends JavaPlugin {

	private HomeStorage homeStorage;
	private Messages messages;

	@Override
	public void onEnable() {

		registerCommands();

		saveDefaultConfig();

		setupStorage();

		messages = new Messages();

		Bukkit.getLogger().info("VitalHome v" + this.getDescription().getVersion() + " enabled");
		Bukkit.getLogger().info("Copyright (C) 2022 Leopold Meinel");
		Bukkit.getLogger().info("This program comes with ABSOLUTELY NO WARRANTY!");
		Bukkit.getLogger().info("This is free software, and you are welcome to redistribute it under certain conditions.");
		Bukkit.getLogger().info("See https://github.com/TamrielNetwork/VitalHome/blob/main/LICENSE for more details.");
	}

	@Override
	public void onDisable() {

		Bukkit.getLogger().info("VitalHome v" + this.getDescription().getVersion() + " disabled");
	}

	private void setupStorage() {

		String storageSystem = getConfig().getString("storage-system");

		if (Objects.requireNonNull(storageSystem).equalsIgnoreCase("mysql")) {
			this.homeStorage = new HomeStorageSql();
		} else {
			this.homeStorage = new HomeStorageYaml();
		}
	}

	private void registerCommands() {

		Objects.requireNonNull(getCommand("home")).setExecutor(new VitalHomeCmd());
		Objects.requireNonNull(getCommand("home")).setTabCompleter(new VitalHomeCmd());
		Objects.requireNonNull(getCommand("homes")).setExecutor(new VitalHomesCmd());
		Objects.requireNonNull(getCommand("sethome")).setExecutor(new VitalSethomeCmd());
		Objects.requireNonNull(getCommand("delhome")).setExecutor(new VitalDelHomeCmd());
		Objects.requireNonNull(getCommand("delhome")).setTabCompleter(new VitalDelHomeCmd());
	}

	public Messages getMessages() {

		return messages;
	}

	public HomeStorage getHomeStorage() {

		return homeStorage;
	}

}

