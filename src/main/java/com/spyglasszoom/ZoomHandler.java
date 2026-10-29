package com.spyglasszoom;

import com.spyglasszoom.config.ConfigManager;
import com.spyglasszoom.config.ZoomConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class ZoomHandler {

    private static float currentZoom = 1.0f;
    private static boolean zooming = false;

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.currentScreen != null) {
            zooming = false;
            currentZoom = 1.0f;
            return;
        }

        ZoomConfig config = ConfigManager.getConfig();
        boolean keyPressed = SpyglassZoomMod.zoomKey.isPressed();

        if (keyPressed) {
            zooming = true;
            float target = config.zoomLevel;
            currentZoom += (target - currentZoom) * config.zoomSpeed;
        } else {
            float target = 1.0f;
            currentZoom += (target - currentZoom) * config.zoomSpeed;
            if (currentZoom < 1.05f) {
                currentZoom = 1.0f;
                zooming = false;
            }
        }

        currentZoom = MathHelper.clamp(currentZoom, 1.0f, config.zoomLevel);
    }

    public static boolean isZooming() {
        return zooming;
    }

    public static double getZoomDivisor() {
        return currentZoom;
    }

    public static float getOverlayAlpha() {
        if (currentZoom <= 1.0f) return 0.0f;
        ZoomConfig config = ConfigManager.getConfig();
        return (currentZoom - 1.0f) / (config.zoomLevel - 1.0f);
    }
}
