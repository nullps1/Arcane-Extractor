# Arcane Extraction

A NeoForge 1.21.1 mod focused on one mechanic: moving a selected enchantment from an enchanted item onto a book while preserving the enchantment level and all unrelated item state.

## Current status

**v1.0.0 release candidate accepted for release.** Arcane Extraction now includes the medieval-magic Arcane Extractor workstation, exact registry-ID enchantment selection, a six-slot output buffer, Extract All, automation support, persistence, and multiplayer-safe server-authoritative extraction.

The project owner completed live gameplay and final visual acceptance on 2026-09-27. See [M7 visuals and validation](docs/M7_VISUALS.md) and [the pre-release test plan](docs/PRE_RELEASE_TEST_PLAN.md) for the validation trail.

## Toolchain

- Minecraft 1.21.1
- NeoForge 21.1.252
- Java 21
- ModDevGradle 2.0.147
- Gradle 9.2.1 wrapper distribution

## First build

The repository does not embed the binary Gradle wrapper JAR. Bootstrap it once from the official NeoForge MDK:

### Windows PowerShell

```powershell
.\bootstrap-wrapper.ps1
.\gradlew.bat build
```

### Linux/macOS

```bash
sh ./bootstrap-wrapper.sh
sh ./gradlew build
```

The built mod JAR will be written under `build/libs/` as the `1.0.0` release artifact.

## Design rule

The extraction engine is independent from the block/menu UI. UI code requests an extraction by registry ID; server-side domain logic owns validation and mutation.

## Gameplay contract

Input:

1. One enchanted source item.
2. One ordinary book.
3. One enchantment selected from the source item.

Output:

1. A copy of the source item with only the selected enchantment removed.
2. One enchanted book carrying exactly that enchantment and level.

Each successful extraction consumes one ordinary book and resets the source item's anvil prior-work penalty. No XP or custom currency is required. Extract All repeats this operation until books, enchantments, or free output slots run out. Hoppers/pipes can insert valid inputs and pull from all six outputs without pulling the inputs.

See [`docs/MILESTONES.md`](docs/MILESTONES.md) and [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).
