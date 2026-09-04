package com.hikone.artisanworktables.common.container;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class WorkshopContainer extends BaseContainer
{
    public static final String NAME = "workshop";

    public WorkshopContainer(int id, Level world, BlockPos blockPos, Inventory playerInventory, double mouseX, double mouseY)
    {
        super(ArtisanWorktablesMod.ContainerTypes.WORKSHOP, id, world, blockPos, playerInventory, mouseX, mouseY);
    }
}
