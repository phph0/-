package com.elimentas.tree;

import com.elimentas.core.ElimenTas;
import net.neoforged.bus.api.IEventBus;

public class TreeModule {

    public static void init(IEventBus modBus) {
        ElimenTas.LOGGER.debug("TreeModule initialized.");
    }
}
