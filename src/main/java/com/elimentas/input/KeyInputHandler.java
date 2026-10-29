package com.elimentas.input;

import com.elimentas.core.ElimenTas;
import com.elimentas.network.SkillActivatePayload;
import com.elimentas.tree.client.SkillTreeScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ElimenTas.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    private static final KeyMapping[] SLOT_KEYS = {
            KeyBindings.SKILL_SLOT_1,
            KeyBindings.SKILL_SLOT_2,
            KeyBindings.SKILL_SLOT_3,
            KeyBindings.SKILL_SLOT_4
    };

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        while (KeyBindings.OPEN_SKILL_TREE.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new SkillTreeScreen());
            }
        }

        if (mc.screen != null) return;

        for (int i = 0; i < SLOT_KEYS.length; i++) {
            while (SLOT_KEYS[i].consumeClick()) {
                PacketDistributor.sendToServer(new SkillActivatePayload(i));
            }
        }
    }
}
