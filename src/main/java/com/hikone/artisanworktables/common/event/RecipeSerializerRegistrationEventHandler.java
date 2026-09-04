package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.common.recipe.ArtisanRecipe;
import com.hikone.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.hikone.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.hikone.artisanworktables.common.recipe.RecipeTypes;
import com.hikone.artisanworktables.common.recipe.serializer.ArtisanRecipeSerializer;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.hikone.artisanworktables.common.reference.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.EnumMap;

public class RecipeSerializerRegistrationEventHandler
{
    private final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped;
    private final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless;

    public RecipeSerializerRegistrationEventHandler(EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped, EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless)
    {
        this.registeredSerializersShaped = registeredSerializersShaped;
        this.registeredSerializersShapeless = registeredSerializersShapeless;
    }

    @SubscribeEvent
    public void on(RegisterEvent event)
    {
        if (event.getRegistryKey().equals(Registries.RECIPE_TYPE))
        {
            RecipeTypes.register(event);
            return;
        }

        if (!event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER))
        {
            return;
        }

        // Allow the vanilla shaped pattern parser to accept the larger artisan grids.
        ShapedRecipePattern.setCraftingSize(Reference.MAX_RECIPE_WIDTH, Reference.MAX_RECIPE_HEIGHT);

        for (EnumType type : EnumType.values())
        {
            String name = type.getName();

            ArtisanRecipeSerializer<ArtisanRecipeShaped> shapedSerializer = ArtisanRecipeSerializer.shaped(type);
            this.registeredSerializersShaped.put(type, shapedSerializer);
            event.register(Registries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name + "_shaped"), () -> shapedSerializer);

            ArtisanRecipeSerializer<ArtisanRecipeShapeless> shapelessSerializer = ArtisanRecipeSerializer.shapeless(type);
            this.registeredSerializersShapeless.put(type, shapelessSerializer);
            event.register(Registries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name + "_shapeless"), () -> shapelessSerializer);
        }
    }
}
