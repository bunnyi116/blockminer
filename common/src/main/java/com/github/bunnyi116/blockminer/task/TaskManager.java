package com.github.bunnyi116.blockminer.task;

import com.github.bunnyi116.blockminer.Debug;
import com.github.bunnyi116.blockminer.config.Config;
import com.github.bunnyi116.blockminer.util.BlockUtils;
import com.github.bunnyi116.blockminer.util.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

import static net.minecraft.network.chat.Component.literal;

public class TaskManager {
    private static final List<Task> tasks = new LinkedList<>();
    private static @Nullable Task lastTask = null;
    private static boolean working;

    public static void tick() {
        if (!isWorking()) return;
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        var player = minecraft.player;
        if (level == null || player == null) return;
        if (lastTask != null && (lastTask.level != level || !level.getWorldBorder().isWithinBounds(lastTask.pos) || !lastTask.pos.closerToCenterThan(player.getEyePosition(), 3.5F))) {
            lastTask = null;
        }
        if (lastTask == null) {
            for (var task : tasks) {
                if (!level.getWorldBorder().isWithinBounds(task.pos)) continue;
                if (!task.pos.closerToCenterThan(player.getEyePosition(), player.blockInteractionRange() - 1F))
                    continue;
                lastTask = task;
            }
        }
        if (lastTask != null) {
            lastTask.tick();
            if (lastTask.isComplete()) {
                tasks.remove(lastTask);
                lastTask = null;
            }
        }
    }

    public static void addTask(ClientLevel level, Block block, BlockPos blockPos) {
        if (!isWorking() || !isAllowBlock(block)) return;
        var multiPlayerGameMode = Minecraft.getInstance().gameMode;
        if (multiPlayerGameMode == null) return;
        if (multiPlayerGameMode.getPlayerMode().isSurvival()) {
            if (!isAllowBlock(block)) return;
            for (var targetBlock : tasks) {
                if (targetBlock.pos.equals(blockPos)) {
                    return;
                }
            }
            Debug.write("[%s] [任务添加] %s", blockPos.toShortString(), block.getName().getString());
            tasks.add(new Task(level, block, blockPos));
        }
    }

    public static void switchOnOff(@Nullable Block block) {
        if (block == null || block.equals(Blocks.BEDROCK)) {
            if (isWorking()) {
                setWorking(false);
            } else if (Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.getPlayerMode().isCreative()) {
                MessageUtils.addMessage(literal("仅限在生存模式工作！"));
            } else {
                setWorking(true);
                if (!Minecraft.getInstance().isLocalServer()) {
                    MessageUtils.addMessage(literal("§7看起来你好像是在服务器使用Bedrock Miner？§r\n§7在使用本mod前请先征询其他玩家的意见。§r"));
                }
            }
        }
    }

    public static boolean isAllowBlock(Block block) {
        var mc = Minecraft.getInstance();
        var config = Config.INSTANCE;
        // 方块黑名单检查(服务器)
        if (!mc.isLocalServer()) {
            for (var defaultBlockBlack : config.blockBlacklistServer) {
                var resourceLocation = ResourceLocation.parse(defaultBlockBlack);
                if (BlockUtils.getResourceLocation(block).equals(resourceLocation)) {
                    return false;
                }
            }
        }
        // 方块黑名单检查(用户自定义)
        for (var blockBlack : config.blockBlacklist) {
            var resourceLocation = ResourceLocation.parse(blockBlack);
            if (BlockUtils.getResourceLocation(block).equals(resourceLocation)) {
                return false;
            }
        }
        // 方块白名单检查(用户自定义)
        for (var blockBlack : config.blockWhitelist) {
            var resourceLocation = ResourceLocation.parse(blockBlack);
            if (BlockUtils.getResourceLocation(block).equals(resourceLocation)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isWorking() {
        return working;
    }

    public static void setWorking(boolean working) {
        TaskManager.working = working;
        if (TaskManager.working) {
            MessageUtils.addMessage(literal("已开启"));
        } else {
            MessageUtils.addMessage(literal("已关闭"));
        }
    }
}
