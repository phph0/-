package com.ctl.config;

import com.ctl.ChatTranslationMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("ctl.json");
    private static ChatTranslationConfig config = new ChatTranslationConfig();

    public static ChatTranslationConfig getConfig() {
        return config;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                config = GSON.fromJson(json, ChatTranslationConfig.class);
                if (config == null) {
                    config = new ChatTranslationConfig();
                }
            } catch (IOException e) {
                ChatTranslationMod.LOGGER.error("Failed to load config", e);
                config = new ChatTranslationConfig();
            }
        }
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(config));
        } catch (IOException e) {
            ChatTranslationMod.LOGGER.error("Failed to save config", e);
        }
    }
}
