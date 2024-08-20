package com.github.bunnyi116.blockminer.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.jetbrains.annotations.Nullable;

public class TaskServerboundMovePlayerPacketManager {
    private static boolean modifyYaw = false;
    private static boolean modifyPitch = false;
    private static float yaw = 0.0F;
    private static float pitch = 0.0F;
    private static int ticks = 0;
    private static @Nullable Task taskHandler = null;

    public static float onModifyLookYaw(float yaw) {
        return TaskServerboundMovePlayerPacketManager.modifyYaw ? TaskServerboundMovePlayerPacketManager.yaw : yaw;
    }

    public static float onModifyLookPitch(float pitch) {
        return TaskServerboundMovePlayerPacketManager.modifyPitch ? TaskServerboundMovePlayerPacketManager.pitch : pitch;
    }

    public static void resetAndSendMovePlayerPacket() {
        reset();
        var minecraft = Minecraft.getInstance();
        var connection = minecraft.getConnection();
        var player = minecraft.player;
        if (connection != null && player != null) {
            connection.send(getLookAndOnGroundPacket(player));
        }
    }

    public static void reset() {
        resetYaw();
        resetPitch();
    }

    public static void resetYaw() {
        TaskServerboundMovePlayerPacketManager.modifyYaw = false;
        TaskServerboundMovePlayerPacketManager.yaw = 0.0F;
    }

    public static void resetPitch() {
        TaskServerboundMovePlayerPacketManager.modifyPitch = false;
        TaskServerboundMovePlayerPacketManager.pitch = 0.0F;
    }

    public static void setAndSendMovePlayerPacket(float yaw, float pitch) {
        setYawPitch(yaw, pitch);
        var minecraft = Minecraft.getInstance();
        var connection = minecraft.getConnection();
        var player = minecraft.player;
        if (connection != null && player != null) {
            connection.send(getLookAndOnGroundPacket(player));
        }
    }

    public static void setYawPitch(float yaw, float pitch) {
        setYaw(yaw);
        setPitch(pitch);
    }

    public static void setYaw(float yaw) {
        TaskServerboundMovePlayerPacketManager.modifyYaw = true;
        TaskServerboundMovePlayerPacketManager.yaw = yaw;
    }

    public static void setPitch(float pitch) {
        TaskServerboundMovePlayerPacketManager.modifyPitch = true;
        TaskServerboundMovePlayerPacketManager.pitch = pitch;
    }

    private static ServerboundMovePlayerPacket getLookAndOnGroundPacket(LocalPlayer player) {
        var yaw = modifyYaw ? TaskServerboundMovePlayerPacketManager.yaw : player.getYRot();
        var pitch = modifyPitch ? TaskServerboundMovePlayerPacketManager.pitch : player.getXRot();
        return new ServerboundMovePlayerPacket.Rot(yaw, pitch, player.onGround());
    }
}
