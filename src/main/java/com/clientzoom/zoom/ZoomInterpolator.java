package com.clientzoom.zoom;

import com.clientzoom.config.ZoomConfig;

public class ZoomInterpolator {
    private float progress;
    private float targetProgress;
    private long lastUpdateNanos = -1;

    public void setTarget(float target) {
        this.targetProgress = target;
    }

    public void update() {
        long now = System.nanoTime();
        if (lastUpdateNanos < 0) {
            lastUpdateNanos = now;
            return;
        }
        float deltaSec = (now - lastUpdateNanos) / 1_000_000_000.0f;
        lastUpdateNanos = now;

        float transitionTime = ZoomConfig.getInstance().transitionTime;
        if (transitionTime <= 0) transitionTime = 0.01f;
        float speed = 1.0f / transitionTime;
        float step = speed * deltaSec;

        if (targetProgress > progress) {
            progress = Math.min(progress + step, targetProgress);
        } else if (targetProgress < progress) {
            progress = Math.max(progress - step, targetProgress);
        }
    }

    public float getEasedProgress() {
        float t = Math.clamp(progress, 0.0f, 1.0f);
        return 1.0f - (1.0f - t) * (1.0f - t);
    }

    public float getCurrentZoomFactor() {
        float maxZoom = ZoomConfig.getInstance().zoomFactor;
        float eased = getEasedProgress();
        return 1.0f + (maxZoom - 1.0f) * eased;
    }

    public boolean isActive() {
        return progress > 0.001f || targetProgress > 0;
    }

    public void reset() {
        progress = 0;
        targetProgress = 0;
        lastUpdateNanos = -1;
    }
}
