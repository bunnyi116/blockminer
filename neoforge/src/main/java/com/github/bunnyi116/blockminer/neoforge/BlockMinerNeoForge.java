package com.github.bunnyi116.blockminer.neoforge;

import com.github.bunnyi116.blockminer.BlockMiner;
import net.neoforged.fml.common.Mod;

@Mod(BlockMiner.MOD_ID)
public final class BlockMinerNeoForge {
    public BlockMinerNeoForge() {
        // Run our common setup.
        BlockMiner.init();
    }
}
