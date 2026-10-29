package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import net.neoforged.bus.api.IEventBus;

public class NetworkModule {

    public static void init(IEventBus modBus) {
        ElimenTas.LOGGER.debug("NetworkModule initialized.");
    }
}
