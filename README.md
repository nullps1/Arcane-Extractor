# Arcane Extraction

A NeoForge 1.21.1 mod focused on one mechanic: moving a selected enchantment from an enchanted item onto a book while preserving the enchantment level and all unrelated item state.

## Current status

**Milestone M0 — repository bootstrap.** Gameplay extraction is intentionally not implemented yet.

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

The built mod JAR will be written under `build/libs/`.

## Design rule

The extraction engine will remain independent from the block/menu UI. UI code may request an extraction, but server-side domain logic owns validation and mutation.

## Planned first gameplay contract

Input:

1. One enchanted source item.
2. One ordinary book.
3. One enchantment selected from the source item.

Output:

1. A copy of the source item with only the selected enchantment removed.
2. One enchanted book carrying exactly that enchantment and level.

No XP cost or custom currency is part of the first gameplay milestone.

See [`docs/MILESTONES.md`](docs/MILESTONES.md) and [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).
