package com.elimentas.effect.vfx;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class FireVfx {

    private static final ParticleOptions EMBER = new DustParticleOptions(new Vector3f(1.0f, 0.3f, 0.05f), 2.2f);
    private static final ParticleOptions GOLD = new DustParticleOptions(new Vector3f(1.0f, 0.8f, 0.2f), 1.6f);

    private FireVfx() {}

    public static void impact(ClientLevel level, double x, double y, double z) {
        Vec3 c = new Vec3(x, y + 0.2, z);

        level.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.4f, 0.75f, false);
        level.playLocalSound(x, y, z, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.5f, 0.6f, false);

        level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 0, 0, 0);
        level.addParticle(ParticleTypes.FLASH, x, y + 0.5, z, 0, 0, 0);

        ParticleShapes.burst(level, ParticleTypes.FLAME, c, 140, 0.55);
        ParticleShapes.burst(level, EMBER, c, 90, 0.5);
        ParticleShapes.burst(level, GOLD, c, 50, 0.7);
        ParticleShapes.burst(level, ParticleTypes.LAVA, c, 40, 0.35);
        ParticleShapes.burst(level, ParticleTypes.LARGE_SMOKE, c, 45, 0.25);

        for (int t = 0; t < 8; t++) {
            final int tick = t;
            VfxScheduler.schedule(tick, () -> {
                double r = (tick + 1) * 0.75;
                ParticleShapes.ring(level, ParticleTypes.FLAME, c, r, 30 + tick * 8, 0.06, 0.03);
                ParticleShapes.ring(level, EMBER, c, r * 0.85, 24 + tick * 6, 0.05, 0.04);
                ParticleShapes.ring(level, ParticleTypes.SMOKE, c, r * 0.6, 16, 0.03, 0.05);
            });
        }

        for (int t = 0; t < 16; t++) {
            final int tick = t;
            VfxScheduler.schedule(tick, () -> {
                Vec3 p = c.add(0, tick * 0.55, 0);
                double radius = 0.5 + Math.sin(tick * 0.35) * 0.5;
                ParticleShapes.ring(level, ParticleTypes.FLAME, p, radius, 12, 0.01, 0.12);
                ParticleShapes.ring(level, GOLD, p, radius * 0.5, 6, 0.0, 0.1);
                level.addParticle(ParticleTypes.LARGE_SMOKE, p.x, p.y + 0.4, p.z, 0, 0.08, 0);
            });
        }

        for (int t = 4; t < 24; t += 2) {
            VfxScheduler.schedule(t, () -> {
                ParticleShapes.burst(level, ParticleTypes.SMALL_FLAME, c.add(0, 2.5, 0), 14, 0.35);
                ParticleShapes.burst(level, ParticleTypes.LAVA, c.add(0, 1.5, 0), 3, 0.3);
            });
        }
    }
}
