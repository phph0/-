package com.ctl.ui;

import com.ctl.config.ConfigManager;
import com.ctl.translation.LanguageInfo;
import com.ctl.translation.LanguageManager;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

public class LanguageSelectionScreen extends Screen {
    private static final int ENTRY_HEIGHT = 24;
    private static final int LIST_PADDING = 4;

    private final Screen parent;
    private TextFieldWidget searchField;
    private List<LanguageInfo> filteredLanguages;
    private int scrollOffset = 0;
    private int listTop;
    private int listBottom;
    private String selectedCode;

    public LanguageSelectionScreen(Screen parent) {
        super(Text.translatable("ctl.language.title"));
        this.parent = parent;
        this.selectedCode = ConfigManager.getConfig().getTargetLanguage();
        this.filteredLanguages = LanguageManager.getAllLanguages();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        // Search field
        searchField = new TextFieldWidget(this.textRenderer, centerX - 100, 30, 200, 20,
                Text.translatable("ctl.language.search"));
        searchField.setPlaceholder(Text.literal("Search..."));
        searchField.setChangedListener(this::onSearchChanged);
        addDrawableChild(searchField);

        listTop = 58;
        listBottom = this.height - 40;

        // Done button
        addDrawableChild(ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> close())
                .dimensions(centerX - 100, this.height - 30, 200, 20)
                .build());
    }

    private void onSearchChanged(String query) {
        scrollOffset = 0;
        if (query.isEmpty()) {
            filteredLanguages = LanguageManager.getAllLanguages();
        } else {
            filteredLanguages = LanguageManager.search(query);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, 10, 0xFFFFFF);

        int centerX = this.width / 2;
        int listWidth = 220;
        int listX = centerX - listWidth / 2;

        // Draw list background
        context.fill(listX - 2, listTop - 2, listX + listWidth + 2, listBottom + 2, 0x80000000);

        // Enable scissor to clip list
        context.enableScissor(listX, listTop, listX + listWidth, listBottom);

        int visibleHeight = listBottom - listTop;
        int y = listTop - scrollOffset;

        for (int i = 0; i < filteredLanguages.size(); i++) {
            if (y + ENTRY_HEIGHT < listTop) {
                y += ENTRY_HEIGHT;
                continue;
            }
            if (y > listBottom) break;

            LanguageInfo lang = filteredLanguages.get(i);
            boolean isSelected = lang.code().equals(selectedCode);
            boolean isHovered = mouseX >= listX && mouseX <= listX + listWidth
                    && mouseY >= y && mouseY < y + ENTRY_HEIGHT
                    && mouseY >= listTop && mouseY <= listBottom;

            // Background
            if (isSelected) {
                context.fill(listX, y, listX + listWidth, y + ENTRY_HEIGHT, 0x80446688);
            } else if (isHovered) {
                context.fill(listX, y, listX + listWidth, y + ENTRY_HEIGHT, 0x40FFFFFF);
            }

            // Flag icon
            int flagX = listX + LIST_PADDING;
            int flagY = y + (ENTRY_HEIGHT - FlagRenderer.getFlagHeight()) / 2;
            FlagRenderer.renderFlag(context, lang.region(), flagX, flagY);

            // Language name
            int textX = flagX + FlagRenderer.getFlagWidth() + 6;
            int textY = y + (ENTRY_HEIGHT - 9) / 2;
            int textColor = isSelected ? 0xFFFF00 : 0xFFFFFF;
            context.drawTextWithShadow(this.textRenderer, lang.nativeName(), textX, textY, textColor);

            // English name on right
            String englishName = lang.name();
            int englishWidth = this.textRenderer.getWidth(englishName);
            context.drawTextWithShadow(this.textRenderer, englishName,
                    listX + listWidth - englishWidth - LIST_PADDING, textY, 0xAAAAAA);

            y += ENTRY_HEIGHT;
        }

        context.disableScissor();

        // Scrollbar
        int totalHeight = filteredLanguages.size() * ENTRY_HEIGHT;
        if (totalHeight > visibleHeight) {
            int scrollbarHeight = Math.max(20, visibleHeight * visibleHeight / totalHeight);
            int scrollbarY = listTop + (int) ((float) scrollOffset / (totalHeight - visibleHeight)
                    * (visibleHeight - scrollbarHeight));
            context.fill(listX + listWidth + 2, scrollbarY, listX + listWidth + 5,
                    scrollbarY + scrollbarHeight, 0xFFAAAAAA);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (super.mouseClicked(click, doubled)) return true;

        double mouseX = click.x();
        double mouseY = click.y();

        int centerX = this.width / 2;
        int listWidth = 220;
        int listX = centerX - listWidth / 2;

        if (mouseX >= listX && mouseX <= listX + listWidth
                && mouseY >= listTop && mouseY <= listBottom) {
            int relativeY = (int) mouseY - listTop + scrollOffset;
            int index = relativeY / ENTRY_HEIGHT;
            if (index >= 0 && index < filteredLanguages.size()) {
                LanguageInfo lang = filteredLanguages.get(index);
                selectedCode = lang.code();
                ConfigManager.getConfig().setTargetLanguage(lang.code());
                ConfigManager.save();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int totalHeight = filteredLanguages.size() * ENTRY_HEIGHT;
        int visibleHeight = listBottom - listTop;
        int maxScroll = Math.max(0, totalHeight - visibleHeight);

        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) (verticalAmount * ENTRY_HEIGHT)));
        return true;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
