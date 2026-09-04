package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.sound.ModSounds;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class RegistryInitEventHandler
{

    @SubscribeEvent
    public void on(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ArtisanWorktablesMod.TileEntityTypes.init();
            ArtisanWorktablesMod.ContainerTypes.init();
            ArtisanWorktablesMod.ParticleTypes.init();
            ModSounds.init();
        });
    }
}
