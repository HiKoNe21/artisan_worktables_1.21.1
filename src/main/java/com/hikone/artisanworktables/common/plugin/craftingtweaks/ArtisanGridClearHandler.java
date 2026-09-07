package com.hikone.artisanworktables.common.plugin.craftingtweaks;

import com.hikone.artisanworktables.common.container.BaseContainer;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridClearHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ArtisanGridClearHandler extends DefaultGridClearHandler
{
    @Override
    public void clearGrid(CraftingGrid grid, Player player, AbstractContainerMenu menu, boolean forced)
    {
        if (forced)
        {
            ClearTools(grid, player, menu);
            ClearSecondaryIngredients(grid, player, menu);
        }
        super.clearGrid(grid, player, menu, false);
    }

    private static void ClearTools(CraftingGrid grid, Player player, AbstractContainerMenu menu)
    {
        if (menu instanceof BaseContainer baseContainer && baseContainer.getTile() != null)
        {
            var toolHandler = baseContainer.getTile().getToolHandler();

            for (int i = 0; i < toolHandler.getSlots(); i++)
            {
                ItemStack itemStack = toolHandler.getStackInSlot(i);
                if (itemStack.isEmpty())
                {
                    continue;
                }

                ItemStack remainder = itemStack.copy();

                if (baseContainer.canPlayerUseToolbox())
                {
                    baseContainer.mergeToolbox(remainder, false);
                }
                else
                {
                    player.getInventory().add(remainder);
                }

                if (remainder.getCount() != itemStack.getCount())
                {
                    toolHandler.setStackInSlot(i, remainder.isEmpty() ? ItemStack.EMPTY : remainder);
                }
            }
        }
    }

    private static void ClearSecondaryIngredients(CraftingGrid grid, Player player, AbstractContainerMenu menu)
    {
        if (menu instanceof BaseContainer baseContainer && baseContainer.getTile() != null)
        {
            if (baseContainer.slotIndexSecondaryInputStart == -1)
            {
                return;
            }

            var secondaryHandler = baseContainer.getTile().getSecondaryIngredientHandler();

            for (int i = 0; i < secondaryHandler.getSlots(); i++)
            {
                ItemStack itemStack = secondaryHandler.getStackInSlot(i);

                if (!itemStack.isEmpty())
                {
                    ItemStack remainder = itemStack.copy();

                    player.getInventory().add(remainder);

                    if (remainder.getCount() != itemStack.getCount())
                    {
                        secondaryHandler.setStackInSlot(i, remainder.isEmpty() ? ItemStack.EMPTY : remainder);
                    }
                }
            }
        }
    }
}
