package com.hikone.artisanworktables.common.network.spi.tile;

import com.hikone.artisanworktables.common.inventory.spi.IObservableEnergyStorage;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Energy storage data elements need to implement the
 * {@link IObservableEnergyStorage} interface.
 */
public interface ITileDataEnergyStorage extends IObservableEnergyStorage, INBTSerializable<Tag>
{
}
