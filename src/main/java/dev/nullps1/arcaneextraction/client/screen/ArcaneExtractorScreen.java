package dev.nullps1.arcaneextraction.client.screen;

import dev.nullps1.arcaneextraction.extraction.ExtractionCandidate;
import dev.nullps1.arcaneextraction.menu.ArcaneExtractorMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ArcaneExtractorScreen extends AbstractContainerScreen<ArcaneExtractorMenu> {
    private static final int LIST_X = 8;
    private static final int LIST_Y = 58;
    private static final int ROW_HEIGHT = 12;
    private static final int MAX_VISIBLE_ROWS = 3;

    private int selectedIndex = -1;
    private Button extractButton;

    public ArcaneExtractorScreen(
            ArcaneExtractorMenu menu,
            Inventory inventory,
            Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 186;
        inventoryLabelY = 92;
    }

    @Override
    protected void init() {
        super.init();
        extractButton = addRenderableWidget(Button.builder(
                        Component.translatable("screen.arcane_extraction.extract"),
                        button -> requestExtraction())
                .bounds(leftPos + 103, topPos + 58, 65, 20)
                .build());
        refreshButtonState();
    }

    private void requestExtraction() {
        List<ExtractionCandidate> candidates = menu.candidates();
        if (selectedIndex < 0 || selectedIndex >= candidates.size() || minecraft == null
                || minecraft.gameMode == null) {
            return;
        }

        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, selectedIndex);
        selectedIndex = -1;
        refreshButtonState();
    }

    private void refreshButtonState() {
        if (extractButton != null) {
            int size = menu.candidates().size();
            if (selectedIndex >= size) {
                selectedIndex = -1;
            }
            extractButton.active = selectedIndex >= 0 && selectedIndex < size;
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF1C1724);
        graphics.fill(leftPos + 7, topPos + 17, leftPos + 169, topPos + 84, 0xFF2C2238);

        drawSlot(graphics, 25, 34);
        drawSlot(graphics, 61, 34);
        drawSlot(graphics, 133, 34);

        graphics.drawCenteredString(font, "→", leftPos + 51, topPos + 39, 0xFFE0D7F0);
        graphics.drawCenteredString(font, "→", leftPos + 113, topPos + 39, 0xFFE0D7F0);
    }

    private void drawSlot(GuiGraphics graphics, int x, int y) {
        graphics.fill(leftPos + x, topPos + y, leftPos + x + 18, topPos + y + 18, 0xFF665577);
        graphics.fill(leftPos + x + 1, topPos + y + 1, leftPos + x + 17, topPos + y + 17, 0xFF211A2A);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0xFFE8DFF2, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xFFD0C7DA, false);

        List<ExtractionCandidate> candidates = menu.candidates();
        int visible = Math.min(candidates.size(), MAX_VISIBLE_ROWS);
        for (int i = 0; i < visible; i++) {
            int y = LIST_Y + i * ROW_HEIGHT;
            if (i == selectedIndex) {
                graphics.fill(LIST_X - 2, y - 1, 96, y + 10, 0xFF604879);
            }
            ExtractionCandidate candidate = candidates.get(i);
            Component name = Enchantment.getFullname(candidate.enchantment(), candidate.level());
            graphics.drawString(font, name, LIST_X, y, 0xFFE9E0F2, false);
        }

        if (candidates.isEmpty()) {
            graphics.drawString(
                    font,
                    Component.translatable("screen.arcane_extraction.no_enchantments"),
                    LIST_X,
                    LIST_Y,
                    0xFF9F93AA,
                    false);
        }

        refreshButtonState();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int relativeX = (int) mouseX - leftPos;
        int relativeY = (int) mouseY - topPos;
        List<ExtractionCandidate> candidates = menu.candidates();

        if (relativeX >= LIST_X && relativeX < 98
                && relativeY >= LIST_Y
                && relativeY < LIST_Y + MAX_VISIBLE_ROWS * ROW_HEIGHT) {
            int index = (relativeY - LIST_Y) / ROW_HEIGHT;
            if (index < candidates.size()) {
                selectedIndex = index;
                refreshButtonState();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
