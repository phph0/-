package com.elimentas.tree.client;

import com.elimentas.skill.Element;
import com.elimentas.tree.SkillNode;
import com.elimentas.tree.SkillTreeDefinition;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.HashSet;
import java.util.Set;

public class TreeConnectionRenderer {

    public void render(GuiGraphics graphics, float scrollX, float scrollY, float zoom,
                       int screenW, int screenH) {
        Set<String> drawn = new HashSet<>();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance()
                .begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (SkillNode node : SkillTreeDefinition.allNodes()) {
            for (ResourceLocation connId : node.connections()) {
                String key = makeKey(node.id(), connId);
                if (drawn.contains(key)) continue;
                drawn.add(key);

                SkillNode target = SkillTreeDefinition.getNode(connId);
                if (target == null) continue;

                float x1 = node.x() * zoom + scrollX + screenW / 2f;
                float y1 = node.y() * zoom + scrollY + screenH / 2f;
                float x2 = target.x() * zoom + scrollX + screenW / 2f;
                float y2 = target.y() * zoom + scrollY + screenH / 2f;

                int color = elementColor(node.element(), 0.5f);
                float r = ((color >> 16) & 0xFF) / 255f;
                float g = ((color >> 8) & 0xFF) / 255f;
                float b = (color & 0xFF) / 255f;

                builder.addVertex(matrix, x1, y1, 0).setColor(r, g, b, 0.6f);
                builder.addVertex(matrix, x2, y2, 0).setColor(r, g, b, 0.3f);
            }
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    private String makeKey(ResourceLocation a, ResourceLocation b) {
        if (a.toString().compareTo(b.toString()) < 0) {
            return a + "|" + b;
        }
        return b + "|" + a;
    }

    static int elementColor(Element element, float brightness) {
        return switch (element) {
            case FIRE -> lerpColor(0xFF6A00, 0xFFD700, brightness);
            case ICE -> lerpColor(0x4FC3F7, 0xE0F7FA, brightness);
            case LIGHTNING -> lerpColor(0xAB47BC, 0xFFEB3B, brightness);
        };
    }

    private static int lerpColor(int a, int b, float t) {
        int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF;
        int br = (b >> 16) & 0xFF, bg = (b >> 8) & 0xFF, bb = b & 0xFF;
        int r = (int) (ar + (br - ar) * t);
        int g = (int) (ag + (bg - ag) * t);
        int bl = (int) (ab + (bb - ab) * t);
        return (r << 16) | (g << 8) | bl;
    }
}
