package com.github.bunnyi116.blockminer.command;

import com.github.bunnyi116.blockminer.BlockMinerMod;
import com.github.bunnyi116.blockminer.Debug;
import com.github.bunnyi116.blockminer.command.commands.DebugCommand;
import com.github.bunnyi116.blockminer.command.commands.TestCommand;
import com.github.bunnyi116.blockminer.command.commands.ToggleCommand;
import com.github.bunnyi116.blockminer.task.TaskManager;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent.ClientCommandSourceStack;

import java.util.ArrayList;

import static dev.architectury.event.events.client.ClientCommandRegistrationEvent.literal;

public class CommandManager {
    public static void init() {
        // 创建命令根节点
        var root = literal(BlockMinerMod.MOD_COMMAND_COMMAND_PREFIX).executes(CommandManager::rootCommandExecutes);

        // 初始化子命令
        var commands = new ArrayList<Command>();
        commands.add(new ToggleCommand());
        commands.add(new DebugCommand());
        commands.add(new TestCommand());

        // 注册命令
        ClientCommandRegistrationEvent.EVENT.register((dispatcher, context) -> {
            for (var command : commands) {
                command.register(root, dispatcher, context);
            }
            dispatcher.register(root);
        });
        Debug.alwaysWrite("客户端命令注册成功");
    }

    private static int rootCommandExecutes(CommandContext<ClientCommandSourceStack> context) {
        TaskManager.setWorking(!TaskManager.isWorking());
        return 1;
    }

}
