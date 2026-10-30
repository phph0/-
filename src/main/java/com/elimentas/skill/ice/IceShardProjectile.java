package com.elimentas.skill.ice;

import com.elimentas.effect.vfx.TrailVfx;
import com.elimentas.network.NetworkModule;
import com.elimentas.network.SkillEffectPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class IceShardProjectile extends ThrowableProjectile {

    private static final float DAMAGE = 5.0f;
    private static final float SLOW_RADIUS = 3.0f;
    private static final int FREEZE_TICKS = 60;
    private static final int MAX_LIFE = 50;

    private int life = 0;

    public IceShardProjectile(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        life++;

        if (level() instanceof ServerLevel serverLevel) {
            TrailVfx.ice(serverLevel, position(), getDeltaMovement(), life);
        }

        if (life >= MAX_LIFE) {
            if (!level().isClientSide) {
                impact();
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
            target.setTicksFrozen(target.getTicksFrozen() + FREEZE_TICKS);
            impact();
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            impact();
            discard();
        }
    }

    private void impact() {
        if (!(level() instanceof ServerLevel serverLevel)) return;

        Vec3 pos = position();

        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 40, 0.8, 0.8, 0.8, 0.1);
        serverLevel.sendParticles(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 10, 0.5, 0.5, 0.5, 0.05);
        serverLevel.sendParticles(ParticleTypes.WHITE_ASH, pos.x, pos.y, pos.z, 20, 0.6, 0.6, 0.6, 0.02);

        for (Entity entity : level().getEntities(this, getBoundingBox().inflate(SLOW_RADIUS))) {
            if (entity == getOwner()) continue;
            double dist = entity.distanceTo(this);
            if (dist <= SLOW_RADIUS) {
                int freezeAmount = (int) (FREEZE_TICKS * (1.0 - dist / SLOW_RADIUS));
                entity.setTicksFrozen(entity.getTicksFrozen() + freezeAmount);
            }
        }

        NetworkModule.sendEffectToNearby(
                serverLevel, pos, 64.0,
                new SkillEffectPayload(IceShardSkill.ID.toString(), pos.x, pos.y, pos.z)
        );
    }

    @Override
    protected double getDefaultGravity() {
        return 0.005;
    }
}
