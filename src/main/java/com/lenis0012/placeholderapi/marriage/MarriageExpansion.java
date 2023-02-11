package com.lenis0012.placeholderapi.marriage;

import com.lenis0012.bukkit.marriage2.MPlayer;
import com.lenis0012.bukkit.marriage2.MarriageAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;

import static com.lenis0012.placeholderapi.marriage.MarriagePlaceholder.*;
import static me.clip.placeholderapi.PlaceholderAPIPlugin.booleanFalse;
import static me.clip.placeholderapi.PlaceholderAPIPlugin.booleanTrue;

public class MarriageExpansion extends PlaceholderExpansion {
    private final Map<String, MarriagePlaceholder> placeholders = new LinkedHashMap<>();
    private final Map<String, MarriagePlaceholder> partnerPlaceholders = new LinkedHashMap<>();

    @Override
    public @NotNull String getIdentifier() {
        return "marriage";
    }

    @Override
    public @NotNull String getAuthor() {
        return "lenis0012";
    }

    @Override
    public @NotNull String getVersion() {
        InputStream input = MarriageExpansion.class.getResourceAsStream("/expansion.properties");
        Properties info = new Properties();
        try {
            info.load(input);
            return info.getProperty("expansion.version");
        } catch (Exception e) {
            return "unknown";
        }
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "Marriage";
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return new ArrayList<>(placeholders.keySet());
    }

    @Override
    public boolean register() {
        placeholders.put("is_married", mPlayer -> mPlayer.isMarried() ? booleanTrue() : booleanFalse());
        placeholders.put("is_priest", mPlayer -> mPlayer.isPriest() ? booleanTrue() : booleanFalse());
        placeholders.put("is_in_chat", mPlayer -> mPlayer.isInChat() ? booleanTrue() : booleanFalse());
        placeholders.put("gender", mPlayer -> mPlayer.getGender().toString().toLowerCase());
        placeholders.put("gender_chat_prefix", mPlayer -> mPlayer.getGender().getChatPrefix());
        placeholders.put("has_pvp_enabled", mPlayer -> mPlayer.isMarried() && mPlayer.getActiveRelationship().isPVPEnabled() ? booleanTrue() : booleanFalse());
        placeholders.put("has_home_set", mPlayer -> mPlayer.isMarried() && mPlayer.getActiveRelationship().isHomeSet() ? booleanTrue() : booleanFalse());
        placeholders.put("home_location", mPlayer -> (mPlayer.isMarried() && mPlayer.getActiveRelationship().isHomeSet()) ?
            location(mPlayer.getActiveRelationship().getHome()) : "");
        placeholders.put("home_world", mPlayer -> (mPlayer.isMarried() && mPlayer.getActiveRelationship().isHomeSet()) ?
            world(mPlayer.getActiveRelationship().getHome()) : "");
        placeholders.put("home_x", mPlayer -> (mPlayer.isMarried() && mPlayer.getActiveRelationship().isHomeSet()) ?
            String.valueOf(mPlayer.getActiveRelationship().getHome().getBlockX()) : "");
        placeholders.put("home_y", mPlayer -> (mPlayer.isMarried() && mPlayer.getActiveRelationship().isHomeSet()) ?
            String.valueOf(mPlayer.getActiveRelationship().getHome().getBlockY()) : "");
        placeholders.put("home_z", mPlayer -> (mPlayer.isMarried() && mPlayer.getActiveRelationship().isHomeSet()) ?
            String.valueOf(mPlayer.getActiveRelationship().getHome().getBlockZ()) : "");
        placeholders.put("partner", mPlayer -> {
            if (!mPlayer.isMarried()) {
                return "";
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(mPlayer.getActiveRelationship().getOtherPlayer(mPlayer.getUniqueId()));
            String name = offlinePlayer.getName();
            return name == null ? "" : name;
        });
        partnerPlaceholders.put("is_priest", mPlayer -> mPlayer.isPriest() ? booleanTrue() : booleanFalse());
        partnerPlaceholders.put("is_in_chat", mPlayer -> mPlayer.isInChat() ? booleanTrue() : booleanFalse());
        partnerPlaceholders.put("gender", mPlayer -> mPlayer.getGender().toString().toLowerCase());
        partnerPlaceholders.put("gender_chat_prefix", mPlayer -> mPlayer.getGender().getChatPrefix());
        partnerPlaceholders.put("seen", mPlayer -> time(mPlayer.getLastLogin()));
        partnerPlaceholders.put("seen_logout", mPlayer -> time(mPlayer.getLastLogout()));
        return super.register();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        MPlayer mPlayer = MarriageAPI.getMPlayer(player);
        if (mPlayer == null) {
            mPlayer = MarriageAPI.getInstance().getMPlayer(player);
        }

        if (mPlayer != null && identifier.startsWith("partner_")) {
            MPlayer partner = mPlayer.getPartner();
            if (partner != null) {
                return partnerPlaceholders.getOrDefault(identifier.substring(8), MarriagePlaceholder.IDENTITY).process(partner);
            }
        }
        return placeholders.getOrDefault(identifier, MarriagePlaceholder.IDENTITY).process(mPlayer);
    }
}