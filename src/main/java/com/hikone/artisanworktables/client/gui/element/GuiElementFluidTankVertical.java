package com.hikone.artisanworktables.client.gui.element;

import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class GuiElementFluidTankVertical extends GuiElementFluidTankBase
{

    public GuiElementFluidTankVertical(GuiContainerBase<? extends AbstractContainerMenu> guiBase, FluidTank fluidTank, int elementX, int elementY,int elementWidth, int elementHeight)
    {
        super(guiBase, elementX, elementY, elementWidth, elementHeight, fluidTank);
    }

    @Override
    protected int elementHeightModifiedGet()
    {
        int elementHeightModified = (int) (this.scalarPercentageGet() * this.elementHeight);
        int min = Math.min(elementHeightModified, this.elementHeight);
        return Math.max(0, min);
    }

    @Override
    protected int elementYModifiedGet()
    {
        int elementHeightModified = (int) (this.scalarPercentageGet() * this.elementHeight);
        int min = Math.min(elementHeightModified, this.elementHeight);
        return this.elementHeight - Math.max(0, min) + super.elementYModifiedGet();
    }
}
