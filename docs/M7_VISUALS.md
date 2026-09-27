# M7 visual release

## Current release-candidate direction: M7.1 medieval magic retheme

The original M7 implementation proved the directional custom-model and themed-GUI pipeline, but owner testing found its art direction too modern/industrial. M7.1 keeps the working M6/M7 integration and rethemes only presentation.

### Block

The Arcane Extractor is now a medieval magical workstation inspired by the vanilla grindstone silhouette:

- crying obsidian feet and upright supports;
- dark oak structural crossbeam and front work shelf;
- polished blackstone axle/hardware;
- a thick, faceted amethyst extraction core;
- a small amethyst rune inset on the work face;
- open negative space so the block reads as a crafted workstation rather than a full machine cube.

The model retains four horizontal facings and a matching directional outline/collision shape. It intentionally uses vanilla Minecraft material textures rather than custom industrial panels, which helps it sit naturally beside enchanting tables, grindstones, lecterns, and other medieval utility blocks.

### GUI

The M6 layout and coordinates are unchanged. Presentation is now rendered procedurally:

- carved dark-oak outer frame;
- warm parchment input/enchantment/inventory panels;
- dark blackstone output altar;
- amethyst corner inlays, dividers, transfer marks, and selection highlight;
- carved-tablet Extract / Extract All / paging buttons;
- vanilla text styling and full-name hover tooltips.

The previous industrial GUI PNG remains in the repository as a historical unused asset but is no longer referenced by `ArcaneExtractorScreen`.

## Compatibility boundary

M7.1 changes presentation only.

Unchanged from M6:
- all eight machine slots and 36 player slots;
- exact registry-ID selection payloads;
- Extract and Extract All transactions;
- six-slot output buffer;
- source/book/output automation restrictions;
- item components and repair-cost behavior;
- block entity inventory serialization/migration;
- recipe, loot, sounds, particles, and registry IDs.

Block facing/shape support introduced in M7 remains intact.

## M7.1 acceptance

Before tagging v1.0.0:

1. Place the block from all four directions and confirm the shelf/crystal workstation reads correctly.
2. Check crying obsidian, dark oak, blackstone, and amethyst textures in daylight and darkness.
3. Verify outline/collision around the open model.
4. Check inventory/hand/item-frame presentation.
5. Open the GUI at the intended GUI scale and inspect wood/parchment/blackstone/amethyst readability.
6. Verify all slots align, long names clip safely, tooltips show full names, curse styling remains readable, and selection/paging is clear.
7. Exercise all button states.
8. Repeat exact single extraction and Extract All once to prove M6 behavior survived the visual-only patch.
9. Save/reload and verify facing and rendering.
10. Record the final artifact hash and exact modpack/NeoForge versions.

## Historical M7 note

The first M7 release candidate used custom obsidian/trim/rune/amethyst/deck PNGs and a 256x256 industrial GUI atlas. Its implementation and screenshots remain useful historical evidence that the custom model/menu path worked, but they no longer represent the intended v1.0.0 visual theme.
