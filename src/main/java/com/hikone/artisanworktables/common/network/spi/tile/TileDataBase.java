package com.hikone.artisanworktables.common.network.spi.tile;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TileDataBase implements ITileData
{
    public interface IChangeObserver<D extends TileDataBase>
    {
        void onDirtyStateChanged(D data);

        class OnDirtyMarkTileDirty<D extends TileDataBase> implements IChangeObserver<D>
        {
            private final BlockEntity tile;

            public OnDirtyMarkTileDirty(BlockEntity tile)
            {
                this.tile = tile;
            }

            @Override
            public void onDirtyStateChanged(D data)
            {
                if (data.isDirty())
                {
                    this.tile.setChanged();
                }
            }
        }
    }

    private final int updateInterval;
    private int updateCounter;
    private boolean dirty;
    private boolean forceUpdate;
    private List<IChangeObserver> changeObservers;

    protected TileDataBase(int updateInterval)
    {
        this.updateInterval = updateInterval;
    }

    public void addChangeObserver(IChangeObserver observer)
    {
        if (this.changeObservers == null)
        {
            this.changeObservers = new ArrayList<>(1);
        }
        this.changeObservers.add(observer);
    }

    @Override
    public void setDirty(boolean dirty)
    {
        boolean changed = (this.dirty != dirty);
        this.dirty = dirty;

        if (this.changeObservers != null && changed)
        {
            for (int i = 0; i < this.changeObservers.size(); i++)
            {
                this.changeObservers.get(i).onDirtyStateChanged(this);
            }
        }
    }

    @Override
    public boolean isDirty()
    {
        return this.dirty && (this.updateCounter == 0);
    }

    @Override
    public void forceUpdate()
    {
        this.forceUpdate = true;
    }

    @Override
    public void update()
    {
        if (this.forceUpdate)
        {
            this.updateCounter = 0;
            this.forceUpdate = false;
        }
        else
        {
            this.updateCounter += 1;

            if (this.updateCounter >= this.updateInterval)
            {
                this.updateCounter = 0;
            }
        }
    }
}
