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
