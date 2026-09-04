package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.tile.BaseBlockEntity;
import com.hikone.artisanworktables.common.tile.ToolboxBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CapabilityRegistrationEventHandler
{
    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void register(RegisterCapabilitiesEvent event)
    {
        ArtisanWorktablesMod.TileEntityTypes.init();

        this.registerFluidHandler(event, ArtisanWorktablesMod.TileEntityTypes.WORKTABLE);
        this.registerFluidHandler(event, ArtisanWorktablesMod.TileEntityTypes.WORKSTATION);
        this.registerFluidHandler(event, ArtisanWorktablesMod.TileEntityTypes.WORKSHOP);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType<ToolboxBlockEntity>) ArtisanWorktablesMod.TileEntityTypes.TOOLBOX, (blockEntity, side) -> blockEntity.getItemStackHandler());
    }

    @SuppressWarnings("unchecked")
    private void registerFluidHandler(RegisterCapabilitiesEvent event, BlockEntityType<?> type)
    {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType<BaseBlockEntity>) type, (blockEntity, side) -> blockEntity.getTank());
    }
}
