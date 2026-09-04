package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.hikone.artisanworktables.common.network.SCPacketIncompatible;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class PlayerLoggedInEventHandler
{
    private final List<String> incompatibleModList;

    public PlayerLoggedInEventHandler(List<String> incompatibleModList)
    {
        this.incompatibleModList = incompatibleModList;
    }

    @SubscribeEvent
    public void on(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (ArtisanWorktablesModCommonConfig.hideIncompatibilityMessage)
        {
            return;
        }

        List<String> result = new ArrayList<>();

        for (String modId : this.incompatibleModList)
        {
            if (ModList.get().isLoaded(modId))
            {
                result.add(modId);
            }
        }

        if (!result.isEmpty())
        {
            ArtisanWorktablesMod.getProxy().getPacketService().sendToPlayer((ServerPlayer) event.getEntity(), new SCPacketIncompatible(result));
        }
    }
}