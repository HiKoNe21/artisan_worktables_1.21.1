package com.hikone.artisanworktables.common.plugin.kubejs.wrapper;

import com.hikone.artisanworktables.common.recipe.ChanceResult;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public interface ChanceResultWrapper
{
    static ChanceResult of(ChanceResult chanceResult)
    {
        return chanceResult;
    }

    static ChanceResult of(ItemStack stack, float chance)
    {
        return new ChanceResult(stack, chance);
    }

    static ChanceResult wrap(Context context, @Nullable Object o)
    {
        while (o instanceof Wrapper w)
        {
            o = w.unwrap();
        }

        if (o == null || o == ItemStack.EMPTY || o == Items.AIR || o == Ingredient.EMPTY)
        {
            return new ChanceResult(ItemStack.EMPTY, 0);
        }

        return new ChanceResult(ItemWrapper.wrap(context, o), 1);
    }
}
