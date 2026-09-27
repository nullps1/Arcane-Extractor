# Milestones

## M0 — Repository bootstrap

Status: **implemented; build verification pending on a networked Gradle environment**.

Scope:
- NeoForge 1.21.1 project skeleton.
- Java 21 toolchain.
- Stable mod ID/package naming.
- GitHub Actions build workflow.
- Documentation baseline.

Exit criteria:
- `./gradlew build` succeeds.
- CI succeeds on a clean checkout.
- Mod loads in a NeoForge 1.21.1 development client and dedicated server.

## M1 — Extraction engine

Scope:
- Read enchantments from a source `ItemStack`.
- Represent selectable extraction candidates.
- Remove exactly one selected enchantment.
- Produce an enchanted book with the exact original level.
- Preserve every unrelated source-item component.
- Reject invalid requests without mutating inputs.
- Focused automated tests/GameTests.

## M2 — Arcane Extractor block

Scope:
- Block + block item.
- Block entity.
- Source/book/output inventory contract.
- Persistence.
- Server-owned extraction execution.

## M3 — Menu and GUI

Scope:
- Enchantment list.
- Selection state.
- Extract action.
- Server validation of client requests.

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
