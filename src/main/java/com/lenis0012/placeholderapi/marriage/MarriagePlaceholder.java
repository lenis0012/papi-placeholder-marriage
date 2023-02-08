package com.lenis0012.placeholderapi.marriage;

import com.lenis0012.bukkit.marriage2.MPlayer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Date;

import static me.clip.placeholderapi.PlaceholderAPIPlugin.getDateFormat;

@FunctionalInterface
public interface MarriagePlaceholder {

    String process(MPlayer player);

    MarriagePlaceholder IDENTITY = mPlayer -> null;
    static String time(long mills) {
        return getDateFormat().format(new Date(mills));
    }
    static String world(Location location) {
        World world = location.getWorld();
        return world == null ? "" : world.getName();
    }
    static String location(Location location) {
        if (location == null) {
            return "";
        }
        World world = location.getWorld();
        return (world == null ? "" : (world.getName() + " / ")) + location.getBlockX() + " / " + location.getBlockY() + " / " + location.getBlockZ();
    }
}
