/*
 * File: HomeStorageSql.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.storage;

import dev.meinel.leo.vitalhome.storage.mysql.SqlManager;
import dev.meinel.leo.vitalhome.utils.Chat;
import dev.meinel.leo.vitalhome.utils.commands.CmdSpec;
import dev.meinel.leo.vitalhome.utils.sql.Sql;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HomeStorageSql extends HomeStorage {

    private static final String SQLEXCEPTION =
            "VitalHome encountered an SQLException while executing task";

    public HomeStorageSql() {
        new SqlManager();
    }

    @Override
    public Location loadHome(@NotNull Player player, @NotNull String arg) {
        String playerUUID = player.getUniqueId().toString();
        World world = null;
        int x = 0;
        int y = 0;
        int z = 0;
        int yaw = 0;
        int pitch = 0;
        try (PreparedStatement selectStatement =
                SqlManager.getConnection().prepareStatement("SELECT * FROM ?Home")) {
            selectStatement.setString(1, Sql.getPrefix());
            selectStatement.executeUpdate();
            try (ResultSet rs = selectStatement.executeQuery()) {
                while (rs.next()) {
                    if (!Objects.equals(rs.getString(1), playerUUID) || rs.getString(1) == null
                            || !Objects.equals(rs.getString(2), arg) || rs.getString(3) == null) {
                        continue;
                    }
                    world = Bukkit.getWorld(Objects.requireNonNull(rs.getString(3)));
                    x = rs.getInt(4);
                    y = rs.getInt(5);
                    z = rs.getInt(6);
                    yaw = rs.getInt(7);
                    pitch = rs.getInt(8);
                }
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
            return null;
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public Set<String> listHome(@NotNull Player player) {
        String playerUUID = player.getUniqueId().toString();
        Set<String> homes = new HashSet<>();
        try (PreparedStatement selectStatement =
                SqlManager.getConnection().prepareStatement("SELECT * FROM ?Home")) {
            selectStatement.setString(1, Sql.getPrefix());
            selectStatement.executeUpdate();
            try (ResultSet rs = selectStatement.executeQuery()) {
                while (rs.next()) {
                    if (!Objects.equals(rs.getString(1), playerUUID) || rs.getString(1) == null) {
                        continue;
                    }
                    homes.add(rs.getString(2));
                }
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
            return Collections.emptySet();
        }
        return homes;
    }

    @Override
    public void saveHome(@NotNull Player player, @NotNull String arg) {
        String playerUUID = player.getUniqueId().toString();
        Location location = player.getLocation();
        int homes = 0;
        try (PreparedStatement selectStatement = SqlManager.getConnection()
                .prepareStatement("SELECT COUNT(*) FROM ?Home WHERE `UUID`=?")) {
            selectStatement.setString(1, Sql.getPrefix());
            selectStatement.setString(2, playerUUID);
            selectStatement.executeUpdate();
            try (ResultSet rs = selectStatement.executeQuery()) {
                rs.next();
                homes = rs.getInt(1);
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
        if (homes >= CmdSpec.getAllowedHomes(player, 1)) {
            Chat.sendMessage(player, "max-homes");
            return;
        }
        Chat.sendMessage(player, "home-set");
        clear(playerUUID, arg);
        try (PreparedStatement insertStatement = SqlManager.getConnection().prepareStatement(
                "INSERT INTO ?Home (`UUID`, `Home`, `World`, `X`, `Y`, `Z`, `Yaw`, `Pitch`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            insertStatement.setString(1, Sql.getPrefix());
            insertStatement.setString(2, playerUUID);
            insertStatement.setString(3, arg);
            insertStatement.setString(4, location.getWorld().getName());
            insertStatement.setInt(5, (int) location.getX());
            insertStatement.setInt(6, (int) location.getY());
            insertStatement.setInt(7, (int) location.getZ());
            insertStatement.setInt(8, (int) location.getYaw());
            insertStatement.setInt(9, (int) location.getPitch());
            insertStatement.executeUpdate();
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
    }

    @Override
    public void clear(@NotNull String playerUUID, @NotNull String arg) {
        try (PreparedStatement deleteStatement = SqlManager.getConnection()
                .prepareStatement("DELETE FROM ?Home WHERE `UUID`=?" + " AND `Home`=?")) {
            deleteStatement.setString(1, Sql.getPrefix());
            deleteStatement.setString(2, playerUUID);
            deleteStatement.setString(3, arg);
            deleteStatement.executeUpdate();
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
    }
}
