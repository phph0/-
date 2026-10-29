package com.ctl.chat;

import net.minecraft.text.OrderedText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ChatMessageData(String sender, String content, String fullMessage) {
    // vanilla: <Name> message
    private static final Pattern ANGLE_BRACKET_PATTERN = Pattern.compile("^<([^>]+)>\\s*(.+)$");
    // common server format: Name: message / Name» message / Name> message / Name - message
    private static final Pattern NAME_SEPARATOR_PATTERN =
            Pattern.compile("^([A-Za-z0-9_]{1,16})\\s*[:»>-]\\s*(.+)$");

    public static ChatMessageData parse(String message) {
        Matcher angleMatcher = ANGLE_BRACKET_PATTERN.matcher(message);
        if (angleMatcher.matches()) {
            return new ChatMessageData(angleMatcher.group(1), angleMatcher.group(2), message);
        }

        Matcher nameMatcher = NAME_SEPARATOR_PATTERN.matcher(message);
        if (nameMatcher.matches()) {
            return new ChatMessageData(nameMatcher.group(1), nameMatcher.group(2), message);
        }

        // fallback: translate the raw line, sender unknown
        return new ChatMessageData(null, message, message);
    }

    public boolean isSamePlayer(String playerName) {
        return sender != null && sender.equalsIgnoreCase(playerName);
    }

    public static String orderedTextToString(OrderedText text) {
        StringBuilder builder = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }
}
