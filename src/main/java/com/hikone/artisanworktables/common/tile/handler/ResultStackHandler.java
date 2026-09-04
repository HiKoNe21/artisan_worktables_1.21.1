package com.hikone.artisanworktables.common.tile.handler;

import com.hikone.artisanworktables.common.inventory.spi.ObservableStackHandler;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataItemStackHandler;

public class ResultStackHandler extends ObservableStackHandler implements ITileDataItemStackHandler
{
    public ResultStackHandler(int size)
    {
        super(size);
    }
}
