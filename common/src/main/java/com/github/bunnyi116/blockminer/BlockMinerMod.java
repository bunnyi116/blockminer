package com.github.bunnyi116.blockminer;

import com.github.bunnyi116.blockminer.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlockMinerMod {
    public static final int PRIORITY = 999;
    public static final String MOD_NAME = "BlockMiner";
    public static final String MOD_ID = "blockminer";
    public static final String MOD_COMMAND_COMMAND_PREFIX = "blockMiner";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        CommandManager.init();
        Debug.alwaysWrite("模组启动成功");
    }
}
