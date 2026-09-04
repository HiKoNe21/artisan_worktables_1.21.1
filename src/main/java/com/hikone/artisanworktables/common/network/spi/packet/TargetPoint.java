package com.hikone.artisanworktables.common.network.spi.packet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class TargetPoint
{
    @Nullable
    public final ServerPlayer excluded;
    public final double x;
    public final double y;
    public final double z;
    public final double radius;
    public final ResourceKey<Level> dimension;

    public TargetPoint(double x, double y, double z, double radius, ResourceKey<Level> dimension)
    {
        this(null, x, y, z, radius, dimension);
    }

    public TargetPoint(@Nullable ServerPlayer excluded, double x, double y, double z, double radius, ResourceKey<Level> dimension)
    {
        this.excluded = excluded;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.dimension = dimension;
    }
}
