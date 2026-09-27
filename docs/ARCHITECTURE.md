# Architecture

## Package root

`dev.nullps1.arcaneextraction`

## Layering

### `extraction`
Pure gameplay/domain logic. It owns candidate discovery, validation, source-copy mutation, and enchanted-book construction.

This layer must not depend on screens or client-only classes.

### `block` / `blockentity`
World representation and inventory persistence. It delegates extraction rules rather than implementing them.

### `menu`
Server-backed container/menu state and slot behavior.

### `client` / `screen`
Rendering and user input only. The client selects an enchantment; the server decides whether the requested extraction is valid.

### `registry`
NeoForge deferred registrations.

## Extraction invariants

1. Inputs are never mutated when validation fails.
2. Exactly one selected enchantment is removed per successful operation.
3. The extracted level is preserved exactly.
4. Unselected enchantments are preserved.
5. Unrelated item data components are preserved.
6. The server is authoritative.
7. Compatibility should be registry/data driven, not based on a hardcoded list of mod IDs or enchantments.

## M7 visual boundary

`ArcaneExtractorBlock` owns horizontal facing and the static collision/outline shapes. The JSON model and original PNGs use ordinary baked block rendering. `noOcclusion` prevents the open stand from hiding neighboring faces. The registered block/block-entity/item/menu IDs and inventory NBT are unchanged; a pre-M7 state with no facing uses the north default.

`ArcaneExtractorScreen` reads the same M6 menu and sends the same registry-ID/all payloads. Its 204x233 procedural medieval UI preserves the M6 slot identities while using roomier screen coordinates while drawing the carved-wood, parchment, blackstone, and amethyst presentation at render time. `ArcaneButton` changes painting only and inherits vanilla button interaction/narration. All rendering imports remain in the client package.

The successful extraction repair-cost reset is the intentional exception to unrelated-component preservation in invariant 5; failures preserve it.
