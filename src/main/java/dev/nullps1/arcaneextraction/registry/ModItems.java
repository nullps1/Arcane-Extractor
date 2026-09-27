package dev.nullps1.arcaneextraction.registry;

import dev.nullps1.arcaneextraction.ArcaneExtraction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ArcaneExtraction.MOD_ID);

    public static final DeferredItem<BlockItem> ARCANE_EXTRACTOR =
            ITEMS.register("arcane_extractor",
                    () -> new BlockItem(ModBlocks.ARCANE_EXTRACTOR.get(), new Item.Properties()));

    private ModItems() {}

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
