package com.hikone.artisanworktables.common.network.spi.tile;

import com.hikone.artisanworktables.common.network.spi.tile.data.service.ITileDataService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base class for BlockEntities that use the tile data network system.
 * This provides a default implementation of the packet update methods.
 *
 * Call {@link #registerTileDataForNetwork(ITileData[])} in the subclass'
 * constructor to register tile data.
 */
public abstract class BlockEntityDataBase extends TileDataContainerBase
{
    protected final ITileDataService tileDataService;

    protected BlockEntityDataBase(BlockEntityType<?> type, BlockPos pos, BlockState state, ITileDataService tileDataService)
    {
        super(type, pos, state);
        this.tileDataService = tileDataService;
    }

    // ---------------------------------------------------------------------------
    // - Network
    // ---------------------------------------------------------------------------

    protected void registerTileDataForNetwork(ITileData[] data)
    {
        this.tileDataService.register(this, data);
    }

    @OnlyIn(Dist.CLIENT)
    public void onTileDataUpdate()
    {
        // Override in subclass if needed
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return this.saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // ---------------------------------------------------------------------------
    // - Serialization
    // ---------------------------------------------------------------------------

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        this.read(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        this.write(tag, registries);
    }

    /**
     * Read tile entity data from NBT. Override this instead of loadAdditional().
     */
    protected void read(CompoundTag tag, HolderLookup.Provider registries)
    {
        // Override in subclass
    }

    /**
     * Write tile entity data to NBT. Override this instead of saveAdditional().
     */
    protected void write(CompoundTag tag, HolderLookup.Provider registries)
    {
        // Override in subclass
    }
}
