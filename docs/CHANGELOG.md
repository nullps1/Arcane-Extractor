# Changelog

## Unreleased

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
