package dev.nullps1.arcaneextraction.block;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import dev.nullps1.arcaneextraction.registry.ModBlocks;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.junit.jupiter.api.Test;

class ArcaneExtractorVisualTest {
    private static final String ROOT = "/assets/arcane_extraction/";

    private InputStream resource(String path) {
        InputStream stream = getClass().getResourceAsStream(ROOT + path);
        assertNotNull(stream, path);
        return stream;
    }

    private JsonObject json(String path) throws Exception {
        try (var reader = new InputStreamReader(resource(path), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test
    void everyFacingMatchesTheVisibleModelAndCollision() throws Exception {
        var block = ModBlocks.ARCANE_EXTRACTOR.get();
        var model = json("models/block/arcane_extractor.json");
        var variants = json("blockstates/arcane_extractor.json").getAsJsonObject("variants");
        assertEquals(4, variants.size());
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        for (int turns = 0; turns < 4; turns++) {
            var variant = variants.getAsJsonObject("facing=" + directions[turns].getName());
            assertEquals(turns * 90, variant.get("y").getAsInt());
            assertEquals("arcane_extraction:block/arcane_extractor", variant.get("model").getAsString());
            VoxelShape expected = Shapes.empty();
            for (var element : model.getAsJsonArray("elements")) {
                var from = element.getAsJsonObject().getAsJsonArray("from");
                var to = element.getAsJsonObject().getAsJsonArray("to");
                double x0 = from.get(0).getAsDouble() / 16;
                double z0 = from.get(2).getAsDouble() / 16;
                double x1 = to.get(0).getAsDouble() / 16;
                double z1 = to.get(2).getAsDouble() / 16;
                for (int rotation = 0; rotation < turns; rotation++) {
                    double oldX0 = x0, oldX1 = x1;
                    x0 = 1 - z1; x1 = 1 - z0; z0 = oldX0; z1 = oldX1;
                }
                expected = Shapes.or(expected, Shapes.box(x0, from.get(1).getAsDouble() / 16, z0,
                        x1, to.get(1).getAsDouble() / 16, z1));
            }
            var state = block.defaultBlockState().setValue(ArcaneExtractorBlock.FACING, directions[turns]);
            assertFalse(Shapes.joinIsNotEmpty(expected,
                    state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), BooleanOp.NOT_SAME));
            assertFalse(Shapes.joinIsNotEmpty(expected,
                    state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty()),
                    BooleanOp.NOT_SAME));
            assertFalse(state.canOcclude());
        }
    }

    @Test
    void defaultAndStructureTransformsRetainHorizontalFacing() {
        var block = ModBlocks.ARCANE_EXTRACTOR.get();
        var north = block.defaultBlockState();
        assertEquals(Direction.NORTH, north.getValue(ArcaneExtractorBlock.FACING));
        assertEquals(Direction.EAST, north.rotate(Rotation.CLOCKWISE_90).getValue(ArcaneExtractorBlock.FACING));
        assertEquals(Direction.SOUTH, north.mirror(Mirror.LEFT_RIGHT).getValue(ArcaneExtractorBlock.FACING));
        assertEquals(north, north.rotate(Rotation.CLOCKWISE_90).rotate(Rotation.COUNTERCLOCKWISE_90));
    }

    @Test
    void preM7PaletteWithoutPropertiesLoadsTheSameBlockFacingNorth() {
        var legacy = new CompoundTag();
        legacy.putString("Name", "arcane_extraction:arcane_extractor");
        var loaded = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), legacy);
        assertEquals(ModBlocks.ARCANE_EXTRACTOR.get().defaultBlockState(), loaded);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            var state = loaded.setValue(ArcaneExtractorBlock.FACING, direction);
            assertEquals(state, NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), NbtUtils.writeBlockState(state)));
        }
    }

    @Test
    void packagedModelUsesMedievalVanillaMaterialsAndItemInheritance() throws Exception {
        var model = json("models/block/arcane_extractor.json");
        var textures = model.getAsJsonObject("textures");

        assertEquals("minecraft:block/crying_obsidian", textures.get("obsidian").getAsString());
        assertEquals("minecraft:block/dark_oak_planks", textures.get("wood").getAsString());
        assertEquals("minecraft:block/amethyst_block", textures.get("crystal").getAsString());
        assertEquals("minecraft:block/polished_blackstone", textures.get("stone").getAsString());

        for (var element : model.getAsJsonArray("elements")) {
            for (var face : element.getAsJsonObject().getAsJsonObject("faces").entrySet()) {
                String reference = face.getValue().getAsJsonObject().get("texture").getAsString();
                assertTrue(reference.startsWith("#"), reference);
                assertTrue(textures.has(reference.substring(1)), reference);
            }
        }

        assertEquals("arcane_extraction:block/arcane_extractor",
                json("models/item/arcane_extractor.json").get("parent").getAsString());
    }
}
