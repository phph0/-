package com.elimentas.effect;

public class SlowMotionEffect {

    private float targetScale;
    private int durationTicks;
    private int ticksRemaining;

    public void start(float scale, int durationTicks) {
        this.targetScale = scale;
        this.durationTicks = durationTicks;
        this.ticksRemaining = durationTicks;
    }

    public void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
    }

    public boolean isActive() {
        return ticksRemaining > 0;
    }

    public float getTimeScale() {
        if (!isActive()) return 1.0f;
        float progress = (float) ticksRemaining / durationTicks;
        return targetScale + (1.0f - targetScale) * (1.0f - progress);
    }
}
