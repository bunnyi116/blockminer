package com.github.bunnyi116.blockminer.command.commands;

import com.github.bunnyi116.blockminer.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent.ClientCommandSourceStack;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class TestCommand extends Command {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    protected void build(LiteralArgumentBuilder<ClientCommandSourceStack> builder, CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandBuildContext context) {
        builder.executes(this::executes);
    }

    private int executes(CommandContext<ClientCommandSourceStack> context) {
        var minecraft = Minecraft.getInstance();
        var hitResult = minecraft.hitResult;
        var level = minecraft.level;
        var player = minecraft.player;
        var gameMode = minecraft.gameMode;
        var connection = minecraft.getConnection();
        if (level == null || player == null || gameMode == null || connection == null) return -1;

        if (hitResult != null && (hitResult.getType() == HitResult.Type.BLOCK || player.getMainHandItem().isEmpty())) {
            var blockHitResult = (BlockHitResult) hitResult;
            var blockPos = blockHitResult.getBlockPos();
            var blockState = level.getBlockState(blockPos);
            var block = blockState.getBlock();

//            BlockBreakerUtils.continueDestroyBlock(blockPos, Direction.UP, null);
        }
        return 1;
    }
}
