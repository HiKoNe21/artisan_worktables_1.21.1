package com.hikone.artisanworktables.common.network;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.packet.SPacketTileEntityBase;
import com.hikone.artisanworktables.common.tile.BaseBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class CSPacketWorktableTankDestroyFluid
        extends SPacketTileEntityBase<CSPacketWorktableTankDestroyFluid>
{

    @SuppressWarnings("unused")
    public CSPacketWorktableTankDestroyFluid()
    {
        // Serialization
    }

    public CSPacketWorktableTankDestroyFluid(BlockPos blockPos)
    {
        super(blockPos);
    }

    @Override
    protected IMessage<CSPacketWorktableTankDestroyFluid> onMessage(CSPacketWorktableTankDestroyFluid message, IPayloadContext context, BlockEntity tileEntity)
    {
        if (tileEntity instanceof BaseBlockEntity)
        {
            FluidTank tank = ((BaseBlockEntity) tileEntity).getTank();
            tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
            ArtisanWorktablesMod.getProxy().getPacketService().sendToNear(tileEntity, new SCPacketWorktableFluidUpdate(tileEntity.getBlockPos(), tank));
        }

        return null;
    }
}
