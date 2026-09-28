# Changelog

## v1.0.0 — 2026-09-27

### Release
- First stable Arcane Extraction release for Minecraft 1.21.1 / NeoForge 21.1.252.
- Server-authoritative extraction by exact enchantment registry ID.
- One-book-per-enchantment extraction with exact level preservation.
- Six-slot enchanted-book output buffer and Extract All.
- Generic support for normal vanilla/modded/data-driven enchantments stored in Minecraft's standard enchantment component.
- Successful extraction clears the source item's existing anvil prior-work penalty while preserving unrelated item state.
- Automation can insert valid source/books and pull completed outputs without stealing machine inputs.
- Persistent block entity inventory, legacy three-slot migration, survival recipe, loot, creative-tab entry, sounds, and particles.
- Multiplayer, dedicated-server, persistence, automation, curse/treasure/modded enchantments, stale selection, and output-buffer behavior were exercised during pre-release testing.
- Final visuals use the owner-approved medieval magical workstation direction: crying obsidian supports, dark oak frame, blackstone hardware, amethyst core, and a widened carved-wood/parchment GUI.

### Release acceptance
- M6 gameplay checklist: passed by owner on 2026-09-27.
- M7.2 final visual pass: passed by owner on 2026-09-27.
- Release version: `1.0.0`.
- Target: Minecraft 1.21.1, NeoForge 21.1.252, Java 21.
- Exact final modpack revision/hash was not recorded in the repository; record it alongside the deployed release artifact.

## Unreleased

### M7.2 — Visual polish — 2026-09-27

#### Changed
- Widened the Arcane Extractor GUI from 176 to 204 pixels to reduce crowding.
- Increased separation between Source and Books, widened the enchantment ledger, and gave the output/actions their own roomier column.
- Re-centered the player inventory beneath the wider workstation interface.
- Kept all eight machine slots and 36 player slots functionally identical while moving only their screen coordinates.
- Thickened the amethyst core/support depth and added a short rear dark-oak brace so the workstation has a stronger side silhouette.
- Updated collision geometry to match the revised block model.

#### Why
Owner visual testing approved the medieval-magic theme but found the GUI cramped and the block side profile too thin. M7.2 is a focused spacing/silhouette pass with no gameplay changes.

#### Validation
- Existing visual tests continue to compare model geometry against collision/outline geometry for all four facings.
- Existing M1–M6 tests remain the gameplay regression gate.
- Final acceptance requires a local visual pass at the owner's normal GUI scale.

#### Next
Run the M7.2 visual acceptance pass. If accepted, freeze visuals and prepare v1.0.0.

### M7.1 — Medieval Magic Retheme — 2026-09-27

#### Changed
- Replaced the industrial standing-apparatus visual language with a grindstone-inspired medieval magical workstation.
- The block now uses crying obsidian feet/supports, a dark-oak frame and work shelf, polished-blackstone hardware, and an amethyst crystal extraction core.
- Preserved four horizontal facings and open workstation geometry while updating collision/outline shapes to match the new model.
- Replaced the industrial GUI presentation with a procedural carved-wood, parchment, blackstone, and amethyst theme.
- Rethemed action buttons as carved arcane tablets while preserving vanilla button interaction, narration, and all M6 controls.
- Preserved the existing M6 slot positions, six-slot output buffer, exact registry-ID selection, Extract All, automation, persistence, recipe, sound, particles, and networking.

#### Why
Owner visual testing found the M7 art direction polished but too modern/industrial. M7.1 shifts the same proven interface toward a vanilla-adjacent medieval magic identity: a wizard-artisan workstation rather than an arcane machine.

#### Validation
- Updated the visual regression contract for vanilla material texture references and the new model geometry.
- Existing M1–M6 functional tests remain the gameplay regression gate.
- PR CI is the clean-environment build/test gate.
- Final acceptance requires an in-game visual pass at normal GUI scale and placement from all four directions.

#### Next
Run the M7.1 visual acceptance checklist. If accepted, proceed to v1.0.0 release packaging.

### M7 — 2026-09-27

#### Changed
- Replaced the placeholder cube with an original standing obsidian workstation: stone plinth, side supports, input shelf, engraved upright housing, and an amethyst focus.
- Added five original 16x16 block textures and the inherited 3D item model.
- Added horizontal placement facing, structure rotation/mirroring, and model-matched outline/collision shapes. Existing saved machines without a facing default north.
- Added a 256x256 GUI atlas with a 176x233 visible panel, source/book labels, output shelf, enchantment ledger, and all player inventory/hotbar slot frames.
- Added themed action buttons with active, disabled, hover, and focus states. Selected rows have a bright edge as well as a color highlight.
- Constrained long enchantment names to the list panel, retained curse styling, exposed full names on hover, and wrapped the empty-state prompt.
- Added model/texture/shape regression tests and a reproducible original-art generator.
- Refreshed the README and milestone status to reflect implemented gameplay and the owner's reported M6 acceptance.

#### Compatibility
- M6 slot coordinates/order, network payloads, registry IDs, save inventory, extraction rules, automation, recipe, loot, and success feedback are unchanged.
- No external art packs, renderer library, emissive shader, or runtime asset generator is required.
- See [M7_VISUALS.md](M7_VISUALS.md) for validation evidence and remaining live release gates. The snapshot version remains unchanged pending final release acceptance.

### M6 — 2026-09-27

#### Changed
- Replaced candidate-index extraction requests with a custom serverbound payload carrying the selected enchantment registry ID.
- Server-side extraction now resolves that exact registry ID against the current authoritative source stack before mutating inventory.
- Replaced the single output slot with a six-slot output buffer.
- Repeated extraction now writes each enchanted book into the first free output slot.
- Added an **Extract All** action that repeatedly extracts from the current source until no candidates remain, input books run out, or the six-slot output buffer fills.
- Updated automation exposure so external systems may pull from any output-buffer slot while input/source protection remains unchanged.
- Updated the GUI to display a 3x2 output bank alongside separate Extract and Extract All controls.
- Added legacy inventory migration so existing 3-slot Arcane Extractors expand to the new 8-slot layout without losing their old source, book, or output contents.
- Added regression tests for repeated buffered extraction, full-buffer rejection, and extraction from any automation-visible output slot.

#### Why
Live pre-v1 testing found that client/server candidate ordering could diverge, allowing a selected row such as Sharpness to remove a different enchantment such as Mending. Live testing also showed that requiring the single output slot to be manually cleared after every extraction made multi-enchantment rework unnecessarily tedious.

#### Validation
- Exact registry identity is now the selection contract; list order is presentation-only.
- Full-output and missing-book failures remain non-mutating.
- M6 CI is the compile/test gate before renewed live Minecraft testing.
- The anvil prior-work reset from the pre-v1 repair-cost fix remains unchanged; vanilla/anvil-mod prior-work accumulation after applying new books is outside the extractor transaction.

#### Next
Repeat the pre-v1 Minecraft test plan with special focus on exact selected-enchantment identity, repeated extraction, Extract All, output-buffer persistence, automation, and multiplayer concurrency.

### Pre-v1 repair-cost regression fix — 2026-09-27

#### Changed
- Successful extraction now clears `DataComponents.REPAIR_COST` on the returned source item.
- Failed extraction attempts preserve the original repair cost.
- Updated compatibility tests and documentation to reflect the intended anvil prior-work reset.

#### Why
Live Minecraft testing showed that preserving the source item's prior-work penalty caused anvil costs to grow to extreme values after stripping and reapplying enchantments. Resetting the penalty on successful extraction keeps the machine's "rework" behavior usable without affecting failed requests or unrelated item state.

#### Validation
- Added regression coverage for repair-cost reset on success.
- Added regression coverage proving failed extraction does not reset repair cost.
- PR CI is the clean-environment validation gate before merge.

### M5 — 2026-09-27

#### Changed
- Added compatibility tests proving arbitrary/data-driven enchantments require no hardcoded allowlist.
- Added exact preservation tests for above-normal enchantment levels.
- Expanded component-preservation coverage to include custom name, damage, and repair cost.
- Added a restricted automation item-handler wrapper.
- External automation may insert valid source items/books and extract completed output, but may not extract input/source stacks or insert directly into the output.
- Added automation simulation tests to guard against mutation during capability probing.
- Expanded compatibility documentation around curses, treasure enchantments, modded enchantments, and custom storage mechanisms.
- Added a full pre-v1 Minecraft test plan covering dedicated server, multiplayer, persistence, block breaking, automation, stale selections, and real modpack enchantments.

#### Why
M5 moves Arcane Extraction from feature-complete to release-candidate hardening. The core extractor is already generic, so this milestone focuses on proving that assumption, locking down automation boundaries, and defining the live test gates required before v1.0.0.

#### Validation
- M4 is merged and CI-validated.
- M5 adds automated compatibility and automation regression coverage.
- PR CI is the authoritative clean-environment compile/test validation.
- Real curse, treasure, modded-enchantment, dedicated-server, and multiplayer behavior remains intentionally gated by the pre-release Minecraft test plan.

#### Limitations
- Arcane Extraction only sees enchantments stored in Minecraft's normal item enchantment component.
- Mods that store enchantments in a separate custom capability/component may require explicit integration.
- v1.0.0 is not ready until the live Minecraft test plan passes.

#### Next
Run the pre-v1 Minecraft test plan on the actual NeoForge 1.21.1 server pack, fix any failures, then prepare v1.0.0.

### M4 — 2026-09-27

#### Changed
- Added a shaped survival recipe for the Arcane Extractor using four crying obsidian, four amethyst shards, and one enchanting table.
- Added recipe-book unlock advancement data.
- Added block loot so a correctly harvested Arcane Extractor drops itself.
- Added pickaxe and diamond-tool block tags.
- Added inventory drop behavior when the machine is broken so stored items are not silently deleted.
- Added the Arcane Extractor to the Functional Blocks creative tab for testing.
- Replaced the single-texture placeholder model with a distinct vanilla-texture composition: crying-obsidian sides, obsidian bottom, and amethyst top.
- Added server-side enchanting-table sound and enchant particles after a successful extraction.

#### Why
M4 makes the machine viable in a survival world without prematurely adding a speculative XP or custom-currency tax. The crafting recipe establishes the primary up-front cost; each extraction still consumes one ordinary book.

#### Validation
- M3 is merged and CI-validated.
- Recipe format follows the Minecraft 1.21.1 shaped-recipe schema.
- Loot data uses the 1.21 singular `loot_table` path.
- PR CI is the authoritative clean-environment compile/test validation for the M4 code changes.

#### Balancing
- Machine cost: 4 crying obsidian + 4 amethyst shards + 1 enchanting table.
- Per-extraction cost: 1 ordinary book.
- No XP cost in the initial balancing pass.
- Diamond-tier pickaxe requirement protects the machine from trivial early-game harvesting.

#### Limitations
- Visuals use composed vanilla textures; original custom pixel art is deferred to a later polish pass.
- Extraction feedback is intentionally simple and uses vanilla sound/particles.
- Detailed client-facing error messages are still not shown for rejected extraction attempts.

#### Next
Implement M5 compatibility hardening: curses, treasure/modded enchantments, nonstandard levels, dedicated-server verification, and automation edge cases.

### M3 — 2026-09-27

#### Changed
- Added and registered the Arcane Extractor menu type.
- Right-clicking the Arcane Extractor now opens its server-backed menu.
- Added standard source, book, output, player-inventory, and hotbar slots.
- Added shift-click routing for enchanted source items and ordinary books.
- Added a client-only Arcane Extractor screen with selectable enchantment rows.
- Added paging so items with more than four enchantments remain fully selectable.
- Added an Extract button that uses vanilla menu-button networking rather than a custom payload.
- The server recomputes the candidate list from its authoritative source stack and rejects invalid candidate indices before invoking M2.
- Added user-facing menu translations.

#### Why
M3 completes the first playable interaction loop while preserving the authority boundary established in M1/M2: the client chooses an index, but never supplies or mutates enchantment data directly.

#### Validation
- M2 is merged and CI-validated.
- Menu and screen code are isolated by physical side; common classes do not import client-only Minecraft classes.
- PR CI is the authoritative clean-environment compile/test validation for the 1.21.1 menu, screen, and opening APIs.

#### Limitations
- The screen currently uses a code-drawn background rather than final art.
- No crafting recipe, XP cost, sound, particles, or custom block texture exists yet.
- Extraction failures are safely rejected server-side but are not yet surfaced as detailed user-facing error messages.

#### Next
Implement M4: survival recipe, custom visual assets, sound/feedback, and first-pass balancing.

### M2 — 2026-09-27

#### Changed
- Added the Arcane Extractor block and block item.
- Added the Arcane Extractor block entity with a persistent 3-slot `ItemStackHandler`.
- Added NeoForge item-handler capability exposure for automation compatibility.
- Added source/book/output slot validation.
- Added a server-owned extraction transaction that consumes exactly one book, replaces the source with the cleaned copy, and writes the enchanted-book output only after all preconditions pass.
- Added placeholder block/item models using vanilla crying obsidian so the block is renderable before custom art lands.
- Added focused M2 unit tests for success, missing-book rejection, occupied-output rejection, and slot rules.

#### Why
M2 turns the already-tested extraction engine into a persistent world machine without mixing in menu, networking, or client-screen concerns.

#### Validation
- M1 is merged and CI-validated.
- M2 transaction logic is covered by unit tests.
- PR CI is the authoritative clean-environment compile/test validation for the block, block entity, capability, and persistence APIs.

#### Limitations
- No menu or GUI exists yet; extraction is exposed as a server-side block-entity operation for M3 to call.
- No crafting recipe or custom textures are included yet.
- The machine currently requires an empty output slot before extraction.
- Hopper/automation side rules are intentionally unrestricted at the capability layer for now and can be hardened later if needed.

#### Next
Implement M3: menu, enchantment selection UI, and validated server-side extract action.

### M1 — 2026-09-27

#### Changed
- Added the standalone enchantment extraction domain engine.
- Added extraction candidate discovery from the source item's `DataComponents.ENCHANTMENTS` component.
- Added typed success/failure results for extraction requests.
- Added exact enchantment-level preservation onto `DataComponents.STORED_ENCHANTMENTS` on the output enchanted book.
- Added input immutability: extraction mutates only copies of the supplied source stack.
- Added JUnit 5 coverage for candidate discovery, selective removal, component preservation, input immutability, and failure paths.

#### Why
M1 establishes the gameplay contract before block, menu, networking, or rendering code exists. That keeps server-side validation and item mutation testable in isolation.

#### Validation
- Source-level implementation follows Minecraft 1.21.1's `ItemEnchantments` component model.
- Automated tests are included and run as part of `gradlew build`.
- PR CI is the authoritative clean-environment build validation for this milestone.

#### Limitations
- M1 operates on normal item enchantments in `DataComponents.ENCHANTMENTS`; extracting stored enchantments from enchanted books is intentionally out of scope.
- Book consumption, machine inventory state, XP/currency cost, menus, networking, and blocks are not implemented yet.
- M1 does not enforce enchantment compatibility rules on the output book because the book stores the enchantment that already existed on the source.

#### Next
Implement M2: Arcane Extractor block, block entity, and server-owned inventory transaction.

### M0 — 2026-09-27

#### Changed
- Created Arcane Extraction NeoForge 1.21.1 repository skeleton.
- Pinned Java 21, NeoForge 21.1.252, ModDevGradle 2.0.147, and Gradle 9.2.1 distribution.
- Established mod ID `arcane_extraction` and package `dev.nullps1.arcaneextraction`.
- Added minimal mod entry point and metadata.
- Added GitHub Actions build workflow.
- Added architecture, milestone, testing, and compatibility documentation.

#### Why
A boring, reproducible bootstrap keeps future gameplay work isolated from build-system churn and establishes documentation before the first feature lands.

#### Validation
- Static repository structure validation completed.
- Full Gradle dependency resolution/build remains pending CI or another networked Gradle environment.

#### Limitations
- The repository omits the binary `gradle-wrapper.jar`; bootstrap scripts retrieve the official wrapper JAR from the NeoForge 1.21.1 MDK before the first build.
- No gameplay mechanic exists in M0.

#### Next
Implement M1: standalone enchantment extraction domain logic and focused validation tests.
