package com.hikone.artisanworktables.common.tile;

import com.hikone.artisanworktables.client.gui.Texture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

public interface IBlockEntityDesigner
{
    ItemStackHandler getPatternStackHandler();

    BlockEntity getTileEntity();

    boolean canPlayerUse(Player player);

    Texture getTexturePatternSide();
}
