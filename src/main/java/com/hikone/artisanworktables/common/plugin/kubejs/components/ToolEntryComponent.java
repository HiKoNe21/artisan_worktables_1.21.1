package com.hikone.artisanworktables.common.plugin.kubejs.components;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.recipe.ToolEntry;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ItemMatch;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.ResourceLocation;

public record ToolEntryComponent(RecipeComponentType<?> type) implements RecipeComponent<ToolEntry>
{
    public static final RecipeComponentType<ToolEntry> TOOL_ENTRY =
            RecipeComponentType.unit(ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, "tool_entry"), ToolEntryComponent::new);

    @Override
    public Codec<ToolEntry> codec()
    {
        return ToolEntry.CODEC;
    }

    @Override
    public TypeInfo typeInfo()
    {
        return TypeInfo.of(ToolEntry.class);
    }

    @Override
    public boolean matches(RecipeMatchContext cx, ToolEntry value, ReplacementMatchInfo match)
    {
        if (match.match() instanceof ItemMatch itemMatch)
        {
            return !value.getTool().isEmpty() && itemMatch.matches(cx, value.getTool(), match.exact());
        }

        return false;
    }

    @Override
    public boolean isEmpty(ToolEntry value)
    {
        return value.getTool().isEmpty();
    }

    @Override
    public String toString()
    {
        return "tool_entry";
    }
}
