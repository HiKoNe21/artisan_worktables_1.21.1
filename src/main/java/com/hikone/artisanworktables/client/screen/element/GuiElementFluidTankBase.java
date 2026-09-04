package com.hikone.artisanworktables.client.screen.element;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.hikone.artisanworktables.client.gui.element.GuiElementFluidTankVertical;
import com.hikone.artisanworktables.common.network.CSPacketWorktableTankDestroyFluid;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class GuiElementFluidTankBase extends GuiElementFluidTankVertical
{
    protected final BlockPos blockPos;
    protected final int overlayColor;

    public GuiElementFluidTankBase(GuiContainerBase guiBase, FluidTank fluidTank, int elementX, int elementY, int elementWidth, int elementHeight, int overlayColor, BlockPos blockPos)
    {
        super(guiBase, fluidTank, elementX, elementY, elementWidth, elementHeight);
        this.overlayColor = overlayColor;
        this.blockPos = blockPos;
    }

    public void elementClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (mouseButton == 0 && Screen.hasShiftDown())
        {
            ArtisanWorktablesMod.getProxy().getPacketService().sendToServer(new CSPacketWorktableTankDestroyFluid(this.blockPos));
        }
    }

    public List<Component> tooltipTextGet(List<Component> list, double mouseX, double mouseY)
    {
        if (this.fluidTank.getFluid() == FluidStack.EMPTY || this.fluidTank.getFluidAmount() == 0)
        {
            list.add(Component.translatable("gui.artisanworktables.tooltip.fluid.empty"));
            list.add(Component.literal(this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB").withStyle(ChatFormatting.GRAY));
        }
        else
        {
            Fluid fluid = this.fluidTank.getFluid().getFluid();
            list.add(fluid.getFluidType().getDescription(this.fluidTank.getFluid()));
            list.add(Component.literal(this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("gui.artisanworktables.tooltip.fluid.destroy").withStyle(ChatFormatting.DARK_GRAY));
        }

        return list;
    }
}
