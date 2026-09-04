package com.hikone.artisanworktables.common.network.spi.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketBlockPosBase<Q extends PacketBlockPosBase> implements IMessage<Q>, IMessageHandler<Q, IMessage>
{
    protected BlockPos blockPos;

    public PacketBlockPosBase()
    {
    }

    public PacketBlockPosBase(BlockPos blockPos)
    {
        this.blockPos = blockPos;
    }

    @Override
    public Q decode(Q message, FriendlyByteBuf buffer)
    {
        message.blockPos = buffer.readBlockPos();
        return message;
    }

    @Override
    public void encode(Q message, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(message.blockPos);
    }
}
