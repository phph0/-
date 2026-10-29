package com.spyglasszoom.ui;

import com.spyglasszoom.config.ConfigManager;
import com.spyglasszoom.config.ZoomConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private ZoomConfig editConfig;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("screen.spyglasszoom.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.editConfig = ConfigManager.getConfig().copy();

        int centerX = this.width / 2;
        int y = this.height / 4;

        this.addDrawableChild(new SliderWidget(
                centerX - 100, y, 200, 20,
                Text.translatable("option.spyglasszoom.zoom_level")
                        .append(": " + String.format("%.1fx", editConfig.zoomLevel)),
                (editConfig.zoomLevel - 2.0f) / 8.0f
        ) {
            @Override
            protected void updateMessage() {
                float val = 2.0f + (float) this.value * 8.0f;
                this.setMessage(Text.translatable("option.spyglasszoom.zoom_level")
                        .append(": " + String.format("%.1fx", val)));
            }

            @Override
            protected void applyValue() {
                editConfig.zoomLevel = MathHelper.clamp(2.0f + (float) this.value * 8.0f, 2.0f, 10.0f);
            }
        });

        this.addDrawableChild(new SliderWidget(
                centerX - 100, y + 28, 200, 20,
                Text.translatable("option.spyglasszoom.zoom_speed")
                        .append(": " + String.format("%.0f%%", editConfig.zoomSpeed * 100)),
                (editConfig.zoomSpeed - 0.1f) / 0.9f
        ) {
            @Override
            protected void updateMessage() {
                float val = 0.1f + (float) this.value * 0.9f;
                this.setMessage(Text.translatable("option.spyglasszoom.zoom_speed")
                        .append(": " + String.format("%.0f%%", val * 100)));
            }

            @Override
            protected void applyValue() {
                editConfig.zoomSpeed = MathHelper.clamp(0.1f + (float) this.value * 0.9f, 0.1f, 1.0f);
            }
        });

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("option.spyglasszoom.show_overlay")
                        .append(": ")
                        .append(Text.translatable(editConfig.showOverlay
                                ? "options.on" : "options.off")),
                button -> {
                    editConfig.showOverlay = !editConfig.showOverlay;
                    button.setMessage(
                            Text.translatable("option.spyglasszoom.show_overlay")
                                    .append(": ")
                                    .append(Text.translatable(editConfig.showOverlay
                                            ? "options.on" : "options.off")));
                }
        ).dimensions(centerX - 100, y + 56, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.done"),
                button -> {
                    ConfigManager.setConfig(editConfig);
                    this.client.setScreen(this.parent);
                }
        ).dimensions(centerX - 100, y + 96, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, this.height / 4 - 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
