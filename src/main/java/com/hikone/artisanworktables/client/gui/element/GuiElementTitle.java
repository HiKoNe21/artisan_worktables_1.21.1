package com.hikone.artisanworktables.client.gui.element;

import com.hikone.artisanworktables.client.gui.ContainerBase;
import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.mojang.blaze3d.vertex.PoseStack;

public class GuiElementTitle extends GuiElementBase
{
    private final String titleKey;

    public GuiElementTitle(GuiContainerBase<? extends ContainerBase> guiBase, String titleKey, int elementX, int elementY)
    {
        // element width and height don't matter
        super(guiBase, elementX, elementY, 0, 0);
        this.titleKey = titleKey;
    }

    @Override
    public void drawBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        //
    }

    @Override
    public void drawForegroundLayer(PoseStack poseStack, int mouseX, int mouseY)
    {
        this.guiBase.drawString(poseStack, this.titleKey, this.elementX, this.elementY);
    }
}
