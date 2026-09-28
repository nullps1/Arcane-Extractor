# Arcane Extraction v1.0.0

Release date: 2026-09-27

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.252
- Java 21

Arcane Extraction is required on both the client and dedicated server because it registers the Arcane Extractor block, menu, networking, and resources.

## What it does

Arcane Extraction adds the **Arcane Extractor**, a medieval magical workstation that moves enchantments from gear onto ordinary books.

- Select one exact enchantment and extract it onto a book.
- Preserve the enchantment's exact stored level, including nonstandard/above-normal levels.
- Preserve remaining enchantments and unrelated source item state.
- Use **Extract All** to process multiple enchantments until books or output capacity run out.
- Store up to six extracted enchanted books in the machine's output buffer.
- Successful extraction clears the source item's existing anvil prior-work penalty.

## Compatibility

The extractor reads Minecraft's normal item enchantment component and does not use a vanilla or mod-ID allowlist.

This was exercised with:
- vanilla enchantments;
- Mending;
- curses;
- mixed vanilla/modded enchantments;
- nonstandard enchantment levels;
- items with more than four enchantments.

Mods that store their enchantments outside Minecraft's normal enchantment component may require dedicated future integration.

## Automation

External item handlers may:
- insert a valid enchanted source item;
- insert ordinary books;
- extract enchanted books from all six output slots.

They may not:
- pull the source item;
- pull input books;
- insert directly into output slots.

## Multiplayer and persistence

Pre-release live testing covered dedicated-server startup, multiplayer interaction, stale selections, concurrent use, save/reload, chunk reload, server restart, block breaking with contents, and automation behavior.

## Visual release

The final Arcane Extractor uses:
- crying obsidian supports;
- dark oak framing and work shelf;
- polished blackstone hardware;
- an amethyst extraction core;
- a widened carved-wood / parchment / amethyst interface.

The final M7.2 visual pass was accepted by the project owner on 2026-09-27.

## Known behavior

Arcane Extraction clears the item's existing anvil prior-work penalty when an extraction succeeds. Applying new books in an anvil can create a new prior-work penalty normally; Arcane Extraction does not globally rewrite vanilla or modded anvil behavior.

## Validation record

- M0–M7.2 completed.
- M6 gameplay checklist reported passed by the project owner.
- M7.2 final visual acceptance reported passed by the project owner.
- Release target is Minecraft 1.21.1 / NeoForge 21.1.252 / Java 21.
- Exact final modpack revision/hash was not captured in-repository. Record it when deploying the final release JAR.
