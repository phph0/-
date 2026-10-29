package com.elimentas.effect;

public class CameraShakeEffect {

    private float strength;
    private int durationTicks;
    private int ticksRemaining;

    public void start(float strength, int durationTicks) {
        this.strength = strength;
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

    public float getYawOffset() {
        if (!isActive()) return 0;
        float progress = (float) ticksRemaining / durationTicks;
        float decay = progress * progress;
        return (float) (Math.random() - 0.5) * 2 * strength * decay;
    }

    public float getPitchOffset() {
        if (!isActive()) return 0;
        float progress = (float) ticksRemaining / durationTicks;
        float decay = progress * progress;
        return (float) (Math.random() - 0.5) * 2 * strength * decay;
    }
}
