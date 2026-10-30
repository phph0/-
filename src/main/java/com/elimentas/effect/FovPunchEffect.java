package com.elimentas.effect;

public class FovPunchEffect {

    private float amount;
    private int durationTicks;
    private int ticksRemaining;

    public void start(float amount, int durationTicks) {
        this.amount = amount;
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

    public float getFovOffset() {
        if (!isActive()) return 0;
        float progress = (float) ticksRemaining / durationTicks;
        return amount * progress * progress;
    }
}
