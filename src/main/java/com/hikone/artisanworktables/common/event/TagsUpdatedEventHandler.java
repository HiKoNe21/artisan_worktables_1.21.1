package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.common.util.ToolValidationHelper;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class TagsUpdatedEventHandler
{
    @SubscribeEvent
    public void on(TagsUpdatedEvent event)
    {
        ToolValidationHelper.clear();
    }
}