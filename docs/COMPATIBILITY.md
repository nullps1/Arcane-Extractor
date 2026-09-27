# Compatibility

Arcane Extraction targets Minecraft 1.21.1 on NeoForge.

## Compatibility contract

Arcane Extraction reads the source item's `DataComponents.ENCHANTMENTS` component and writes the selected enchantment to `DataComponents.STORED_ENCHANTMENTS` on an enchanted book.

The extractor does not maintain:
- a vanilla-enchantment allowlist;
- a mod ID allowlist;
- a maximum-level clamp;
- a curse blacklist;
- a treasure-enchantment blacklist.

If an enchantment exists in the source item's ordinary enchantment component, Arcane Extraction treats it as a candidate and preserves its stored integer level exactly.

For single extraction, the client sends the selected enchantment's registry ID rather than a GUI row index. The server resolves that exact ID against the current source item before extraction. Candidate ordering is therefore presentation-only and cannot change which enchantment is removed.

A successful extraction intentionally clears the source item's anvil prior-work penalty (`DataComponents.REPAIR_COST`). This prevents repeated extract/re-enchant cycles from inheriting an ever-growing anvil cost. Failed extraction attempts do not alter the repair cost.

## Automated M5 coverage

The test suite verifies:
- arbitrary synthetic/data-driven enchantments require no hardcoded ID;
- levels above an enchantment's declared normal maximum are preserved exactly;
- custom name and damage survive extraction;
- anvil prior-work penalty (`REPAIR_COST`) is intentionally reset after a successful extraction;
- external automation may insert valid source items and ordinary books;
- external automation cannot insert directly into the output;
- external automation cannot extract source items or input books;
- external automation can extract completed output from any of the six output-buffer slots;
- simulated automation inserts do not mutate machine state.

## Runtime-only validation

Some behaviors require a real registry/world and are intentionally validated in Minecraft rather than mocked in unit tests:

- vanilla treasure enchantments such as Mending;
- vanilla curses such as Curse of Binding and Curse of Vanishing;
- mod-added enchantments from the actual server pack;
- items with more than four enchantments to exercise UI paging;
- dedicated-server menu opening and extraction;
- hopper/pipe/item-handler interaction;
- save/reload and chunk unload/reload;
- block breaking with populated inventory.

## Other mods

Compatibility is data-driven by default. Another mod should not require explicit Arcane Extraction support when it stores enchantments using Minecraft's normal 1.21.1 item enchantment components.

Apothic Enchanting and other enchantment mods are therefore runtime validation targets, not compile-time dependencies. If a mod uses a separate custom storage mechanism instead of the normal item enchantment component, dedicated integration may be required.
