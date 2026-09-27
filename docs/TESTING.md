# Testing

## M0 checks

```bash
sh ./bootstrap-wrapper.sh
sh ./gradlew build
sh ./gradlew runClient
```

A dedicated-server smoke test is required before calling M0 complete in GitHub:

```bash
sh ./gradlew runServer
```

Accept the development EULA only in the generated run directory, then verify the mod loads without client-only class errors.

## M1 test matrix

Required cases:
- Source has one enchantment.
- Source has multiple enchantments.
- Selected enchantment level is preserved.
- Other enchantments remain unchanged.
- Source with no enchantments is rejected.
- Missing/invalid selected enchantment is rejected.
- Empty book input is rejected by the future machine transaction layer.
- Curse extraction behaves consistently with ordinary enchantments.
- Non-enchantment item components survive extraction.
- Modded/data-driven enchantments do not require Arcane Extraction code changes.
