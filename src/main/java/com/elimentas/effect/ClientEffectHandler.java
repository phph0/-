package com.elimentas.effect;

import com.elimentas.core.ElimenTas;
import com.elimentas.data.ClientSkillData;
import com.elimentas.ui.SkillSlotHudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = ElimenTas.MOD_ID, value = Dist.CLIENT)
public class ClientEffectHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        EffectPlayer.tick();
        ClientSkillData.tickCooldowns();
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        CameraShakeEffect shake = EffectPlayer.getCameraShake();
        if (shake.isActive()) {
            event.setYaw(event.getYaw() + shake.getYawOffset());
            event.setPitch(event.getPitch() + shake.getPitchOffset());
        }
    }

    @EventBusSubscriber(modid = ElimenTas.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class GuiLayerRegistration {

        @SubscribeEvent
        public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(
                    ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "screen_tint"),
                    (GuiGraphics graphics, DeltaTracker deltaTracker) -> {
                        ScreenTintRenderer tint = EffectPlayer.getScreenTint();
                        if (tint.isActive()) {
                            int w = graphics.guiWidth();
                            int h = graphics.guiHeight();
                            tint.render(graphics, w, h);
                        }
                    }
            );

            event.registerAboveAll(
                    ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "skill_slots"),
                    SkillSlotHudRenderer::render
            );
        }
    }
}
