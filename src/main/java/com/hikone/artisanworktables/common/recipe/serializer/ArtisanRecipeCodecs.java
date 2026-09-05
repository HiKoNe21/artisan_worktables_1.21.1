package com.hikone.artisanworktables.common.recipe.serializer;

import com.hikone.artisanworktables.common.recipe.*;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public final class ArtisanRecipeCodecs
{
    // ---------------------------------------------------------------------------
    // JSON map codecs
    // ---------------------------------------------------------------------------

    public static MapCodec<ArtisanRecipeShapeless> shapelessCodec(EnumType type)
    {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ArtisanRecipe::getGroup),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(ArtisanRecipe::getResultItem),
                ToolEntry.CODEC.listOf().optionalFieldOf("tools", List.of()).forGetter(ArtisanRecipe::getTools),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(ArtisanRecipe::getIngredients),
                Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("secondaryIngredients", List.of()).forGetter(ArtisanRecipe::getSecondaryIngredients),
                Codec.BOOL.optionalFieldOf("consumeSecondaryIngredients", true).forGetter(ArtisanRecipe::consumeSecondaryIngredients),
                SizedFluidIngredient.FLAT_CODEC.optionalFieldOf("fluidIngredient", new SizedFluidIngredient(FluidIngredient.empty(), 1)).forGetter(ArtisanRecipe::getFluidIngredient),
                ChanceResult.CODEC.listOf().optionalFieldOf("extraOutput", List.of()).forGetter(ArtisanRecipe::getExtraOutputs),
                Codec.INT.optionalFieldOf("minimumTier", 0).forGetter(ArtisanRecipe::getMinimumTier),
                Codec.INT.optionalFieldOf("maximumTier", 2).forGetter(ArtisanRecipe::getMaximumTier),
                Codec.INT.optionalFieldOf("experienceRequired", 0).forGetter(ArtisanRecipe::getExperienceRequired),
                Codec.INT.optionalFieldOf("levelRequired", 0).forGetter(ArtisanRecipe::getLevelRequired),
                Codec.BOOL.optionalFieldOf("consumeExperience", true).forGetter(ArtisanRecipe::consumeExperience),
                Codec.STRING.optionalFieldOf("craftSound", "").forGetter(ArtisanRecipe::getCraftSound)
        ).apply(instance, (group, result, tools, ingredients, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
                           minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound) ->
        {
            try
            {
                return commonBuilder(group, result, tools, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
                        minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound)
                        .setIngredients(nonNull(ingredients))
                        .buildShapeless(type);

            }
            catch (Exception e)
            {
                throw new IllegalStateException("Invalid artisan shapeless recipe", e);
            }
        }));
    }

    public static MapCodec<ArtisanRecipeShaped> shapedCodec(EnumType type)
    {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ArtisanRecipe::getGroup),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(ArtisanRecipe::getResultItem),
                ToolEntry.CODEC.listOf().optionalFieldOf("tools", List.of()).forGetter(ArtisanRecipe::getTools),
                ShapedRecipePattern.MAP_CODEC.forGetter(ArtisanRecipeShaped::getPattern),
                Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("secondaryIngredients", List.of()).forGetter(ArtisanRecipe::getSecondaryIngredients),
                Codec.BOOL.optionalFieldOf("consumeSecondaryIngredients", true).forGetter(ArtisanRecipe::consumeSecondaryIngredients),
                SizedFluidIngredient.FLAT_CODEC.optionalFieldOf("fluidIngredient", new SizedFluidIngredient(FluidIngredient.empty(), 1)).forGetter(ArtisanRecipe::getFluidIngredient),
                ChanceResult.CODEC.listOf().optionalFieldOf("extraOutput", List.of()).forGetter(ArtisanRecipe::getExtraOutputs),
                Codec.BOOL.optionalFieldOf("mirrored", true).forGetter(ArtisanRecipeShaped::isMirrored),
                Codec.INT.optionalFieldOf("minimumTier", 0).forGetter(ArtisanRecipe::getMinimumTier),
                Codec.INT.optionalFieldOf("maximumTier", 2).forGetter(ArtisanRecipe::getMaximumTier),
                Codec.INT.optionalFieldOf("experienceRequired", 0).forGetter(ArtisanRecipe::getExperienceRequired),
                Codec.INT.optionalFieldOf("levelRequired", 0).forGetter(ArtisanRecipe::getLevelRequired),
                Codec.BOOL.optionalFieldOf("consumeExperience", true).forGetter(ArtisanRecipe::consumeExperience),
                Codec.STRING.optionalFieldOf("craftSound", "").forGetter(ArtisanRecipe::getCraftSound)
        ).apply(instance, (group, result, tools, pattern, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
                           mirrored, minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound) ->
        {
            try
            {
                return commonBuilder(group, result, tools, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
                        minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound)
                        .setShapedPattern(pattern)
                        .setMirrored(mirrored)
                        .buildShaped(type);

            }
            catch (Exception e)
            {
                throw new IllegalStateException("Invalid artisan shaped recipe", e);
            }
        }));
    }

    // ---------------------------------------------------------------------------
    // Network stream codecs
    // ---------------------------------------------------------------------------

    public static StreamCodec<RegistryFriendlyByteBuf, ArtisanRecipeShapeless> shapelessStreamCodec(EnumType type)
    {

        return StreamCodec.of(
                (buffer, recipe) ->
                {
                    writeCommon(buffer, recipe);
                    writeIngredients(buffer, recipe.getIngredients());
                },
                buffer ->
                {
                    ArtisanRecipeBuilder builder = readCommon(buffer);
                    builder.setIngredients(readIngredients(buffer));

                    try
                    {
                        return builder.buildShapeless(type);
                    }
                    catch (Exception e)
                    {
                        throw new IllegalStateException("Invalid artisan shapeless recipe", e);
                    }
                }
        );
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ArtisanRecipeShaped> shapedStreamCodec(EnumType type)
    {
        return StreamCodec.of(
                (buffer, recipe) ->
                {
                    writeCommon(buffer, recipe);
                    ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.getPattern());
                    buffer.writeBoolean(recipe.isMirrored());
                },
                buffer ->
                {
                    ArtisanRecipeBuilder builder = readCommon(buffer);
                    builder.setShapedPattern(ShapedRecipePattern.STREAM_CODEC.decode(buffer));
                    builder.setMirrored(buffer.readBoolean());

                    try
                    {
                        return builder.buildShaped(type);
                    }
                    catch (Exception e)
                    {
                        throw new IllegalStateException("Invalid artisan shaped recipe", e);
                    }
                }
        );
    }

    // ---------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------

    private static ArtisanRecipeBuilder commonBuilder(
            String group,
            ItemStack result,
            List<ToolEntry> tools,
            List<Ingredient> secondaryIngredients,
            boolean consumeSecondaryIngredients,
            SizedFluidIngredient fluidIngredient,
            List<ChanceResult> extraOutputs,
            int minimumTier,
            int maximumTier,
            int experienceRequired,
            int levelRequired,
            boolean consumeExperience,
            String craftSound)
    {
        return new ArtisanRecipeBuilder()
                .setGroup(group)
                .setResult(result)
                .setTools(nonNull(tools))
                .setSecondaryIngredients(nonNull(secondaryIngredients))
                .setConsumeSecondaryIngredients(consumeSecondaryIngredients)
                .setFluidIngredient(fluidIngredient)
                .setExtraOutputs(nonNull(extraOutputs))
                .setMinimumTier(minimumTier)
                .setMaximumTier(maximumTier)
                .setExperienceRequired(experienceRequired)
                .setLevelRequired(levelRequired)
                .setConsumeExperience(consumeExperience)
                .setCraftSound(craftSound);
    }

    private static void writeCommon(RegistryFriendlyByteBuf buffer, ArtisanRecipe recipe)
    {
        buffer.writeUtf(recipe.getGroup());
        writeTools(buffer, recipe.getTools());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem());
        writeIngredients(buffer, recipe.getSecondaryIngredients());
        buffer.writeBoolean(recipe.consumeSecondaryIngredients());
        SizedFluidIngredient.STREAM_CODEC.encode(buffer, recipe.getFluidIngredient());
        buffer.writeVarInt(recipe.getExtraOutputs().size());
        recipe.getExtraOutputs().forEach((r) -> r.write(buffer));
        buffer.writeVarInt(recipe.getMinimumTier());
        buffer.writeVarInt(recipe.getMaximumTier());
        buffer.writeVarInt(recipe.getExperienceRequired());
        buffer.writeVarInt(recipe.getLevelRequired());
        buffer.writeBoolean(recipe.consumeExperience());
        buffer.writeUtf(recipe.getCraftSound());
    }

    private static ArtisanRecipeBuilder readCommon(RegistryFriendlyByteBuf buffer)
    {
        String group = buffer.readUtf();
        NonNullList<ToolEntry> tools = readTools(buffer);
        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
        NonNullList<Ingredient> secondaryIngredients = readIngredients(buffer);
        boolean consumeSecondary = buffer.readBoolean();
        SizedFluidIngredient fluidIngredient = SizedFluidIngredient.STREAM_CODEC.decode(buffer);
        NonNullList<ChanceResult> extraOutputs = NonNullList.withSize(buffer.readVarInt(), ChanceResult.EMPTY);
        extraOutputs.replaceAll(ignored -> ChanceResult.read(buffer));
        int minimumTier = buffer.readVarInt();
        int maximumTier = buffer.readVarInt();
        int experienceRequired = buffer.readVarInt();
        int levelRequired = buffer.readVarInt();
        boolean consumeExperience = buffer.readBoolean();
        String craftSound = buffer.readUtf();

        return new ArtisanRecipeBuilder()
                .setGroup(group)
                .setResult(result)
                .setTools(tools)
                .setSecondaryIngredients(secondaryIngredients)
                .setConsumeSecondaryIngredients(consumeSecondary)
                .setFluidIngredient(fluidIngredient)
                .setExtraOutputs(extraOutputs)
                .setMinimumTier(minimumTier)
                .setMaximumTier(maximumTier)
                .setExperienceRequired(experienceRequired)
                .setLevelRequired(levelRequired)
                .setConsumeExperience(consumeExperience)
                .setCraftSound(craftSound);
    }

    private static void writeTools(RegistryFriendlyByteBuf buffer, List<ToolEntry> tools)
    {
        buffer.writeVarInt(tools.size());

        for (ToolEntry tool : tools)
        {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, tool.getTool());
            buffer.writeVarInt(tool.getDamage());
            buffer.writeBoolean(tool.matchNbt());

            ItemAbility ability = tool.getItemAbility();
            buffer.writeBoolean(ability != null);

            if (ability != null)
            {
                buffer.writeUtf(ability.name());
            }
        }
    }

    private static NonNullList<ToolEntry> readTools(RegistryFriendlyByteBuf buffer)
    {
        int size = buffer.readVarInt();
        NonNullList<ToolEntry> tools = NonNullList.create();

        for (int i = 0; i < size; i++)
        {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int damage = buffer.readVarInt();
            boolean matchNbt = buffer.readBoolean();
            ItemAbility ability = buffer.readBoolean() ? ItemAbility.get(buffer.readUtf()) : null;
            tools.add(new ToolEntry(ingredient, damage, matchNbt, ability));
        }

        return tools;
    }

    private static void writeIngredients(RegistryFriendlyByteBuf buffer, List<Ingredient> ingredients)
    {
        buffer.writeVarInt(ingredients.size());

        for (Ingredient ingredient : ingredients)
        {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
    }

    private static NonNullList<Ingredient> readIngredients(RegistryFriendlyByteBuf buffer)
    {
        int size = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (int i = 0; i < size; i++)
        {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        }

        return ingredients;
    }

    private static <T> NonNullList<T> nonNull(List<T> list)
    {
        NonNullList<T> result = NonNullList.create();
        result.addAll(list);
        return result;
    }

    private ArtisanRecipeCodecs()
    {
        //
    }
}
