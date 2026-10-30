package com.elimentas.effect.vfx;

import com.elimentas.skill.fire.FireballSkill;
import com.elimentas.skill.ice.IceShardSkill;
import com.elimentas.skill.lightning.LightningBoltSkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class VfxDispatcher {

    private VfxDispatcher() {}

    public static void play(String skillId, double x, double y, double z) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        if (skillId.equals(FireballSkill.ID.toString())) {
            FireVfx.impact(level, x, y, z);
        } else if (skillId.equals(IceShardSkill.ID.toString())) {
            IceVfx.impact(level, x, y, z);
        } else if (skillId.equals(LightningBoltSkill.ID.toString())) {
            LightningVfx.impact(level, x, y, z);
        }
    }
}
