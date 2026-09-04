package com.hikone.artisanworktables.common.network.spi.tile.data;

import com.hikone.artisanworktables.common.network.spi.tile.ITileDataFluidTank;
import com.hikone.artisanworktables.common.network.spi.tile.TileDataBase;
import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;

public class TileDataFluidTank<T extends FluidTank & ITileDataFluidTank> extends TileDataBase
{
    private T fluidTank;

    public TileDataFluidTank(T fluidTank)
    {
        this(fluidTank, 1);
    }

    public TileDataFluidTank(T fluidTank, int updateInterval)
    {
        super(updateInterval);
        this.fluidTank = fluidTank;
        this.fluidTank.addObserver((handler, slot) -> this.setDirty(true));
        this.setDirty(true);
    }

    public FluidTank getFluidTank()
    {
        return this.fluidTank;
    }

    @Override
    public void setDirty(boolean dirty)
    {
        super.setDirty(dirty);
    }

    @Override
    public void read(FriendlyByteBuf buffer) throws IOException
    {
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) buffer;
        this.fluidTank.readFromNBT(registryBuffer.registryAccess(), Preconditions.checkNotNull(buffer.readNbt()));
    }

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) buffer;
        buffer.writeNbt(this.fluidTank.writeToNBT(registryBuffer.registryAccess(), new CompoundTag()));
    }
}
