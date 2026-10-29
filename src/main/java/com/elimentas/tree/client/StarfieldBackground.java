package com.elimentas.tree.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;

import java.util.Random;

public class StarfieldBackground {

    private static final int STAR_COUNT = 200;
    private final float[] starX;
    private final float[] starY;
    private final float[] starSize;
    private final float[] starBrightness;
    private final float[] starPhase;

    public StarfieldBackground() {
        Random rand = new Random(42);
        starX = new float[STAR_COUNT];
        starY = new float[STAR_COUNT];
        starSize = new float[STAR_COUNT];
        starBrightness = new float[STAR_COUNT];
        starPhase = new float[STAR_COUNT];

        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = rand.nextFloat() * 2000 - 1000;
            starY[i] = rand.nextFloat() * 2000 - 1000;
            starSize[i] = 0.5f + rand.nextFloat() * 1.5f;
            starBrightness[i] = 0.3f + rand.nextFloat() * 0.7f;
            starPhase[i] = rand.nextFloat() * (float) Math.PI * 2;
        }
    }

    public void render(GuiGraphics graphics, float scrollX, float scrollY, float zoom, float time) {
        Matrix4f matrix = graphics.pose().last().pose();
        int screenW = graphics.guiWidth();
        int screenH = graphics.guiHeight();

        float parallax = 0.3f;
        float offsetX = scrollX * parallax;
        float offsetY = scrollY * parallax;

        for (int i = 0; i < STAR_COUNT; i++) {
            float sx = (starX[i] - offsetX) * zoom + screenW / 2f;
            float sy = (starY[i] - offsetY) * zoom + screenH / 2f;

            if (sx < -10 || sx > screenW + 10 || sy < -10 || sy > screenH + 10) continue;

            float twinkle = (float) Math.sin(time * 0.05f + starPhase[i]) * 0.3f + 0.7f;
            float alpha = starBrightness[i] * twinkle;
            float size = starSize[i] * zoom;

            int a = (int) (alpha * 255);
            int color = (a << 24) | 0xE8E8FF;
            graphics.fill((int) (sx - size / 2), (int) (sy - size / 2),
                    (int) (sx + size / 2), (int) (sy + size / 2), color);
        }
    }
}
