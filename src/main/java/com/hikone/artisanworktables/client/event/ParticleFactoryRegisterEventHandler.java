package com.hikone.artisanworktables.client.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.particle.MageParticle;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ParticleFactoryRegisterEventHandler
{
    @SubscribeEvent
    public void on(RegisterParticleProvidersEvent event)
    {
        ArtisanWorktablesMod.ParticleTypes.init();

        event.registerSpriteSet(ArtisanWorktablesMod.ParticleTypes.MAGE, MageParticle.Factory::new);
    }
}