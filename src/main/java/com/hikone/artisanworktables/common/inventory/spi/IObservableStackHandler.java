package com.hikone.artisanworktables.common.inventory.spi;

import net.neoforged.neoforge.items.ItemStackHandler;

public interface IObservableStackHandler
{
    void addObserver(IContentsChangedEventHandler handler);

    interface IContentsChangedEventHandler
    {
        void onContentsChanged(ItemStackHandler stackHandler, int slotIndex);
    }
}
