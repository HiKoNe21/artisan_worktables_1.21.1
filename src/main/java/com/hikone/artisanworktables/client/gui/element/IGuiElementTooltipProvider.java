package com.hikone.artisanworktables.client.gui.element;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IGuiElementTooltipProvider extends IGuiElement
{
    List<Component> tooltipTextGet(List<Component> tooltip, double mouseX, double mouseY);
}
