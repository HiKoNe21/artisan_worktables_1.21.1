package com.hikone.artisanworktables.common.plugin.craftingtweaks;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.container.BaseContainer;
import net.blay09.mods.craftingtweaks.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ArtisanCraftingGrid implements CraftingGrid
{
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, "artisan_grid");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    public Container getCraftingMatrix(Player player, AbstractContainerMenu menu)
    {
        if (menu instanceof BaseContainer baseContainer)
        {
            return baseContainer.getTile().getInventory(player);
        }
        return null;
    }

    @Override
    public int getGridStartSlot(Player player, AbstractContainerMenu menu)
    {
        if (menu instanceof BaseContainer container)
        {
            return container.slotIndexCraftingMatrixStart;
        }
        return 0;
    }

    @Override
    public int getGridSize(Player player, AbstractContainerMenu menu)
    {
        if (menu instanceof BaseContainer container)
        {
            return (container.slotIndexCraftingMatrixEnd - container.slotIndexCraftingMatrixStart) + 1;
        }
        return 0;
    }

    @Override
    public GridClearHandler<AbstractContainerMenu> clearHandler()
    {
        return new ArtisanGridClearHandler();
    }
}
