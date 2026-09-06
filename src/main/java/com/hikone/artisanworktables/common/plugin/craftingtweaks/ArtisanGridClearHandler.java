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
        ClearSecondaryIngredients(grid, player, menu, forced);
        super.clearGrid(grid, player, menu, forced);
    }

    private static void ClearSecondaryIngredients(CraftingGrid grid, Player player, AbstractContainerMenu menu, boolean forced)
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

                    if (!remainder.isEmpty() && forced)
                    {
                        player.drop(remainder, false);
                        secondaryHandler.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
