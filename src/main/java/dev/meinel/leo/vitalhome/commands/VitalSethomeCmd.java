/*
 * File: VitalSethomeCmd.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.commands;

import dev.meinel.leo.vitalhome.VitalHome;
import dev.meinel.leo.vitalhome.utils.commands.Cmd;
import dev.meinel.leo.vitalhome.utils.commands.CmdSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VitalSethomeCmd implements CommandExecutor {

    private final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
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
        main.getHomeStorage().saveHome(senderPlayer, arg.toLowerCase());
    }
}
