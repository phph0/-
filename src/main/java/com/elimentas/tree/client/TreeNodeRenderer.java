package com.elimentas.tree.client;

import com.elimentas.tree.SkillNode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;

public class TreeNodeRenderer {

    private static final int CORE_SIZE = 10;
    private static final int NODE_SIZE = 7;
    private static final int GLOW_EXTRA = 4;

    public void render(GuiGraphics graphics, SkillNode node, float screenX, float screenY,
                       float zoom, boolean hovered, float time) {
        int baseSize = node.tier() == 0 ? CORE_SIZE : NODE_SIZE;
        float size = baseSize * zoom;

        int color = TreeConnectionRenderer.elementColor(node.element(), 0.7f);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        float pulse = (float) Math.sin(time * 0.08f + node.x() * 0.01f) * 0.15f + 0.85f;
        if (hovered) pulse = 1.0f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        float glowSize = (size + GLOW_EXTRA * zoom) * pulse;
        int glowAlpha = hovered ? 80 : 40;
        int glowColor = (glowAlpha << 24) | (r << 16) | (g << 8) | b;
        graphics.fill(
                (int) (screenX - glowSize), (int) (screenY - glowSize),
                (int) (screenX + glowSize), (int) (screenY + glowSize),
                glowColor
        );

        float coreSize = size * pulse;
        int coreAlpha = hovered ? 255 : 200;
        int coreColor = (coreAlpha << 24) | (r << 16) | (g << 8) | b;
        graphics.fill(
                (int) (screenX - coreSize / 2), (int) (screenY - coreSize / 2),
                (int) (screenX + coreSize / 2), (int) (screenY + coreSize / 2),
                coreColor
        );

        int centerAlpha = hovered ? 255 : 230;
        int white = (centerAlpha << 24) | 0xFFFFFF;
        float centerSize = coreSize * 0.4f;
        graphics.fill(
                (int) (screenX - centerSize / 2), (int) (screenY - centerSize / 2),
                (int) (screenX + centerSize / 2), (int) (screenY + centerSize / 2),
                white
        );

        RenderSystem.disableBlend();
    }

    public static float hitRadius(SkillNode node, float zoom) {
        int baseSize = node.tier() == 0 ? CORE_SIZE : NODE_SIZE;
        return (baseSize + GLOW_EXTRA) * zoom;
    }
}
