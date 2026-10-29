package com.ctl.mixin;

import com.ctl.chat.ChatTranslationManager;
import com.ctl.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigManager.getConfig().isEnabled()) return;
        if (click.button() != 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        ChatHud chatHud = client.inGameHud.getChatHud();
        ChatHudAccessor accessor = (ChatHudAccessor) chatHud;
        double chatScale = accessor.ctl$invokeGetChatScale();
        if (chatScale <= 0) return;

        int chatBottom = client.getWindow().getScaledHeight() - 40;
        int lineHeight = (int) (9 * chatScale);

        double relativeX = click.x() - 4;
        double relativeY = chatBottom - click.y();

        if (relativeX < 0 || relativeX > accessor.ctl$invokeGetWidth() * chatScale) return;
        if (relativeY < 0) return;

        int messageIndex = (int) (relativeY / lineHeight);

        List<ChatHudLine> messages = accessor.ctl$getMessages();

        if (messageIndex >= 0 && messageIndex < messages.size()) {
            String messageText = messages.get(messageIndex).content().getString();
            ChatTranslationManager.toggleTranslation(messageIndex, messageText);
        }
    }
}
