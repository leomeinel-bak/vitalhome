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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalHome/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalhome.commands;

import com.tamrielnetwork.vitalhome.VitalHome;
import com.tamrielnetwork.vitalhome.utils.commands.Cmd;
import com.tamrielnetwork.vitalhome.utils.commands.CmdSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VitalSethomeCmd
		implements CommandExecutor {

	private final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
	                         @NotNull String[] args) {
		if (Cmd.isArgsLengthNotEqualTo(sender, args, 1)) {
			return false;
		}
		setHome(sender, args[0]);
		return true;
	}

	private void setHome(@NotNull CommandSender sender, String arg) {
		if (CmdSpec.isInvalidCmd(sender, arg, "vitalhome.sethome")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		main.getHomeStorage()
		    .saveHome(senderPlayer, arg.toLowerCase());
	}
}
