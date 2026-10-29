package com.clientzoom.zoom;

import com.clientzoom.config.ZoomConfig;

public class ZoomState {
    private static final ZoomInterpolator interpolator = new ZoomInterpolator();
    private static boolean active;

    public static void setActive(boolean zoom) {
        active = zoom;
        interpolator.setTarget(zoom ? 1.0f : 0.0f);
    }

    public static void update() {
        interpolator.update();
    }

    public static boolean isZooming() {
        return interpolator.isActive();
    }

    public static double getCurrentZoomFactor() {
        return interpolator.getCurrentZoomFactor();
    }

    public static double getSensitivityMultiplier() {
        ZoomConfig config = ZoomConfig.getInstance();
        if (!config.sensitivityEnabled) return 1.0;
        double factor = interpolator.getCurrentZoomFactor();
        double multiplier = 1.0 / factor;
        return Math.max(config.sensitivityMinRatio, multiplier);
    }

    public static void reset() {
        active = false;
        interpolator.reset();
    }
}
