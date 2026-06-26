package com.teamradar.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TeamData {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("teamradar_members.json");

    // List of teammate usernames to track
    private static final List<String> members = new ArrayList<>();

    public static List<String> getMembers() {
        return new ArrayList<>(members);
    }

    public static void addMember(String name) {
        if (!members.contains(name)) {
            members.add(name);
            save();
        }
    }

    public static void removeMember(String name) {
        members.remove(name);
        save();
    }

    public static boolean isMember(String name) {
        return members.stream().anyMatch(m -> m.equalsIgnoreCase(name));
    }

    public static void save() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(members, writer);
        } catch (IOException e) {
            System.err.println("[TeamRadar] Failed to save team members: " + e.getMessage());
        }
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            // Create default empty config
            save();
            return;
        }
        try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
            Type type = new TypeToken<List<String>>() {}.getType();
            List<String> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                members.clear();
                members.addAll(loaded);
            }
        } catch (IOException e) {
            System.err.println("[TeamRadar] Failed to load team members: " + e.getMessage());
        }
    }
}
