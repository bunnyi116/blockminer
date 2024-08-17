package com.github.bunnyi116.blockminer.task;

import com.github.bunnyi116.blockminer.Debug;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public class Task {
    public final ClientLevel level;
    public final Block block;
    public final BlockPos pos;
    private int ticks;
    private int tickTotalMax;
    private int tickTimeoutMax;
    private boolean timeout;
    private TaskStatus status;

    public Task(ClientLevel level, Block block, BlockPos pos) {
        this.level = level;
        this.block = block;
        this.pos = pos;
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
            case COMPLETE -> complete();
        }
        debug("结束");
        ++this.ticks;
    }

    private void complete() {
        debug("任务已结束");
    }

    private void timeout() {
        debug("任务已超时");
        this.status = TaskStatus.COMPLETE;
    }

    private void waitGameUpdate() {
    }

    private void waitCustomUpdate() {
    }

    public void init() {
        this.ticks = 0;
        this.tickTotalMax = 100;
        this.tickTimeoutMax = 25;
        this.status = TaskStatus.WAIT_GAME_UPDATE;
        debug("任务已初始化");
    }

    public boolean isComplete() {
        return this.status == TaskStatus.COMPLETE;
    }

    private void debug(String var1, Object... var2) {
        Debug.alwaysWrite("[%s] [%s] [%s] %s", pos.toShortString(), ticks, status, String.format(var1, var2));
    }
}
