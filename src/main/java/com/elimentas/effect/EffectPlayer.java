package com.elimentas.effect;

import com.elimentas.effect.vfx.VfxDispatcher;
import com.elimentas.effect.vfx.VfxScheduler;
import com.elimentas.skill.fire.FireballSkill;
import com.elimentas.skill.ice.IceShardSkill;
import com.elimentas.skill.lightning.LightningBoltSkill;

public class EffectPlayer {

    private static final CameraShakeEffect CAMERA_SHAKE = new CameraShakeEffect();
    private static final SlowMotionEffect SLOW_MOTION = new SlowMotionEffect();
    private static final ScreenTintRenderer SCREEN_TINT = new ScreenTintRenderer();
    private static final FovPunchEffect FOV_PUNCH = new FovPunchEffect();

    public static void play(String skillId, double x, double y, double z) {
        if (skillId.equals(FireballSkill.ID.toString())) {
            CAMERA_SHAKE.start(1.4f, 16);
            SLOW_MOTION.start(0.3f, 8);
            SCREEN_TINT.start(0xFF5A1F, 22, 0.7f);
            FOV_PUNCH.start(12f, 12);
        } else if (skillId.equals(IceShardSkill.ID.toString())) {
            CAMERA_SHAKE.start(0.8f, 12);
            SLOW_MOTION.start(0.5f, 6);
            SCREEN_TINT.start(0x4FC3F7, 20, 0.65f);
            FOV_PUNCH.start(-8f, 14);
        } else if (skillId.equals(LightningBoltSkill.ID.toString())) {
            CAMERA_SHAKE.start(1.8f, 14);
            SLOW_MOTION.start(0.2f, 10);
            SCREEN_TINT.start(0xE1BEE7, 12, 0.95f);
            FOV_PUNCH.start(16f, 10);
        }
        VfxDispatcher.play(skillId, x, y, z);
    }

    public static void tick() {
        CAMERA_SHAKE.tick();
        SLOW_MOTION.tick();
        SCREEN_TINT.tick();
        FOV_PUNCH.tick();
        VfxScheduler.tick();
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

    public static FovPunchEffect getFovPunch() {
        return FOV_PUNCH;
    }
}
