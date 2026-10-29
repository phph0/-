package com.chattranslation;

import com.chattranslation.chat.ChatTranslationManager;
import com.chattranslation.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatTranslationMod implements ClientModInitializer {
    public static final String MOD_ID = "chattranslation";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
        ChatTranslationManager.init();
        LOGGER.info("CTL mod initialized");
    }
}
