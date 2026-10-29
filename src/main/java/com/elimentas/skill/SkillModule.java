package com.elimentas.skill;

import com.elimentas.core.ElimenTas;
import net.neoforged.bus.api.IEventBus;

public class SkillModule {

    public static void init(IEventBus modBus) {
        ElimenTas.LOGGER.debug("SkillModule initialized.");
    }
}
