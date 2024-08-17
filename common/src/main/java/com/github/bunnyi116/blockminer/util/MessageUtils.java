package com.github.bunnyi116.blockminer.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.Minecart;

public class MessageUtils {

    public static void setOverlayMessage(Component message) {
        Minecraft.getInstance().gui.setOverlayMessage(message, false);
    }

    public static void addMessage(Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }
}
