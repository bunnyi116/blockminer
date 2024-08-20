package com.github.bunnyi116.blockminer.task;

import com.github.bunnyi116.blockminer.Debug;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Task {
    public final ClientLevel level;
    public final Block block;
    public final BlockPos pos;
    private final Map<BlockPos, Block> recycledItems;
    private int ticks;
    private int tickTotalMax;
    private int tickTimeoutMax;
    private int tickWaitMax;
    private boolean timeout;
    private TaskStatus status;
    private @Nullable TaskStatus nextStatus;

    public Task(ClientLevel level, Block block, BlockPos pos) {
        this.level = level;
        this.block = block;
        this.pos = pos;
        this.recycledItems = new HashMap<>();
        this.init();
    }

    public void tick() {
        debug("开始");
        if (this.isComplete()) {
            return;
        }
        if (this.ticks >= this.tickTotalMax) {
            this.status = TaskStatus.COMPLETE;

        }
        if (!timeout && this.ticks >= this.tickTimeoutMax) {
            this.timeout = true;
            this.status = TaskStatus.TIMEOUT;

        }
        switch (this.status) {
            case INITIALIZE -> init();
            case WAIT_CUSTOM_UPDATE -> waitCustomUpdate();
            case WAIT_GAME_UPDATE -> waitGameUpdate();
            case TIMEOUT -> timeout();
            case RECYCLED_ITEMS -> recycledItems();
            case COMPLETE -> complete();
        }
        debug("结束");
        ++this.ticks;
    }

    private void complete() {
        debug("任务已结束");
    }


    private void recycledItems() {
        debug("回收物品");
        for (var entry : recycledItems.entrySet()) {

        }
    }

    private void timeout() {
        debug("任务已超时");
        this.status = TaskStatus.COMPLETE;
    }

    private void waitGameUpdate() {
        if (!this.level.getBlockState(pos).is(block) || this.level.getBlockState(pos).isAir()) {
            debugUpdateStates("目标不存在");
            this.status = recycledItems.isEmpty() ? TaskStatus.COMPLETE : TaskStatus.RECYCLED_ITEMS;
            return;
        }
    }

    private void waitCustomUpdate() {
        if (--this.tickWaitMax <= 0) {
            this.status = this.nextStatus == null ? TaskStatus.WAIT_GAME_UPDATE : this.nextStatus;
            this.tickWaitMax = 0;
            debug("等待已结束, 状态设置为: %s", this.status);
        } else {
            ++this.tickTotalMax;
            ++this.tickTimeoutMax;
            debug("剩余等待TICK: %s", tickWaitMax);
        }
    }

    public void init() {
        this.ticks = 0;
        this.tickTotalMax = 100;
        this.tickTimeoutMax = 25;
        this.tickWaitMax = 0;
        this.status = TaskStatus.WAIT_GAME_UPDATE;
        debug("任务已初始化");
    }

    public boolean isComplete() {
        return this.status == TaskStatus.COMPLETE;
    }

    private void debug(String var1, Object... var2) {
        Debug.write("[%s] [%s] [%s] %s", pos.toShortString(), ticks, status, String.format(var1, var2));
    }

    private void debugUpdateStates(String var1, Object... var2) {
        Debug.write("[%s] [%s] [%s] [状态更新] %s", pos.toShortString(), ticks, status, String.format(var1, var2));
    }
}
