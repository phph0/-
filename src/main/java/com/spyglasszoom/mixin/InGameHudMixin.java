package com.spyglasszoom.mixin;

import com.spyglasszoom.ZoomHandler;
import com.spyglasszoom.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Invoker("renderSpyglassOverlay")
    abstract void invokeRenderSpyglassOverlay(DrawContext context, float scale);

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (ZoomHandler.isZooming()
                && ConfigManager.getConfig().showOverlay
                && this.client.options.getPerspective().isFirstPerson()) {
            invokeRenderSpyglassOverlay(context, 1.0f);
        }
    }
}
