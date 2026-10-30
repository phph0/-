package com.elimentas.effect.vfx;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class TrailVfx {

    private static final ParticleOptions EMBER = new DustParticleOptions(new Vector3f(1.0f, 0.35f, 0.05f), 1.6f);
    private static final ParticleOptions FROST = new DustParticleOptions(new Vector3f(0.55f, 0.85f, 1.0f), 1.4f);

    private TrailVfx() {}

    public static void fire(ServerLevel level, Vec3 pos, Vec3 motion, int life) {
        level.sendParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 6, 0.15, 0.15, 0.15, 0.02);
        level.sendParticles(ParticleTypes.SMALL_FLAME, pos.x, pos.y, pos.z, 4, 0.25, 0.25, 0.25, 0.01);
        level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0.0);
        level.sendParticles(EMBER, pos.x, pos.y, pos.z, 4, 0.2, 0.2, 0.2, 0.0);
        if (life % 2 == 0) {
            level.sendParticles(ParticleTypes.LAVA, pos.x, pos.y, pos.z, 1, 0.2, 0.2, 0.2, 0.0);
        }
        spiral(level, ParticleTypes.FLAME, pos, motion, life, 0.55);
        spiral(level, EMBER, pos, motion, life + 20, 0.55);
    }

    public static void ice(ServerLevel level, Vec3 pos, Vec3 motion, int life) {
        level.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 6, 0.15, 0.15, 0.15, 0.03);
        level.sendParticles(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 2, 0.1, 0.1, 0.1, 0.01);
        level.sendParticles(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 1, 0.1, 0.1, 0.1, 0.0);
        level.sendParticles(FROST, pos.x, pos.y, pos.z, 4, 0.2, 0.2, 0.2, 0.0);
        spiral(level, ParticleTypes.END_ROD, pos, motion, life, 0.5);
        spiral(level, ParticleTypes.SNOWFLAKE, pos, motion, life + 20, 0.5);
    }

    private static void spiral(ServerLevel level, ParticleOptions p, Vec3 pos, Vec3 motion, int phase, double radius) {
        Vec3 forward = motion.lengthSqr() < 1e-6 ? new Vec3(0, 0, 1) : motion.normalize();
        Vec3 side = forward.cross(new Vec3(0, 1, 0));
        if (side.lengthSqr() < 1e-6) side = new Vec3(1, 0, 0);
        side = side.normalize();
        Vec3 up = side.cross(forward).normalize();

        double a = phase * 0.9;
        Vec3 offset = side.scale(Math.cos(a) * radius).add(up.scale(Math.sin(a) * radius));
        level.sendParticles(p, pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, 1, 0, 0, 0, 0);
    }
}
