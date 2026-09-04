package com.hikone.artisanworktables.client.gui.element;

import com.hikone.artisanworktables.client.gui.ContainerBase;
import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.hikone.artisanworktables.client.gui.GuiHelper;
import com.hikone.artisanworktables.client.gui.Texture;
import com.mojang.blaze3d.vertex.PoseStack;

public class GuiElementTextureRectangle extends GuiElementTextureBase
{
    public GuiElementTextureRectangle(GuiContainerBase<? extends ContainerBase> guiBase, Texture texture, int elementX, int elementY, int elementWidth, int elementHeight)
    {
        this(guiBase, new Texture[]{texture}, elementX, elementY, elementWidth, elementHeight);
    }

    public GuiElementTextureRectangle(GuiContainerBase<? extends ContainerBase> guiBase, Texture[] textures, int elementX, int elementY, int elementWidth, int elementHeight)
    {
        super(guiBase, elementX, elementY, elementWidth, elementHeight, textures);
    }

    @Override
    public void drawBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        Texture texture = this.textureGet(mouseX, mouseY);

        this.textureBind(texture);
        this.elementDraw(poseStack, texture);
    }

    protected void elementDraw(PoseStack poseStack, Texture texture)
    {
        GuiHelper.drawModalRectWithCustomSizedTexture(poseStack, this.elementXModifiedGet(), this.elementYModifiedGet(), 0, this.texturePositionXModifiedGet(texture), this.texturePositionYModifiedGet(texture), this.elementWidthModifiedGet(), this.elementHeightModifiedGet(), texture.getWidth(), texture.getHeight());
    }

    @Override
    public void drawForegroundLayer(PoseStack poseStack, int mouseX, int mouseY)
    {
        //
    }
}
