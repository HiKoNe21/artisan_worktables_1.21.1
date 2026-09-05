package com.hikone.artisanworktables.common.plugin.kubejs;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.plugin.kubejs.components.ChanceResultComponent;
import com.hikone.artisanworktables.common.plugin.kubejs.components.ToolEntryComponent;
import com.hikone.artisanworktables.common.plugin.kubejs.wrapper.ChanceResultWrapper;
import com.hikone.artisanworktables.common.plugin.kubejs.wrapper.ToolEntryWrapper;
import com.hikone.artisanworktables.common.recipe.ChanceResult;
import com.hikone.artisanworktables.common.recipe.ToolEntry;
import com.hikone.artisanworktables.common.reference.EnumType;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
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

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry)
    {
        registry.register(ChanceResultComponent.CHANCE_RESULT);
        registry.register(ToolEntryComponent.TOOL_ENTRY);
    }

    @Override
    public void registerBindings(BindingRegistry bindings)
    {
        bindings.add("ChanceResult", ChanceResultWrapper.class);
        bindings.add("ToolEntry", ToolEntryWrapper.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry)
    {
        registry.register(ChanceResult.class, ChanceResultWrapper::wrap);
        registry.register(ToolEntry.class, ToolEntryWrapper::wrap);
    }
}
