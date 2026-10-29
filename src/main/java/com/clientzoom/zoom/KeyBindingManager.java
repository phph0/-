package com.clientzoom.zoom;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindingManager {
    private static final KeyBinding.Category CATEGORY =
            KeyBinding.Category.create(Identifier.of("clientzoom", "category"));
    private static KeyBinding zoomKey;

    public static void register() {
        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.clientzoom.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                CATEGORY
        ));
    }

    public static boolean isZoomPressed() {
        return zoomKey != null && zoomKey.isPressed();
    }
}
