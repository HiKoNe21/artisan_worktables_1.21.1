package com.hikone.artisanworktables.common.internal.network.packet;

import com.hikone.artisanworktables.common.network.spi.packet.IMessage;
import com.hikone.artisanworktables.common.network.spi.packet.IMessageHandler;
import com.hikone.artisanworktables.common.network.spi.packet.IPacketService;
import com.hikone.artisanworktables.common.network.spi.packet.TargetPoint;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketService implements IPacketService
{
    public static final int DEFAULT_RANGE = 64;

    private static final List<PacketService> SERVICES = new CopyOnWriteArrayList<>();

    private final String protocolVersion;
    private final CustomPacketPayload.Type<MessageWrapper> payloadType;
    private final StreamCodec<RegistryFriendlyByteBuf, MessageWrapper> streamCodec;

    private final Int2ObjectMap<Class<? extends IMessage>> messageTypesById;
    private final Int2ObjectMap<IMessageHandler> handlersById;
    private final Map<Class<?>, Integer> idsByMessageType;

    private int nextRegistrationIndex;

    public static PacketService create(String modId, String channelName, String protocolVersion)
    {
        PacketService service = new PacketService(ResourceLocation.fromNamespaceAndPath(modId, channelName), protocolVersion);
        SERVICES.add(service);
        return service;
    }

    private PacketService(ResourceLocation channel, String protocolVersion)
    {
        this.protocolVersion = protocolVersion;
        this.payloadType = new CustomPacketPayload.Type<>(channel);
        this.messageTypesById = new Int2ObjectOpenHashMap<>();
        this.handlersById = new Int2ObjectOpenHashMap<>();
        this.idsByMessageType = new HashMap<>();
        this.streamCodec = StreamCodec.of(
                (buffer, wrapper) -> {
                    buffer.writeVarInt(wrapper.messageId);
                    //noinspection unchecked
                    wrapper.message.encode(wrapper.message, buffer);
                },
                buffer -> {
                    int id = buffer.readVarInt();
                    IMessage message = this.instantiateMessage(this.messageTypesById.get(id));
                    //noinspection unchecked
                    IMessage decoded = message.decode(message, buffer);
                    return new MessageWrapper(this.payloadType, id, decoded);
                }
        );
    }

    public static void registerAll(RegisterPayloadHandlersEvent event)
    {
        for (PacketService service : SERVICES)
        {
            service.register(event);
        }
    }

    private void register(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar(this.protocolVersion).optional();
        registrar.playBidirectional(this.payloadType, this.streamCodec, this::handle);
    }

    private void handle(MessageWrapper wrapper, IPayloadContext context)
    {
        IMessageHandler handler = this.handlersById.get(wrapper.messageId);

        if (handler != null)
        {
            //noinspection unchecked
            handler.onMessage(wrapper.message, context);
        }
    }

    @Override
    public <Q extends IMessage, A extends IMessage> void registerMessage(Class<? extends IMessageHandler<Q, A>> messageHandler, Class<Q> requestMessageType)
    {
        int id = this.nextRegistrationIndex;

        IMessageHandler<? super Q, ? extends A> handler = this.instantiateHandler(messageHandler);
        this.handlersById.put(id, new ThreadedMessageReplyHandler(this, handler));
        this.messageTypesById.put(id, requestMessageType);
        this.idsByMessageType.put(requestMessageType, id);

        this.nextRegistrationIndex += 1;
    }

    private MessageWrapper wrap(IMessage message)
    {
        Integer id = this.idsByMessageType.get(message.getClass());

        if (id == null)
        {
            throw new IllegalArgumentException("Message type not registered: " + message.getClass());
        }

        return new MessageWrapper(this.payloadType, id, message);
    }

    private <Q extends IMessage> Q instantiateMessage(Class<Q> messageType)
    {
        try
        {
            return messageType.getDeclaredConstructor().newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private <Q extends IMessage, A extends IMessage> IMessageHandler<? super Q, ? extends A> instantiateHandler(Class<? extends IMessageHandler<? super Q, ? extends A>> handler)
    {
        try
        {
            return handler.getDeclaredConstructor().newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendToTrackingChunk(BlockEntity tileEntity, IMessage message)
    {
        BlockPos pos = tileEntity.getBlockPos();
        Level level = tileEntity.getLevel();

        if (level instanceof ServerLevel serverLevel)
        {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(pos), this.wrap(message));
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, IMessage message)
    {
        PacketDistributor.sendToPlayer(player, this.wrap(message));
    }

    @Override
    public void sendToDimension(ResourceKey<Level> dimension, IMessage message)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server != null)
        {
            ServerLevel level = server.getLevel(dimension);

            if (level != null)
            {
                PacketDistributor.sendToPlayersInDimension(level, this.wrap(message));
            }
        }
    }

    @Override
    public void sendToNear(TargetPoint targetPoint, IMessage message)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server != null)
        {
            ServerLevel level = server.getLevel(targetPoint.dimension);

            if (level != null)
            {
                PacketDistributor.sendToPlayersNear(level, targetPoint.excluded, targetPoint.x, targetPoint.y, targetPoint.z, targetPoint.radius, this.wrap(message));
            }
        }
    }

    @Override
    public void sendToNear(BlockEntity tileEntity, IMessage message)
    {
        Level level = tileEntity.getLevel();
        ResourceKey<Level> dimensionKey = Objects.requireNonNull(level).dimension();
        BlockPos pos = tileEntity.getBlockPos();
        this.sendToNear(new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), DEFAULT_RANGE, dimensionKey), message);
    }

    @Override
    public void sendToAll(IMessage message)
    {
        PacketDistributor.sendToAllPlayers(this.wrap(message));
    }

    @Override
    public void sendToServer(IMessage message)
    {
        PacketDistributor.sendToServer(this.wrap(message));
    }

    @Override
    public void sendToTrackingEntity(Entity entity, IMessage message)
    {
        PacketDistributor.sendToPlayersTrackingEntity(entity, this.wrap(message));
    }

    @Override
    public void sendToTrackingEntityAndSelf(Entity entity, IMessage message)
    {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, this.wrap(message));
    }

    @Override
    public void sendToTrackingChunk(LevelChunk chunk, IMessage message)
    {
        if (chunk.getLevel() instanceof ServerLevel serverLevel)
        {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, chunk.getPos(), this.wrap(message));
        }
    }

    public static final class MessageWrapper implements CustomPacketPayload
    {

        private final Type<MessageWrapper> type;
        private final int messageId;
        private final IMessage message;

        MessageWrapper(Type<MessageWrapper> type, int messageId, IMessage message)
        {
            this.type = type;
            this.messageId = messageId;
            this.message = message;
        }

        @Override
        public Type<? extends CustomPacketPayload> type()
        {
            return this.type;
        }
    }
}
