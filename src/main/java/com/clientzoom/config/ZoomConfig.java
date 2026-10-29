package com.clientzoom.config;

import com.clientzoom.ClientZoomMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZoomConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ZoomConfig instance = new ZoomConfig();

    public float zoomFactor = 4.0f;
    public float transitionTime = 0.20f;
    public boolean sensitivityEnabled = true;
    public float sensitivityMinRatio = 0.25f;
    public boolean enabled = true;

    public static ZoomConfig getInstance() {
        return instance;
    }

    public static void load() {
        Path path = getConfigPath();
        if (Files.exists(path)) {
            try {
                String json = Files.readString(path);
                ZoomConfig loaded = GSON.fromJson(json, ZoomConfig.class);
                if (loaded != null) {
                    loaded.validate();
                    instance = loaded;
                    return;
                }
            } catch (Exception e) {
                ClientZoomMod.LOGGER.warn("Failed to load config, using defaults", e);
            }
        }
        instance = new ZoomConfig();
        save();
    }

    public static void save() {
        try {
            Path path = getConfigPath();
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(instance));
        } catch (IOException e) {
            ClientZoomMod.LOGGER.warn("Failed to save config", e);
        }
    }

    private void validate() {
        zoomFactor = Math.clamp(zoomFactor, 1.5f, 10.0f);
        transitionTime = Math.clamp(transitionTime, 0.05f, 0.60f);
        sensitivityMinRatio = Math.clamp(sensitivityMinRatio, 0.2f, 1.0f);
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("clientzoom.json");
    }
}
