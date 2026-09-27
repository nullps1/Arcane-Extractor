package dev.nullps1.arcaneextraction.client.screen;

import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorInventory;
import dev.nullps1.arcaneextraction.extraction.EnchantmentExtractor;
import dev.nullps1.arcaneextraction.extraction.ExtractionCandidate;
import dev.nullps1.arcaneextraction.menu.ArcaneExtractorMenu;
import dev.nullps1.arcaneextraction.network.ExtractionRequestPayload;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ArcaneExtractorScreen extends AbstractContainerScreen<ArcaneExtractorMenu> {
    private static final int LIST_X = 8;
    private static final int LIST_Y = 69;
    private static final int LIST_WIDTH = 90;
    private static final int ROW_HEIGHT = 12;
    private static final int ROWS_PER_PAGE = 4;

    private String selectedEnchantmentId;
    private int pageStart = 0;
    private Button extractButton;
    private Button extractAllButton;
    private Button previousButton;
    private Button nextButton;

    public ArcaneExtractorScreen(
            ArcaneExtractorMenu menu,
            Inventory inventory,
            Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 233;
        inventoryLabelY = 139;
    }

    @Override
    protected void init() {
        super.init();

        extractButton = addRenderableWidget(Button.builder(
                        Component.translatable("screen.arcane_extraction.extract"),
                        button -> requestExtraction())
                .bounds(leftPos + 103, topPos + 69, 65, 20)
                .build());

        extractAllButton = addRenderableWidget(Button.builder(
                        Component.translatable("screen.arcane_extraction.extract_all"),
                        button -> requestExtractAll())
                .bounds(leftPos + 103, topPos + 93, 65, 20)
                .build());

        previousButton = addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> changePage(-ROWS_PER_PAGE))
                .bounds(leftPos + 103, topPos + 117, 30, 20)
                .build());

        nextButton = addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> changePage(ROWS_PER_PAGE))
                .bounds(leftPos + 138, topPos + 117, 30, 20)
                .build());

        refreshControls();
    }

    private void requestExtraction() {
        if (selectedEnchantmentId == null || selectedEnchantmentId.isBlank()) {
            return;
        }

        PacketDistributor.sendToServer(
                ExtractionRequestPayload.single(menu.containerId, selectedEnchantmentId));
        selectedEnchantmentId = null;
        refreshControls();
    }

    private void requestExtractAll() {
        if (menu.candidates().isEmpty()) {
            return;
        }

        PacketDistributor.sendToServer(ExtractionRequestPayload.all(menu.containerId));
        selectedEnchantmentId = null;
        refreshControls();
    }

    private void changePage(int delta) {
        int size = menu.candidates().size();
        pageStart = Math.max(0, Math.min(pageStart + delta, lastPageStart(size)));
        refreshControls();
    }

    private int lastPageStart(int size) {
        if (size <= ROWS_PER_PAGE) {
            return 0;
        }
        return ((size - 1) / ROWS_PER_PAGE) * ROWS_PER_PAGE;
    }

    private String registryId(ExtractionCandidate candidate) {
        return EnchantmentExtractor.registryId(candidate.enchantment()).orElse("");
    }

    private void refreshControls() {
        List<ExtractionCandidate> candidates = menu.candidates();
        int size = candidates.size();

        if (selectedEnchantmentId != null
                && candidates.stream().noneMatch(candidate ->
                        selectedEnchantmentId.equals(registryId(candidate)))) {
            selectedEnchantmentId = null;
        }

        if (pageStart >= size && size > 0) {
            pageStart = lastPageStart(size);
        }
        if (size == 0) {
            pageStart = 0;
        }

        if (extractButton != null) {
            extractButton.active = selectedEnchantmentId != null;
        }
        if (extractAllButton != null) {
            extractAllButton.active = !candidates.isEmpty();
        }
        if (previousButton != null) {
            previousButton.active = pageStart > 0;
        }
        if (nextButton != null) {
            nextButton.active = pageStart + ROWS_PER_PAGE < size;
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF1C1724);
        graphics.fill(leftPos + 7, topPos + 17, leftPos + 169, topPos + 137, 0xFF2C2238);

        drawSlot(graphics, 19, 34);
        drawSlot(graphics, 55, 34);

        for (int index = 0; index < ArcaneExtractorInventory.OUTPUT_COUNT; index++) {
            int column = index % 3;
            int row = index / 3;
            drawSlot(graphics, 115 + column * 18, 25 + row * 18);
        }

        graphics.drawCenteredString(font, "→", leftPos + 45, topPos + 39, 0xFFE0D7F0);
        graphics.drawCenteredString(font, "→", leftPos + 92, topPos + 39, 0xFFE0D7F0);
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
        int end = Math.min(candidates.size(), pageStart + ROWS_PER_PAGE);

        for (int index = pageStart; index < end; index++) {
            int row = index - pageStart;
            int y = LIST_Y + row * ROW_HEIGHT;
            ExtractionCandidate candidate = candidates.get(index);
            String candidateId = registryId(candidate);

            if (!candidateId.isEmpty() && candidateId.equals(selectedEnchantmentId)) {
                graphics.fill(LIST_X - 2, y - 1, LIST_X + LIST_WIDTH, y + 10, 0xFF604879);
            }

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

        if (candidates.size() > ROWS_PER_PAGE) {
            int page = pageStart / ROWS_PER_PAGE + 1;
            int pages = (candidates.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
            graphics.drawString(
                    font,
                    Component.translatable("screen.arcane_extraction.page", page, pages),
                    104,
                    140,
                    0xFFAFA2BC,
                    false);
        }

        refreshControls();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int relativeX = (int) mouseX - leftPos;
        int relativeY = (int) mouseY - topPos;
        List<ExtractionCandidate> candidates = menu.candidates();

        if (relativeX >= LIST_X && relativeX < LIST_X + LIST_WIDTH
                && relativeY >= LIST_Y
                && relativeY < LIST_Y + ROWS_PER_PAGE * ROW_HEIGHT) {
            int row = (relativeY - LIST_Y) / ROW_HEIGHT;
            int index = pageStart + row;
            if (index < candidates.size()) {
                String candidateId = registryId(candidates.get(index));
                selectedEnchantmentId = candidateId.isEmpty() ? null : candidateId;
                refreshControls();
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
