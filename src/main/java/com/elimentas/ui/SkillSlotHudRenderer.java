package com.elimentas.ui;

import com.elimentas.data.ClientSkillData;
import com.elimentas.skill.Element;
import com.elimentas.tree.SkillNode;
import com.elimentas.tree.SkillTreeDefinition;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SkillSlotHudRenderer {

    private static final int SLOT_SIZE = 22;
    private static final int SLOT_GAP = 4;
    private static final int ICON_SIZE = 16;
    private static final String[] KEY_LABELS = {"Z", "X", "C", "V"};

    private static final ResourceLocation SLOT_FRAME =
            ResourceLocation.fromNamespaceAndPath("elimentas", "textures/gui/slot_frame.png");
    private static final ResourceLocation SLOT_FRAME_ACTIVE =
            ResourceLocation.fromNamespaceAndPath("elimentas", "textures/gui/slot_frame_active.png");
    private static final ResourceLocation ICON_FIRE =
            ResourceLocation.fromNamespaceAndPath("elimentas", "textures/skill/fire_fireball.png");
    private static final ResourceLocation ICON_ICE =
            ResourceLocation.fromNamespaceAndPath("elimentas", "textures/skill/ice_shard.png");
    private static final ResourceLocation ICON_LIGHTNING =
            ResourceLocation.fromNamespaceAndPath("elimentas", "textures/skill/lightning_bolt.png");

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
        ResourceLocation nodeId = ClientSkillData.getSlot(slotIndex);
        boolean hasSkill = nodeId != null;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        graphics.blit(hasSkill ? SLOT_FRAME_ACTIVE : SLOT_FRAME, x, y, 0, 0,
                SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);

        if (hasSkill) {
            SkillNode node = SkillTreeDefinition.getNode(nodeId);
            if (node != null && node.skillId() != null) {
                ResourceLocation icon = getSkillIcon(node.element());
                int iconX = x + (SLOT_SIZE - ICON_SIZE) / 2;
                int iconY = y + (SLOT_SIZE - ICON_SIZE) / 2;
                graphics.blit(icon, iconX, iconY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

                if (ClientSkillData.isOnCooldown(node.skillId())) {
                    float progress = ClientSkillData.getCooldownProgress(node.skillId());
                    int cooldownHeight = (int) (SLOT_SIZE * progress);
                    graphics.fill(x, y + SLOT_SIZE - cooldownHeight,
                            x + SLOT_SIZE, y + SLOT_SIZE, 0xAA000000);
                }
            }
        }

        RenderSystem.disableBlend();

        graphics.drawCenteredString(
                net.minecraft.client.Minecraft.getInstance().font,
                KEY_LABELS[slotIndex],
                x + SLOT_SIZE / 2, y + SLOT_SIZE + 2,
                hasSkill ? 0xFFFFD700 : 0xAAAAAA);
    }

    private static ResourceLocation getSkillIcon(Element element) {
        return switch (element) {
            case FIRE -> ICON_FIRE;
            case ICE -> ICON_ICE;
            case LIGHTNING -> ICON_LIGHTNING;
        };
    }
}
