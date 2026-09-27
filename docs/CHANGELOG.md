# Changelog

## Unreleased

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
