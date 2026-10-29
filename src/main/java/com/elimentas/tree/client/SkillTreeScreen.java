package com.elimentas.tree.client;

import com.elimentas.data.ClientSkillData;
import com.elimentas.network.NodeUnlockPayload;
import com.elimentas.tree.SkillNode;
import com.elimentas.tree.SkillTreeDefinition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeScreen extends Screen {

    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 2.0f;
    private static final float ZOOM_SPEED = 0.1f;
    private static final double CLICK_THRESHOLD = 4.0;

    private final StarfieldBackground starfield = new StarfieldBackground();
    private final TreeNodeRenderer nodeRenderer = new TreeNodeRenderer();
    private final TreeConnectionRenderer connectionRenderer = new TreeConnectionRenderer();

    private float scrollX = 0;
    private float scrollY = 0;
    private float zoom = 1.0f;
    private float animTime = 0;

    private boolean dragging = false;
    private double dragStartX, dragStartY;
    private float dragScrollStartX, dragScrollStartY;
    private boolean didDrag = false;

    private SkillNode hoveredNode = null;

    public SkillTreeScreen() {
        super(Component.translatable("screen.elimentas.skill_tree"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        animTime += partialTick;

        graphics.fill(0, 0, width, height, 0xFF0A0A1A);

        starfield.render(graphics, scrollX, scrollY, zoom, animTime);

        connectionRenderer.render(graphics, scrollX, scrollY, zoom, width, height);

        hoveredNode = null;
        List<SkillNode> nodes = new ArrayList<>(SkillTreeDefinition.allNodes());
        for (SkillNode node : nodes) {
            float sx = node.x() * zoom + scrollX + width / 2f;
            float sy = node.y() * zoom + scrollY + height / 2f;

            float hitR = TreeNodeRenderer.hitRadius(node, zoom);
            boolean hovered = Math.abs(mouseX - sx) < hitR && Math.abs(mouseY - sy) < hitR;
            if (hovered) hoveredNode = node;

            boolean unlocked = ClientSkillData.isUnlocked(node.id());
            nodeRenderer.render(graphics, node, sx, sy, zoom, hovered, unlocked, animTime);
        }

        renderElementLabels(graphics);

        renderSkillPointsHud(graphics);

        if (hoveredNode != null) {
            renderTooltip(graphics, mouseX, mouseY);
        }

        renderControls(graphics);
    }

    private void renderSkillPointsHud(GuiGraphics graphics) {
        String text = "Skill Points: " + ClientSkillData.skillPoints();
        int textWidth = font.width(text);
        graphics.fill(width - textWidth - 16, 4, width - 4, 20, 0x88000000);
        graphics.drawString(font, text, width - textWidth - 10, 7, 0xFFFFD700, false);
    }

    private void renderElementLabels(GuiGraphics graphics) {
        float fireX = -200 * zoom + scrollX + width / 2f;
        float fireY = 40 * zoom + scrollY + height / 2f;
        graphics.drawCenteredString(font, Component.literal("FIRE"), (int) fireX, (int) fireY, 0xFFFF6A00);

        float iceX = 200 * zoom + scrollX + width / 2f;
        float iceY = 40 * zoom + scrollY + height / 2f;
        graphics.drawCenteredString(font, Component.literal("ICE"), (int) iceX, (int) iceY, 0xFF4FC3F7);

        float lightX = 0 * zoom + scrollX + width / 2f;
        float lightY = (250 + 50) * zoom + scrollY + height / 2f;
        graphics.drawCenteredString(font, Component.literal("LIGHTNING"), (int) lightX, (int) lightY, 0xFFAB47BC);
    }

    private void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Component> lines = new ArrayList<>();
        boolean unlocked = ClientSkillData.isUnlocked(hoveredNode.id());

        lines.add(Component.literal(hoveredNode.name()).withStyle(s ->
                s.withColor(getElementTextColor(hoveredNode)).withBold(true)));
        lines.add(Component.literal("Element: " + hoveredNode.element().name())
                .withStyle(s -> s.withColor(0xAAAAAA)));
        lines.add(Component.literal("Tier: " + hoveredNode.tier())
                .withStyle(s -> s.withColor(0x888888)));

        if (unlocked) {
            lines.add(Component.literal("✦ UNLOCKED")
                    .withStyle(s -> s.withColor(0x66BB6A).withBold(true)));
        } else {
            lines.add(Component.literal("✧ LOCKED")
                    .withStyle(s -> s.withColor(0xFF5555)));
            if (canUnlockLocally(hoveredNode)) {
                lines.add(Component.literal("Click to unlock (1 SP)")
                        .withStyle(s -> s.withColor(0xFFD700)));
            }
        }

        if (hoveredNode.skillId() != null) {
            lines.add(Component.literal("Skill: " + hoveredNode.skillId().getPath())
                    .withStyle(s -> s.withColor(0x66BB6A)));
        }

        graphics.renderTooltip(font, lines, java.util.Optional.empty(), mouseX, mouseY);
    }

    private boolean canUnlockLocally(SkillNode node) {
        if (ClientSkillData.isUnlocked(node.id())) return false;
        if (ClientSkillData.skillPoints() <= 0) return false;

        if (node.tier() == 0) return true;

        for (net.minecraft.resources.ResourceLocation connId : node.connections()) {
            SkillNode conn = SkillTreeDefinition.getNode(connId);
            if (conn != null && conn.tier() < node.tier() && ClientSkillData.isUnlocked(connId)) {
                return true;
            }
        }
        return false;
    }

    private int getElementTextColor(SkillNode node) {
        return switch (node.element()) {
            case FIRE -> 0xFF8A00;
            case ICE -> 0x81D4FA;
            case LIGHTNING -> 0xCE93D8;
        };
    }

    private void renderControls(GuiGraphics graphics) {
        graphics.drawString(font, "Scroll: Zoom | Drag: Pan | Click: Unlock | ESC: Close",
                8, height - 14, 0x66FFFFFF, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = true;
            didDrag = false;
            dragStartX = mouseX;
            dragStartY = mouseY;
            dragScrollStartX = scrollX;
            dragScrollStartY = scrollY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
            if (!didDrag && hoveredNode != null) {
                tryUnlockNode(hoveredNode);
            }
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void tryUnlockNode(SkillNode node) {
        if (!canUnlockLocally(node)) return;
        PacketDistributor.sendToServer(new NodeUnlockPayload(node.id().toString()));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && button == 0) {
            double dist = Math.sqrt(Math.pow(mouseX - dragStartX, 2) + Math.pow(mouseY - dragStartY, 2));
            if (dist > CLICK_THRESHOLD) {
                didDrag = true;
            }
            scrollX = dragScrollStartX + (float) (mouseX - dragStartX);
            scrollY = dragScrollStartY + (float) (mouseY - dragStartY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        float oldZoom = zoom;
        if (scrollY > 0) {
            zoom = Math.min(MAX_ZOOM, zoom + ZOOM_SPEED);
        } else if (scrollY < 0) {
            zoom = Math.max(MIN_ZOOM, zoom - ZOOM_SPEED);
        }

        if (oldZoom != zoom) {
            float scale = zoom / oldZoom;
            this.scrollX = (float) (mouseX - width / 2f) * (1 - scale) + this.scrollX * scale;
            this.scrollY = (float) (mouseY - height / 2f) * (1 - scale) + this.scrollY * scale;
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_K) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
