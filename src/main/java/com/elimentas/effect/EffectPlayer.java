package com.elimentas.effect;

import com.elimentas.skill.fire.FireballSkill;

public class EffectPlayer {

    private static final CameraShakeEffect CAMERA_SHAKE = new CameraShakeEffect();
    private static final SlowMotionEffect SLOW_MOTION = new SlowMotionEffect();
    private static final ScreenTintRenderer SCREEN_TINT = new ScreenTintRenderer();

    public static void play(String skillId, double x, double y, double z) {
        if (skillId.equals(FireballSkill.ID.toString())) {
            CAMERA_SHAKE.start(0.6f, 10);
            SLOW_MOTION.start(0.3f, 8);
            SCREEN_TINT.start(0xFF5A1F, 15);
        }
    }

    public static void tick() {
        CAMERA_SHAKE.tick();
        SLOW_MOTION.tick();
        SCREEN_TINT.tick();
    }

    public static CameraShakeEffect getCameraShake() {
        return CAMERA_SHAKE;
    }

    public static SlowMotionEffect getSlowMotion() {
        return SLOW_MOTION;
    }

    public static ScreenTintRenderer getScreenTint() {
        return SCREEN_TINT;
    }
}
