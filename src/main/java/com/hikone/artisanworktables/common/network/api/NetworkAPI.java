package com.hikone.artisanworktables.common.network.api;

import com.hikone.artisanworktables.common.internal.network.packet.PacketService;
import com.hikone.artisanworktables.common.internal.network.tile.TileDataServiceContainer;
import com.hikone.artisanworktables.common.network.spi.packet.IPacketService;
import com.hikone.artisanworktables.common.network.spi.tile.data.service.ITileDataService;
import net.minecraft.resources.ResourceLocation;

public final class NetworkAPI
{
    public static IPacketService createPacketService(String modId, String channelName, String protocolVersion)
    {
        return PacketService.create(modId, channelName, protocolVersion);
    }

    public static ITileDataService createTileDataService(String modId, String serviceName, IPacketService packetService)
    {
        return TileDataServiceContainer.register(ResourceLocation.fromNamespaceAndPath(modId, serviceName), packetService);
    }
}
