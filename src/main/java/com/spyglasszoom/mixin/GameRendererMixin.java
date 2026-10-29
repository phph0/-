package com.spyglasszoom.mixin;

import com.spyglasszoom.ZoomHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void modifyFov(Camera camera, float tickDelta, boolean changingFov,
                           CallbackInfoReturnable<Float> cir) {
        if (ZoomHandler.isZooming()) {
            float fov = cir.getReturnValue();
            cir.setReturnValue((float) (fov / ZoomHandler.getZoomDivisor()));
        }
    }
}
