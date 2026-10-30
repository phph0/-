package com.elimentas.skill.fire;

import com.elimentas.effect.vfx.TrailVfx;
import com.elimentas.network.NetworkModule;
import com.elimentas.network.SkillEffectPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireballProjectile extends ThrowableProjectile {

    private static final float DAMAGE = 6.0f;
    private static final float EXPLOSION_RADIUS = 2.5f;
    private static final int MAX_LIFE = 60;

    private ResourceLocation skillId = FireballSkill.ID;
    private int life = 0;

    public FireballProjectile(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public void setSkillId(ResourceLocation id) {
        this.skillId = id;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        life++;

        if (level() instanceof ServerLevel serverLevel) {
            TrailVfx.fire(serverLevel, position(), getDeltaMovement(), life);
        }

        if (life >= MAX_LIFE) {
            if (!level().isClientSide) {
                explode();
            }
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            Entity target = result.getEntity();
            target.hurt(damageSources().thrown(this, getOwner()), DAMAGE);
            explode();
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            explode();
            discard();
        }
    }

    private void explode() {
        if (!(level() instanceof ServerLevel serverLevel)) return;

        Vec3 pos = position();

        serverLevel.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 30, 0.5, 0.5, 0.5, 0.1);
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 15, 0.3, 0.3, 0.3, 0.05);
        serverLevel.sendParticles(ParticleTypes.LAVA, pos.x, pos.y, pos.z, 10, 0.4, 0.4, 0.4, 0.0);

        for (Entity entity : level().getEntities(this, getBoundingBox().inflate(EXPLOSION_RADIUS))) {
            if (entity == getOwner()) continue;
            double dist = entity.distanceTo(this);
            if (dist <= EXPLOSION_RADIUS) {
                float dmg = DAMAGE * (float) (1.0 - dist / EXPLOSION_RADIUS);
                entity.hurt(damageSources().thrown(this, getOwner()), dmg);
                Vec3 knockback = entity.position().subtract(pos).normalize().scale(0.8);
                entity.push(knockback.x, 0.3, knockback.z);
            }
        }

        NetworkModule.sendEffectToNearby(
                serverLevel, pos, 64.0,
                new SkillEffectPayload(skillId.toString(), pos.x, pos.y, pos.z)
        );
    }

    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }
}
