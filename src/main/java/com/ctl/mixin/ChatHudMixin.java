package com.ctl.mixin;

import com.ctl.chat.ChatMessageData;
import com.ctl.chat.ChatTranslationManager;
import com.ctl.config.ConfigManager;
import com.ctl.translation.TranslationResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract double getChatScale();

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTranslations(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (!ConfigManager.getConfig().isEnabled()) return;
        if (visibleMessages.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.getChatVisibility().getValue() == net.minecraft.network.message.ChatVisibility.HIDDEN) return;

        double chatScale = getChatScale();
        if (chatScale <= 0) return;

        int chatWidth = getWidth();

        context.getMatrices().push();
        context.getMatrices().translate(4.0f, 0.0f, 0.0f);
        context.getMatrices().scale((float) chatScale, (float) chatScale, 1.0f);

        int lineHeight = 9;
        int yOffset = 0;

        for (int i = 0; i < visibleMessages.size() && i < messages.size(); i++) {
            ChatHudLine originalMessage = messages.get(i);
            String messageText = originalMessage.content().getString();

            if (!ChatTranslationManager.isExpanded(i)) {
                continue;
            }

            var entry = ChatTranslationManager.getTranslation(i);
            if (entry == null) continue;

            int messageY = -lineHeight * (i + 1 + yOffset) - 1;

            if (entry.loading()) {
                context.drawTextWithShadow(client.textRenderer,
                        "  └ Translating...", 0, messageY - lineHeight, 0xFFAAAA00);
                yOffset++;
            } else if (entry.result() != null) {
                TranslationResult result = entry.result();
                ChatMessageData data = ChatMessageData.parse(messageText);
                String sender = data != null ? data.sender() : "?";

                String translationLine = ChatTranslationManager.formatTranslationLine(result, sender);

                int wrappedLines = (int) Math.ceil((double) client.textRenderer.getWidth(translationLine)
                        / chatWidth);
                if (wrappedLines < 1) wrappedLines = 1;

                for (int line = 0; line < wrappedLines; line++) {
                    int start = 0;
                    String remaining = translationLine;
                    for (int l = 0; l < line; l++) {
                        int trimmed = client.textRenderer.trimToWidth(remaining, chatWidth).length();
                        remaining = remaining.substring(trimmed);
                    }
                    String lineText = client.textRenderer.trimToWidth(remaining, chatWidth);

                    context.fill(0, messageY - lineHeight * (wrappedLines - line),
                            chatWidth, messageY - lineHeight * (wrappedLines - line - 1),
                            0x80000000);
                    context.drawTextWithShadow(client.textRenderer, lineText,
                            line == 0 ? 2 : 0,
                            messageY - lineHeight * (wrappedLines - line) + 1,
                            0xFF88CCFF);
                }
                yOffset += wrappedLines;
            }
        }

        context.getMatrices().pop();
    }
}
