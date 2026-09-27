package dev.nullps1.arcaneextraction.registry;

import dev.nullps1.arcaneextraction.ArcaneExtraction;
import dev.nullps1.arcaneextraction.menu.ArcaneExtractorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ArcaneExtraction.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneExtractorMenu>> ARCANE_EXTRACTOR =
            MENUS.register("arcane_extractor",
                    () -> IMenuTypeExtension.create(ArcaneExtractorMenu::new));

    private ModMenus() {}

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
