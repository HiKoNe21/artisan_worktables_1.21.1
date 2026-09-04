package com.hikone.artisanworktables.common.tile;

import com.hikone.artisanworktables.common.inventory.spi.ObservableStackHandler;
import com.hikone.artisanworktables.common.network.spi.tile.data.service.ITileDataService;
import com.hikone.artisanworktables.common.recipe.ISecondaryIngredientMatcher;
import com.hikone.artisanworktables.common.recipe.SecondaryIngredientMatcher;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.hikone.artisanworktables.common.tile.handler.MutuallyExclusiveStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class SecondaryInputBaseBlockEntity extends BaseBlockEntity
{
    protected ObservableStackHandler secondaryIngredientHandler;
    protected MutuallyExclusiveStackHandlerWrapper wrapper;

    // ---------------------------------------------------------------------------
    // Initialization
    // ---------------------------------------------------------------------------

    protected SecondaryInputBaseBlockEntity(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state, ITileDataService tileDataService)
    {
        super(tileEntityType, pos, state, tileDataService);
    }

    protected SecondaryInputBaseBlockEntity(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state, ITileDataService tileDataService, EnumType type)
    {
        super(tileEntityType, pos, state, tileDataService, type);
    }

    @Override
    protected void initialize(EnumType type)
    {
        super.initialize(type);
        this.secondaryIngredientHandler = new ObservableStackHandler(9);
        this.secondaryIngredientHandler.addObserver((stackHandler, slotIndex) ->
        {
            this.setChanged();
            this.requiresRecipeUpdate = true;
        });
        this.wrapper = new MutuallyExclusiveStackHandlerWrapper(this.secondaryIngredientHandler);
    }

    // ---------------------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------------------

    @Override
    public ISecondaryIngredientMatcher getSecondaryIngredientMatcher()
    {
        int slotCount = this.secondaryIngredientHandler.getSlots();
        List<ItemStack> inputs = new ArrayList<>(slotCount);

        for (int i = 0; i < slotCount; i++)
        {
            ItemStack itemStack = this.secondaryIngredientHandler.getStackInSlot(i);
            inputs.add(itemStack);
        }

        return new SecondaryIngredientMatcher(inputs);
    }

    @Override
    public IItemHandlerModifiable getSecondaryIngredientHandler()
    {
        return this.secondaryIngredientHandler;
    }

    // ---------------------------------------------------------------------------
    // Serialization
    // ---------------------------------------------------------------------------

    @Override
    protected void read(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries)
    {
        super.read(nbt, registries);
        this.secondaryIngredientHandler.deserializeNBT(registries, nbt.getCompound("secondaryIngredientHandler"));
    }

    @Override
    protected void write(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries)
    {
        super.write(nbt, registries);
        nbt.put("secondaryIngredientHandler", this.secondaryIngredientHandler.serializeNBT(registries));
    }
}
