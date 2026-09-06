package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.sound.ModSounds;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class RegistryInitEventHandler
{
    @SubscribeEvent
    public void on(FMLCommonSetupEvent event)
    {
        if (ModList.get().isLoaded("craftingtweaks"))
        {
            try
            {
                Class.forName("com.hikone.artisanworktables.common.plugin.craftingtweaks.ArtisanCraftingGridProvider").getConstructor().newInstance();
                Class.forName("com.hikone.artisanworktables.common.plugin.craftingtweaks.ArtisanGridGuiHandler").getConstructor().newInstance();
                ArtisanWorktablesMod.LOGGER.info("Artisan Worktables: Crafting Tweaks integration loaded successfully!");
            }
            catch (Exception e)
            {
                ArtisanWorktablesMod.LOGGER.warn("Failed to load Crafting Tweaks integration.", e);
            }
        }

        event.enqueueWork(() ->
        {
            ArtisanWorktablesMod.TileEntityTypes.init();
            ArtisanWorktablesMod.ContainerTypes.init();
            ArtisanWorktablesMod.ParticleTypes.init();
            ModSounds.init();
        });
    }
}
