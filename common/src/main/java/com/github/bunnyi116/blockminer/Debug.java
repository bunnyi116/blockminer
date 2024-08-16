package com.github.bunnyi116.blockminer;

import org.slf4j.Logger;

public class Debug {

    public static Logger getLogger() {
        return BlockMinerMod.LOGGER;
    }

    public static void alwaysWrite(String msg) {
        getLogger().info(msg);
    }

    public static void alwaysWrite(String var1, Object... var2) {
        getLogger().info(String.format(var1, var2));
    }

    public static void alwaysWrite(Object obj) {
        getLogger().info(obj.toString());
    }
}
