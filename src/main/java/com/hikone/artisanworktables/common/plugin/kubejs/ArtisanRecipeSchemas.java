package com.hikone.artisanworktables.common.plugin.kubejs;

import com.hikone.artisanworktables.common.plugin.kubejs.components.ChanceResultComponent;
import com.hikone.artisanworktables.common.plugin.kubejs.components.ToolEntryComponent;
import com.hikone.artisanworktables.common.recipe.ChanceResult;
import com.hikone.artisanworktables.common.recipe.ToolEntry;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public interface ArtisanRecipeSchemas
{
    @Info("Output Item, Use String ID or `Item.of(...)`")
    RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");

    RecipeKey<List<String>> PATTERN = StringComponent.STRING.instance().asList().otherKey("pattern");

    RecipeKey<TinyMap<Character, Ingredient>> KEY = IngredientComponent.INGREDIENT.instance().asPatternKey().otherKey("key");

    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().inputKey("ingredients");

    RecipeKey<Boolean> MIRRORED = BooleanComponent.BOOLEAN.otherKey("mirrored").optional(true);

    RecipeKey<String> GROUP = StringComponent.STRING.otherKey("group").defaultOptional();

    RecipeKey<List<Ingredient>> SECONDARY_INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().inputKey("secondaryIngredients").defaultOptional();

    RecipeKey<Boolean> CONSUME_SECONDARY = BooleanComponent.BOOLEAN.otherKey("consumeSecondaryIngredients").optional(true);

    RecipeKey<List<ToolEntry>> TOOLS = ToolEntryComponent.TOOL_ENTRY.instance().asList().otherKey("tools").defaultOptional();

    RecipeKey<List<ChanceResult>> EXTRA_OUTPUT = ChanceResultComponent.CHANCE_RESULT.instance().asList().otherKey("extraOutput").defaultOptional();

    RecipeKey<SizedFluidIngredient> FLUID_INGREDIENT = SizedFluidIngredientComponent.OPTIONAL_FLAT.otherKey("fluidIngredient").defaultOptional();

    RecipeKey<Integer> MINIMUM_TIER = NumberComponent.INT.otherKey("minimumTier").optional(0);

    RecipeKey<Integer> MAXIMUM_TIER = NumberComponent.INT.otherKey("maximumTier").optional(2);

    RecipeKey<Integer> EXPERIENCE_REQUIRED = NumberComponent.INT.otherKey("experienceRequired").optional(0);

    RecipeKey<Integer> LEVEL_REQUIRED = NumberComponent.INT.otherKey("levelRequired").optional(0);

    RecipeKey<Boolean> CONSUME_EXPERIENCE = BooleanComponent.BOOLEAN.otherKey("consumeExperience").optional(true);

    RecipeKey<String> CRAFT_SOUND = StringComponent.STRING.otherKey("craftSound").defaultOptional();

    RecipeSchema SHAPED = new RecipeSchema(
            RESULT,
            PATTERN,
            KEY,
            MIRRORED,
            GROUP,
            SECONDARY_INGREDIENTS,
            CONSUME_SECONDARY,
            TOOLS,
            EXTRA_OUTPUT,
            FLUID_INGREDIENT,
            MINIMUM_TIER,
            MAXIMUM_TIER,
            EXPERIENCE_REQUIRED,
            LEVEL_REQUIRED,
            CONSUME_EXPERIENCE,
            CRAFT_SOUND
    ).constructor(RESULT, PATTERN, KEY);

    RecipeSchema SHAPELESS = new RecipeSchema(
            RESULT,
            INGREDIENTS,
            GROUP,
            SECONDARY_INGREDIENTS,
            CONSUME_SECONDARY,
            TOOLS,
            EXTRA_OUTPUT,
            FLUID_INGREDIENT,
            MINIMUM_TIER,
            MAXIMUM_TIER,
            EXPERIENCE_REQUIRED,
            LEVEL_REQUIRED,
            CONSUME_EXPERIENCE,
            CRAFT_SOUND
    ).constructor(RESULT, INGREDIENTS);
}
