package com.github.bunnyi116.blockminer.mixin;

import com.github.bunnyi116.blockminer.BlockMinerMod;
import com.github.bunnyi116.blockminer.task.TaskManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Minecraft.class, priority = BlockMinerMod.PRIORITY)
public abstract class MixinMinecraft {
    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Inject(method = "startUseItem", at = @At(value = "HEAD"))
    private void continueAttack(CallbackInfo ci) {
        if (level == null || player == null || hitResult == null) {
            return;
        }
        if (hitResult.getType() == HitResult.Type.BLOCK || player.getMainHandItem().isEmpty()) {
            var blockHitResult = (BlockHitResult) hitResult;
            var blockPos = blockHitResult.getBlockPos();
            var blockState = level.getBlockState(blockPos);
            var block = blockState.getBlock();
            TaskManager.switchOnOff(block);
        }
    }
}
