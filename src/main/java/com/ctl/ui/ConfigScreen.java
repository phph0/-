package com.ctl.ui;

import com.ctl.config.ChatTranslationConfig;
import com.ctl.config.ConfigManager;
import com.ctl.translation.LanguageInfo;
import com.ctl.translation.LanguageManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("ctl.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ChatTranslationConfig config = ConfigManager.getConfig();

        int centerX = this.width / 2;
        int startY = this.height / 4;

        // Toggle enabled
        addDrawableChild(ButtonWidget.builder(
                        Text.literal(config.isEnabled() ? "Translation: ON" : "Translation: OFF"),
                        button -> {
                            config.setEnabled(!config.isEnabled());
                            button.setMessage(Text.literal(
                                    config.isEnabled() ? "Translation: ON" : "Translation: OFF"));
                            ConfigManager.save();
                        })
                .dimensions(centerX - 100, startY, 200, 20)
                .build());

        // Target language button
        LanguageInfo currentLang = LanguageManager.getLanguage(config.getTargetLanguage());
        String langDisplay = currentLang != null ? currentLang.displayName() : config.getTargetLanguage();

        addDrawableChild(ButtonWidget.builder(
                        Text.literal("Target Language: " + langDisplay),
                        button -> {
                            if (this.client != null) {
                                this.client.setScreen(new LanguageSelectionScreen(this));
                            }
                        })
                .dimensions(centerX - 100, startY + 30, 200, 20)
                .build());

        // Done button
        addDrawableChild(ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> close())
                .dimensions(centerX - 100, startY + 70, 200, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, this.height / 4 - 20, 0xFFFFFF);

        // Render current flag
        ChatTranslationConfig config = ConfigManager.getConfig();
        LanguageInfo lang = LanguageManager.getLanguage(config.getTargetLanguage());
        if (lang != null) {
            int flagX = this.width / 2 - FlagRenderer.getFlagWidth() / 2;
            int flagY = this.height / 4 - 45;
            FlagRenderer.renderFlag(context, lang.region(), flagX, flagY);
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
