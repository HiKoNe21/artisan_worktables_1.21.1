package com.hikone.artisanworktables.common.network.spi.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class SPacketTileEntityBase<Q extends SPacketTileEntityBase> extends PacketBlockPosBase<Q>
{
    public SPacketTileEntityBase()
    {
    }

    public SPacketTileEntityBase(BlockPos blockPos)
    {
        super(blockPos);
    }

    @Override
    public IMessage onMessage(Q message, IPayloadContext context)
    {
        if (context.player() instanceof ServerPlayer player)
        {
            BlockEntity blockEntity = player.level().getBlockEntity(message.blockPos);
            return this.onMessage(message, context, blockEntity);
        }

        return null;
    }

    protected abstract IMessage onMessage(Q message, IPayloadContext context, BlockEntity blockEntity);
}
