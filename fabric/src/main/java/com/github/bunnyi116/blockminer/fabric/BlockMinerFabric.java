package com.github.bunnyi116.blockminer.fabric;

import com.github.bunnyi116.blockminer.BlockMinerMod;
import net.fabricmc.api.ModInitializer;

public final class BlockMinerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BlockMinerMod.init();
    }
}
