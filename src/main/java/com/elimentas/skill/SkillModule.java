package com.elimentas.skill;

import com.elimentas.core.ElimenTas;
import com.elimentas.skill.fire.FireballSkill;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class SkillModule {

    public static void init(IEventBus modBus) {
        SkillRegistry.register(FireballSkill.INSTANCE);
        NeoForge.EVENT_BUS.register(CooldownManager.class);
        ElimenTas.LOGGER.debug("SkillModule initialized. {} skills registered.", SkillRegistry.all().size());
    }
}
