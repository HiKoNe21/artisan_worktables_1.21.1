package com.hikone.artisanworktables.common.recipe;

import com.hikone.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.hikone.artisanworktables.common.reference.EnumTier;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;

public class RecipeTierCalculator
{

    @Nullable
    public static EnumTier calculateTier(int width, int height, int toolCount, int secondaryIngredientCount, SizedFluidIngredient fluidIngredient)
    {
        // test for tier one requirements
        if (width <= 3 && height <= 3 && toolCount <= 1 && secondaryIngredientCount == 0)
        {
            if (fluidIngredient == null || fluidIngredient.amount() <= ArtisanWorktablesModCommonConfig.fluidCapacityWorktable)
            {
                return EnumTier.WORKTABLE;
            }
        }

        // test for tier two requirements
        if (width <= 3 && height <= 3 && toolCount <= 2)
        {
            if (fluidIngredient == null || fluidIngredient.amount() <= ArtisanWorktablesModCommonConfig.fluidCapacityWorkstation)
            {
                return EnumTier.WORKSTATION;
            }
        }

        // test for tier three requirements
        if (width <= 5 && height <= 5 && toolCount <= 3)
        {
            if (fluidIngredient == null || fluidIngredient.amount() <= ArtisanWorktablesModCommonConfig.fluidCapacityWorkshop)
            {
                return EnumTier.WORKSHOP;
            }
        }

        return null;
    }

    private RecipeTierCalculator()
    {
        //
    }
}
