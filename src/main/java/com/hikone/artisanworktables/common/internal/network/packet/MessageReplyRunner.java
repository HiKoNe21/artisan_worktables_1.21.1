package com.hikone.artisanworktables.common.internal.network.packet;

import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.packet.IMessageHandler;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Executes the message handler and sends a reply if a message is returned from the handler.
 */
final class MessageReplyRunner<Q extends IMessage, A extends IMessage> implements Runnable
{
    private final Q message;
    private final IPayloadContext context;
    private final PacketService packetService;
    private final IMessageHandler<Q, A> handler;

    public MessageReplyRunner(Q message, IPayloadContext context, PacketService packetService, IMessageHandler<Q, A> handler)
    {
        this.message = message;
        this.context = context;
        this.packetService = packetService;
        this.handler = handler;
    }

    @Override
    public void run() {

        final A reply = this.handler.onMessage(this.message, this.context);

        if (reply != null) {
            if (this.context.flow() == PacketFlow.CLIENTBOUND)
            {
                this.packetService.sendToServer(reply);
            }
            else if (this.context.player() instanceof ServerPlayer player)
            {
                this.packetService.sendToPlayer(player, reply);
            }
        }
    }
}
