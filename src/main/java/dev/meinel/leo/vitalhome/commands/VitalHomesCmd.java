/*
 * File: VitalHomesCmd.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.commands;

import dev.meinel.leo.vitalhome.VitalHome;
import dev.meinel.leo.vitalhome.utils.Chat;
import dev.meinel.leo.vitalhome.utils.commands.Cmd;
import dev.meinel.leo.vitalhome.utils.commands.CmdSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class VitalHomesCmd
		implements CommandExecutor {

	private final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (Cmd.isArgsLengthNotEqualTo(sender, args, 0)) {
			return false;
		}
		doHomes(sender);
		return true;
	}

	private void doHomes(@NotNull CommandSender sender) {
		if (CmdSpec.isInvalidCmd(sender, "vitalhome.list")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		StringBuilder homesBuilder = new StringBuilder();
		Set<String> homesSet = main.getHomeStorage()
				.listHome(senderPlayer);
		if (homesSet.isEmpty()) {
			Chat.sendMessage(sender, "no-homes");
			return;
		}
		List<String> homesList = main.getHomeStorage()
				.listHome(senderPlayer)
				.stream()
				.toList();
		for (String home : homesList) {
			if (home.equals(homesList.get(0))) {
				homesBuilder.append("&b")
						.append(home);
				continue;
			}
			homesBuilder.append("&f, &b")
					.append(home);
		}
		String homes = homesBuilder.toString();
		sender.sendMessage(Chat.replaceColors(homes));
	}
}
