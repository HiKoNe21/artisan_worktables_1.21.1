package com.hikone.artisanworktables;

import com.google.common.collect.Lists;
import com.hikone.artisanworktables.client.ClientProxy;
import com.hikone.artisanworktables.common.CommonProxy;
import com.hikone.artisanworktables.common.block.ToolboxBlock;
import com.hikone.artisanworktables.common.block.ToolboxMechanicalBlock;
import com.hikone.artisanworktables.common.container.*;
import com.hikone.artisanworktables.common.tile.ToolboxBlockEntity;
import com.hikone.artisanworktables.common.tile.WorkshopBlockEntity;
import com.hikone.artisanworktables.common.tile.WorkstationBlockEntity;
import com.hikone.artisanworktables.common.tile.WorktableBlockEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import java.util.List;

@Mod(ArtisanWorktablesMod.MODID)
public class ArtisanWorktablesMod
{
    public static final String MODID = "artisanworktables";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String PACKET_SERVICE_PROTOCOL_VERSION = "1";

    public static final List<String> KNOWN_INCOMPATIBLE = Lists.newArrayList("performant");

    private static ArtisanWorktablesMod instance;

    private final IProxy proxy;

    public ArtisanWorktablesMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ArtisanWorktablesMod.instance = this;

        this.proxy = (FMLEnvironment.dist == Dist.CLIENT) ? new ClientProxy() : new CommonProxy();
        this.proxy.initialize(modContainer);
        this.proxy.registerModEventHandlers(modEventBus);
        this.proxy.registerGameEventHandlers(NeoForge.EVENT_BUS);
    }

    public static ArtisanWorktablesMod getInstance()
    {
        return ArtisanWorktablesMod.instance;
    }

    public static IProxy getProxy()
    {
        return ArtisanWorktablesMod.getInstance().proxy;
    }

    public static class Blocks
    {
        public static ToolboxBlock TOOLBOX;
        public static ToolboxMechanicalBlock MECHANICAL_TOOLBOX;

        public static void init()
        {
            TOOLBOX = (ToolboxBlock) BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(MODID, ToolboxBlock.NAME));
            MECHANICAL_TOOLBOX = (ToolboxMechanicalBlock) BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(MODID, ToolboxMechanicalBlock.NAME));
        }
    }

    public static class TileEntityTypes
    {
        public static BlockEntityType<?> WORKTABLE;
        public static BlockEntityType<?> WORKSTATION;
        public static BlockEntityType<?> WORKSHOP;
        public static BlockEntityType<?> TOOLBOX;

        public static void init()
        {
            WORKTABLE = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ResourceLocation.fromNamespaceAndPath(MODID, WorktableBlockEntity.NAME));
            WORKSTATION = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ResourceLocation.fromNamespaceAndPath(MODID, WorkstationBlockEntity.NAME));
            WORKSHOP = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ResourceLocation.fromNamespaceAndPath(MODID, WorkshopBlockEntity.NAME));
            TOOLBOX = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ResourceLocation.fromNamespaceAndPath(MODID, ToolboxBlockEntity.NAME));
        }
    }

    public static class ContainerTypes
    {
        public static MenuType<WorktableContainer> WORKTABLE;
        public static MenuType<WorkstationContainer> WORKSTATION;
        public static MenuType<WorkshopContainer> WORKSHOP;
        public static MenuType<ToolboxContainer> TOOLBOX;
        public static MenuType<ToolboxMechanicalContainer> MECHANICAL_TOOLBOX;

        @SuppressWarnings("unchecked")
        public static void init()
        {
            WORKTABLE = (MenuType<WorktableContainer>) BuiltInRegistries.MENU.get(ResourceLocation.fromNamespaceAndPath(MODID, WorktableContainer.NAME));
            WORKSTATION = (MenuType<WorkstationContainer>) BuiltInRegistries.MENU.get(ResourceLocation.fromNamespaceAndPath(MODID, WorkstationContainer.NAME));
            WORKSHOP = (MenuType<WorkshopContainer>) BuiltInRegistries.MENU.get(ResourceLocation.fromNamespaceAndPath(MODID, WorkshopContainer.NAME));
            TOOLBOX = (MenuType<ToolboxContainer>) BuiltInRegistries.MENU.get(ResourceLocation.fromNamespaceAndPath(MODID, ToolboxContainer.NAME));
            MECHANICAL_TOOLBOX = (MenuType<ToolboxMechanicalContainer>) BuiltInRegistries.MENU.get(ResourceLocation.fromNamespaceAndPath(MODID, ToolboxMechanicalContainer.NAME));
        }
    }

    public static class ParticleTypes
    {
        public static SimpleParticleType MAGE;

        public static void init()
        {
            MAGE = (SimpleParticleType) BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.fromNamespaceAndPath(MODID, "mage"));
        }
    }
}
