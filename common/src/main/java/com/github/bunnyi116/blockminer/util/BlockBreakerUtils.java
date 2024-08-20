package com.github.bunnyi116.blockminer.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BlockBreakerUtils {
    private static BlockPos destroyBlockPos = new BlockPos(-1, -1, -1);
    private static float destroyProgress;
    private static float destroyTicks;
    private static boolean isDestroying;


    public static boolean startDestroyBlock(BlockPos blockPos, Direction direction, @Nullable Consumer<BlockPos> beforeDestroyBlock) {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        var player = minecraft.player;
        var gameMode = minecraft.gameMode;
        var connection = minecraft.getConnection();
        if (level == null || player == null || gameMode == null || connection == null) return false;
        if (player.blockActionRestricted(level, blockPos, gameMode.getPlayerMode())) {
            return false;
        } else if (!level.getWorldBorder().isWithinBounds(blockPos)) {
            return false;
        } else {
            BlockState blockState;
            if (gameMode.getPlayerMode().isCreative()) {
                blockState = level.getBlockState(blockPos);
                minecraft.getTutorial().onDestroyBlock(level, blockPos, blockState, 1.0F);
                if (beforeDestroyBlock != null) {   // 创造模式情况下只发送START_DESTROY_BLOCK数据包即可破坏
                    beforeDestroyBlock.accept(blockPos);
                }
                gameMode.startPrediction(level, (i) -> {
                    gameMode.destroyBlock(blockPos);
                    return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos, direction, i);
                });
            } else if (!isDestroying || !sameDestroyTarget(blockPos)) {
                if (isDestroying) {
                    connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, destroyBlockPos, direction));
                }
                blockState = level.getBlockState(blockPos);
                minecraft.getTutorial().onDestroyBlock(level, blockPos, blockState, 0.0F);
                gameMode.startPrediction(level, (i) -> {
                    boolean bl = !blockState.isAir();
                    if (bl && destroyProgress == 0.0F) {
                        blockState.attack(level, blockPos, player);
                    }
                    if (bl && blockState.getDestroyProgress(player, player.level(), blockPos) >= 1.0F) {
                        if (beforeDestroyBlock != null) {
                            beforeDestroyBlock.accept(blockPos);
                        }
                        gameMode.destroyBlock(blockPos);
                    } else {
                        isDestroying = true;
                        destroyBlockPos = blockPos;
                        destroyProgress = 0.0F;
                        destroyTicks = 0.0F;
                        level.destroyBlockProgress(player.getId(), destroyBlockPos, getDestroyStage());
                    }
                    return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos, direction, i);
                });
            }
            return true;
        }
    }

    public static void stopDestroyBlock() {
        if (!isDestroying) return;
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        var player = minecraft.player;
        var connection = minecraft.getConnection();
        if (level == null || player == null || connection == null) return;

        BlockState blockState = level.getBlockState(destroyBlockPos);
        minecraft.getTutorial().onDestroyBlock(level, destroyBlockPos, blockState, -1.0F);
        connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, destroyBlockPos, Direction.DOWN));
        isDestroying = false;
        destroyProgress = 0.0F;
        level.destroyBlockProgress(player.getId(), destroyBlockPos, -1);
        player.resetAttackStrengthTicker();
    }

    public static boolean continueDestroyBlock(BlockPos blockPos, Direction direction, @Nullable Consumer<BlockPos> beforeDestroyBlock) {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        var player = minecraft.player;
        var gameMode = minecraft.gameMode;
        var connection = minecraft.getConnection();
        if (level == null || player == null || gameMode == null || connection == null) return false;
        gameMode.ensureHasSentCarriedItem();
        BlockState blockState;
        if (gameMode.getPlayerMode().isCreative() && level.getWorldBorder().isWithinBounds(blockPos)) {
            blockState = level.getBlockState(blockPos);
            minecraft.getTutorial().onDestroyBlock(level, blockPos, blockState, 1.0F);
            if (beforeDestroyBlock != null) {   // 创造模式情况下只发送START_DESTROY_BLOCK数据包即可破坏
                beforeDestroyBlock.accept(blockPos);
            }
            gameMode.startPrediction(level, (i) -> {
                gameMode.destroyBlock(blockPos);
                return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos, direction, i);
            });
            return true;
        } else if (sameDestroyTarget(blockPos)) {
            blockState = level.getBlockState(blockPos);
            if (blockState.isAir()) {
                isDestroying = false;
                return false;
            } else {
                destroyProgress += blockState.getDestroyProgress(player, player.level(), blockPos);
                if (destroyTicks % 4.0F == 0.0F) {
                    SoundType soundType = blockState.getSoundType();
                    minecraft.getSoundManager().play(new SimpleSoundInstance(soundType.getHitSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), blockPos));
                }

                ++destroyTicks;
                minecraft.getTutorial().onDestroyBlock(level, blockPos, blockState, Mth.clamp(destroyProgress, 0.0F, 1.0F));
                if (destroyProgress >= 1.0F) {
                    isDestroying = false;
                    if (beforeDestroyBlock != null) {
                        beforeDestroyBlock.accept(blockPos);
                    }
                    gameMode.startPrediction(level, (i) -> {
                        gameMode.destroyBlock(blockPos);
                        return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction, i);
                    });
                    destroyProgress = 0.0F;
                    destroyTicks = 0.0F;
                }
                level.destroyBlockProgress(player.getId(), destroyBlockPos, getDestroyStage());
                return true;
            }
        } else {
            return startDestroyBlock(blockPos, direction, beforeDestroyBlock);
        }
    }

    private static int getDestroyStage() {
        return destroyProgress > 0.0F ? (int) (destroyProgress * 10.0F) : -1;
    }

    private static boolean sameDestroyTarget(BlockPos arg) {
        return arg.equals(destroyBlockPos);
    }
}
