package com.hikone.artisanworktables.common.inventory.spi;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LIFOStackHandler extends ObservableStackHandler
{
    @Deprecated
    public interface IContentsClearedEventHandler
    {
        void onContentsCleared(ItemStackHandler stackHandler);
    }

    @Deprecated
    private final List<IContentsClearedEventHandler> contentsClearedEventHandlerList;

    public LIFOStackHandler(int size)
    {
        super(size);
        this.contentsClearedEventHandlerList = new ArrayList<>(1);
    }

    @Deprecated
    public void addObserverContentsCleared(IContentsClearedEventHandler handler)
    {
        this.contentsClearedEventHandlerList.add(handler);
    }

    public void clear()
    {
        for (int i = 0; i < this.stacks.size(); i++)
        {
            this.stacks.set(i, ItemStack.EMPTY);
        }

        for (IContentsClearedEventHandler handler : this.contentsClearedEventHandlerList)
        {
            handler.onContentsCleared(this);
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        int index = this.getFirstEmptyIndex();

        if (index < 0)
        {
            return stack;
        }

        return super.insertItem(index, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        int index = this.getLastNonEmptyIndex();

        if (index < 0)
        {
            return ItemStack.EMPTY;
        }

        return super.extractItem(index, amount, simulate);
    }

    public ItemStack getLastNonEmptyStack()
    {
        int index = this.getLastNonEmptyIndex();

        if (index < 0)
        {
            return ItemStack.EMPTY;
        }

        return this.getStackInSlot(index);
    }

    public int getLastNonEmptyIndex()
    {
        for (int i = this.stacks.size() - 1; i >= 0; i--)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                return i;
            }
        }
        return -1;
    }

    public int getFirstEmptyIndex()
    {
        for (int i = 0; i < this.stacks.size(); i++)
        {
            if (this.stacks.get(i).isEmpty())
            {
                return i;
            }
        }
        return -1;
    }
}
