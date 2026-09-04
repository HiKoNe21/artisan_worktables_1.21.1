package com.hikone.artisanworktables.common.network.spi.tile.data.service;

import com.hikone.artisanworktables.common.internal.network.tile.TileDataServiceContainer;
import com.hikone.artisanworktables.common.internal.network.tile.TileDataServiceLogger;
import com.hikone.artisanworktables.common.internal.network.tile.TileDataTracker;
import com.hikone.artisanworktables.common.internal.network.tile.client.TileDataServiceClientMonitor;
import com.hikone.artisanworktables.common.network.spi.packet.CPacketTileEntityBase;
import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.tile.TileDataContainerBase;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SCPacketTileData extends CPacketTileEntityBase<SCPacketTileData>
{
    private int serviceId;
    private RegistryFriendlyByteBuf buffer;

    @SuppressWarnings("unused")
    public SCPacketTileData()
    {
        // serialization
    }

    public SCPacketTileData(int serviceId, BlockPos origin, FriendlyByteBuf buffer)
    {
        super(origin);
        this.serviceId = serviceId;

        // The source buffer is a long-lived, reused field owned by the tracker
        // that is cleared and rewritten every tick. Since payload encoding is
        // deferred to the network thread (and may run once per recipient) on
        // NeoForge, we must snapshot its readable bytes now instead of holding a
        // live reference. The copy is non-consuming, leaving the source untouched.
        RegistryFriendlyByteBuf source = (RegistryFriendlyByteBuf) buffer;
        this.buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(Math.max(1, source.readableBytes())), source.registryAccess());
        this.buffer.writeBytes(source, source.readerIndex(), source.readableBytes());
    }

    @Override
    public SCPacketTileData decode(SCPacketTileData message, FriendlyByteBuf packetBuffer)
    {
        super.decode(message, packetBuffer);
        RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf) packetBuffer;
        message.serviceId = packetBuffer.readInt();
        int size = packetBuffer.readInt();
        message.buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(size), registryBuffer.registryAccess());
        packetBuffer.readBytes(message.buffer, size);
        return message;
    }

    @Override
    public void encode(SCPacketTileData message, FriendlyByteBuf packetBuffer)
    {
        super.encode(message, packetBuffer);
        packetBuffer.writeInt(message.serviceId);

        // Non-consuming write: encoding can run more than once (e.g. one
        // recipient per tracking player), so we must not advance the source
        // buffer's readerIndex here.
        int length = message.buffer.readableBytes();
        packetBuffer.writeInt(length);
        packetBuffer.writeBytes(message.buffer, message.buffer.readerIndex(), length);
    }

    @Override
    protected IMessage onMessage(SCPacketTileData message, IPayloadContext context, BlockEntity tileEntity)
    {
        if (tileEntity instanceof TileDataContainerBase)
        {
            ITileDataService dataService = TileDataServiceContainer.find(message.serviceId);

            if (dataService != null)
            {
                TileDataContainerBase tile = (TileDataContainerBase) tileEntity;
                TileDataTracker tracker = dataService.getTracker(tile);

                if (tracker != null)
                {
                    try
                    {
                        tracker.updateClient(message.buffer);
                        TileDataServiceClientMonitor.onClientPacketReceived(tracker, message.blockPos, message.buffer.writerIndex());
                    }
                    catch (Exception e)
                    {
                        TileDataServiceLogger.LOGGER.error("", e);
                    }
                }
            }
        }

        return null;
    }
}
