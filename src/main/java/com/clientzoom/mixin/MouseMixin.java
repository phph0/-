package com.clientzoom.mixin;

import com.clientzoom.zoom.ZoomState;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    private double cursorDeltaX;
    @Shadow
    private double cursorDeltaY;

    @Inject(method = "updateMouse(D)V", at = @At("HEAD"))
    private void clientzoom_adjustSensitivity(double timeDelta, CallbackInfo ci) {
        if (ZoomState.isZooming()) {
            double scale = ZoomState.getSensitivityMultiplier();
            this.cursorDeltaX *= scale;
            this.cursorDeltaY *= scale;
        }
    }
}
