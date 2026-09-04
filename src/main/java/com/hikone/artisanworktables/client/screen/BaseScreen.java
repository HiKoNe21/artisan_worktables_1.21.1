package com.hikone.artisanworktables.client.screen;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.client.ReferenceTexture;
import com.hikone.artisanworktables.client.gui.GuiContainerBase;
import com.hikone.artisanworktables.client.gui.GuiHelper;
import com.hikone.artisanworktables.client.gui.Texture;
import com.hikone.artisanworktables.client.gui.element.GuiElementTextureRectangle;
import com.hikone.artisanworktables.client.screen.element.GuiElementFluidTankSmall;
import com.hikone.artisanworktables.client.screen.element.GuiElementMageEffect;
import com.hikone.artisanworktables.client.screen.element.GuiElementTabs;
import com.hikone.artisanworktables.client.screen.element.GuiElementToolboxSide;
import com.hikone.artisanworktables.common.container.BaseContainer;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.hikone.artisanworktables.common.tile.BaseBlockEntity;
import com.hikone.artisanworktables.common.tile.ToolboxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.awt.*;

public abstract class BaseScreen extends GuiContainerBase<BaseContainer>
{
    protected final int textColor;
    private final BaseBlockEntity tile;

    public BaseScreen(BaseContainer container, Inventory playerInventory, Component title, int width, int height)
    {
        super(container, playerInventory, title, width, height);

        // This needs to be set here because the playerInventoryTitleY is calculated
        // from the imageHeight in the call to super ctor, but the correct value of imageHeight is
        // set after the call to super ctor.
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 93;

        // background texture
        this.guiContainerElementAdd(new GuiElementTextureRectangle(this, new Texture(this.getBackgroundTexture(), 0, 0, 256, 256), 0, 0, this.imageWidth, this.imageHeight));

        EnumType tableType = container.getTile().getTableType();
        this.textColor = TextColorProvider.getColorFor(tableType);

        this.tile = this.getMenu().getTile();

        this.addFluidTankElement(this.tile, this.textColor);

        // toolbox side
        ToolboxBlockEntity toolbox = container.getToolbox();

        if (toolbox != null && container.canPlayerUseToolbox())
        {
            this.guiContainerElementAdd(new GuiElementToolboxSide(this, toolbox, toolbox.isMechanical() ? ReferenceTexture.TEXTURE_TOOLBOX_MECHANICAL_SIDE : ReferenceTexture.TEXTURE_TOOLBOX_SIDE, -70, () -> 0));
        }

        // tabs
        this.guiContainerElementAdd(new GuiElementTabs(this, this.tile, 176));

        if (tableType == EnumType.MAGE)
        {
            this.guiContainerElementAdd(new GuiElementMageEffect(this, container, 115, 35));
        }
    }

    protected void addFluidTankElement(BaseBlockEntity tile, int overlayColor)
    {
        this.guiContainerElementAdd(new GuiElementFluidTankSmall(this, tile.getTank(), tile.getBlockPos(), overlayColor, 8, 17));
    }

    public <T extends BlockEntity> T getTile()
    {
        //noinspection unchecked
        return (T) this.menu.getTile();
    }

    protected ResourceLocation getBackgroundTexture()
    {
        BaseBlockEntity tile = this.menu.getTile();
        String typeName = tile.getTableType().getName();
        String tierName = tile.getTableTier().getName();
        return ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, "textures/gui/" + tierName + "_" + typeName + ".png");
    }

    @Override
    protected void init()
    {
        super.init();
        if (menu.mouseX > 0 && menu.mouseY > 0)
        {
            double guiScale = this.minecraft.getWindow().getGuiScale();
            GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), menu.mouseX * guiScale, menu.mouseY * guiScale);
        }
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        PoseStack matrixStack = guiGraphics.pose();

        if (this.tile.getTableType() == EnumType.MAGE)
        {
            GuiHelper.drawStringOutlined(matrixStack, this.maybeGalactic(this.title), this.titleLabelX, this.titleLabelY, this.font, this.textColor, false);
            GuiHelper.drawStringOutlined(matrixStack, this.maybeGalactic(Component.translatable("container.inventory")), this.inventoryLabelX, this.inventoryLabelY, this.font, this.textColor, false);
        }
        else if (this.tile.getTableType() == EnumType.DESIGNER)
        {
            guiGraphics.drawString(this.getFontRenderer(), this.title, this.titleLabelX, this.titleLabelY, Color.WHITE.getRGB());
            guiGraphics.drawString(this.getFontRenderer(), Component.translatable("container.inventory"), this.inventoryLabelX, this.inventoryLabelY, Color.WHITE.getRGB());
        }
        else
        {
            GuiHelper.drawStringOutlined(matrixStack, this.title, this.titleLabelX, this.titleLabelY, this.font, this.textColor, false);
            GuiHelper.drawStringOutlined(matrixStack, Component.translatable("container.inventory"), this.inventoryLabelX, this.inventoryLabelY, this.font, this.textColor, false);
        }
    }

    /**
     * The Standard Galactic Alphabet font ({@code minecraft:alt}) only provides
     * glyphs for basic ASCII. Applying it to non-ASCII text (e.g. CJK) renders
     * missing-glyph boxes, so the galactic style is only applied when every
     * character is ASCII; otherwise the text is returned unchanged.
     */
    private Component maybeGalactic(Component component)
    {
        String text = component.getString();

        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) > 0x7F)
            {
                return component;
            }
        }

        return GuiHelper.asGalactic(component.copy());
    }

    public void drawString(GuiGraphics guiGraphics, String translateKey, int x, int y)
    {
        Font fontRenderer = this.getFontRenderer();
        PoseStack matrixStack = guiGraphics.pose();

        if (this.tile.getTableType() == EnumType.MAGE)
        {
            GuiHelper.drawStringOutlined(matrixStack, this.maybeGalactic(Component.translatable(translateKey)), x, y, fontRenderer, this.textColor);
        }
        else if (this.tile.getTableType() == EnumType.DESIGNER)
        {
            String displayText = I18n.get(translateKey);
            guiGraphics.drawString(fontRenderer, displayText, x - 1, y, Color.WHITE.getRGB());
        }
        else
        {
            GuiHelper.drawStringOutlined(matrixStack, Component.translatable(translateKey), x, y, fontRenderer, this.textColor);
        }
    }
}