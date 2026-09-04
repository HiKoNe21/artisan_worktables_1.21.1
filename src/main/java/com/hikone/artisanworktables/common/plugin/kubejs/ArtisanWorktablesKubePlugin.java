package com.hikone.artisanworktables.common.plugin.kubejs;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.reference.EnumType;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.minecraft.resources.ResourceLocation;

public class ArtisanWorktablesKubePlugin implements KubeJSPlugin
{
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry)
    {
        for (EnumType type : EnumType.values())
        {
            registry.register(ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, type.getName() + "_shaped"), ArtisanRecipeSchemas.SHAPED);
            registry.register(ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, type.getName() + "_shapeless"), ArtisanRecipeSchemas.SHAPELESS);
        }
    }
}
