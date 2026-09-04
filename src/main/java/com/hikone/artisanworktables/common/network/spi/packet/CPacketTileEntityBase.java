package com.hikone.artisanworktables.common.network.spi.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class CPacketTileEntityBase<Q extends CPacketTileEntityBase> extends PacketBlockPosBase<Q> {

    public CPacketTileEntityBase()
    {
    }

    public CPacketTileEntityBase(BlockPos blockPos)
    {
        super(blockPos);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IMessage onMessage(Q message, IPayloadContext context)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        Level level = player.level();

        if (level.isLoaded(message.blockPos))
        {
            BlockEntity blockEntity = level.getBlockEntity(message.blockPos);
            return this.onMessage(message, context, blockEntity);
        }

        return null;
    }

    protected abstract IMessage onMessage(Q message, IPayloadContext context, BlockEntity blockEntity);
}
