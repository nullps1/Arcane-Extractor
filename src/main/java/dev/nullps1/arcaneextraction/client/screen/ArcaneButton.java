package dev.nullps1.arcaneextraction.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/** Keeps vanilla click, keyboard and narration behavior with the workstation palette. */
final class ArcaneButton extends Button {
    private final boolean primary;

    ArcaneButton(int x, int y, int width, Component text, OnPress onPress, boolean primary) {
        super(x, y, width, 20, text, onPress, DEFAULT_NARRATION);
        this.primary = primary;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        int border = !active ? 0xFF514858 : isHoveredOrFocused() ? 0xFFE5C7FF : 0xFF9984AC;
        int background = !active ? 0xFF27222F : isHoveredOrFocused() ? 0xFF614979
                : primary ? 0xFF513762 : 0xFF382C44;
        graphics.fill(x, y, x + width, y + height, 0xFF100D17);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, border);
        graphics.fill(x + 2, y + 2, x + width - 2, y + height - 2, background);
        graphics.fill(x + 2, y + height - 3, x + width - 2, y + height - 2, 0xFF231B2B);
        renderScrollingString(graphics, Minecraft.getInstance().font, 4,
                active ? 0xFFF3E6FC : 0xFFAEA2B8);
    }
}
