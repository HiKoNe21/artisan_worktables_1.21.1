package com.hikone.artisanworktables.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public final class StackHelper
{
    public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";

    public static boolean isFuel(ItemStack itemStack)
    {
        return itemStack.getBurnTime(RecipeType.SMELTING) > 0;
    }

    public static int getItemBurnTime(ItemStack itemStack)
    {
        return itemStack.getBurnTime(RecipeType.SMELTING);
    }

    public static ItemStack decreaseStackInSlot(ItemStackHandler stackHandler, int slot, int amount, boolean checkContainer)
    {
        ItemStack stackInSlot = stackHandler.getStackInSlot(slot).copy();
        ItemStack adjustedStack = StackHelper.decrease(stackInSlot, amount, checkContainer);
        stackHandler.setStackInSlot(slot, adjustedStack);
        return adjustedStack;
    }

    @Nonnull
    public static CompoundTag getTagSafe(ItemStack itemStack)
    {
        if (itemStack.isEmpty())
        {
            return new CompoundTag();
        }

        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    @ParametersAreNonnullByDefault
    public static List<ItemStack> copyInto(List<ItemStack> sourceList, List<ItemStack> targetList)
    {
        for (ItemStack itemStack : sourceList)
        {
            targetList.add(itemStack.copy());
        }
        return targetList;
    }

    public static ItemStack decrease(ItemStack itemStack, int amount, boolean checkContainer)
    {
        if (itemStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        itemStack.shrink(amount);

        if (itemStack.getCount() <= 0)
        {
            if (checkContainer && itemStack.hasCraftingRemainingItem())
            {
                return itemStack.getCraftingRemainingItem();
            }
            else
            {
                return ItemStack.EMPTY;
            }
        }

        return itemStack;
    }

    public static void addToInventoryOrSpawn(Level level, Player player, ItemStack itemStack, BlockPos pos, double offsetY, boolean preferActiveSlot, boolean playPickupSound)
    {
        if (preferActiveSlot)
        {
            IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());

            ItemStack remainder = inventory.insertItem(player.getInventory().selected, itemStack, false);

            if (!remainder.isEmpty())
            {
                remainder = ItemHandlerHelper.insertItemStacked(inventory, remainder, false);
            }

            if (playPickupSound && (remainder.isEmpty() || remainder.getCount() != itemStack.getCount()))
            {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            if (!remainder.isEmpty() && !level.isClientSide)
            {
                StackHelper.spawnStackOnTop(level, itemStack, pos, offsetY);
            }

        }
        else
        {
            if (!player.getInventory().add(itemStack))
            {
                if (!level.isClientSide)
                {
                    StackHelper.spawnStackOnTop(level, itemStack, pos, offsetY);
                }
            }
            else if
            (playPickupSound)
            {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }

    public static void spawnStackHandlerContentsOnTop(Level level, ItemStackHandler stackHandler, BlockPos pos)
    {
        StackHelper.spawnStackHandlerContentsOnTop(level, stackHandler, pos, 1.0);
    }

    public static void spawnStackHandlerContentsOnTop(Level level, ItemStackHandler stackHandler, BlockPos pos, double offsetY)
    {
        for (int slot = 0; slot < stackHandler.getSlots(); slot++)
        {
            StackHelper.spawnStackHandlerSlotContentsOnTop(level, stackHandler, slot, pos, offsetY);
        }
    }

    public static void spawnStackHandlerSlotContentsOnTop(Level level, ItemStackHandler stackHandler, int slot, BlockPos pos)
    {
        StackHelper.spawnStackHandlerSlotContentsOnTop(level, stackHandler, slot, pos, 1.0);
    }

    public static void spawnStackHandlerSlotContentsOnTop(Level level, ItemStackHandler stackHandler, int slot, BlockPos pos, double offsetY)
    {
        ItemStack itemStack;

        while (!(itemStack = stackHandler.extractItem(slot, stackHandler.getSlotLimit(slot), false)).isEmpty())
        {
            StackHelper.spawnStackOnTop(level, itemStack, pos, offsetY);
        }
    }

    public static void spawnStackOnTop(Level level, ItemStack itemStack, BlockPos pos)
    {
        StackHelper.spawnStackOnTop(level, itemStack, pos, 1.0);
    }

    public static void spawnStackOnTop(Level level, ItemStack itemStack, BlockPos pos, double offsetY)
    {
        ItemEntity entityItem = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5 + offsetY, pos.getZ() + 0.5, itemStack);
        entityItem.setDeltaMovement(0, 0.1, 0);

        level.addFreshEntity(entityItem);
    }

    public static ItemStack createItemStackFromTileEntity(Block block, int amount, BlockEntity blockEntity, HolderLookup.Provider provider)
    {
        return StackHelper.createItemStackFromTileEntity(block.asItem(), amount, blockEntity, provider);
    }

    public static ItemStack createItemStackFromTileEntity(Item item, int amount, BlockEntity blockEntity, HolderLookup.Provider provider)
    {
        ItemStack itemStack = new ItemStack(item, amount);
        return StackHelper.writeTileEntityToItemStack(blockEntity, itemStack, provider);
    }

    public static ItemStack writeTileEntityToItemStack(BlockEntity blockEntity, ItemStack itemStack, HolderLookup.Provider provider)
    {
        blockEntity.saveToItem(itemStack, provider);
        return itemStack;
    }

    public static <T extends BlockEntity> T readTileEntityFromItemStack(T tile, BlockState blockState, ItemStack itemStack, HolderLookup.Provider provider)
    {
        CustomData blockEntityData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);

        if (blockEntityData != null)
        {
            tile.loadWithComponents(blockEntityData.copyTag(), provider);
        }

        return tile;
    }

    public static ItemStack readLargeItemStack(CompoundTag compound, HolderLookup.Provider provider)
    {
        ItemStack itemStack = ItemStack.parse(provider, compound).orElse(ItemStack.EMPTY);
        itemStack.setCount(compound.getInt("CountLarge"));
        return itemStack;
    }

    public static CompoundTag writeLargeItemStack(ItemStack itemStack, CompoundTag compound, HolderLookup.Provider provider)
    {
        itemStack.copyWithCount(1).save(provider, compound);
        compound.putInt("CountLarge", itemStack.getCount());
        return compound;
    }

    private StackHelper()
    {
    }
}
