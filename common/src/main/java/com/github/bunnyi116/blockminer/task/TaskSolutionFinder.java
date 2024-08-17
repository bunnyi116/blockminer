package com.github.bunnyi116.blockminer.task;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

public class TaskSolutionFinder {
    public void piston(BlockPos targetPos) {
        for (Direction pistonDirection : Direction.values()) {
            var pistonPos = targetPos.relative(pistonDirection);
            for (Direction pistonFacing : Direction.values()) {
                var pistonHeadPos = pistonPos.relative(pistonFacing);
                if (pistonHeadPos.equals(targetPos)) continue;  // 活塞臂处于目标方块位置
                // 拉杆放置在目标方块上(破坏成功自动掉落,避免二次拆除,最优方案1)
                for (Direction leverDirection : Direction.values()) {
                    var leverPos = targetPos.relative(leverDirection);
                    if (leverPos.equals(pistonPos)) continue;  // 拉杆处于活塞位置
                    // ...
                }
                // 拉杆放置在活塞底面(破坏成功自动掉落,避免二次拆除,最优方案2)
                {
                    var leverDirection = pistonFacing.getOpposite();
                    var leverPos = pistonPos.relative(leverDirection);
                    var leverBasePos = pistonPos.relative(leverDirection.getOpposite());
                    // ...
                }
            }
        }

    }
}
