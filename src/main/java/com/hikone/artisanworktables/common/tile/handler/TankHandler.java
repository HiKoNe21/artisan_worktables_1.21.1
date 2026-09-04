package com.hikone.artisanworktables.common.tile.handler;

import com.hikone.artisanworktables.common.inventory.spi.ObservableFluidTank;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataFluidTank;

public class TankHandler extends ObservableFluidTank implements ITileDataFluidTank
{
    public TankHandler(int capacity)
    {
        super(capacity);
    }
}
