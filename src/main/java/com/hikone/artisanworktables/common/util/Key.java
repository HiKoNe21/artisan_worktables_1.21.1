package com.hikone.artisanworktables.common.util;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.resources.ResourceLocation;

public final class Key
{
    public static ResourceLocation from(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, path);
    }

    private Key()
    {
        //
    }
}
