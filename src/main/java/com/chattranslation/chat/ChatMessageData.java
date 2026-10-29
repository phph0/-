package com.chattranslation.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ChatMessageData(String sender, String content, String fullMessage) {
    private static final Pattern CHAT_PATTERN = Pattern.compile("^<([^>]+)>\\s*(.+)$");

    public static ChatMessageData parse(String message) {
        Matcher matcher = CHAT_PATTERN.matcher(message);
        if (matcher.matches()) {
            return new ChatMessageData(matcher.group(1), matcher.group(2), message);
        }
        return null;
    }

    public boolean isSamePlayer(String playerName) {
        return sender != null && sender.equals(playerName);
    }
}
