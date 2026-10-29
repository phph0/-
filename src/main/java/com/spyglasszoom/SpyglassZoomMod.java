package com.spyglasszoom;

import com.spyglasszoom.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SpyglassZoomMod implements ClientModInitializer {

    public static final String MOD_ID = "spyglasszoom";

    public static KeyBinding zoomKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.spyglasszoom.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ZoomHandler.tick();
        });
    }
}
