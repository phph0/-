package com.ctl.chat;

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
        if (!ConfigManager.getConfig().isEnabled()) return;

        if (expandedMessages.containsKey(messageIndex) && expandedMessages.get(messageIndex)) {
            expandedMessages.put(messageIndex, false);
            return;
        }

        expandedMessages.put(messageIndex, true);

        if (translations.containsKey(messageIndex)) {
            return;
        }

        ChatMessageData data = ChatMessageData.parse(messageText);
        if (data == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && data.isSamePlayer(client.player.getName().getString())) {
            expandedMessages.put(messageIndex, false);
            return;
        }

        String targetLang = ConfigManager.getConfig().getTargetLanguage();
        translations.put(messageIndex, new TranslationEntry(null, true));

        TranslationService.translate(data.content(), targetLang).thenAccept(result -> {
            if (result != null) {
                translations.put(messageIndex, new TranslationEntry(result, false));
            } else {
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

        return "└[" + sourceFlag + "→" + targetFlag + "] <" + sender + "> "
                + result.translatedText();
    }

    public record TranslationEntry(TranslationResult result, boolean loading) {}
}
