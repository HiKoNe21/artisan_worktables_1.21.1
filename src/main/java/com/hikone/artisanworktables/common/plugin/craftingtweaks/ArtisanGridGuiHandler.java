package com.hikone.artisanworktables.common.plugin.craftingtweaks;

import com.hikone.artisanworktables.client.screen.BaseScreen;
import com.hikone.artisanworktables.client.screen.WorkshopScreen;
import net.blay09.mods.craftingtweaks.api.*;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.function.Consumer;

public class ArtisanGridGuiHandler implements GridGuiHandler
{
    public ArtisanGridGuiHandler()
    {
        CraftingTweaksClientAPI.registerCraftingGridGuiHandler(BaseScreen.class ,this);
    }

    @Override
    public void createButtons(AbstractContainerScreen<?> screen, CraftingGrid grid, Consumer<AbstractWidget> addWidgetFunc)
    {
        int x = screen.getGuiLeft() - 11;
        int y = screen.getGuiTop();

        if (screen instanceof WorkshopScreen)
            y += 18;

        addWidgetFunc.accept(CraftingTweaksClientAPI.createTweakButton(grid, x, y + 17, TweakType.Rotate, CraftingTweaksButtonStyles.SMALL_WIDTH));
        addWidgetFunc.accept(CraftingTweaksClientAPI.createTweakButton(grid, x, y + 35, TweakType.Balance, CraftingTweaksButtonStyles.SMALL_WIDTH));
        addWidgetFunc.accept(CraftingTweaksClientAPI.createTweakButton(grid, x, y + 53, TweakType.Clear, CraftingTweaksButtonStyles.SMALL_WIDTH));
    }
}
