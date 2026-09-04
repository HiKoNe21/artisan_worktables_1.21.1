package com.hikone.artisanworktables.common.inventory.spi;

import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IObservableEnergyStorage
{
    void addObserver(IContentsChangedEventHandler handler);

    interface IContentsChangedEventHandler
    {
        void onContentsChanged(IEnergyStorage energyStorage, int amount);
    }
}
