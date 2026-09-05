package com.hikone.artisanworktables.common.plugin.kubejs.wrapper;

import com.hikone.artisanworktables.common.recipe.ToolEntry;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.IngredientWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public interface ToolEntryWrapper
{
    static ToolEntry of(ToolEntry ToolEntry)
    {
        return ToolEntry;
    }

    static ToolEntry of(Ingredient ingredient)
    {
        return new ToolEntry(ingredient, 1);
    }

    static ToolEntry of(Ingredient ingredient, int damage)
    {
        return new ToolEntry(ingredient, damage);
    }

    static ToolEntry of(Ingredient ingredient, int damage, boolean matchNbt)
    {
        return new ToolEntry(ingredient, damage, matchNbt);
    }

    static ToolEntry of(Ingredient ingredient, int damage, boolean matchNbt, ItemAbility itemAbility)
    {
        return new ToolEntry(ingredient, damage, matchNbt, itemAbility);
    }

    static ToolEntry wrap(Context context, @Nullable Object o)
    {
        while (o instanceof Wrapper w)
        {
            o = w.unwrap();
        }

        if (o == null || o == ItemStack.EMPTY || o == Items.AIR || o == Ingredient.EMPTY)
        {
            return new ToolEntry(Ingredient.EMPTY, 0);
        }

        var ingredient = IngredientWrapper.wrap(context, o);
        var items = ingredient.getItems();
        boolean isDamageable = false;
        if (items.length > 0)
        {
            isDamageable = items[0].isDamageableItem();
        }

        return new ToolEntry(ingredient, isDamageable ? 1 : 0);
    }
}
