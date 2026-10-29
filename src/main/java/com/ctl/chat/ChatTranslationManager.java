package com.ctl.chat;

import com.ctl.ChatTranslationMod;
import com.ctl.config.ConfigManager;
import com.ctl.translation.LanguageManager;
import com.ctl.translation.TranslationResult;
import com.ctl.translation.TranslationService;
import net.minecraft.client.MinecraftClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatTranslationManager {
    private static final Map<Integer, TranslationEntry> translations = new ConcurrentHashMap<>();
    private static final Map<Integer, Boolean> expandedMessages = new ConcurrentHashMap<>();

    public static void init() {
        // nothing extra needed at startup
    }

    public static void toggleTranslation(int messageIndex, String messageText) {
        if (!ConfigManager.getConfig().isEnabled()) {
            ChatTranslationMod.LOGGER.info("[CTL] translation disabled in config");
            return;
        }

        if (expandedMessages.containsKey(messageIndex) && expandedMessages.get(messageIndex)) {
            ChatTranslationMod.LOGGER.info("[CTL] collapsing index {}", messageIndex);
            expandedMessages.put(messageIndex, false);
            return;
        }

        expandedMessages.put(messageIndex, true);

        if (translations.containsKey(messageIndex)) {
            ChatTranslationMod.LOGGER.info("[CTL] already have translation cached for index {}", messageIndex);
            return;
        }

        ChatMessageData data = ChatMessageData.parse(messageText);
        ChatTranslationMod.LOGGER.info("[CTL] parsed sender=\"{}\" content=\"{}\"", data.sender(), data.content());

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && data.isSamePlayer(client.player.getName().getString())) {
            ChatTranslationMod.LOGGER.info("[CTL] skipping own message");
            expandedMessages.put(messageIndex, false);
            return;
        }

        String targetLang = ConfigManager.getConfig().getTargetLanguage();
        ChatTranslationMod.LOGGER.info("[CTL] requesting translation to \"{}\"", targetLang);
        translations.put(messageIndex, new TranslationEntry(null, true));

        TranslationService.translate(data.content(), targetLang).thenAccept(result -> {
            if (result != null) {
                ChatTranslationMod.LOGGER.info("[CTL] translation success: \"{}\"", result.translatedText());
                translations.put(messageIndex, new TranslationEntry(result, false));
            } else {
                ChatTranslationMod.LOGGER.warn("[CTL] translation returned null result for index {}", messageIndex);
                translations.remove(messageIndex);
                expandedMessages.put(messageIndex, false);
            }
        });
    }

    public static boolean isExpanded(int messageIndex) {
        return expandedMessages.getOrDefault(messageIndex, false);
    }

    public static TranslationEntry getTranslation(int messageIndex) {
        return translations.get(messageIndex);
    }

    public static void clearAll() {
        translations.clear();
        expandedMessages.clear();
    }

    public static String formatTranslationLine(TranslationResult result, String sender) {
        String sourceLang = result.sourceLanguage();
        String targetLang = result.targetLanguage();

        var sourceInfo = LanguageManager.getLanguage(sourceLang);
        var targetInfo = LanguageManager.getLanguage(targetLang);

        String sourceFlag = sourceInfo != null
                ? LanguageManager.getFlagEmoji(sourceInfo.region())
                : LanguageManager.getFlagEmoji(sourceLang.toUpperCase());
        String targetFlag = targetInfo != null
                ? LanguageManager.getFlagEmoji(targetInfo.region())
                : LanguageManager.getFlagEmoji(targetLang.toUpperCase());

        String senderPart = (sender != null && !sender.isEmpty()) ? "<" + sender + "> " : "";
        return "└[" + sourceFlag + "→" + targetFlag + "] " + senderPart
                + result.translatedText();
    }

    public record TranslationEntry(TranslationResult result, boolean loading) {}
}
