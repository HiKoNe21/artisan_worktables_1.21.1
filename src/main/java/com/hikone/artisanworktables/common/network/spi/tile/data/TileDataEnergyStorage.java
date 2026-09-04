package com.hikone.artisanworktables.common.network.spi.tile.data;

import com.google.common.base.Preconditions;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataEnergyStorage;
import com.hikone.artisanworktables.common.network.spi.tile.TileDataBase;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.io.IOException;

public class TileDataEnergyStorage<T extends IEnergyStorage & ITileDataEnergyStorage> extends TileDataBase
{
    private final T energyStorage;

    public TileDataEnergyStorage(T energyStorage)
    {
        this(energyStorage, 1);
    }

    public TileDataEnergyStorage(T energyStorage, int updateInterval)
    {
        super(updateInterval);
        this.energyStorage = energyStorage;
        this.energyStorage.addObserver((handler, amount) -> this.setDirty(true));
        this.setDirty(true);
    }

    public IEnergyStorage getEnergyStorage()
    {
        return this.energyStorage;
    }

    @Override
    public void read(FriendlyByteBuf buffer) throws IOException
    {
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) buffer;
        Tag tag = Preconditions.checkNotNull(buffer.readNbt(NbtAccounter.create(FriendlyByteBuf.DEFAULT_NBT_QUOTA)));
        this.energyStorage.deserializeNBT(registryBuffer.registryAccess(), tag);
    }

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) buffer;
        buffer.writeNbt(this.energyStorage.serializeNBT(registryBuffer.registryAccess()));
    }
}
