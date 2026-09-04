package com.hikone.artisanworktables.client.gui;

import com.hikone.artisanworktables.client.gui.element.GuiElementBase;
import com.hikone.artisanworktables.client.gui.element.IGuiElementClickable;
import com.hikone.artisanworktables.client.gui.element.IGuiElementTooltipExtendedProvider;
import com.hikone.artisanworktables.client.gui.element.IGuiElementTooltipProvider;
import com.hikone.artisanworktables.mixin.client.AbstractContainerScreenAccessor;
import com.hikone.artisanworktables.util.TooltipHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiContainerBase<T extends ContainerBase> extends AbstractContainerScreen<T>
{
    private static final float ITEM_RENDER_Z = 232.0F;
    private static final int DRAGGED_STACK_DECORATION_OFFSET = 8;

    protected final List<GuiElementBase> guiElementList;
    protected final List<IGuiElementClickable> guiElementClickableList;
    protected final List<IGuiElementTooltipProvider> tooltipProviderList;
    protected final List<Component> tooltipTextList;

    protected int scaledWidth;
    protected int scaledHeight;

    public GuiContainerBase(T container, Inventory playerInventory, Component title, int width, int height)
    {
        super(container, playerInventory, title);
        this.imageWidth = width;
        this.imageHeight = height;
        this.guiElementList = new ArrayList<>();
        this.guiElementClickableList = new ArrayList<>();
        this.tooltipProviderList = new ArrayList<>();
        this.tooltipTextList = new ArrayList<>();
        this.updateScaledResolution();
    }

    private void updateScaledResolution()
    {
        if (this.minecraft != null && this.minecraft.getWindow() != null)
        {
            this.scaledWidth = this.minecraft.getWindow().getGuiScaledWidth();
            this.scaledHeight = this.minecraft.getWindow().getGuiScaledHeight();
        }
    }

    public Font getFontRenderer()
    {
        return this.font;
    }

    public ItemRenderer getItemRender()
    {
        return Minecraft.getInstance().getItemRenderer();
    }

    public void drawItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String altText)
    {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, ITEM_RENDER_Z);
        guiGraphics.renderItem(stack, x, y);
        Font itemFont = IClientItemExtensions.of(stack).getFont(stack, IClientItemExtensions.FontContext.ITEM_COUNT);
        ItemStack draggingItem = ((AbstractContainerScreenAccessor) this).getDraggingItem();
        int decorationY = y - (draggingItem.isEmpty() ? 0 : DRAGGED_STACK_DECORATION_OFFSET);
        guiGraphics.renderItemDecorations(itemFont == null ? this.font : itemFont, stack, x, decorationY, altText);
        guiGraphics.pose().popPose();
    }

    protected void guiContainerElementAdd(GuiElementBase... elements)
    {
        for (GuiElementBase element : elements)
        {
            this.guiElementList.add(element);

            if (element instanceof IGuiElementClickable)
            {
                this.guiElementClickableList.add((IGuiElementClickable) element);
            }
        }
    }

    public int guiContainerOffsetXGet()
    {
        return (this.scaledWidth - this.imageWidth) / 2;
    }

    public int guiContainerOffsetYGet()
    {
        return (this.scaledHeight - this.imageHeight) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
    {
        this.updateScaledResolution();

        for (GuiElementBase element : this.guiElementList)
        {
            element.update(partialTicks);

            if (element.elementIsVisible(mouseX, mouseY))
            {
                element.drawBackgroundLayer(guiGraphics.pose(), partialTicks, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        this.tooltipProviderList.clear();

        for (GuiElementBase element : this.guiElementList)
        {
            if (element.elementIsVisible(mouseX, mouseY))
            {
                element.drawForegroundLayer(guiGraphics.pose(), mouseX, mouseY);

                if (element instanceof IGuiElementTooltipProvider && element.elementIsMouseInside(mouseX, mouseY))
                {
                    this.tooltipProviderList.add((IGuiElementTooltipProvider) element);
                }
            }
        }

        for (IGuiElementTooltipProvider element : this.tooltipProviderList)
        {
            this.tooltipTextList.clear();

            if (element.elementIsVisible(mouseX, mouseY) && element.elementIsMouseInside(mouseX, mouseY))
            {
                element.tooltipTextGet(this.tooltipTextList, mouseX, mouseY);

                if (element instanceof IGuiElementTooltipExtendedProvider)
                {
                    if (Screen.hasShiftDown())
                    {
                        ((IGuiElementTooltipExtendedProvider) element).tooltipTextExtendedGet(this.tooltipTextList);
                    }
                    else
                    {
                        this.tooltipTextList.add(TooltipHelper.getTooltipHoldShiftTextComponent());
                    }
                }

                guiGraphics.renderComponentTooltip(this.font, this.tooltipTextList, mouseX - this.guiContainerOffsetXGet(), mouseY - this.guiContainerOffsetYGet());
            }
        }
    }

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI).
     */
    public boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY)
    {
        int i = this.leftPos;
        int j = this.topPos;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boolean result = super.mouseClicked(mouseX, mouseY, button);

        for (IGuiElementClickable element : this.guiElementClickableList)
        {
            element.mouseClicked(mouseX, mouseY, button);

            if (((GuiElementBase) element).elementIsMouseInside(mouseX, mouseY) && ((GuiElementBase) element).elementIsVisible(mouseX, mouseY))
            {
                element.elementClicked(mouseX, mouseY, button);
            }
        }

        return result;
    }

    public void drawScaledTexturedModalRectFromIcon(PoseStack poseStack, int x, int y, TextureAtlasSprite icon, int width, int height)
    {
        GuiHelper.drawScaledTexturedModalRectFromIcon(poseStack, x, y, 0, icon, width, height);
    }

    public void drawScaledTexturedModalRectFromIconAnchorBottomLeft(PoseStack poseStack, int x, int y, TextureAtlasSprite icon, int width, int height)
    {
        GuiHelper.drawScaledTexturedModalRectFromIconAnchorBottomLeft(poseStack, x, y, 0, icon, width, height);
    }

    /**
     * Draws a textured square with an optionally rotated texture.
     *
     * @param x        the x
     * @param y        the y
     * @param textureX the texture x
     * @param textureY the texture y
     * @param size     the size
     * @param rotation (clockwise) 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
     */
    public void drawRotatedTexturedModalSquare(PoseStack poseStack, int x, int y, int textureX, int textureY, int size, int rotation)
    {
        GuiHelper.drawRotatedTexturedModalSquare(poseStack, x, y, 0, textureX, textureY, size, rotation);
    }

    public void drawString(PoseStack poseStack, String text, int x, int y)
    {
        this.drawString(poseStack, text, x, y, Color.WHITE.getRGB());
    }

    public void drawString(PoseStack poseStack, String text, int x, int y, int color)
    {
        this.font.drawInBatch(text, (float) x, (float) y, color, false, poseStack.last().pose(), Minecraft.getInstance().renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public int getGuiLeft()
    {
        return this.leftPos;
    }

    public int getGuiTop()
    {
        return this.topPos;
    }
}
