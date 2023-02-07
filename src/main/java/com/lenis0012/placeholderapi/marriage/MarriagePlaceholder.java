package com.lenis0012.placeholderapi.marriage;

import com.lenis0012.bukkit.marriage2.MPlayer;
import org.bukkit.Location;

@FunctionalInterface
public interface MarriagePlaceholder {

    String process(MPlayer player);

    MarriagePlaceholder IDENTITY = mPlayer -> null;

    static String location(Location location) {
        if (location == null) {
            return "";
        }

        return location.getBlockX() + " / " + location.getBlockY() + " / " + location.getBlockZ();
    }
}
