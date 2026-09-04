package com.hikone.artisanworktables.common.container;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

public class WorktableContainer extends BaseContainer
{
    public static final String NAME = "worktable";

    public WorktableContainer(int id, Level world, BlockPos blockPos, Inventory playerInventory, double mouseX, double mouseY)
    {
        super(ArtisanWorktablesMod.ContainerTypes.WORKTABLE, id, world, blockPos, playerInventory, mouseX, mouseY);
    }
}
