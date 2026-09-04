package com.hikone.artisanworktables.client.gui.element;

import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class GuiElementItemStack extends GuiElementBase implements IGuiElementTooltipProvider
{
    protected final Supplier<ItemStack> itemStackSupplier;
    protected final float alpha;

    public GuiElementItemStack(Supplier<ItemStack> itemStackSupplier, float alpha, GuiContainerBase<? extends AbstractContainerMenu> guiBase, int elementX, int elementY)
    {
        super(guiBase, elementX, elementY, 16, 16);
        this.itemStackSupplier = itemStackSupplier;
        this.alpha = alpha;
    }

    @Override
    public void drawBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        ItemStack stack = this.itemStackSupplier.get();

        if (!stack.isEmpty())
        {
            GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 32);

            this.guiBase.drawItemStack(guiGraphics, stack, this.elementXModifiedGet(), this.elementYModifiedGet(), null);

            guiGraphics.pose().popPose();
        }
    }

    @Override
    public void drawForegroundLayer(PoseStack poseStack, int mouseX, int mouseY)
    {
        //
    }

    @Override
    public List<Component> tooltipTextGet(List<Component> tooltip)
    {
        ItemStack itemStack = this.itemStackSupplier.get();
        Minecraft minecraft = Minecraft.getInstance();

        if (!itemStack.isEmpty() && minecraft.player != null && minecraft.player.containerMenu.getCarried().isEmpty())
        {
            TooltipFlag tooltipFlag = minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
            List<Component> itemStackTooltip = itemStack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, tooltipFlag);
            tooltip.addAll(itemStackTooltip);
        }

        return tooltip;
    }
}
