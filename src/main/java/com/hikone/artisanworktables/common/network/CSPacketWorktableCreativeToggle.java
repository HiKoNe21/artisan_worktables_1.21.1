package com.hikone.artisanworktables.common.network;

import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.packet.SPacketTileEntityBase;
import com.hikone.artisanworktables.common.tile.BaseBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class CSPacketWorktableCreativeToggle extends SPacketTileEntityBase<CSPacketWorktableCreativeToggle>
{
    @SuppressWarnings("unused")
    public CSPacketWorktableCreativeToggle()
    {
        // Serialization
    }

    public CSPacketWorktableCreativeToggle(BlockPos blockPos)
    {
        super(blockPos);
    }

    @Override
    protected IMessage<CSPacketWorktableCreativeToggle> onMessage(CSPacketWorktableCreativeToggle message, IPayloadContext context, BlockEntity tileEntity)
    {
        if (tileEntity instanceof BaseBlockEntity)
        {
            BaseBlockEntity table = (BaseBlockEntity) tileEntity;
            // table.setCreative(!table.isCreative());
            // table.setLocked(false);
            CSPacketWorktableClear.clear(table, CSPacketWorktableClear.CLEAR_ALL);
            table.setChanged();
        }

        return null;
    }
}
