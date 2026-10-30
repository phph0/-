package com.elimentas.effect.vfx;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class LightningVfx {

    private static final ParticleOptions VIOLET = new DustParticleOptions(new Vector3f(0.75f, 0.4f, 1.0f), 1.6f);
    private static final double SKY_HEIGHT = 30.0;

    private LightningVfx() {}

    public static void impact(ClientLevel level, double x, double y, double z) {
        Vec3 c = new Vec3(x, y + 0.2, z);

        level.playLocalSound(x, y, z, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 2.0f, 1.1f, false);
        level.playLocalSound(x, y, z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1.5f, 1.3f, false);

        level.addParticle(ParticleTypes.FLASH, x, y + 1.0, z, 0, 0, 0);
        level.addParticle(ParticleTypes.SONIC_BOOM, x, y + 1.0, z, 0, 0, 0);

        ParticleShapes.burst(level, ParticleTypes.ELECTRIC_SPARK, c, 110, 0.9);
        ParticleShapes.burst(level, VIOLET, c, 60, 0.6);
        ParticleShapes.burst(level, ParticleTypes.END_ROD, c, 40, 0.5);
        ParticleShapes.burst(level, ParticleTypes.SOUL_FIRE_FLAME, c, 35, 0.35);

        for (int t = 0; t < 8; t += 2) {
            VfxScheduler.schedule(t, () -> {
                skyBolt(level, c);
                groundArcs(level, c);
            });
        }

        for (int t = 0; t < 8; t++) {
            final int tick = t;
            VfxScheduler.schedule(tick, () -> {
                double radius = (tick + 1) * 0.7;
                ParticleShapes.ring(level, ParticleTypes.ELECTRIC_SPARK, c, radius, 32 + tick * 6, 0.1, 0.02);
                ParticleShapes.ring(level, VIOLET, c, radius * 0.8, 20, 0.04, 0.02);
            });
        }
    }

    private static void skyBolt(ClientLevel level, Vec3 target) {
        RandomSource r = level.random;
        Vec3 prev = target.add((r.nextDouble() - 0.5) * 4, SKY_HEIGHT, (r.nextDouble() - 0.5) * 4);
        int segments = 14;
        for (int i = 1; i <= segments; i++) {
            double t = (double) i / segments;
            Vec3 next;
            if (i == segments) {
                next = target;
            } else {
                double jitter = 1.6 * (1 - t * 0.6);
                next = prev.lerp(target, 1.0 / (segments - i + 1))
                        .add((r.nextDouble() - 0.5) * jitter, 0, (r.nextDouble() - 0.5) * jitter);
            }
            ParticleShapes.line(level, ParticleTypes.ELECTRIC_SPARK, prev, next, 0.3);
            ParticleShapes.line(level, ParticleTypes.END_ROD, prev, next, 0.8);
            ParticleShapes.line(level, VIOLET, prev, next, 0.6);
            if (i < segments - 1 && r.nextFloat() < 0.3f) {
                branch(level, next);
            }
            prev = next;
        }
    }

    private static void branch(ClientLevel level, Vec3 start) {
        RandomSource r = level.random;
        Vec3 dir = new Vec3(r.nextDouble() - 0.5, -0.4 - r.nextDouble() * 0.4, r.nextDouble() - 0.5).normalize();
        Vec3 prev = start;
        for (int i = 0; i < 4; i++) {
            Vec3 next = prev.add(dir.scale(1.2))
                    .add((r.nextDouble() - 0.5) * 0.8, (r.nextDouble() - 0.5) * 0.4, (r.nextDouble() - 0.5) * 0.8);
            ParticleShapes.line(level, ParticleTypes.ELECTRIC_SPARK, prev, next, 0.3);
            prev = next;
        }
    }

    private static void groundArcs(ClientLevel level, Vec3 c) {
        RandomSource r = level.random;
        for (int b = 0; b < 10; b++) {
            double a = Math.PI * 2 * b / 10 + r.nextDouble() * 0.4;
            Vec3 dir = new Vec3(Math.cos(a), 0, Math.sin(a));
            Vec3 prev = c;
            for (int s = 0; s < 6; s++) {
                Vec3 next = prev.add(dir.scale(0.8))
                        .add((r.nextDouble() - 0.5) * 0.7, (r.nextDouble() - 0.3) * 0.5, (r.nextDouble() - 0.5) * 0.7);
                ParticleShapes.line(level, ParticleTypes.ELECTRIC_SPARK, prev, next, 0.25);
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, next.x, next.y, next.z, 0, 0.03, 0);
                prev = next;
            }
        }
    }
}
