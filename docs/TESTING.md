# Testing

## Build

### Windows

```powershell
.\bootstrap-wrapper.ps1
.\gradlew.bat build
```

### Linux/macOS

```bash
sh ./bootstrap-wrapper.sh
sh ./gradlew build
```

## M1 automated coverage

`EnchantmentExtractorTest` verifies:

- Candidate discovery exposes every enchantment and exact level.
- Extraction removes only the selected enchantment.
- Remaining enchantments preserve their levels.
- Output is an enchanted book containing exactly the extracted enchantment.
- Custom name and durability damage survive on the copied source.
- The caller's original source stack is not mutated.
- Selecting an enchantment not present on the source returns `ENCHANTMENT_NOT_PRESENT`.
- An unenchanted source returns `SOURCE_NOT_ENCHANTED`.
- An empty source returns `EMPTY_SOURCE`.

## M2 automated coverage

`ArcaneExtractorTransactionTest` verifies:

- A successful transaction consumes exactly one ordinary book.
- The cleaned source remains in the source slot.
- The selected enchantment appears at the exact level in the output enchanted book.
- Missing-book rejection leaves the source and output unchanged.
- Occupied-output rejection does not consume a book or modify the source.
- Source, book, and output insertion rules match the machine contract.

## M2 runtime checks

Before merging/releasing M2, place an Arcane Extractor in a development world and verify:

- the block renders and creates its block entity;
- inventory survives save/reload;
- the item-handler capability is discoverable server-side;
- no client-only class is loaded on a dedicated server.

## Runtime smoke tests

Before a release:

```bash
sh ./gradlew runClient
sh ./gradlew runServer
```

Verify the mod loads without client-only class errors.

## Future compatibility matrix

Required later:
- Vanilla treasure enchantments.
- Curses.
- Above-vanilla enchantment levels.
- Modded/data-driven enchantments.
- Dedicated multiplayer server transaction behavior.
