/*
 * File: VitalHome.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome;

import dev.meinel.leo.vitalhome.commands.VitalDelHomeCmd;
import dev.meinel.leo.vitalhome.commands.VitalHomeCmd;
import dev.meinel.leo.vitalhome.commands.VitalHomesCmd;
import dev.meinel.leo.vitalhome.commands.VitalSethomeCmd;
import dev.meinel.leo.vitalhome.files.Messages;
import dev.meinel.leo.vitalhome.storage.HomeStorage;
import dev.meinel.leo.vitalhome.storage.HomeStorageSql;
import dev.meinel.leo.vitalhome.storage.HomeStorageYaml;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VitalHome
        extends JavaPlugin {

    private HomeStorage homeStorage;
    private Messages messages;

    @Override
    public void onEnable() {
        registerCommands();
        saveDefaultConfig();
        setupStorage();
        messages = new Messages();
        Bukkit.getLogger()
                .info("VitalHome v" + this.getDescription()
                        .getVersion() + " enabled");
        Bukkit.getLogger()
                .info("Copyright (C) 2022 Leopold Meinel");
        Bukkit.getLogger()
                .info("This program comes with ABSOLUTELY NO WARRANTY!");
        Bukkit.getLogger()
                .info("This is free software, and you are welcome to redistribute it under certain conditions.");
        Bukkit.getLogger()
                .info("See https://github.com/LeoMeinel/VitalHome/blob/main/LICENSE for more details.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger()
                .info("VitalHome v" + this.getDescription()
                        .getVersion() + " disabled");
    }

    private void setupStorage() {
        String storageSystem = getConfig().getString("storage-system");
        if (Objects.requireNonNull(storageSystem)
                .equalsIgnoreCase("mysql")) {
            this.homeStorage = new HomeStorageSql();
        } else {
            this.homeStorage = new HomeStorageYaml();
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("home"))
                .setExecutor(new VitalHomeCmd());
        Objects.requireNonNull(getCommand("home"))
                .setTabCompleter(new VitalHomeCmd());
        Objects.requireNonNull(getCommand("homes"))
                .setExecutor(new VitalHomesCmd());
        Objects.requireNonNull(getCommand("sethome"))
                .setExecutor(new VitalSethomeCmd());
        Objects.requireNonNull(getCommand("delhome"))
                .setExecutor(new VitalDelHomeCmd());
        Objects.requireNonNull(getCommand("delhome"))
                .setTabCompleter(new VitalDelHomeCmd());
    }

    public Messages getMessages() {
        return messages;
    }

    public HomeStorage getHomeStorage() {
        return homeStorage;
    }
}
