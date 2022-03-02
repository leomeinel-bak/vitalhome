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

package com.tamrielnetwork.vitalhome.commands;

import com.tamrielnetwork.vitalhome.VitalHome;
import com.tamrielnetwork.vitalhome.utils.Chat;
import com.tamrielnetwork.vitalhome.utils.commands.Cmd;
import com.tamrielnetwork.vitalhome.utils.commands.CmdSpec;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VitalHomeCmd implements CommandExecutor {

	private final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (Cmd.isArgsLengthNotEqualTo(sender, args, 2)) {
			return true;
		}
		if (args[0].equals("sethome")) {
			setHome(sender, args[1]);
			return true;
		}
		if (args[0].equals("home")) {
			doHome(sender, args[1]);
			return true;
		}
		Chat.sendMessage(sender, "cmd");
		return true;

	}

	private void doHome(@NotNull CommandSender sender, String arg) {

		if (CmdSpec.isInvalidCmd(sender, "vitalhome.home")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		Location location = main.getHomeStorage().loadHome(senderPlayer, arg.toLowerCase());
		if (CmdSpec.isInvalidLocation(sender, location)) {
			return;
		}

		senderPlayer.teleport(location);

	}

	private void setHome(@NotNull CommandSender sender, String arg) {

		if (CmdSpec.isInvalidCmd(sender, "vitalhome.sethome")) {
			return;
		}
		Player senderPlayer = (Player) sender;

		main.getHomeStorage().saveHome(senderPlayer, arg.toLowerCase());

	}

}