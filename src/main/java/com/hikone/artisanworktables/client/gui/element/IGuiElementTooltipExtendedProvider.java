package com.hikone.artisanworktables.client.gui.element;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IGuiElementTooltipExtendedProvider extends IGuiElementTooltipProvider
{
    List<Component> tooltipTextExtendedGet(List<Component> tooltip);
}
