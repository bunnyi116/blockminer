package com.github.bunnyi116.blockminer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;

import static dev.architectury.event.events.client.ClientCommandRegistrationEvent.ClientCommandSourceStack;
import static dev.architectury.event.events.client.ClientCommandRegistrationEvent.literal;

public abstract class Command {
    public abstract String getName();

    protected abstract void build(LiteralArgumentBuilder<ClientCommandSourceStack> builder, CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandBuildContext context);

    public final void register(LiteralArgumentBuilder<ClientCommandSourceStack> rootBuilder, CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandBuildContext context) {
        var cmd = literal(this.getName());
        build(cmd, dispatcher, context);    // 为命令提供子命令对象构建器
        rootBuilder.then(cmd);  // 追加到根命令节点
    }
}
