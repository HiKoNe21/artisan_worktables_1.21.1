package com.hikone.artisanworktables.common.network;

import com.hikone.artisanworktables.common.container.ContainerProvider;
import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.packet.SPacketTileEntityBase;
import com.hikone.artisanworktables.common.tile.BaseBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

/**
 * Sent from the client to the server to signal a worktable tab change.
 */
public class CSPacketWorktableTab extends SPacketTileEntityBase<CSPacketWorktableTab>
{
    protected double mouseX;

    protected double mouseY;

    @SuppressWarnings("unused")
    public CSPacketWorktableTab()
    {
        // Serialization
    }

    public CSPacketWorktableTab(BlockPos blockPos, double mouseX, double mouseY)
    {
        super(blockPos);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public CSPacketWorktableTab decode(CSPacketWorktableTab message, FriendlyByteBuf buffer)
    {
        var msg = super.decode(message, buffer);
        msg.mouseX = buffer.readDouble();
        msg.mouseY = buffer.readDouble();
        return msg;
    }

    @Override
    public void encode(CSPacketWorktableTab message, FriendlyByteBuf buffer)
    {
        super.encode(message, buffer);
        buffer.writeDouble(mouseX);
        buffer.writeDouble(mouseY);
    }

    @Override
    protected IMessage<CSPacketWorktableTab> onMessage(CSPacketWorktableTab message, IPayloadContext context, BlockEntity tileEntity)
    {
        ServerPlayer player = (ServerPlayer) context.player();
        ItemStack heldStack = Objects.requireNonNull(player).containerMenu.getCarried();

        if (!heldStack.isEmpty())
        {
            player.containerMenu.setCarried(ItemStack.EMPTY);
        }

        if (tileEntity instanceof BaseBlockEntity)
        {
            BaseBlockEntity table = (BaseBlockEntity) tileEntity;
            ContainerProvider containerProvider = new ContainerProvider(table.getTableTier(), table.getTableType(), table.getLevel(), table.getBlockPos());
            player.openMenu(containerProvider, buf ->
            {
                buf.writeBlockPos(tileEntity.getBlockPos());
                buf.writeDouble(message.mouseX);
                buf.writeDouble(message.mouseY);
            });
        }

        if (!heldStack.isEmpty())
        {
            player.containerMenu.setCarried(heldStack);
            player.connection.send(new ClientboundContainerSetSlotPacket(-1, player.containerMenu.incrementStateId(), -1, heldStack));
        }

        return null;
    }
}
