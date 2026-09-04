package com.hikone.artisanworktables.common.internal.network.tile;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.network.spi.packet.IPacketService;
import com.hikone.artisanworktables.common.network.spi.tile.data.service.ITileDataService;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class retains collections of registered tile data services.
 */
@EventBusSubscriber(modid = ArtisanWorktablesMod.MODID)
public final class TileDataServiceContainer
{
    private static final Map<ResourceLocation, ITileDataService> SERVICE_MAP;
    private static final Int2ObjectOpenHashMap<ITileDataService> SERVICE_ID_MAP;

    private static int NEXT_ID;

    static
    {
        SERVICE_MAP = new HashMap<>();
        SERVICE_ID_MAP = new Int2ObjectOpenHashMap<>();
    }

    public static ITileDataService register(ResourceLocation location, IPacketService packetService)
    {
        if (SERVICE_MAP.get(location) != null)
        {
            throw new IllegalStateException("Tile data service already registered for id: " + location);
        }

        TileDataService service = new TileDataService(NEXT_ID, packetService);
        SERVICE_MAP.put(location, service);
        SERVICE_ID_MAP.put(NEXT_ID, service);

        NEXT_ID += 1;

        return service;
    }

    @Nullable
    public static ITileDataService find(ResourceLocation location)
    {
        return SERVICE_MAP.get(location);
    }

    @Nullable
    public static ITileDataService find(int serviceId)
    {
        return SERVICE_ID_MAP.get(serviceId);
    }

    @SubscribeEvent
    public static void on(ServerTickEvent.Post event)
    {
        for (ITileDataService service : SERVICE_MAP.values())
        {
            service.update();
        }
    }
}
