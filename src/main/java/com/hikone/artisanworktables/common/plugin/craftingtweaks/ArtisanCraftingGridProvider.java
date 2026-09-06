package com.hikone.artisanworktables.common.plugin.craftingtweaks;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.container.BaseContainer;
import net.blay09.mods.craftingtweaks.api.CraftingGridBuilder;
import net.blay09.mods.craftingtweaks.api.CraftingGridProvider;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ArtisanCraftingGridProvider implements CraftingGridProvider
{
    public ArtisanCraftingGridProvider()
    {
        CraftingTweaksAPI.registerCraftingGridProvider(this);
    }

    @Override
    public String getModId()
    {
        return ArtisanWorktablesMod.MODID;
    }

    @Override
    public boolean requiresServerSide()
    {
        return true;
    }

    @Override
    public boolean handles(AbstractContainerMenu menu)
    {
        return menu instanceof BaseContainer;
    }

    @Override
    public void buildCraftingGrids(CraftingGridBuilder builder, AbstractContainerMenu menu)
    {
        if (menu instanceof BaseContainer)
        {
            builder.addCustomGrid(new ArtisanCraftingGrid());
        }
    }
}
