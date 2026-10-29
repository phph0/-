package com.elimentas.skill.fire;

import com.elimentas.core.ElimenTas;
import com.elimentas.core.ModEntities;
import com.elimentas.skill.Element;
import com.elimentas.skill.Skill;
import com.elimentas.skill.SkillContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class FireballSkill implements Skill {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "fire_fireball");
    public static final FireballSkill INSTANCE = new FireballSkill();

    private static final int COOLDOWN = 60;
    private static final float SPEED = 1.5f;

    @Override
    public ResourceLocation id() { return ID; }

    @Override
    public Element element() { return Element.FIRE; }

    @Override
    public int cooldownTicks() { return COOLDOWN; }

    @Override
    public void execute(SkillContext context) {
        Vec3 eye = context.player().getEyePosition();
        Vec3 dir = context.lookDirection().normalize();

        FireballProjectile fireball = new FireballProjectile(
                ModEntities.FIREBALL_PROJECTILE.get(),
                context.level()
        );
        fireball.setOwner(context.player());
        fireball.setPos(eye.x + dir.x * 0.5, eye.y + dir.y * 0.5, eye.z + dir.z * 0.5);
        fireball.setDeltaMovement(dir.scale(SPEED));
        fireball.setSkillId(ID);

        context.level().addFreshEntity(fireball);
    }
}
