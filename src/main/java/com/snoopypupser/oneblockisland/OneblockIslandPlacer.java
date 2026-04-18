package com.snoopypupser.oneblockisland;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.io.InputStream;

@EventBusSubscriber(modid = OneblockIsland.MOD_ID)
public class OneblockIslandPlacer {

    private static final String NBT_KEY = "introcutscene_played";

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (!level.dimension().equals(ServerLevel.OVERWORLD)) return;

        if (!(level.getChunkSource().getGenerator() instanceof OneblockChunkGenerator)) return;

        // Scoreboard als persistenter Speicher
        net.minecraft.world.scores.Scoreboard scoreboard = level.getScoreboard();
        net.minecraft.world.scores.Objective objective = scoreboard.getObjective("ob_placed");

        if (objective == null) {
            objective = scoreboard.addObjective(
                    "ob_placed",
                    net.minecraft.world.scores.criteria.ObjectiveCriteria.DUMMY,
                    net.minecraft.network.chat.Component.literal("ob_placed"),
                    net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType.INTEGER,
                    false,
                    null
            );
        }

        net.minecraft.world.scores.ScoreHolder holder = net.minecraft.world.scores.ScoreHolder.forNameOnly("island");
        var score = scoreboard.getOrCreatePlayerScore(holder, objective);

        if (score.get() == 1) return;

        score.set(1);

        try {
            InputStream stream = OneblockIslandPlacer.class
                    .getResourceAsStream("/data/oneblockisland/structures/island.nbt");

            if (stream == null) {
                System.out.println("[OneblockIsland] island.nbt not found!");
                return;
            }

            CompoundTag nbt = NbtIo.readCompressed(stream,
                    net.minecraft.nbt.NbtAccounter.unlimitedHeap());
            StructureTemplate template = new StructureTemplate();
            template.load(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), nbt);

            BlockPos placePos = new BlockPos(0, 64, 0);
            StructurePlaceSettings settings = new StructurePlaceSettings();
            template.placeInWorld(level, placePos, placePos, settings,
                    level.getRandom(), 2);

            System.out.println("[OneblockIsland] Succesfully placed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        // Nur in OneBlock-Welten (prüft ob der Generator ein OneblockChunkGenerator ist)
        ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);
        if (overworld == null) return;
        if (!(overworld.getChunkSource().getGenerator() instanceof OneblockChunkGenerator)) return;

        // Bereits gespielt?
        CompoundTag data = player.getPersistentData();
        if (data.getBoolean(NBT_KEY)) return;

        // Flag setzen
        data.putBoolean(NBT_KEY, true);

        // 40 Ticks warten damit der Spieler vollständig geladen ist
        server.tell(new net.minecraft.server.TickTask(server.getTickCount() + 40, () -> {
            if (!player.isAlive() || !player.isAddedToLevel()) return;

            String command = String.format(
                    "playvideo \"water://local/config/watermedia/cutscene/intro.mp4\" %s 100 1.0 false 20 20 false true",
                    player.getGameProfile().getName()
            );

            server.getCommands().performPrefixedCommand(
                    server.createCommandSourceStack(),
                    command
            );
        }));
    }
}