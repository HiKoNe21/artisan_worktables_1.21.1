package com.hikone.artisanworktables.common.internal.network.tile;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TileDataServiceLogger
{
    public static final Logger LOGGER = LogManager.getLogger(ArtisanWorktablesMod.MODID + "." + TileDataService.class.getSimpleName());
    
    private TileDataServiceLogger()
    {
        //
    }
}
