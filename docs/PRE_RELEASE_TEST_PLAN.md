# Pre-v1 Minecraft Test Plan

Run this checklist on the exact NeoForge 1.21.1 client/server pack intended for release.

The owner reported the M6 checklist passed on 2026-09-27. These boxes remain a reusable checklist for the final artifact, not a claim that every case was rerun during M7. Record the actual pack versions and artifact hash with the final acceptance.

## Gate A — install and startup

- [ ] Client starts with Arcane Extraction installed.
- [ ] Dedicated server starts with Arcane Extraction installed.
- [ ] Client joins the dedicated server without registry or handshake errors.
- [ ] Arcane Extractor appears in the Functional Blocks creative tab.
- [ ] Arcane Extractor recipe loads and is craftable.

## Gate B — vanilla extraction

For every case, verify:
1. the selected enchantment is removed from the source;
2. all unselected enchantments remain at their exact levels;
3. one ordinary book is consumed;
4. one enchanted book is produced with the exact selected level;
5. custom name/durability/other visible item state survives.

Cases:
- [ ] single enchantment;
- [ ] multiple enchantments, verifying the exact highlighted enchantment is the one removed;
- [ ] Mending;
- [ ] Curse of Binding on valid equipment;
- [ ] Curse of Vanishing;
- [ ] item with more than four enchantments;
- [ ] heavily damaged named item;
- [ ] item with a high anvil prior-work penalty: verify a successful extraction clears the old prior-work penalty before the next anvil operation. New anvil operations may build a new prior-work penalty normally.

## Gate C — modded and nonstandard enchantments

Using the actual server modpack:
- [ ] extract at least three non-vanilla enchantments;
- [ ] extract from an item mixing vanilla and modded enchantments;
- [ ] verify a nonstandard/above-normal enchantment level is preserved exactly if such an item can be produced;
- [ ] verify candidate names and levels render correctly in the GUI;
- [ ] verify paging works when the combined enchantment count exceeds four.

Record the mod and enchantment names tested in the release notes.

## Gate D — rejection behavior

- [ ] source slot empty;
- [ ] source item unenchanted;
- [ ] book slot empty;
- [ ] all six output-buffer slots occupied;
- [ ] selection becomes stale because the source changes;
- [ ] repeated Extract click after the selected enchantment has already been removed;
- [ ] send a stale selection after changing the source item and confirm the requested registry ID is rejected if absent;
- [ ] use Extract All with too few books and confirm it stops cleanly;
- [ ] use Extract All with insufficient output capacity and confirm it stops cleanly.

Expected result: no duplicate output, no lost source item, no extra book consumption, no partial mutation.

## Gate E — persistence

- [ ] put items in the machine and save/quit;
- [ ] reload and verify all slots;
- [ ] leave/re-enter chunk and verify all slots;
- [ ] restart dedicated server and verify all slots;
- [ ] perform an extraction after reload.

## Gate F — block breaking

Test with:
- [ ] source only;
- [ ] books only;
- [ ] output only;
- [ ] source, books, and all six output slots populated.

Expected result: machine and stored items drop exactly once with no duplication or loss.

## Gate G — automation

Using vanilla hoppers and any installed item pipes:
- [ ] insert enchanted source item;
- [ ] insert ordinary books;
- [ ] confirm automation cannot insert into output;
- [ ] confirm automation cannot pull the source;
- [ ] confirm automation cannot pull input books;
- [ ] confirm automation can pull completed enchanted-book output from every output-buffer slot;
- [ ] confirm repeated polling/simulation by pipes does not mutate inventory.

## Gate H — multiplayer

With two players:
- [ ] both can open the machine without desync;
- [ ] one player's extraction updates the other open menu and output bank;
- [ ] simultaneous clicks do not duplicate books or enchantments;
- [ ] removing/changing the source while another player has a selection does not produce a stale extraction;
- [ ] with Sharpness and Mending on the same item, selecting either one extracts that exact enchantment for both players;
- [ ] Extract All under two-player observation produces no duplicate books or enchantments.

## Gate J — M7.1 medieval visual acceptance

- [ ] Place from each horizontal direction; the input shelf and crystal face the player.
- [ ] Check outline/collision around the base, shelf, sides, top, and open space under the housing.
- [ ] Check adjacent solid blocks, daylight, darkness, and normal resource-pack lighting for missing faces/textures.
- [ ] Check the block item in inventory, hand, on the ground, and in an item frame.
- [ ] Load a pre-M7 machine: it faces north and all eight inventory slots survive; rotate a structure and verify facing.
- [ ] Open the GUI at supported GUI scales/window sizes; all 44 slots align with their frames.
- [ ] Check empty source, long vanilla/modded names, curse color, selection marker, full-name tooltips, both pages, and enabled/disabled/hover/focused buttons.
- [ ] Repeat exact single extraction and Extract All, including six filled outputs and missing books.
- [ ] Save/reload and repeat two-player interaction and installed hopper/pipe checks with the M7 artifact.
- [ ] Confirm the block reads as a medieval magical grindstone/workstation rather than an industrial machine, then accept the final workstation/GUI appearance before tagging v1.0.0.

## Release gate

Do not tag v1.0.0 until:
- all blocking checks above pass;
- failures are documented with reproduction steps;
- any compatibility exceptions are documented in `COMPATIBILITY.md`;
- final tested modpack and NeoForge versions are recorded.
