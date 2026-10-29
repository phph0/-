package com.chattranslation.translation;

public record LanguageInfo(
        String code,
        String name,
        String nativeName,
        String region
) {
    public String displayName() {
        return nativeName + " (" + name + ")";
    }
}
