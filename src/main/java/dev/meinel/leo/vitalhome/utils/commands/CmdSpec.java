/*
 * File: CmdSpec.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.utils.commands;

import dev.meinel.leo.vitalhome.VitalHome;
import dev.meinel.leo.vitalhome.utils.Chat;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CmdSpec {

    private static final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);
    private static final List<UUID> onActiveDelay = new ArrayList<>();

    private CmdSpec() {
        throw new IllegalStateException("Utility class");
    }

    public static void doDelay(@NotNull CommandSender sender, Location location) {
        Player senderPlayer = (Player) sender;
        if (!senderPlayer.hasPermission("vitalspawn.delay.bypass")) {
            if (onActiveDelay.contains(senderPlayer.getUniqueId())) {
                Chat.sendMessage(sender, "active-delay");
                return;
            }
            onActiveDelay.add(senderPlayer.getUniqueId());
            String timeRemaining = String.valueOf(main.getConfig().getLong("delay.time"));
            Chat.sendMessage(senderPlayer, Map.of("%countdown%", timeRemaining), "countdown");
            new BukkitRunnable() {

                @Override
                public void run() {
                    if (Cmd.isInvalidPlayer(senderPlayer)) {
                        onActiveDelay.remove(senderPlayer.getUniqueId());
                        return;
                    }
                    senderPlayer.teleport(location);
                    onActiveDelay.remove(senderPlayer.getUniqueId());
                }
            }.runTaskLater(main, (main.getConfig().getLong("delay.time") * 20L));
        } else {
            senderPlayer.teleport(location);
        }
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String arg,
            @NotNull String perm) {
        return Cmd.isInvalidSender(sender) || !Cmd.isPermitted(sender, perm)
                || isInvalidHome(sender, arg);
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {
        return Cmd.isInvalidSender(sender) || !Cmd.isPermitted(sender, perm);
    }

    public static boolean isInvalidLocation(Location location) {
        return location == null || location.getWorld() == null;
    }

    private static boolean isInvalidHome(@NotNull CommandSender sender, @NotNull String arg) {
        if (arg.length() > 64) {
            Chat.sendMessage(sender, "invalid-home");
            return true;
        }
        return false;
    }

    public static int getAllowedHomes(@NotNull Player player, int defaultValue) {
        List<Integer> values = new ArrayList<>();
        values.add(defaultValue);
        String permissionPrefix = "vitalhome.homes.";
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getPermission().startsWith(permissionPrefix)) {
                String permission = attachmentInfo.getPermission();
                values.add(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
            }
        }
        return Collections.max(values);
    }
}
