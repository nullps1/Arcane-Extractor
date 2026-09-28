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

Status: **complete; owner reported the M5/M6 live checklist passed on 2026-09-27**. Exact modpack versions and test evidence were not recorded in this repository; retain the release checklist for the final build.

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

## M6 — Interaction hardening

Status: **complete; owner reported live Minecraft testing passed on 2026-09-27**. M7 must preserve this behavior and receive its own visual acceptance.

Scope:
- Exact enchantment registry-ID selection over a serverbound payload.
- Server-side resolution against the current source stack.
- Six-slot enchanted-book output buffer.
- Consecutive extraction without manually clearing output.
- Extract All workflow.
- Updated automation rules for all buffered outputs.
- GUI layout for the output bank and new controls.
- Regression coverage for output-buffer behavior.

Exit criteria:
- `./gradlew build` succeeds.
- Selecting Sharpness cannot extract Mending or any other different registry ID.
- Stale/missing registry IDs are rejected without mutation.
- Consecutive extractions fill separate output slots.
- Full buffer stops extraction without consuming another book.
- Extract All stops safely on no candidates, no books, or no output capacity.
- Automation can extract from every output slot but still cannot pull source/input stacks.
- Dedicated-server and two-player live checks pass.

## M7 — Visual release

Status: **complete; superseded visually by M7.1/M7.2**.

Scope:
- Original obsidian/amethyst workstation model and five 16x16 material textures.
- Four horizontal facings with matching outline/collision and non-occluding rendering.
- Existing blocks default north when the saved state has no facing; registry IDs and inventory format remain unchanged.
- Original pixel GUI background, inset inventory/output slots, framed enchantment ledger, themed action buttons, selection marker, and full-name hover tooltips.
- Preserve M6 menu slots, payloads, extraction, automation, persistence, recipe, drops, sound, and particles.
- Automated resource/model/shape checks plus the M6 regression suite.

Exit criteria:
- Build/tests and packaged asset checks pass.
- Actual client displays the model and GUI without missing textures or overlapping controls.
- Empty, selected, paged, and full-output GUI states are verified.
- Dedicated server starts without client class linkage failures.
- Owner accepts the final look and completes the M7 checklist in the intended modpack.

See [M7_VISUALS.md](M7_VISUALS.md) for evidence and explicit remaining checks.

## M7.1 — Medieval Magic Retheme

Status: **complete; owner accepted the medieval-magic retheme on 2026-09-27**.

Scope:
- Grindstone-inspired open workstation silhouette.
- Crying obsidian support legs/feet.
- Dark oak structural frame and work shelf.
- Amethyst crystal extraction core with blackstone hardware.
- Carved wood + parchment + amethyst GUI theme.
- Medieval arcane button styling.
- Preserve every M6 gameplay/network/inventory contract.

Exit criteria:
- `./gradlew build` succeeds.
- Model and outline/collision geometry agree for all four facings.
- No missing vanilla texture references or purple/black missing-texture faces appear.
- GUI slots, labels, list selection, paging, Extract, and Extract All remain aligned and functional.
- M6 live behavior remains unchanged.
- Owner accepts the medieval-magic visual direction in the intended modpack.

## M7.2 — Visual polish

Status: **complete; owner accepted the widened GUI and final workstation silhouette on 2026-09-27**.

Scope:
- Widen GUI to 204 pixels for more breathing room.
- Separate Source/Books labels and slots.
- Widen enchantment ledger.
- Expand output/action column.
- Re-center player inventory.
- Strengthen block side silhouette with deeper supports/crystal and a rear wooden brace.
- Preserve every M6 gameplay/network/inventory contract.

Exit criteria:
- `./gradlew build` succeeds.
- All machine/player slots align with the widened visual frames.
- GUI no longer reads as cramped at the owner's normal GUI scale.
- Model and collision/outline geometry agree for all four facings.
- Side profile reads as a balanced workstation rather than a thin slab.
- M6 extraction, Extract All, automation, persistence, and networking remain unchanged.

## v1.0.0

Status: **release ready — 2026-09-27**.

Exit criteria:
- Stable extraction behavior. **Met.**
- Multiplayer-safe. **Met in live testing.**
- No hardcoded enchantment allowlist required for ordinary compatibility. **Met.**
- User-facing configuration and compatibility behavior documented. **Met.**
- Final medieval visual direction accepted. **Met.**
- Release version pinned to `1.0.0`. **Met.**

Release note: the owner reported the final local visual/gameplay pass successful on 2026-09-27. The configured release target is Minecraft 1.21.1 / NeoForge 21.1.252 / Java 21. The exact modpack revision/hash was not recorded in-repo and should be captured when the final JAR is deployed.
