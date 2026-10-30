package com.elimentas.effect.vfx;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class IceVfx {

    private static final ParticleOptions FROST = new DustParticleOptions(new Vector3f(0.55f, 0.85f, 1.0f), 1.8f);
    private static final ParticleOptions SHARD =
            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PACKED_ICE.defaultBlockState());

    private IceVfx() {}

    public static void impact(ClientLevel level, double x, double y, double z) {
        Vec3 c = new Vec3(x, y + 0.2, z);
        RandomSource r = level.random;

        level.playLocalSound(x, y, z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.6f, 0.7f, false);
        level.playLocalSound(x, y, z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.8f, 1.2f, false);
        level.playLocalSound(x, y, z, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1.5f, 0.8f, false);

        level.addParticle(ParticleTypes.FLASH, x, y + 0.5, z, 0, 0, 0);

        ParticleShapes.burst(level, ParticleTypes.SNOWFLAKE, c, 120, 0.5);
        ParticleShapes.burst(level, SHARD, c, 90, 0.6);
        ParticleShapes.burst(level, ParticleTypes.END_ROD, c, 60, 0.45);
        ParticleShapes.burst(level, FROST, c, 70, 0.5);
        ParticleShapes.burst(level, ParticleTypes.CLOUD, c, 25, 0.2);

        for (int i = 0; i < 9; i++) {
            double a = Math.PI * 2 * i / 9 + r.nextDouble() * 0.3;
            double len = 2.5 + r.nextDouble() * 2.0;
            Vec3 tip = c.add(Math.cos(a) * len * 0.6, len, Math.sin(a) * len * 0.6);
            ParticleShapes.line(level, ParticleTypes.END_ROD, c, tip, 0.25);
            ParticleShapes.line(level, FROST, c, tip, 0.35);
            ParticleShapes.burst(level, SHARD, tip, 6, 0.15);
        }

        for (int t = 0; t < 9; t++) {
            final int tick = t;
            VfxScheduler.schedule(tick, () -> {
                double radius = (tick + 1) * 0.65;
                ParticleShapes.ring(level, ParticleTypes.SNOWFLAKE, c, radius, 30 + tick * 8, 0.08, 0.02);
                ParticleShapes.ring(level, ParticleTypes.CLOUD, c, radius * 0.9, 14, 0.04, 0.01);
                ParticleShapes.ring(level, FROST, c, radius, 22, 0.02, 0.03);
            });
        }

        for (int t = 0; t < 18; t++) {
            final int tick = t;
            VfxScheduler.schedule(tick, () -> {
                double phase = tick * 0.5;
                ParticleShapes.helix(level, ParticleTypes.END_ROD, c, 1.2, 5.0, 2.0, 24, phase);
                ParticleShapes.helix(level, ParticleTypes.SNOWFLAKE, c, 1.2, 5.0, 2.0, 24, phase + Math.PI);
            });
        }
    }
}
