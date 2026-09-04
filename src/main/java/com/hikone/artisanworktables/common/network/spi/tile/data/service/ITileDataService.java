package com.hikone.artisanworktables.common.network.spi.tile.data.service;

import com.hikone.artisanworktables.common.internal.network.tile.TileDataTracker;
import com.hikone.artisanworktables.common.network.spi.tile.ITileData;
import com.hikone.artisanworktables.common.network.spi.tile.TileDataContainerBase;

import javax.annotation.Nullable;

public interface ITileDataService
{
    int getServiceId();
    
    @Nullable
    TileDataTracker getTracker(TileDataContainerBase tile);
    
    void register(TileDataContainerBase tile, ITileData[] data);
    
    void update();
}
