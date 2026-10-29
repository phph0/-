package com.clientzoom.mixin;

import com.clientzoom.zoom.ZoomState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void clientzoom_modifyFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        ZoomState.update();
        if (ZoomState.isZooming()) {
            float baseFov = cir.getReturnValue();
            float zoomedFov = (float) (baseFov / ZoomState.getCurrentZoomFactor());
            cir.setReturnValue(zoomedFov);
        }
    }
}
