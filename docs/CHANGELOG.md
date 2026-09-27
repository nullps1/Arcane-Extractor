# Changelog

## Unreleased

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
