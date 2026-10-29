package com.elimentas.input;

import com.elimentas.core.ElimenTas;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = ElimenTas.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (KeyBindings.OPEN_SKILL_TREE.consumeClick()) {
            ElimenTas.LOGGER.info("[Input] Skill tree key pressed (K)");
        }
        while (KeyBindings.SKILL_SLOT_1.consumeClick()) {
            ElimenTas.LOGGER.info("[Input] Skill slot 1 pressed (Z)");
        }
        while (KeyBindings.SKILL_SLOT_2.consumeClick()) {
            ElimenTas.LOGGER.info("[Input] Skill slot 2 pressed (X)");
        }
        while (KeyBindings.SKILL_SLOT_3.consumeClick()) {
            ElimenTas.LOGGER.info("[Input] Skill slot 3 pressed (C)");
        }
        while (KeyBindings.SKILL_SLOT_4.consumeClick()) {
            ElimenTas.LOGGER.info("[Input] Skill slot 4 pressed (V)");
        }
    }
}
