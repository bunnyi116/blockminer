package com.github.bunnyi116.blockminer;

import com.github.bunnyi116.blockminer.config.Config;
import org.slf4j.Logger;

public class Debug {

    private static Logger getLogger() {
        return BlockMinerMod.LOGGER;
    }

    private static boolean isDebug() {
        return Config.INSTANCE.debug;
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


    public static void write(String msg) {
        if (isDebug()) {
            getLogger().info(msg);
        }
    }

    public static void write(String var1, Object... var2) {
        if (isDebug()) {
            getLogger().info(String.format(var1, var2));
        }
    }

    public static void write(Object obj) {
        if (isDebug()) {
            getLogger().info(obj.toString());
        }
    }
}
