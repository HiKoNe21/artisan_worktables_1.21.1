package com.hikone.artisanworktables.client.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.client.screen.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientSetupEventHandler
{
    @SubscribeEvent
    public void on(RegisterMenuScreensEvent event)
    {
        ArtisanWorktablesMod.ContainerTypes.init();

        event.register(ArtisanWorktablesMod.ContainerTypes.WORKTABLE, WorktableScreen::new);
        event.register(ArtisanWorktablesMod.ContainerTypes.WORKSTATION, WorkstationScreen::new);
        event.register(ArtisanWorktablesMod.ContainerTypes.WORKSHOP, WorkshopScreen::new);
        event.register(ArtisanWorktablesMod.ContainerTypes.TOOLBOX, ToolboxScreen::new);
        event.register(ArtisanWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, ToolboxMechanicalScreen::new);
    }
}
