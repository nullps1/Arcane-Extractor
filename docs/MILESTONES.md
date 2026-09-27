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

Status: **complete**.

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

Status: **complete**.

Scope:
- Survival crafting recipe.
- Recipe-book discovery.
- Block loot and correct-tool tags.
- Safe inventory drops when broken.
- Creative-tab visibility.
- Distinct block/item model using vanilla texture assets.
- Success sound and particles.
- Initial balancing without an XP tax.

Exit criteria:
- `./gradlew build` succeeds.
- Recipe loads and crafts one Arcane Extractor.
- Recipe appears in the recipe book after obtaining relevant ingredients.
- Correctly harvested machine drops itself.
- Breaking a machine with stored items drops those items.
- Successful extraction plays sound and particles exactly once.

## M5 — Compatibility hardening

Status: **implemented; PR CI validation pending, live Minecraft release-gate testing remains**.

Scope:
- Generic/data-driven enchantment regression coverage.
- Above-normal level preservation.
- Extra item-component preservation.
- Hardened automation capability boundaries.
- Curse and treasure-enchantment runtime test matrix.
- Modded-enchantment runtime test matrix.
- Dedicated-server, persistence, automation, and multiplayer pre-release plan.

Exit criteria:
- `./gradlew build` succeeds.
- Arbitrary enchantment holders extract without an allowlist.
- Above-normal levels survive exactly.
- Automation cannot steal source/input stacks or insert output.
- Automation may pull completed output.
- Pre-v1 Minecraft test plan is documented and ready to execute.

## v1.0.0

Exit criteria:
- Stable extraction behavior.
- Multiplayer-safe.
- No hardcoded enchantment allowlist required for ordinary compatibility.
- User-facing configuration and compatibility behavior documented.
