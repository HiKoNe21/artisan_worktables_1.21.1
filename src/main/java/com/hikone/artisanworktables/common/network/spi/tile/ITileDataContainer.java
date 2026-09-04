package com.hikone.artisanworktables.common.network.spi.tile;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ITileDataContainer
{
    @OnlyIn(Dist.CLIENT)
    void onTileDataUpdate();
}
