package com.snoopypupser.oneblockisland;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

@Mod(OneblockIsland.MOD_ID)
public class OneblockIsland {

    public static final String MOD_ID = "oneblockisland";

    public static final ResourceKey<WorldPreset> ONEBLOCK_PRESET = ResourceKey.create(
            Registries.WORLD_PRESET,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "oneblock_island")
    );

    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<OneblockChunkGenerator>> ONEBLOCK_GENERATOR =
            CHUNK_GENERATORS.register("oneblock_generator", () -> OneblockChunkGenerator.CODEC);

    public OneblockIsland(IEventBus modEventBus) {
        CHUNK_GENERATORS.register(modEventBus);
    }
}