package com.hikone.artisanworktables.util;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class TooltipHelper
{
    public static final String TOOLTIP_COMMON_HOLD_SHIFT = "gui." + ArtisanWorktablesMod.MODID + ".tooltip.common.hold_shift";

    public static Component getTooltipHoldShiftTextComponent()
    {
        return Component.translatable(TOOLTIP_COMMON_HOLD_SHIFT, ChatFormatting.DARK_GRAY, ChatFormatting.AQUA, ChatFormatting.DARK_GRAY);
    }

    public static void addTooltip(List<String> tooltip, String text, int preferredIndex)
    {
        if (tooltip.size() > preferredIndex)
        {
            tooltip.add(preferredIndex, text);
        }
        else
        {
            tooltip.add(text);
        }
    }

    private TooltipHelper()
    {
    }
}
