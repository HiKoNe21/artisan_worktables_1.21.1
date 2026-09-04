package com.hikone.artisanworktables.common.network.spi.tile;

import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;

public interface ITileData
{
    boolean isDirty();

    void setDirty(boolean dirty);

    void forceUpdate();

    void update();

    void read(FriendlyByteBuf buffer) throws IOException;

    void write(FriendlyByteBuf buffer);
}
