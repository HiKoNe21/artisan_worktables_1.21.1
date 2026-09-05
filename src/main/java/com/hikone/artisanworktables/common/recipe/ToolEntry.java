package com.hikone.artisanworktables.common.recipe;

import com.hikone.artisanworktables.api.IToolHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ToolEntry
{
    public static final Codec<ToolEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.optionalFieldOf("ingredient", Ingredient.EMPTY).forGetter(ToolEntry::getTool),
            Codec.INT.optionalFieldOf("damage", 1).forGetter(ToolEntry::getDamage),
            Codec.BOOL.optionalFieldOf("matchNbt", false).forGetter(ToolEntry::matchNbt),
            ItemAbility.CODEC.optionalFieldOf("toolAction").forGetter(tool -> Optional.ofNullable(tool.getItemAbility()))
    ).apply(instance, (ingredient, damage, matchNbt, ability) ->
            new ToolEntry(ingredient, damage, matchNbt, ability.orElse(null))));

    private static final ThreadLocal<List<ItemStack>> TOOL_CACHE = ThreadLocal.withInitial(ArrayList::new);

    private static ItemStack cache(ItemStack itemStack)
    {
        if (!itemStack.getComponentsPatch().isEmpty())
        {
            return itemStack;
        }

        List<ItemStack> list = TOOL_CACHE.get();

        for (ItemStack stack : list)
        {
            if (stack.getItem() == itemStack.getItem())
            {
                return stack;
            }
        }

        list.add(itemStack);
        return itemStack;
    }

    private final Ingredient tool;
    private final ItemStack[] toolItemStacks;
    private final int damage;
    private final boolean matchNbt;
    @Nullable
    private final ItemAbility itemAbility;

    public ToolEntry(Ingredient tool, int damage)
    {
        this(tool, damage, false);
    }

    public ToolEntry(Ingredient tool, int damage, boolean matchNbt)
    {
        this(tool, damage, matchNbt, null);
    }

    public ToolEntry(Ingredient tool, int damage, boolean matchNbt, @Nullable ItemAbility itemAbility)
    {
        this.tool = tool;
        this.damage = damage;
        this.matchNbt = matchNbt;
        this.itemAbility = itemAbility;

        ItemStack[] matchingStacks = tool.getItems();
        this.toolItemStacks = new ItemStack[matchingStacks.length];

        for (int i = 0; i < matchingStacks.length; i++)
        {
            this.toolItemStacks[i] = ToolEntry.cache(matchingStacks[i]);
        }
    }

    public ItemStack[] getToolStacks()
    {
        return this.toolItemStacks;
    }

    public Ingredient getTool()
    {
        return this.tool;
    }

    public int getDamage()
    {
        return this.damage;
    }

    public boolean matchNbt()
    {
        return this.matchNbt;
    }

    @Nullable
    public ItemAbility getItemAbility()
    {
        return this.itemAbility;
    }

    public boolean matches(IToolHandler handler, ItemStack tool)
    {
        for (ItemStack toolItemStack : this.toolItemStacks)
        {
            if (handler.matches(tool, toolItemStack))
            {
                if (!this.matchNbt)
                {
                    return true;
                }

                // When matchNbt is set the data components must match too (ignoring durability).
                // The match reference is the explicit match stack when present, otherwise the
                // ingredient's own stack.
                // ItemStack reference = (this.matchStack != null) ? this.matchStack : toolItemStack;

                if (ToolEntry.componentsMatch(toolItemStack, tool))
                {
                    return true;
                }
            }
        }

        return !this.matchNbt && this.itemAbility != null && handler.canPerformAction(tool, this.itemAbility);
    }

    private static boolean componentsMatch(ItemStack expected, ItemStack actual)
    {
        return Objects.equals(ToolEntry.componentsIgnoringDamage(expected), ToolEntry.componentsIgnoringDamage(actual));
    }

    private static Map<DataComponentType<?>, Optional<?>> componentsIgnoringDamage(ItemStack itemStack)
    {
        Map<DataComponentType<?>, Optional<?>> map = new HashMap<>();

        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : itemStack.getComponentsPatch().entrySet())
        {
            if (entry.getKey() == DataComponents.DAMAGE)
            {
                continue;
            }

            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }
}
