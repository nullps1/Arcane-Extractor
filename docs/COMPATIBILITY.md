# Compatibility

Arcane Extraction targets Minecraft 1.21.1 on NeoForge.

## Strategy

Compatibility should operate through Minecraft's enchantment registry and item data components. The project should not require a hardcoded vanilla-enchantment list and should not special-case another mod unless that mod breaks or extends the normal component contract.

## Planned validation

- Vanilla enchantments.
- Curses and treasure enchantments.
- Modded enchantments.
- Nonstandard enchantment levels.
- Items carrying other custom data components.
- Dedicated multiplayer server.

Apothic compatibility is a validation target, not a compile-time dependency for the core extraction engine.
