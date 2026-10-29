package com.ctl.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ChatHud.class)
public interface ChatHudAccessor {
    @Accessor("messages")
    List<ChatHudLine> ctl$getMessages();

    @Accessor("visibleMessages")
    List<ChatHudLine.Visible> ctl$getVisibleMessages();

    @Invoker("getWidth")
    int ctl$invokeGetWidth();

    @Invoker("getChatScale")
    double ctl$invokeGetChatScale();
}
