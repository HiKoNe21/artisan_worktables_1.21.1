package com.hikone.artisanworktables.common.plugin.kubejs.components;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.recipe.ChanceResult;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ItemMatch;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.ResourceLocation;

public record ChanceResultComponent(RecipeComponentType<?> type) implements RecipeComponent<ChanceResult>
{
    public static final RecipeComponentType<ChanceResult> CHANCE_RESULT =
            RecipeComponentType.unit(ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, "chance_result"), ChanceResultComponent::new);

    @Override
    public Codec<ChanceResult> codec()
    {
        return ChanceResult.CODEC;
    }

    @Override
    public TypeInfo typeInfo()
    {
        return TypeInfo.of(ChanceResult.class);
    }

    @Override
    public boolean matches(RecipeMatchContext cx, ChanceResult value, ReplacementMatchInfo match)
    {
        if (match.match() instanceof ItemMatch itemMatch)
        {
            return !value.stack().isEmpty() && itemMatch.matches(cx, value.stack(), match.exact());
        }

        return false;
    }

    @Override
    public boolean isEmpty(ChanceResult value)
    {
        return value.stack().isEmpty();
    }

    @Override
    public String toString()
    {
        return "chance_result";
    }
}
