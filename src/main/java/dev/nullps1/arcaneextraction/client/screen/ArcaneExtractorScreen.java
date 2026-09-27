package dev.nullps1.arcaneextraction.client.screen;

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
    private static final int LIST_X = 11;
    private static final int LIST_Y = 70;
    private static final int LIST_WIDTH = 104;
    private static final int ROW_HEIGHT = 12;
    private static final int ROWS_PER_PAGE = 4;

    private static final int WOOD_DARK = 0xFF2A180F;
    private static final int WOOD = 0xFF4A2B1B;
    private static final int WOOD_LIGHT = 0xFF6B4329;
    private static final int BLACKSTONE = 0xFF272329;
    private static final int BLACKSTONE_LIGHT = 0xFF514A53;
    private static final int PARCHMENT = 0xFFD7C69A;
    private static final int PARCHMENT_DARK = 0xFFB29B6D;
    private static final int INK = 0xFF2B2118;
    private static final int AMETHYST = 0xFF9D6CC5;
    private static final int AMETHYST_LIGHT = 0xFFD9B7F2;

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
        imageWidth = 204;
        imageHeight = 233;
        inventoryLabelY = 139;
    }

    @Override
    protected void init() {
        super.init();

        extractButton = addRenderableWidget(new ArcaneButton(leftPos + 127, topPos + 69, 69,
                Component.translatable("screen.arcane_extraction.extract"), button -> requestExtraction(), true));
        extractAllButton = addRenderableWidget(new ArcaneButton(leftPos + 127, topPos + 93, 69,
                Component.translatable("screen.arcane_extraction.extract_all"), button -> requestExtractAll(), false));
        previousButton = addRenderableWidget(new ArcaneButton(leftPos + 127, topPos + 117, 32,
                Component.literal("<"), button -> changePage(-ROWS_PER_PAGE), false));
        nextButton = addRenderableWidget(new ArcaneButton(leftPos + 166, topPos + 117, 30,
                Component.literal(">"), button -> changePage(ROWS_PER_PAGE), false));

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
        int x = leftPos;
        int y = topPos;

        // Carved dark-oak outer frame.
        graphics.fill(x, y, x + imageWidth, y + imageHeight, WOOD_DARK);
        graphics.fill(x + 2, y + 2, x + imageWidth - 2, y + imageHeight - 2, WOOD_LIGHT);
        graphics.fill(x + 4, y + 4, x + imageWidth - 4, y + imageHeight - 4, WOOD);

        // Amethyst corner inlays and title rail.
        graphics.fill(x + 6, y + 6, x + 10, y + 10, AMETHYST);
        graphics.fill(x + imageWidth - 10, y + 6, x + imageWidth - 6, y + 10, AMETHYST);
        graphics.fill(x + 10, y + 11, x + imageWidth - 10, y + 13, WOOD_DARK);
        graphics.fill(x + 14, y + 12, x + imageWidth - 14, y + 13, AMETHYST);

        // Work area: parchment ledger left, dark output altar right.
        drawParchmentPanel(graphics, x + 7, y + 18, x + 119, y + 136);
        drawStonePanel(graphics, x + 121, y + 18, x + 197, y + 136);

        // Enchantment ledger inset.
        graphics.fill(x + 8, y + 64, x + 118, y + 123, PARCHMENT_DARK);
        graphics.fill(x + 10, y + 66, x + 116, y + 121, PARCHMENT);

        // Decorative rune divider.
        graphics.fill(x + 119, y + 24, x + 121, y + 132, WOOD_DARK);
        graphics.fill(x + 120, y + 38, x + 121, y + 50, AMETHYST);
        graphics.fill(x + 120, y + 82, x + 121, y + 94, AMETHYST);

        // Machine slots.
        drawSlotFrame(graphics, x + 27, y + 34);
        drawSlotFrame(graphics, x + 71, y + 34);
        for (int index = 0; index < 6; index++) {
            int column = index % 3;
            int row = index / 3;
            drawSlotFrame(graphics, x + 135 + column * 18, y + 25 + row * 18);
        }

        // Arcane transfer marks.
        graphics.fill(x + 50, y + 41, x + 61, y + 43, AMETHYST);
        graphics.fill(x + 59, y + 39, x + 62, y + 45, AMETHYST_LIGHT);
        graphics.fill(x + 94, y + 41, x + 125, y + 43, AMETHYST);
        graphics.fill(x + 122, y + 39, x + 126, y + 45, AMETHYST_LIGHT);

        // Inventory parchment tray.
        drawParchmentPanel(graphics, x + 18, y + 145, x + 186, y + 229);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlotFrame(graphics, x + 20 + column * 18, y + 150 + row * 18);
            }
        }
        for (int column = 0; column < 9; column++) {
            drawSlotFrame(graphics, x + 20 + column * 18, y + 208);
        }
    }

    private void drawParchmentPanel(GuiGraphics graphics, int left, int top, int right, int bottom) {
        graphics.fill(left, top, right, bottom, WOOD_DARK);
        graphics.fill(left + 1, top + 1, right - 1, bottom - 1, PARCHMENT_DARK);
        graphics.fill(left + 3, top + 3, right - 3, bottom - 3, PARCHMENT);
    }

    private void drawStonePanel(GuiGraphics graphics, int left, int top, int right, int bottom) {
        graphics.fill(left, top, right, bottom, WOOD_DARK);
        graphics.fill(left + 1, top + 1, right - 1, bottom - 1, BLACKSTONE_LIGHT);
        graphics.fill(left + 3, top + 3, right - 3, bottom - 3, BLACKSTONE);
    }

    private void drawSlotFrame(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x + 18, y + 18, WOOD_DARK);
        graphics.fill(x + 1, y + 1, x + 17, y + 17, BLACKSTONE_LIGHT);
        graphics.fill(x + 2, y + 2, x + 16, y + 16, 0xFF171419);
        graphics.fill(x + 2, y + 2, x + 15, y + 3, 0xFF665A68);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0xFFF0DDB1, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, INK, false);
        graphics.drawString(font, Component.translatable("screen.arcane_extraction.source"), 21, 24, INK, false);
        graphics.drawString(font, Component.translatable("screen.arcane_extraction.books"), 66, 24, INK, false);
        graphics.drawString(font, Component.translatable("screen.arcane_extraction.output"), 136, 15, 0xFFE8D6F2, false);
        graphics.drawString(font, Component.translatable("screen.arcane_extraction.enchantments"), 11, 57, INK, false);

        List<ExtractionCandidate> candidates = menu.candidates();
        int end = Math.min(candidates.size(), pageStart + ROWS_PER_PAGE);

        for (int index = pageStart; index < end; index++) {
            int row = index - pageStart;
            int y = LIST_Y + row * ROW_HEIGHT;
            ExtractionCandidate candidate = candidates.get(index);
            String candidateId = registryId(candidate);

            if (!candidateId.isEmpty() && candidateId.equals(selectedEnchantmentId)) {
                graphics.fill(LIST_X - 2, y - 1, LIST_X + LIST_WIDTH, y + 10, 0xFFB79ACB);
                graphics.fill(LIST_X - 2, y - 1, LIST_X, y + 10, AMETHYST);
            }

            Component name = Enchantment.getFullname(candidate.enchantment(), candidate.level());
            graphics.enableScissor(leftPos + LIST_X, topPos + y,
                    leftPos + LIST_X + LIST_WIDTH, topPos + y + 10);
            graphics.drawString(font, name, LIST_X, y, INK, false);
            graphics.disableScissor();
        }

        if (candidates.isEmpty()) {
            graphics.drawWordWrap(font, Component.translatable("screen.arcane_extraction.no_enchantments"),
                    LIST_X, LIST_Y, LIST_WIDTH, 0xFF6B5841);
        }

        if (candidates.size() > ROWS_PER_PAGE) {
            int page = pageStart / ROWS_PER_PAGE + 1;
            int pages = (candidates.size() + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
            graphics.drawString(
                    font,
                    Component.translatable("screen.arcane_extraction.page", page, pages),
                    LIST_X,
                    125,
                    0xFF66533D,
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
        refreshControls();
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        int x = mouseX - leftPos;
        int y = mouseY - topPos;
        List<ExtractionCandidate> candidates = menu.candidates();
        if (x >= LIST_X && x < LIST_X + LIST_WIDTH && y >= LIST_Y
                && y < LIST_Y + ROWS_PER_PAGE * ROW_HEIGHT) {
            int index = pageStart + (y - LIST_Y) / ROW_HEIGHT;
            if (index < candidates.size()) {
                ExtractionCandidate candidate = candidates.get(index);
                graphics.renderTooltip(font,
                        Enchantment.getFullname(candidate.enchantment(), candidate.level()), mouseX, mouseY);
            }
        }
    }
}
