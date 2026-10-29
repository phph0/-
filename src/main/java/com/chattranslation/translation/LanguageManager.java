package com.chattranslation.translation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LanguageManager {
    private static final Map<String, LanguageInfo> LANGUAGES = new LinkedHashMap<>();

    static {
        register("af", "Afrikaans", "Afrikaans", "ZA");
        register("sq", "Albanian", "Shqip", "AL");
        register("ar", "Arabic", "العربية", "SA");
        register("hy", "Armenian", "Հայերեն", "AM");
        register("az", "Azerbaijani", "Azərbaycan", "AZ");
        register("eu", "Basque", "Euskara", "ES");
        register("be", "Belarusian", "Беларуская", "BY");
        register("bn", "Bengali", "বাংলা", "BD");
        register("bs", "Bosnian", "Bosanski", "BA");
        register("bg", "Bulgarian", "Български", "BG");
        register("ca", "Catalan", "Català", "ES");
        register("zh-CN", "Chinese (Simplified)", "简体中文", "CN");
        register("zh-TW", "Chinese (Traditional)", "繁體中文", "TW");
        register("hr", "Croatian", "Hrvatski", "HR");
        register("cs", "Czech", "Čeština", "CZ");
        register("da", "Danish", "Dansk", "DK");
        register("nl", "Dutch", "Nederlands", "NL");
        register("en", "English", "English", "US");
        register("et", "Estonian", "Eesti", "EE");
        register("fi", "Finnish", "Suomi", "FI");
        register("fr", "French", "Français", "FR");
        register("gl", "Galician", "Galego", "ES");
        register("ka", "Georgian", "ქართული", "GE");
        register("de", "German", "Deutsch", "DE");
        register("el", "Greek", "Ελληνικά", "GR");
        register("gu", "Gujarati", "ગુજરાતી", "IN");
        register("ht", "Haitian Creole", "Kreyòl Ayisyen", "HT");
        register("he", "Hebrew", "עברית", "IL");
        register("hi", "Hindi", "हिन्दी", "IN");
        register("hu", "Hungarian", "Magyar", "HU");
        register("is", "Icelandic", "Íslenska", "IS");
        register("id", "Indonesian", "Bahasa Indonesia", "ID");
        register("ga", "Irish", "Gaeilge", "IE");
        register("it", "Italian", "Italiano", "IT");
        register("ja", "Japanese", "日本語", "JP");
        register("kn", "Kannada", "ಕನ್ನಡ", "IN");
        register("kk", "Kazakh", "Қазақ", "KZ");
        register("ko", "Korean", "한국어", "KR");
        register("lv", "Latvian", "Latviešu", "LV");
        register("lt", "Lithuanian", "Lietuvių", "LT");
        register("mk", "Macedonian", "Македонски", "MK");
        register("ms", "Malay", "Bahasa Melayu", "MY");
        register("ml", "Malayalam", "മലയാളം", "IN");
        register("mt", "Maltese", "Malti", "MT");
        register("mn", "Mongolian", "Монгол", "MN");
        register("ne", "Nepali", "नेपाली", "NP");
        register("no", "Norwegian", "Norsk", "NO");
        register("fa", "Persian", "فارسی", "IR");
        register("pl", "Polish", "Polski", "PL");
        register("pt", "Portuguese", "Português", "PT");
        register("pa", "Punjabi", "ਪੰਜਾਬੀ", "IN");
        register("ro", "Romanian", "Română", "RO");
        register("ru", "Russian", "Русский", "RU");
        register("sr", "Serbian", "Српски", "RS");
        register("sk", "Slovak", "Slovenčina", "SK");
        register("sl", "Slovenian", "Slovenščina", "SI");
        register("es", "Spanish", "Español", "ES");
        register("sw", "Swahili", "Kiswahili", "KE");
        register("sv", "Swedish", "Svenska", "SE");
        register("ta", "Tamil", "தமிழ்", "IN");
        register("te", "Telugu", "తెలుగు", "IN");
        register("th", "Thai", "ไทย", "TH");
        register("tr", "Turkish", "Türkçe", "TR");
        register("uk", "Ukrainian", "Українська", "UA");
        register("ur", "Urdu", "اردو", "PK");
        register("uz", "Uzbek", "Oʻzbek", "UZ");
        register("vi", "Vietnamese", "Tiếng Việt", "VN");
        register("cy", "Welsh", "Cymraeg", "GB");
    }

    private static void register(String code, String name, String nativeName, String region) {
        LANGUAGES.put(code, new LanguageInfo(code, name, nativeName, region));
    }

    public static LanguageInfo getLanguage(String code) {
        return LANGUAGES.get(code);
    }

    public static List<LanguageInfo> getAllLanguages() {
        return new ArrayList<>(LANGUAGES.values());
    }

    public static List<LanguageInfo> search(String query) {
        String lower = query.toLowerCase();
        return LANGUAGES.values().stream()
                .filter(lang -> lang.name().toLowerCase().contains(lower)
                        || lang.nativeName().toLowerCase().contains(lower)
                        || lang.code().toLowerCase().contains(lower))
                .toList();
    }

    public static String getFlagEmoji(String region) {
        if (region == null || region.length() != 2) return "";
        int first = Character.codePointAt(region.toUpperCase(), 0) - 'A' + 0x1F1E6;
        int second = Character.codePointAt(region.toUpperCase(), 1) - 'A' + 0x1F1E6;
        return new String(Character.toChars(first)) + new String(Character.toChars(second));
    }
}
