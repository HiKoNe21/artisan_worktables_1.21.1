package com.hikone.artisanworktables.common.plugin.jei;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.client.screen.WorkshopScreen;
import com.hikone.artisanworktables.client.screen.WorkstationScreen;
import com.hikone.artisanworktables.client.screen.WorktableScreen;
import com.hikone.artisanworktables.common.block.BaseBlock;
import com.hikone.artisanworktables.common.container.BaseContainer;
import com.hikone.artisanworktables.common.container.WorkshopContainer;
import com.hikone.artisanworktables.common.container.WorkstationContainer;
import com.hikone.artisanworktables.common.container.WorktableContainer;
import com.hikone.artisanworktables.common.recipe.ArtisanRecipe;
import com.hikone.artisanworktables.common.recipe.RecipeTypes;
import com.hikone.artisanworktables.common.reference.EnumTier;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.hikone.artisanworktables.common.util.Key;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

@JeiPlugin
public class Plugin implements IModPlugin
{
    public static final ResourceLocation RECIPE_BACKGROUND = Key.from("textures/gui/recipe_background.png");

    public static final Map<EnumTier, Map<EnumType, RecipeType<ArtisanRecipe>>> CATEGORY_KEYS;

    static
    {
        CATEGORY_KEYS = new EnumMap<>(EnumTier.class);
        for (EnumTier tier : EnumTier.values())
        {
            for (EnumType type : EnumType.values())
            {
                Map<EnumType, RecipeType<ArtisanRecipe>> map = CATEGORY_KEYS.computeIfAbsent(tier, t -> new EnumMap<>(EnumType.class));
                map.put(type, RecipeType.create(
                        ArtisanWorktablesMod.MODID,
                        String.format("%s_%s", tier.getName(), type.getName()),
                        ArtisanRecipe.class
                ));
            }
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid()
    {
        return Key.from("jei_plugin");
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        CategoryFactory categoryFactory = new CategoryFactory();
        List<Block> registeredWorktables = ArtisanWorktablesMod.getProxy().getRegisteredWorktables();
        List<IRecipeCategory<?>> recipeCategoryList = new ArrayList<>(registeredWorktables.size());

        Map<EnumTier, CategoryDrawHandler> categoryDrawHandlerMap = new EnumMap<>(EnumTier.class);
        Map<EnumTier, CategorySetupHandler> categorySetupHandlerMap = new EnumMap<>(EnumTier.class);

        for (EnumTier tier : EnumTier.values())
        {
            categoryDrawHandlerMap.put(tier, new CategoryDrawHandler(tier));
            categorySetupHandlerMap.put(tier, new CategorySetupHandler(tier));
        }

        for (Block block : registeredWorktables)
        {
            ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(block);
            String path = Objects.requireNonNull(registryName).getPath();
            String[] split = path.split("_");
            EnumTier tier = EnumTier.fromName(split[0]);
            EnumType type = EnumType.fromName(split[1]);
            CategorySetupHandler categorySetupHandler = categorySetupHandlerMap.get(tier);
            CategoryDrawHandler categoryDrawHandler = categoryDrawHandlerMap.get(tier);

            BaseCategory<?> category = categoryFactory.create(tier, type, block, guiHelper, categorySetupHandler, categoryDrawHandler);
            recipeCategoryList.add(category);
        }

        registry.addRecipeCategories(recipeCategoryList.toArray(new IRecipeCategory[0]));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry)
    {
        RecipeManager recipeManager = ArtisanWorktablesMod.getProxy().getRecipeManager();

        if (recipeManager == null)
        {
            throw new RuntimeException("Null recipe manager");
        }

        for (EnumTier tier : EnumTier.values())
        {
            for (EnumType type : EnumType.values())
            {
                List<ArtisanRecipe> recipes = this.getRecipes(recipeManager, tier, type);
                registry.addRecipes(CATEGORY_KEYS.get(tier).get(type), recipes);
            }
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        // WORKTABLE
        registration.addGuiContainerHandler(WorktableScreen.class, new IGuiContainerHandler<>()
        {
            @Override
            public Collection<IGuiClickableArea> getGuiClickableAreas(WorktableScreen containerScreen, double mouseX, double mouseY)
            {
                var tableType = containerScreen.getMenu().getTile().getTableType();
                var result = new ArrayList<IGuiClickableArea>();
                result.add(IGuiClickableArea.createBasic(99, 19, 48, 12, CATEGORY_KEYS.get(EnumTier.WORKTABLE).get(tableType)));
                result.add(IGuiClickableArea.createBasic(99, 55, 48, 12, CATEGORY_KEYS.get(EnumTier.WORKTABLE).get(tableType)));
                result.add(IGuiClickableArea.createBasic(99, 31, 12, 24, CATEGORY_KEYS.get(EnumTier.WORKTABLE).get(tableType)));
                result.add(IGuiClickableArea.createBasic(135, 31, 12, 24, CATEGORY_KEYS.get(EnumTier.WORKTABLE).get(tableType)));
                return result;
            }
        });

        // WORKSTATION
        registration.addGuiContainerHandler(WorkstationScreen.class, new IGuiContainerHandler<>()
        {
            @Override
            public Collection<IGuiClickableArea> getGuiClickableAreas(WorkstationScreen containerScreen, double mouseX, double mouseY)
            {
                var tableType = containerScreen.getMenu().getTile().getTableType();
                var result = new ArrayList<IGuiClickableArea>();
                result.add(IGuiClickableArea.createBasic(99, 19, 48, 12, CATEGORY_KEYS.get(EnumTier.WORKSTATION).get(tableType)));
                result.add(IGuiClickableArea.createBasic(99, 55, 48, 12, CATEGORY_KEYS.get(EnumTier.WORKSTATION).get(tableType)));
                result.add(IGuiClickableArea.createBasic(99, 31, 12, 24, CATEGORY_KEYS.get(EnumTier.WORKSTATION).get(tableType)));
                result.add(IGuiClickableArea.createBasic(135, 31, 12, 24, CATEGORY_KEYS.get(EnumTier.WORKSTATION).get(tableType)));
                return result;
            }
        });

        // WORKSHOP
        registration.addGuiContainerHandler(WorkshopScreen.class, new IGuiContainerHandler<>()
        {
            @Override
            public Collection<IGuiClickableArea> getGuiClickableAreas(WorkshopScreen containerScreen, double mouseX, double mouseY)
            {
                var tableType = containerScreen.getMenu().getTile().getTableType();
                var result = new ArrayList<IGuiClickableArea>();
                result.add(IGuiClickableArea.createBasic(135, 54, 32, 8, CATEGORY_KEYS.get(EnumTier.WORKSHOP).get(tableType)));
                result.add(IGuiClickableArea.createBasic(135, 78, 32, 8, CATEGORY_KEYS.get(EnumTier.WORKSHOP).get(tableType)));
                result.add(IGuiClickableArea.createBasic(135, 62, 8, 16, CATEGORY_KEYS.get(EnumTier.WORKSHOP).get(tableType)));
                result.add(IGuiClickableArea.createBasic(159, 62, 8, 16, CATEGORY_KEYS.get(EnumTier.WORKSHOP).get(tableType)));
                return result;
            }
        });
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry)
    {
        List<Block> registeredWorktables = ArtisanWorktablesMod.getProxy().getRegisteredWorktables();

        for (Block block : registeredWorktables)
        {
            BaseBlock baseBlock = (BaseBlock) block;
            EnumType type = baseBlock.getType();
            EnumTier tier = EnumTier.fromName(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath().split("_")[0]);

            registry.addRecipeCatalyst(new ItemStack(block), CATEGORY_KEYS.get(tier).get(type));
        }
    }

    @Override
    public void registerRecipeTransferHandlers(@Nonnull IRecipeTransferRegistration registry)
    {
        var transferHelper = registry.getTransferHelper();
        var ingredientManager = registry.getJeiHelpers().getIngredientManager();

        for (EnumTier tier : EnumTier.values())
        {
            var containerClass = switch (tier)
            {
                case WORKTABLE -> WorktableContainer.class;
                case WORKSTATION -> WorkstationContainer.class;
                case WORKSHOP -> WorkshopContainer.class;
            };

            for (EnumType type : EnumType.values())
            {
                Plugin.registerToolAwareTransfer(registry, transferHelper, ingredientManager, containerClass, tier, type, CATEGORY_KEYS.get(tier).get(type));
            }
        }
    }

    private static <C extends BaseContainer> void registerToolAwareTransfer(IRecipeTransferRegistration registry, IRecipeTransferHandlerHelper transferHelper, IIngredientManager ingredientManager, Class<C> containerClass, EnumTier tier, EnumType type, RecipeType<ArtisanRecipe> recipeType)
    {
        RecipeTransferInfo<C> transferInfo = new RecipeTransferInfo<>(containerClass, tier, type, recipeType);
        registry.addRecipeTransferHandler(new ArtisanRecipeTransferHandler<>(transferInfo, transferHelper, ingredientManager), recipeType);
    }

    private List<ArtisanRecipe> getRecipes(RecipeManager recipeManager, EnumTier tier, EnumType type)
    {
        List<RecipeHolder<ArtisanRecipe>> holders = new ArrayList<>();
        holders.addAll(recipeManager.getAllRecipesFor(RecipeTypes.SHAPED_RECIPE_TYPES.get(type)));
        holders.addAll(recipeManager.getAllRecipesFor(RecipeTypes.SHAPELESS_RECIPE_TYPES.get(type)));

        holders.sort(Comparator.comparing(holder -> holder.id().toString()));

        List<ArtisanRecipe> result = new ArrayList<>(holders.size());

        for (RecipeHolder<ArtisanRecipe> holder : holders)
        {
            ArtisanRecipe recipe = holder.value();

            if (recipe.matchTier(tier))
            {
                result.add(recipe);
            }
        }

        return result;
    }
}
