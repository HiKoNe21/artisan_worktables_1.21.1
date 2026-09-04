package com.hikone.artisanworktables.common.network.spi.packet;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IMessageHandler<Q, A extends IMessage>
{
    A onMessage(Q message, IPayloadContext context);
}
