package com.elimentas.input;

import com.elimentas.core.ElimenTas;
import net.neoforged.bus.api.IEventBus;

public class InputModule {

    public static void init(IEventBus modBus) {
        ElimenTas.LOGGER.debug("InputModule initialized.");
    }
}
