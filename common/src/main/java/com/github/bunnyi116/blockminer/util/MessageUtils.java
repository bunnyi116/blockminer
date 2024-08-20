package com.github.bunnyi116.blockminer.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static net.minecraft.network.chat.Component.literal;

public class MessageUtils {

    public static void setOverlayMessage(Component message) {
        Minecraft.getInstance().gui.setOverlayMessage(message, false);
    }

    public static void setOverlayMessage(String message) {
        setOverlayMessage(literal(message));
    }

    public static void addMessage(Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }

    public static void addMessage(String message) {
        addMessage(literal(message));
    }
}
