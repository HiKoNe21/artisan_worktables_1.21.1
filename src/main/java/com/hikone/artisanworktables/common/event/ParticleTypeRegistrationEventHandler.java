package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ParticleTypeRegistrationEventHandler
{
    @SubscribeEvent
    public void on(RegisterEvent event)
    {
        if (!event.getRegistryKey().equals(Registries.PARTICLE_TYPE))
        {
            return;
        }

        event.register(Registries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, "mage"), () -> new SimpleParticleType(false));
    }
}
