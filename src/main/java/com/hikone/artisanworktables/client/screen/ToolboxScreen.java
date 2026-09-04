package com.hikone.artisanworktables.client.screen;

import com.hikone.artisanworktables.client.ReferenceTexture;
import com.hikone.artisanworktables.client.gui.Texture;
import com.hikone.artisanworktables.common.container.ToolboxContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ToolboxScreen extends ToolboxBaseScreen<ToolboxContainer>
{
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;

    public ToolboxScreen(ToolboxContainer container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title, WIDTH, HEIGHT);
    }

    @Override
    protected Texture getTexture()
    {
        return ReferenceTexture.TEXTURE_TOOLBOX;
    }
}
