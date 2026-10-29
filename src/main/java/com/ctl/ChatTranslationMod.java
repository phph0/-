package com.ctl;

import com.ctl.chat.ChatTranslationManager;
import com.ctl.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatTranslationMod implements ClientModInitializer {
    public static final String MOD_ID = "ctl";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
        ChatTranslationManager.init();
        LOGGER.info("CTL mod initialized");
    }
}
