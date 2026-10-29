package com.ctl.mixin;

import com.ctl.chat.ChatMessageData;
import com.ctl.chat.ChatTranslationManager;
import com.ctl.config.ConfigManager;
import com.ctl.translation.TranslationResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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
    public abstract int getWidth();

    @Shadow
    public abstract double getChatScale();

    @Shadow
    public abstract int getVisibleLineCount();

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V", at = @At("TAIL"))
    private void renderTranslations(DrawContext context, TextRenderer textRenderer, int currentTick, int mouseX, int mouseY, boolean focused, boolean unused, CallbackInfo ci) {
        if (!ConfigManager.getConfig().isEnabled()) return;
        if (visibleMessages.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.getChatVisibility().getValue() == net.minecraft.network.message.ChatVisibility.HIDDEN) return;

        double chatScale = getChatScale();
        if (chatScale <= 0) return;

        int chatWidth = getWidth();
        int windowHeight = context.getScaledWindowHeight();
        int lineHeight = 9;

        int visibleLineCount = getVisibleLineCount();
        int shownLines = Math.min(visibleMessages.size(), visibleLineCount);
        // local (pre-scale) y of the chat input box baseline, matches vanilla ChatHud's own "k"
        int baseK = (int) ((windowHeight - 40) / chatScale);
        // local y just above the topmost currently rendered chat line: draw translations here,
        // stacked upward, so they never overlap the real chat text underneath.
        int stackTopY = baseK - shownLines * lineHeight;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(4.0f, 0.0f);
        context.getMatrices().scale((float) chatScale, (float) chatScale);

        int stackOffset = 0;

        for (int i = 0; i < visibleMessages.size(); i++) {
            if (!ChatTranslationManager.isExpanded(i)) {
                continue;
            }

            var entry = ChatTranslationManager.getTranslation(i);
            if (entry == null) continue;

            String messageText = ChatMessageData.orderedTextToString(visibleMessages.get(i).content());

            if (entry.loading()) {
                stackOffset++;
                int y = stackTopY - stackOffset * lineHeight;
                context.fill(0, y, chatWidth, y + lineHeight, 0x80000000);
                context.drawTextWithShadow(client.textRenderer,
                        "  └ Translating...", 2, y + 1, 0xFFAAAA00);
            } else if (entry.result() != null) {
                TranslationResult result = entry.result();
                ChatMessageData data = ChatMessageData.parse(messageText);
                String sender = data.sender();

                String translationLine = ChatTranslationManager.formatTranslationLine(result, sender);

                int wrappedLines = (int) Math.ceil((double) client.textRenderer.getWidth(translationLine)
                        / chatWidth);
                if (wrappedLines < 1) wrappedLines = 1;

                String remaining = translationLine;
                for (int line = 0; line < wrappedLines; line++) {
                    String lineText = client.textRenderer.trimToWidth(remaining, chatWidth);
                    remaining = remaining.substring(lineText.length());

                    stackOffset++;
                    int y = stackTopY - stackOffset * lineHeight;
                    context.fill(0, y, chatWidth, y + lineHeight, 0x80000000);
                    context.drawTextWithShadow(client.textRenderer, lineText,
                            line == 0 ? 2 : 10, y + 1, 0xFF88CCFF);
                }
            }
        }

        context.getMatrices().popMatrix();
    }
}
