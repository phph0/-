package com.elimentas.tree;

import com.elimentas.core.ElimenTas;
import net.neoforged.bus.api.IEventBus;

public class TreeModule {

    public static void init(IEventBus modBus) {
        SkillTreeDefinition.init();
        ElimenTas.LOGGER.debug("TreeModule initialized.");
    }
}
