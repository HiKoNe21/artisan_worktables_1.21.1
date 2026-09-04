package com.hikone.artisanworktables.common.network.spi.tile.data;

import com.google.common.base.Preconditions;
import com.hikone.artisanworktables.common.network.spi.tile.ITileDataItemStackHandler;
import com.hikone.artisanworktables.util.StackHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.io.IOException;

/**
 * Supports syncing item stack counts larger than the vanilla stack limit.
 *
 * @param <H> the observable stack handler type
 */
public class TileDataLargeItemStackHandler<H extends ItemStackHandler & ITileDataItemStackHandler> extends TileDataItemStackHandler<H>
{

    public TileDataLargeItemStackHandler(H stackHandler)
    {
        super(stackHandler);
    }

    public TileDataLargeItemStackHandler(H stackHandler, int updateInterval)
    {
        super(stackHandler, updateInterval);
    }

    @Override
    protected ItemStack readItemStack(FriendlyByteBuf buffer) throws IOException
    {
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) buffer;
        CompoundTag tag = Preconditions.checkNotNull(buffer.readNbt());
        return StackHelper.readLargeItemStack(tag, registryBuffer.registryAccess());
    }

    @Override
    protected void writeItemStack(FriendlyByteBuf buffer, ItemStack itemStack)
    {
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) buffer;
        CompoundTag tag = StackHelper.writeLargeItemStack(itemStack, new CompoundTag(), registryBuffer.registryAccess());
        buffer.writeNbt(tag);
    }
}
