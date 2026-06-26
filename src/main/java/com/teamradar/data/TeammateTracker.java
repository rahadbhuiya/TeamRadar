package com.teamradar.data;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public class TeammateTracker {

    private static final Map<String, TeammateInfo> teammates = new LinkedHashMap<>();
    private static final Set<String> lowHpAlerted = new HashSet<>();
    private static final Set<String> deathAlerted = new HashSet<>();
    private static final float LOW_HP_THRESHOLD = 6f;

    public static void reload() {
        teammates.clear();
        lowHpAlerted.clear();
        deathAlerted.clear();
        for (String name : TeamData.getMembers()) {
            teammates.put(name.toLowerCase(), new TeammateInfo(name));
        }
    }

    public static void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;

        for (TeammateInfo info : teammates.values()) {
            info.online = false;
            info.recentlyDied = false;
        }

        for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
            String key = player.getName().getString().toLowerCase();
            if (!teammates.containsKey(key)) continue;

            TeammateInfo info = teammates.get(key);
            info.online = true;
            info.name = player.getName().getString();
            info.health = player.getHealth();
            info.maxHealth = player.getMaxHealth();
            info.x = player.getX();
            info.y = player.getY();
            info.z = player.getZ();
            info.dimension = client.world.getRegistryKey().getValue().toString();

            // Low HP alert
            if (info.health <= LOW_HP_THRESHOLD && !lowHpAlerted.contains(key)) {
                lowHpAlerted.add(key);
                client.player.sendMessage(
                        Text.literal("[TeamRadar] WARNING: " + info.name + " is low on health! ("
                                + String.format("%.1f", info.health) + " HP)"),
                        false
                );
            } else if (info.health > LOW_HP_THRESHOLD) {
                lowHpAlerted.remove(key);
            }

            // Death detection
            if ((player.isDead() || player.getHealth() <= 0) && !deathAlerted.contains(key)) {
                info.recentlyDied = true;
                deathAlerted.add(key);
                client.player.sendMessage(
                        Text.literal("[TeamRadar] " + info.name + " died!"),
                        false
                );
            } else if (!player.isDead() && player.getHealth() > 0) {
                deathAlerted.remove(key);
            }
        }
    }

    public static Collection<TeammateInfo> getAll() {
        return teammates.values();
    }

    public static boolean hasTeammates() {
        return !teammates.isEmpty();
    }
}
