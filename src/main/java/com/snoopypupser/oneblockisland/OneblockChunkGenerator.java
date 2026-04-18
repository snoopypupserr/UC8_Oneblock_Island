package com.snoopypupser.oneblockisland;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OneblockChunkGenerator extends ChunkGenerator {

    public static final MapCodec<OneblockChunkGenerator> CODEC =
            BiomeSource.CODEC.fieldOf("biome_source")
                    .xmap(OneblockChunkGenerator::new, g -> g.biomeSource);

    public OneblockChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState,
                             BiomeManager biomeManager, StructureManager structureManager,
                             ChunkAccess chunk, GenerationStep.Carving step) {}

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager,
                             RandomState randomState, ChunkAccess chunk) {}

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {}

    @Override
    public int getGenDepth() { return 384; }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState,
                                                        StructureManager structureManager,
                                                        ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() { return 63; }

    @Override
    public int getMinY() { return -64; }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type,
                             LevelHeightAccessor level, RandomState randomState) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z,
                                     LevelHeightAccessor level, RandomState randomState) {
        return new NoiseColumn(0, new net.minecraft.world.level.block.state.BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {}

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkAccess chunk, StructureTemplateManager structureTemplateManager) {
        // Leer lassen - keine Strukturen
    }
}