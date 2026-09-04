package com.hikone.artisanworktables.common;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.ArtisanWorktablesModClientConfig;
import com.hikone.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.hikone.artisanworktables.IProxy;
import com.hikone.artisanworktables.common.internal.network.packet.PacketService;
import com.hikone.artisanworktables.common.network.api.NetworkAPI;
import com.hikone.artisanworktables.common.network.spi.packet.IPacketService;
import com.hikone.artisanworktables.common.network.spi.tile.data.service.ITileDataService;
import com.hikone.artisanworktables.common.network.spi.tile.data.service.SCPacketTileData;
import com.hikone.artisanworktables.common.recipe.ArtisanRecipe;
import com.hikone.artisanworktables.common.reference.EnumTier;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.hikone.artisanworktables.common.network.*;
import com.hikone.artisanworktables.common.event.*;
import com.hikone.artisanworktables.util.ConfigHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommonProxy implements IProxy
{
    protected final List<Block> registeredWorktables;
    protected final Map<EnumTier, List<Block>> registeredWorktablesByTier;
    protected final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped;
    protected final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless;

    protected IPacketService packetService;
    protected ITileDataService tileDataService;

    public CommonProxy()
    {
        this.registeredWorktables = new ArrayList<>();
        this.registeredWorktablesByTier = new EnumMap<>(EnumTier.class);
        this.registeredSerializersShaped = new EnumMap<>(EnumType.class);
        this.registeredSerializersShapeless = new EnumMap<>(EnumType.class);
    }

    @Override
    public void initialize(ModContainer modContainer)
    {
        String modId = ArtisanWorktablesMod.MODID;
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = configPath.resolve(modId);

        try
        {
            Files.createDirectories(modConfigPath);

        } catch (IOException e)
        {
            ArtisanWorktablesMod.LOGGER.error("Error creating folder: " + modConfigPath, e);
        }

        String configFilenameCommon = modId + "-common.toml";
        String configFilenameClient = modId + "-client.toml";
        modContainer.registerConfig(ModConfig.Type.COMMON, ArtisanWorktablesModCommonConfig.CONFIG_SPEC, modId + "/" + configFilenameCommon);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ArtisanWorktablesModClientConfig.CONFIG_SPEC, modId + "/" + configFilenameClient);
        ConfigHelper.loadConfig(ArtisanWorktablesModCommonConfig.CONFIG_SPEC, modConfigPath.resolve(configFilenameCommon));
        ConfigHelper.loadConfig(ArtisanWorktablesModClientConfig.CONFIG_SPEC, modConfigPath.resolve(configFilenameClient));

        ArtisanWorktablesMod.LOGGER.debug("=== Packet service for: {} ===", FMLEnvironment.dist);
        this.packetService = NetworkAPI.createPacketService(modId, modId, ArtisanWorktablesMod.PACKET_SERVICE_PROTOCOL_VERSION);
        this.tileDataService = NetworkAPI.createTileDataService(modId, modId, this.packetService);

        this.packetService.registerMessage(SCPacketTileData.class, SCPacketTileData.class);
        this.packetService.registerMessage(CSPacketWorktableClear.class, CSPacketWorktableClear.class);
        this.packetService.registerMessage(CSPacketWorktableCreativeToggle.class, CSPacketWorktableCreativeToggle.class);
        this.packetService.registerMessage(CSPacketWorktableLockedModeToggle.class, CSPacketWorktableLockedModeToggle.class);
        this.packetService.registerMessage(CSPacketWorktableTab.class, CSPacketWorktableTab.class);
        this.packetService.registerMessage(CSPacketWorktableTankDestroyFluid.class, CSPacketWorktableTankDestroyFluid.class);
        this.packetService.registerMessage(SCPacketWorktableContainerJoinedBlockBreak.class, SCPacketWorktableContainerJoinedBlockBreak.class);
        this.packetService.registerMessage(SCPacketWorktableFluidUpdate.class, SCPacketWorktableFluidUpdate.class);
        this.packetService.registerMessage(SCPacketIncompatible.class, SCPacketIncompatible.class);
    }

    @Override
    public void registerModEventHandlers(IEventBus eventBus)
    {
        eventBus.addListener(ArtisanWorktablesModClientConfig::onModConfigEvent);
        eventBus.addListener(PacketService::registerAll);
        eventBus.register(new BlockRegistrationEventHandler(this.registeredWorktables, this.registeredWorktablesByTier));
        eventBus.register(new ItemRegistrationEventHandler(this.registeredWorktables));
        eventBus.register(new BlockEntityRegistrationEventHandler(this.registeredWorktablesByTier));
        eventBus.register(new MenuTypeRegistrationEventHandler());
        eventBus.register(new RecipeSerializerRegistrationEventHandler(this.registeredSerializersShaped, this.registeredSerializersShapeless));
        eventBus.register(new ParticleTypeRegistrationEventHandler());
        eventBus.register(new SoundEventRegistrationEventHandler());
        eventBus.register(new CreativeTabRegistrationEventHandler());
        eventBus.register(new RegistryInitEventHandler());
        eventBus.register(new CapabilityRegistrationEventHandler());
    }

    @Override
    public void registerGameEventHandlers(IEventBus eventBus)
    {
        eventBus.register(new TagsUpdatedEventHandler());
        eventBus.register(new PlayerLoggedInEventHandler(ArtisanWorktablesMod.KNOWN_INCOMPATIBLE));
    }

    // ---------------------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------------------

    @Override
    public List<Block> getRegisteredWorktables()
    {
        return Collections.unmodifiableList(this.registeredWorktables);
    }

    @Override
    public ITileDataService getTileDataService()
    {
        return this.tileDataService;
    }

    @Override
    public IPacketService getPacketService()
    {
        return this.packetService;
    }

    @Override
    public boolean isIntegratedServerRunning()
    {
        return false;
    }

    @Override
    public EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped()
    {
        return this.registeredSerializersShaped;
    }

    @Override
    public EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless()
    {
        return this.registeredSerializersShapeless;
    }

    @Nullable
    @Override
    public RecipeManager getRecipeManager()
    {
        MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();

        if (minecraftServer != null)
        {
            return minecraftServer.getRecipeManager();
        }

        return null;
    }
}
