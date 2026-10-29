package com.ctl.translation;

import com.ctl.ChatTranslationMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class TranslationService {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static CompletableFuture<TranslationResult> translate(String text, String targetLang) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
                String url = "https://translate.googleapis.com/translate_a/single"
                        + "?client=gtx&sl=auto&tl=" + targetLang
                        + "&dt=t&q=" + encoded;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("User-Agent", "Mozilla/5.0")
                        .timeout(Duration.ofSeconds(10))
                        .GET()
                        .build();

                HttpResponse<String> response = HTTP_CLIENT.send(request,
                        HttpResponse.BodyHandlers.ofString());

                ChatTranslationMod.LOGGER.info("[CTL] HTTP request url={}", url);

                if (response.statusCode() != 200) {
                    ChatTranslationMod.LOGGER.warn("[CTL] Translation failed: HTTP {} body={}",
                            response.statusCode(), response.body());
                    return null;
                }

                return parseResponse(response.body(), text, targetLang);
            } catch (Exception e) {
                ChatTranslationMod.LOGGER.warn("[CTL] Translation error", e);
                return null;
            }
        });
    }

    private static TranslationResult parseResponse(String body, String originalText, String targetLang) {
        try {
            JsonArray root = JsonParser.parseString(body).getAsJsonArray();
            JsonArray translations = root.get(0).getAsJsonArray();

            StringBuilder translated = new StringBuilder();
            for (var element : translations) {
                JsonArray sentence = element.getAsJsonArray();
                if (sentence.get(0) != null && !sentence.get(0).isJsonNull()) {
                    translated.append(sentence.get(0).getAsString());
                }
            }

            String detectedLang = "auto";
            if (root.size() > 2 && !root.get(2).isJsonNull()) {
                detectedLang = root.get(2).getAsString();
            }

            return new TranslationResult(detectedLang, targetLang, originalText, translated.toString());
        } catch (Exception e) {
            ChatTranslationMod.LOGGER.warn("Failed to parse translation response", e);
            return null;
        }
    }
}
