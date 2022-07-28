/*
 * VitalHome is a Spigot Plugin that gives players the ability to set homes and teleport to them.
 * Copyright © 2022 Leopold Meinel & contributors
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
 * along with this program. If not, see https://github.com/LeoMeinel/VitalHome/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalhome.utils.commands;

import com.tamrielnetwork.vitalhome.VitalHome;
import com.tamrielnetwork.vitalhome.utils.Chat;
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
			String timeRemaining = String.valueOf(main.getConfig()
			                                          .getLong("delay.time"));
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
			}.runTaskLater(main, (main.getConfig()
			                          .getLong("delay.time") * 20L));
		}
		else {
			senderPlayer.teleport(location);
		}
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String arg, @NotNull String perm) {
		return Cmd.isInvalidSender(sender) || Cmd.isNotPermitted(sender, perm) || isInvalidHome(sender, arg);
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {
		return Cmd.isInvalidSender(sender) || Cmd.isNotPermitted(sender, perm);
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
			if (attachmentInfo.getPermission()
			                  .startsWith(permissionPrefix)) {
				String permission = attachmentInfo.getPermission();
				values.add(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
			}
		}
		return Collections.max(values);
	}
}
