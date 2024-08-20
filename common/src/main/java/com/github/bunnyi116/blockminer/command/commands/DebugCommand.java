package com.github.bunnyi116.blockminer.command.commands;

import com.github.bunnyi116.blockminer.command.Command;
import com.github.bunnyi116.blockminer.config.Config;
import com.github.bunnyi116.blockminer.util.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent.ClientCommandSourceStack;
import net.minecraft.commands.CommandBuildContext;

import static dev.architectury.event.events.client.ClientCommandRegistrationEvent.argument;

public class DebugCommand extends Command {
    @Override
    public String getName() {
        return "debug";
    }

    @Override
    protected void build(LiteralArgumentBuilder<ClientCommandSourceStack> builder, CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandBuildContext context) {
        builder.executes(this::executes)
                .then(argument("bool", BoolArgumentType.bool())
                        .executes(this::executes)
                );
    }

    private int executes(CommandContext<ClientCommandSourceStack> context) {
        boolean b;
        try {
            b = BoolArgumentType.getBool(context, "bool");
        } catch (Exception e) {
            b = !Config.INSTANCE.debug;
        }
        Config.INSTANCE.debug = b;
        Config.save();
        if (Config.INSTANCE.debug) {
            MessageUtils.addMessage("调试开启");
        } else {
            MessageUtils.addMessage("调试关闭");
        }
        return 1;
    }


}
