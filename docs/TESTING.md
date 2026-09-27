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

## M3 runtime checks

In a development client connected to a logical server:

1. Place the Arcane Extractor and right-click it with an empty hand.
2. Confirm source, book, output, player inventory, and hotbar slots render and transfer items correctly.
3. Insert an item with multiple enchantments and confirm the visible list matches the source.
4. Use paging on an item with more than four enchantments.
5. Select an enchantment and click **Extract**.
6. Confirm one book is consumed, the selected enchantment is removed from the source, and the exact level appears on the output book.
7. Change/remove the source before a stale selection can be acted upon and confirm the server rejects an invalid index without partial mutation.
8. Start `runServer` and verify no client-only M3 classes are loaded on the dedicated server.

## M4 runtime checks

In a survival or development world:

1. Obtain an enchanting table or amethyst shard and confirm the Arcane Extractor recipe becomes discoverable.
2. Craft the machine with 4 crying obsidian, 4 amethyst shards, and 1 enchanting table.
3. Confirm it requires a diamond-tier pickaxe for correct harvesting.
4. Break an empty machine and confirm it drops itself.
5. Put items into all three machine slots, break the block, and confirm every stored stack is dropped.
6. Complete one extraction and confirm the enchanting-table sound and enchant particles play once.
7. Verify the machine remains available in the Functional Blocks creative tab.

## M5 automated coverage

Compatibility tests verify:

- arbitrary/data-driven enchantment holders do not require a hardcoded ID;
- above-normal enchantment levels are preserved exactly;
- custom name and damage survive extraction;
- repair cost is intentionally reset on successful extraction while failed extraction preserves it;
- automation can insert valid source items and ordinary books;
- automation cannot insert directly into output;
- automation cannot extract source items or input books;
- automation can extract completed output;
- simulated insertion does not mutate inventory.

## M6 automated coverage

M6 tests verify:

- repeated single extractions fill separate output-buffer slots;
- one ordinary book is consumed per successful extraction;
- a completely full six-slot output buffer rejects extraction atomically;
- output slots reject insertion;
- automation may extract from any buffered output slot;
- automation input/source restrictions remain intact.

The exact registry-ID network selection path is additionally covered by live client/server testing because real enchantment registry holders are required.

## M6 runtime checks

1. Put Sharpness and Mending on the same item.
2. Select Sharpness regardless of its visual row and confirm **only Sharpness** is extracted.
3. Repeat with Mending and with modded enchantments.
4. Extract several enchantments consecutively without removing earlier output books.
5. Verify each result occupies the next free output slot.
6. Fill all six outputs and verify another extraction consumes neither source state nor a book.
7. Use **Extract All** with enough books and capacity; verify one separate enchanted book per enchantment.
8. Repeat Extract All with fewer books than enchantments and confirm it stops cleanly.
9. Confirm all six outputs persist across save/reload.
10. Load a world containing a pre-M6 3-slot Arcane Extractor and confirm its source, book, and old output survive while the new buffer slots are added.
11. Verify hopper/pipe extraction works from every buffered output.
12. Repeat the two-player stale-selection/concurrency checks.

## Pre-v1 release gate

Execute every check in [PRE_RELEASE_TEST_PLAN.md](PRE_RELEASE_TEST_PLAN.md) against the actual NeoForge 1.21.1 client/server pack before tagging v1.0.0.

This is where curse, treasure, real modded-enchantment, multiplayer, dedicated-server, save/reload, and installed-pipe compatibility are validated.

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
