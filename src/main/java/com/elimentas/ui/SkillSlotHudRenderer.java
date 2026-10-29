package com.elimentas.ui;

import com.elimentas.data.ClientSkillData;
import com.elimentas.tree.SkillNode;
import com.elimentas.tree.SkillTreeDefinition;
import com.elimentas.tree.client.TreeConnectionRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SkillSlotHudRenderer {

    private static final int SLOT_SIZE = 22;
    private static final int SLOT_GAP = 4;
    private static final int PADDING = 3;
    private static final String[] KEY_LABELS = {"Z", "X", "C", "V"};

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        int screenW = graphics.guiWidth();
        int screenH = graphics.guiHeight();

        int totalWidth = ClientSkillData.SLOT_COUNT * SLOT_SIZE + (ClientSkillData.SLOT_COUNT - 1) * SLOT_GAP;
        int startX = (screenW - totalWidth) / 2;
        int startY = screenH - SLOT_SIZE - 40;

        for (int i = 0; i < ClientSkillData.SLOT_COUNT; i++) {
            int x = startX + i * (SLOT_SIZE + SLOT_GAP);
            renderSlot(graphics, x, startY, i);
        }
    }

    private static void renderSlot(GuiGraphics graphics, int x, int y, int slotIndex) {
        graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, 0x88000000);

        graphics.fill(x, y, x + SLOT_SIZE, y + 1, 0x66FFFFFF);
        graphics.fill(x, y + SLOT_SIZE - 1, x + SLOT_SIZE, y + SLOT_SIZE, 0x66FFFFFF);
        graphics.fill(x, y, x + 1, y + SLOT_SIZE, 0x66FFFFFF);
        graphics.fill(x + SLOT_SIZE - 1, y, x + SLOT_SIZE, y + SLOT_SIZE, 0x66FFFFFF);

        ResourceLocation nodeId = ClientSkillData.getSlot(slotIndex);
        if (nodeId != null) {
            SkillNode node = SkillTreeDefinition.getNode(nodeId);
            if (node != null) {
                int color = TreeConnectionRenderer.elementColor(node.element(), 0.7f);
                int iconColor = (0xFF << 24) | color;

                int iconSize = SLOT_SIZE - PADDING * 2;
                graphics.fill(x + PADDING, y + PADDING,
                        x + PADDING + iconSize, y + PADDING + iconSize, iconColor);

                int whiteSize = iconSize / 3;
                int cx = x + SLOT_SIZE / 2;
                int cy = y + SLOT_SIZE / 2;
                graphics.fill(cx - whiteSize / 2, cy - whiteSize / 2,
                        cx + whiteSize / 2, cy + whiteSize / 2, 0xDDFFFFFF);

                ResourceLocation skillId = node.skillId();
                if (skillId != null && ClientSkillData.isOnCooldown(skillId)) {
                    float progress = ClientSkillData.getCooldownProgress(skillId);
                    int cooldownHeight = (int) ((SLOT_SIZE) * progress);

                    RenderSystem.enableBlend();
                    graphics.fill(x, y + SLOT_SIZE - cooldownHeight,
                            x + SLOT_SIZE, y + SLOT_SIZE, 0xAA000000);
                    RenderSystem.disableBlend();
                }
            }
        }

        graphics.drawCenteredString(
                net.minecraft.client.Minecraft.getInstance().font,
                KEY_LABELS[slotIndex],
                x + SLOT_SIZE / 2, y + SLOT_SIZE + 2,
                0xAAAAAAA);
    }
}
