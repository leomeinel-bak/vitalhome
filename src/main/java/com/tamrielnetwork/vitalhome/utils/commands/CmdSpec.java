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

package com.tamrielnetwork.vitalhome.utils.commands;

import com.google.common.collect.ImmutableMap;
import com.tamrielnetwork.vitalhome.VitalHome;
import com.tamrielnetwork.vitalhome.utils.Chat;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class CmdSpec {

	private static final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

	public static void doDelay(@NotNull CommandSender sender, Location location) {

		Player senderPlayer = (Player) sender;

		if (!senderPlayer.hasPermission("vitalspawn.delay.bypass")) {
			String timeRemaining = String.valueOf(main.getConfig().getLong("delay.time"));
			Chat.sendMessage(senderPlayer, ImmutableMap.of("%countdown%", timeRemaining), "countdown");
			new BukkitRunnable() {

				@Override
				public void run() {

					senderPlayer.teleport(location);
				}
			}.runTaskLater(main, (main.getConfig().getLong("delay.time") * 20L));
		} else {
			senderPlayer.teleport(location);
		}
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {

		return Cmd.isInvalidSender(sender) || Cmd.isNotPermitted(sender, perm);
	}

	public static boolean isInvalidLocation(Location location) {

		if (location == null) {
			return true;
		}
		return location.getWorld() == null;
	}

	public static int getAllowedHomes(Player player, int defaultValue) {

		String permissionPrefix = "vitalhome.homes.";

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getPermission().startsWith(permissionPrefix)) {
				String permission = attachmentInfo.getPermission();
				return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
			}
		}

		player.sendMessage(String.valueOf(defaultValue));
		return defaultValue;
	}

}
