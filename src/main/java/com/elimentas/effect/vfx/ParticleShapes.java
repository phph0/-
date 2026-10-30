package com.elimentas.effect.vfx;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class ParticleShapes {

    private ParticleShapes() {}

    public static void burst(Level level, ParticleOptions p, Vec3 c, int count, double speed) {
        RandomSource r = level.random;
        for (int i = 0; i < count; i++) {
            double dx = r.nextGaussian(), dy = r.nextGaussian(), dz = r.nextGaussian();
            double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (len < 1e-4) continue;
            double s = speed * (0.4 + r.nextDouble() * 0.6) / len;
            level.addParticle(p, c.x, c.y, c.z, dx * s, dy * s, dz * s);
        }
    }

    public static void ring(Level level, ParticleOptions p, Vec3 c, double radius, int count, double outward, double up) {
        double offset = level.random.nextDouble() * Math.PI * 2;
        for (int i = 0; i < count; i++) {
            double a = offset + Math.PI * 2 * i / count;
            double cx = Math.cos(a), cz = Math.sin(a);
            level.addParticle(p, c.x + cx * radius, c.y, c.z + cz * radius, cx * outward, up, cz * outward);
        }
    }

    public static void line(Level level, ParticleOptions p, Vec3 a, Vec3 b, double step) {
        Vec3 d = b.subtract(a);
        int n = Math.max(1, (int) (d.length() / step));
        for (int i = 0; i <= n; i++) {
            Vec3 pos = a.add(d.scale((double) i / n));
            level.addParticle(p, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    public static void helix(Level level, ParticleOptions p, Vec3 base, double radius, double height,
                             double turns, int points, double phase) {
        for (int i = 0; i < points; i++) {
            double t = (double) i / (points - 1);
            double a = phase + t * turns * Math.PI * 2;
            level.addParticle(p, base.x + Math.cos(a) * radius, base.y + t * height,
                    base.z + Math.sin(a) * radius, 0, 0.02, 0);
        }
    }
}
