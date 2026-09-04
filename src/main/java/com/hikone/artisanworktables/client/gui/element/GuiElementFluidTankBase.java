package com.hikone.artisanworktables.client.gui.element;

import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public abstract class GuiElementFluidTankBase extends GuiElementTiledTextureAtlasSpriteBase
{
    protected final FluidTank fluidTank;
    private TextureAtlasSprite fluidSprite;

    public GuiElementFluidTankBase(GuiContainerBase<? extends AbstractContainerMenu> guiBase, int elementX, int elementY, int elementWidth, int elementHeight, FluidTank fluidTank)
    {
        super(guiBase, elementX, elementY, elementWidth, elementHeight);
        this.fluidTank = fluidTank;
    }

    protected float scalarPercentageGet()
    {

        int fluidAmount = this.fluidTank.getFluidAmount();

        if (fluidAmount > 0)
        {
            int capacity = this.fluidTank.getCapacity();
            return Math.max((float) fluidAmount / (float) capacity, 1 / (float) this.elementHeight);
        }

        return 0;
    }

    @Override
    protected TextureAtlasSprite textureAtlasSpriteGet()
    {
        FluidStack fluidStack = this.fluidTank.getFluid();

        if (fluidStack.isEmpty())
        {
            this.fluidSprite = null;
        }
        else if (this.fluidSprite == null)
        {
            IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
            ResourceLocation resourceLocation = renderProperties.getStillTexture();
            this.fluidSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resourceLocation);
        }

        return this.fluidSprite;
    }

    @Override
    public void drawBackgroundLayer(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        FluidStack fluid = this.fluidTank.getFluid();

        if (fluid.isEmpty())
        {
            super.drawBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
            return;
        }

        // Fluid still textures (e.g. water) are greyscale and require the fluid's tint
        // color to be applied as the shader color, otherwise they render grey.
        int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;

        if (a == 0f)
        {
            a = 1f;
        }

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(r, g, b, a);

        super.drawBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
