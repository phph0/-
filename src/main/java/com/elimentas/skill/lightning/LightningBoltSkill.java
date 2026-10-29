package com.elimentas.skill.lightning;

import com.elimentas.core.ElimenTas;
import com.elimentas.network.NetworkModule;
import com.elimentas.network.SkillEffectPayload;
import com.elimentas.skill.Element;
import com.elimentas.skill.Skill;
import com.elimentas.skill.SkillContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LightningBoltSkill implements Skill {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "lightning_bolt");
    public static final LightningBoltSkill INSTANCE = new LightningBoltSkill();

    private static final int COOLDOWN = 50;
    private static final float DAMAGE = 8.0f;
    private static final double RANGE = 20.0;
    private static final double AOE_RADIUS = 2.0;

    @Override
    public ResourceLocation id() { return ID; }

    @Override
    public Element element() { return Element.LIGHTNING; }

    @Override
    public int cooldownTicks() { return COOLDOWN; }

    @Override
    public void execute(SkillContext context) {
        ServerLevel level = context.level();
        Vec3 eye = context.player().getEyePosition();
        Vec3 dir = context.lookDirection().normalize();

        Vec3 target = findTarget(context, eye, dir);

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(target.x, target.y, target.z);
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }

        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.x, target.y, target.z, 30, 0.5, 1.0, 0.5, 0.2);
        level.sendParticles(ParticleTypes.FLASH, target.x, target.y + 1, target.z, 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, target.x, target.y, target.z, 10, 0.3, 0.3, 0.3, 0.05);

        AABB area = new AABB(target.x - AOE_RADIUS, target.y - 1, target.z - AOE_RADIUS,
                target.x + AOE_RADIUS, target.y + 3, target.z + AOE_RADIUS);
        List<Entity> entities = level.getEntities(context.player(), area);
        for (Entity entity : entities) {
            double dist = entity.position().distanceTo(target);
            if (dist <= AOE_RADIUS) {
                float dmg = DAMAGE * (float) (1.0 - dist / AOE_RADIUS * 0.5);
                entity.hurt(context.player().damageSources().lightningBolt(), dmg);
                entity.push(0, 0.4, 0);
            }
        }

        NetworkModule.sendEffectToNearby(
                level, target, 64.0,
                new SkillEffectPayload(ID.toString(), target.x, target.y, target.z)
        );
    }

    private Vec3 findTarget(SkillContext context, Vec3 eye, Vec3 dir) {
        Vec3 end = eye.add(dir.scale(RANGE));

        for (double d = 0.5; d <= RANGE; d += 0.5) {
            Vec3 point = eye.add(dir.scale(d));
            if (!context.level().getBlockState(
                    new net.minecraft.core.BlockPos((int) Math.floor(point.x),
                            (int) Math.floor(point.y), (int) Math.floor(point.z))).isAir()) {
                return point;
            }
        }

        AABB searchArea = new AABB(eye, end).inflate(1.5);
        Entity closest = null;
        double closestDist = Double.MAX_VALUE;
        for (Entity entity : context.level().getEntities(context.player(), searchArea)) {
            Vec3 toEntity = entity.position().add(0, entity.getBbHeight() / 2, 0).subtract(eye);
            double proj = toEntity.dot(dir);
            if (proj < 0 || proj > RANGE) continue;
            Vec3 closestPoint = eye.add(dir.scale(proj));
            double perpDist = closestPoint.distanceTo(entity.position().add(0, entity.getBbHeight() / 2, 0));
            if (perpDist < 1.5 && perpDist < closestDist) {
                closestDist = perpDist;
                closest = entity;
            }
        }

        if (closest != null) {
            return closest.position();
        }

        return end;
    }
}
