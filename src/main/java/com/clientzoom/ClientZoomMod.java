package com.clientzoom;

import com.clientzoom.config.ZoomConfig;
import com.clientzoom.zoom.KeyBindingManager;
import com.clientzoom.zoom.ZoomState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientZoomMod implements ClientModInitializer {
    public static final String MOD_ID = "clientzoom";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ZoomConfig.load();
        KeyBindingManager.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ZoomConfig.getInstance().enabled) {
                ZoomState.setActive(false);
                return;
            }
            boolean shouldZoom = KeyBindingManager.isZoomPressed() && client.currentScreen == null;
            ZoomState.setActive(shouldZoom);
        });

        LOGGER.info("Client Zoom loaded");
    }
}
