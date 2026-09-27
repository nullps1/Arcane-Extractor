package dev.nullps1.arcaneextraction.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/** Keeps vanilla click, keyboard and narration behavior with a medieval arcane palette. */
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

        int outer = 0xFF24150E;
        int rim = !active ? 0xFF55483C : isHoveredOrFocused() ? 0xFFD8B8E9 : 0xFF7D654D;
        int face = !active ? 0xFF3B312A
                : isHoveredOrFocused() ? 0xFF67457B
                : primary ? 0xFF563968 : 0xFF4A3426;

        graphics.fill(x, y, x + width, y + height, outer);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, rim);
        graphics.fill(x + 2, y + 2, x + width - 2, y + height - 2, face);

        // Carved lower lip and tiny amethyst studs.
        graphics.fill(x + 2, y + height - 4, x + width - 2, y + height - 2, 0xFF2A1B14);
        if (active) {
            graphics.fill(x + 4, y + 4, x + 6, y + 6, 0xFF9D6CC5);
            graphics.fill(x + width - 6, y + 4, x + width - 4, y + 6, 0xFF9D6CC5);
        }

        renderScrollingString(graphics, Minecraft.getInstance().font, 5,
                active ? 0xFFF2E2BD : 0xFFA99C8E);
    }
}
