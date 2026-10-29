package com.chattranslation.translation;

public record TranslationResult(
        String sourceLanguage,
        String targetLanguage,
        String originalText,
        String translatedText
) {}
