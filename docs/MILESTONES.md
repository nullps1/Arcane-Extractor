# Milestones

## M0 — Repository bootstrap

Status: **complete**.

Scope:
- NeoForge 1.21.1 project skeleton.
- Java 21 toolchain.
- Stable mod ID/package naming.
- GitHub Actions build workflow.
- Documentation baseline.

## M1 — Extraction engine

Status: **complete**.

Scope:
- Read enchantments from a source `ItemStack`.
- Represent selectable extraction candidates.
- Remove exactly one selected enchantment.
- Produce an enchanted book with the exact original level.
- Preserve unrelated source-item components through `ItemStack.copy()`.
- Reject invalid requests without mutating inputs.
- Focused JUnit coverage.

Exit criteria:
- `./gradlew build` succeeds.
- Candidate discovery returns every source enchantment and exact level.
- Successful extraction removes only the selected enchantment.
- Output enchanted book stores exactly the selected enchantment and level.
- Original input stack remains unchanged.
- Failure paths are typed and non-mutating.

## M2 — Arcane Extractor block

Status: **complete**.

Scope:
- Block + block item.
- Block entity.
- Source/book/output inventory contract.
- NBT persistence.
- NeoForge item-handler capability exposure.
- Server-owned extraction execution.
- Focused transaction tests.

Exit criteria:
- `./gradlew build` succeeds.
- Block and block entity register cleanly.
- Inventory persists through the block entity's NBT lifecycle.
- A successful transaction consumes exactly one book and writes exactly one enchanted-book output.
- Missing book or occupied output causes no partial mutation.
- Output slot rejects insertion.

## M3 — Menu and GUI

Status: **implemented; PR CI and runtime validation pending**.

Scope:
- Registered server-backed menu.
- Source/book/output and player inventory slots.
- Shift-click routing.
- Client-only selectable enchantment list with paging.
- Extract button using vanilla menu-button networking.
- Server-side candidate-index validation before extraction.

Exit criteria:
- `./gradlew build` succeeds.
- Right-click opens the menu on an integrated and dedicated server.
- Inserting enchanted gear updates the candidate list.
- Every candidate remains reachable through paging.
- Clicking Extract invokes the M2 transaction for the server's current candidate at that index.
- Invalid/stale indices do not mutate inventory.
- Dedicated server loads without client-class linkage failures.

## M4 — Survival integration

Scope:
- Recipe.
- Models/textures.
- Loot table.
- Initial balancing.

## M5 — Compatibility hardening

Scope:
- Vanilla enchantments.
- Modded/data-driven enchantments.
- Treasure enchantments.
- Curses.
- Above-vanilla enchantment levels.
- Dedicated-server validation.

## v1.0.0

Exit criteria:
- Stable extraction behavior.
- Multiplayer-safe.
- No hardcoded enchantment allowlist required for ordinary compatibility.
- User-facing configuration and compatibility behavior documented.
