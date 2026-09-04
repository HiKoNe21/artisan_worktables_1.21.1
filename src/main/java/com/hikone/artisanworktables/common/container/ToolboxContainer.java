package com.hikone.artisanworktables.common.container;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ToolboxContainer extends ToolboxBaseContainer
{
    public static final String NAME = "toolbox";

    public ToolboxContainer(int id, Level world, BlockPos blockPos, Inventory playerInventory)
    {
        super(ArtisanWorktablesMod.ContainerTypes.TOOLBOX, id, world, blockPos, playerInventory);
    }
}
