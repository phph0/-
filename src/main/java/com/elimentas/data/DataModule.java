package com.elimentas.data;

import com.elimentas.core.ElimenTas;
import net.neoforged.bus.api.IEventBus;

public class DataModule {

    public static void init(IEventBus modBus) {
        ModAttachments.ATTACHMENTS.register(modBus);
        ElimenTas.LOGGER.debug("DataModule initialized.");
    }
}
