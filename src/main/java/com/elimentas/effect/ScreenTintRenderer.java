package com.elimentas.effect;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;

public class ScreenTintRenderer {

    private int color;
    private int durationTicks;
    private int ticksRemaining;

    public void start(int color, int durationTicks) {
        this.color = color;
        this.durationTicks = durationTicks;
        this.ticksRemaining = durationTicks;
    }

    public void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
    }

    public boolean isActive() {
        return ticksRemaining > 0;
    }

    public void render(GuiGraphics graphics, int screenWidth, int screenHeight) {
        if (!isActive()) return;

        float progress = (float) ticksRemaining / durationTicks;
        float alpha = progress * 0.4f;

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int edgeSize = Math.min(screenWidth, screenHeight) / 3;

        // top edge
        builder.addVertex(matrix, 0, 0, 0).setColor(r, g, b, alpha);
        builder.addVertex(matrix, screenWidth, 0, 0).setColor(r, g, b, alpha);
        builder.addVertex(matrix, screenWidth, edgeSize, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, 0, edgeSize, 0).setColor(r, g, b, 0);

        // bottom edge
        builder.addVertex(matrix, 0, screenHeight - edgeSize, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, screenWidth, screenHeight - edgeSize, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, screenWidth, screenHeight, 0).setColor(r, g, b, alpha);
        builder.addVertex(matrix, 0, screenHeight, 0).setColor(r, g, b, alpha);

        // left edge
        builder.addVertex(matrix, 0, 0, 0).setColor(r, g, b, alpha);
        builder.addVertex(matrix, edgeSize, 0, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, edgeSize, screenHeight, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, 0, screenHeight, 0).setColor(r, g, b, alpha);

        // right edge
        builder.addVertex(matrix, screenWidth - edgeSize, 0, 0).setColor(r, g, b, 0);
        builder.addVertex(matrix, screenWidth, 0, 0).setColor(r, g, b, alpha);
        builder.addVertex(matrix, screenWidth, screenHeight, 0).setColor(r, g, b, alpha);
        builder.addVertex(matrix, screenWidth - edgeSize, screenHeight, 0).setColor(r, g, b, 0);

        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.disableBlend();
    }
}
