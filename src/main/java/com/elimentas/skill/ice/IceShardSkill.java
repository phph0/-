package com.elimentas.skill.ice;

import com.elimentas.core.ElimenTas;
import com.elimentas.core.ModEntities;
import com.elimentas.skill.Element;
import com.elimentas.skill.Skill;
import com.elimentas.skill.SkillContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class IceShardSkill implements Skill {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "ice_shard");
    public static final IceShardSkill INSTANCE = new IceShardSkill();

    private static final int COOLDOWN = 40;
    private static final float SPEED = 2.0f;

    @Override
    public ResourceLocation id() { return ID; }

    @Override
    public Element element() { return Element.ICE; }

    @Override
    public int cooldownTicks() { return COOLDOWN; }

    @Override
    public void execute(SkillContext context) {
        Vec3 eye = context.player().getEyePosition();
        Vec3 dir = context.lookDirection().normalize();

        IceShardProjectile shard = new IceShardProjectile(
                ModEntities.ICE_SHARD_PROJECTILE.get(),
                context.level()
        );
        shard.setOwner(context.player());
        shard.setPos(eye.x + dir.x * 0.5, eye.y + dir.y * 0.5, eye.z + dir.z * 0.5);
        shard.setDeltaMovement(dir.scale(SPEED));

        context.level().addFreshEntity(shard);
    }
}
