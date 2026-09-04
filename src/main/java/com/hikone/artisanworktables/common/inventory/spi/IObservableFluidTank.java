package com.hikone.artisanworktables.common.inventory.spi;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IObservableFluidTank
{
    void addObserver(IContentsChangedEventHandler handler);

    interface IContentsChangedEventHandler
    {
        void onContentsChanged(FluidTank fluidTank, int amount);
    }
}
