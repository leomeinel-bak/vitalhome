/*
 * File: VitalDelHomeCmd.java
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
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VitalDelHomeCmd
        implements TabExecutor {

    private final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (Cmd.isArgsLengthNotEqualTo(sender, args, 1)) {
            return false;
        }
        delHome(sender, args[0]);
        return true;
    }

    private void delHome(@NotNull CommandSender sender, String arg) {
        if (CmdSpec.isInvalidCmd(sender, arg, "vitalhome.delhome")) {
            return;
        }
        Player senderPlayer = (Player) sender;
        String playerUUID = senderPlayer.getUniqueId()
                .toString();
        main.getHomeStorage()
                .clear(playerUUID, arg.toLowerCase());
        Chat.sendMessage(sender, "home-removed");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        Player senderPlayer = (Player) sender;
        if (main.getHomeStorage()
                .listHome(senderPlayer)
                .isEmpty()) {
            return null;
        }
        return new ArrayList<>(main.getHomeStorage()
                .listHome(senderPlayer));
    }
}
