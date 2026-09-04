package com.hikone.artisanworktables.common.tile.handler;

import com.hikone.artisanworktables.common.inventory.spi.ObservableStackHandler;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataItemStackHandler;
import com.hikone.artisanworktables.common.recipe.ICraftingMatrixStackHandler;

public class CraftingMatrixStackHandler extends ObservableStackHandler implements ICraftingMatrixStackHandler, ITileDataItemStackHandler
{
    private final int width;
    private final int height;

    public CraftingMatrixStackHandler(int width, int height)
    {
        super(width * height);
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    public boolean isEmpty()
    {
        int slotCount = this.getWidth() * this.getHeight();

        for (int i = 0; i < slotCount; i++)
        {
            if (!this.getStackInSlot(i).isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}
