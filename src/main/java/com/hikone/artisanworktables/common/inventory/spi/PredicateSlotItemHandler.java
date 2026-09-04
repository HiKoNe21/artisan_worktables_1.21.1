package com.hikone.artisanworktables.common.inventory.spi;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class PredicateSlotItemHandler extends SlotItemHandler
{
    private final Predicate<ItemStack> predicate;

    public PredicateSlotItemHandler(Predicate<ItemStack> predicate, IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.predicate = predicate;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack)
    {
        return this.predicate.test(stack);
    }
}
