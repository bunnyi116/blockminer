package com.github.bunnyi116.blockminer.task;

import com.github.bunnyi116.blockminer.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class TaskManager {
    private static boolean enable;
    private static int addTaskCooldown;

    public static void addTask(ClientLevel level, Block block, BlockPos blockPos) {
        if (addTaskCooldown-- <= 0) {
            Debug.alwaysWrite("[%s] %s", block.getName().getString(), blockPos.toShortString());
            addTaskCooldown = 5;
        }
    }

    public static void switchOnOff(@Nullable Block block) {
        if (block == null || block.equals(Blocks.BEDROCK)) {
            TaskManager.enable = !enable;
            if (TaskManager.enable) {
                Debug.alwaysWrite("已开启");
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("已开启"));
            } else {
                Debug.alwaysWrite("已关闭");
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("已关闭"));
            }
        }
    }
}
