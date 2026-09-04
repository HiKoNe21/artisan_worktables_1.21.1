package com.hikone.artisanworktables.common.tile.handler;

import com.hikone.artisanworktables.common.inventory.spi.ObservableStackHandler;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataItemStackHandler;

public class ToolStackHandler extends ObservableStackHandler implements ITileDataItemStackHandler
{
    public ToolStackHandler(int size)
    {
        super(size);
    }
}
