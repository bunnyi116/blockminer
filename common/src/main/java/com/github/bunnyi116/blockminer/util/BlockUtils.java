package com.github.bunnyi116.blockminer.util;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class BlockUtils {
    public static String getBlockName(Block block) {
        return block.getName().getString();
    }

    public static String getId(Block block) {
        return getResourceLocation(block).toString();
    }

    public static ResourceLocation getResourceLocation(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}
