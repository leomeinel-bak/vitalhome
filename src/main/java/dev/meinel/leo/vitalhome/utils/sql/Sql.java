/*
 * File: Sql.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.utils.sql;

import dev.meinel.leo.vitalhome.VitalHome;
import org.bukkit.plugin.java.JavaPlugin;

public class Sql {

    private static final VitalHome main = JavaPlugin.getPlugin(VitalHome.class);

    private Sql() {
        throw new IllegalStateException("Utility class");
    }

    public static String getPrefix() {
        return main.getConfig().getString("mysql.prefix");
    }
}
