package com.github.bunnyi116.blockminer.fabric;

import com.github.bunnyi116.blockminer.BlockMiner;
import net.fabricmc.api.ModInitializer;

public final class BlockMinerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        BlockMiner.init();
    }
}
