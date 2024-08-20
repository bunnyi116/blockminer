package com.github.bunnyi116.blockminer.mixin;

import com.github.bunnyi116.blockminer.BlockMinerMod;
import com.github.bunnyi116.blockminer.task.TaskServerboundMovePlayerPacketManager;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ServerboundMovePlayerPacket.class, priority = BlockMinerMod.PRIORITY)
public abstract class MixinServerboundMovePlayerPacket {
    @ModifyVariable(method = "<init>(DDDFFZZZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyLookYaw(float yaw) {
        return TaskServerboundMovePlayerPacketManager.onModifyLookYaw(yaw);
    }

    @ModifyVariable(method = "<init>(DDDFFZZZ)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static float modifyLookPitch(float pitch) {
        return TaskServerboundMovePlayerPacketManager.onModifyLookPitch(pitch);
    }
}
