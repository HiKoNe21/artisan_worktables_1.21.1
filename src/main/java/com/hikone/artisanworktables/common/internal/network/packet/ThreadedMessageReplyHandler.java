package com.hikone.artisanworktables.common.internal.network.packet;

import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.packet.IMessageHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Wraps an {@link IMessageHandler}, executes the handler on the appropriate
 * thread, and sends a reply if a message is returned from the handler.
 *
 * @param <Q>
 * @param <A>
 */
public class ThreadedMessageReplyHandler<Q extends IMessage, A extends IMessage> implements IMessageHandler<Q, A>
{
    private PacketService packetService;
    private final IMessageHandler<Q, A> handler;

    public ThreadedMessageReplyHandler(PacketService packetService, IMessageHandler<Q, A> handler)
    {
        this.packetService = packetService;
        this.handler = handler;
    }

    @Override
    public A onMessage(Q message, IPayloadContext context)
    {
        context.enqueueWork(new MessageReplyRunner<>(message, context, this.packetService, this.handler));
        return null;
    }

    @Override
    public String toString()
    {
        return this.handler.toString();
    }

    @Override
    public int hashCode()
    {
        return this.handler.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ThreadedMessageReplyHandler)
        {
            return this.handler.equals(((ThreadedMessageReplyHandler<?, ?>) obj).handler);
        }
        else
        {
            return this.handler.equals(obj);
        }
    }

}
