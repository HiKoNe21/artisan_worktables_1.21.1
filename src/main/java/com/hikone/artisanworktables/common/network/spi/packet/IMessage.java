package com.hikone.artisanworktables.common.network.spi.packet;

import net.minecraft.network.FriendlyByteBuf;

public interface IMessage<T extends IMessage> {

    void encode(T message, FriendlyByteBuf buffer);

    T decode(T message, FriendlyByteBuf buffer);
}
