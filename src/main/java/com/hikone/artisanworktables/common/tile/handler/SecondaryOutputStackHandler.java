package com.hikone.artisanworktables.common.tile.handler;

import com.hikone.artisanworktables.common.inventory.spi.ObservableStackHandler;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataItemStackHandler;

public class SecondaryOutputStackHandler extends ObservableStackHandler implements ITileDataItemStackHandler
{
    public SecondaryOutputStackHandler(int size)
    {
        super(size);
    }
}
