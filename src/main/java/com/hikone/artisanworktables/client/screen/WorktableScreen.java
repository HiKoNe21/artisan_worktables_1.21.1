package com.hikone.artisanworktables.client.screen;

import com.hikone.artisanworktables.common.container.BaseContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class WorktableScreen extends BaseScreen
{
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;

    public WorktableScreen(BaseContainer container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title, WIDTH, HEIGHT);
    }
}
