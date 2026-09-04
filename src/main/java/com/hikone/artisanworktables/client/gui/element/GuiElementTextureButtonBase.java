package com.hikone.artisanworktables.client.gui.element;

import com.hikone.artisanworktables.client.gui.ContainerBase;
import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.hikone.artisanworktables.client.gui.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public abstract class GuiElementTextureButtonBase extends GuiElementTextureRectangle implements IGuiElementClickable
{
    private static final int TEXTURE_BASE_INDEX = 0;
    private static final int TEXTURE_HOVERED_INDEX = 1;

    public GuiElementTextureButtonBase(GuiContainerBase<? extends ContainerBase> guiBase, Texture[] textures, int elementX, int elementY, int elementWidth, int elementHeight)
    {
        super(guiBase, textures, elementX, elementY, elementWidth, elementHeight);
    }

    @Override
    protected int textureIndexGet(int mouseX, int mouseY)
    {
        if (this.elementIsMouseInside(mouseX, mouseY))
        {
            return TEXTURE_HOVERED_INDEX;
        }

        return TEXTURE_BASE_INDEX;
    }

    @Override
    public void elementClicked(double mouseX, double mouseY, int mouseButton)
    {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
    }
}
