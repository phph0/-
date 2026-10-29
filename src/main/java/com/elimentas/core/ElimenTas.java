package com.elimentas.core;

import com.elimentas.data.DataModule;
import com.elimentas.input.InputModule;
import com.elimentas.network.NetworkModule;
import com.elimentas.skill.SkillModule;
import com.elimentas.tree.TreeModule;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ElimenTas.MOD_ID)
public class ElimenTas {

    public static final String MOD_ID = "elimentas";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ElimenTas(IEventBus modBus) {
        LOGGER.info("ElimenTas initializing...");

        ModEntities.init(modBus);
        DataModule.init(modBus);
        TreeModule.init(modBus);
        SkillModule.init(modBus);
        NetworkModule.init(modBus);
        InputModule.init(modBus);

        LOGGER.info("ElimenTas initialized.");
    }
}
