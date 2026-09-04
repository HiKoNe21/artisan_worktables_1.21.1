package com.hikone.artisanworktables.client.screen.element;

import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.hikone.artisanworktables.client.gui.Texture;
import com.hikone.artisanworktables.client.gui.element.GuiElementTextureRectangle;
import com.hikone.artisanworktables.common.tile.IBlockEntityDesigner;
import com.mojang.blaze3d.vertex.PoseStack;

public class GuiElementDesignersSide extends GuiElementTextureRectangle
{
    private final IBlockEntityDesigner tile;

    public GuiElementDesignersSide(GuiContainerBase guiBase, IBlockEntityDesigner tile, Texture texture, int elementX, int elementY)
    {
        super(guiBase, texture, elementX, elementY, 68, 176);
        this.tile = tile;
    }

    @Override
    public void drawBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        if (this.tile != null && !this.tile.getTileEntity().isRemoved())
        {
            super.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        }
    }
}
