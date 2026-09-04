package com.hikone.artisanworktables.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public final class FluidHelper
{
    private static final ItemStack WATER_BOTTLE = PotionContents.createItemStack(Items.POTION, Potions.WATER);

    public static boolean drainWaterIntoBottle(Player player, IFluidHandler tank)
    {
        if (player.getMainHandItem().getItem() == Items.GLASS_BOTTLE)
        {
            FluidStack drain = tank.drain(250, IFluidHandler.FluidAction.SIMULATE);

            if (!drain.isEmpty() && drain.getAmount() == 250 && drain.getFluid() == Fluids.WATER)
            {
                if (player.addItem(WATER_BOTTLE.copy()))
                {
                    if (!player.isCreative())
                    {
                        player.getMainHandItem().shrink(1);
                    }

                    tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean drainWaterFromBottle(Player player, IFluidHandler fluidHandler)
    {
        ItemStack heldItem = player.getMainHandItem();
        PotionContents potionContents = heldItem.get(DataComponents.POTION_CONTENTS);

        if (heldItem.getItem() == Items.POTION && potionContents != null && potionContents.is(Potions.WATER))
        {
            FluidStack water = new FluidStack(Fluids.WATER, 250);

            if (fluidHandler.fill(water, IFluidHandler.FluidAction.SIMULATE) == water.getAmount())
            {
                if (player.addItem(new ItemStack(Items.GLASS_BOTTLE)))
                {
                    if (!player.isCreative())
                    {
                        heldItem.shrink(1);
                    }

                    fluidHandler.fill(water, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
        }

        return false;
    }

    public static void playFluidFillSoundServer(Fluid fluid, Level level, BlockPos pos)
    {
        if (level.isClientSide)
        {
            return;
        }
        level.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, fluid.getFluidType().getSound(SoundActions.BUCKET_FILL), SoundSource.BLOCKS, 0.2F + (float) Math.random() * 0.2F, 0.9F + (float) Math.random() * 0.15F);
    }

    public static void playFluidEmptySoundServer(Fluid fluid, Level level, BlockPos pos)
    {
        if (level.isClientSide)
        {
            return;
        }

        level.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, fluid.getFluidType().getSound(SoundActions.BUCKET_EMPTY), SoundSource.BLOCKS, 0.2F + (float) Math.random() * 0.2F, 0.9F + (float) Math.random() * 0.15F);
    }

    private FluidHelper()
    {
    }
}
