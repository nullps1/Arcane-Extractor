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
