package com.hikone.artisanworktables.common.block;

import com.hikone.artisanworktables.common.reference.EnumTier;
import com.hikone.artisanworktables.common.reference.EnumType;
import com.hikone.artisanworktables.common.tile.WorkshopBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class MageWorkshopBlock extends MageBaseBlock
{
    public MageWorkshopBlock(SoundType soundType, float hardness, float resistance)
    {
        super(EnumType.MAGE, soundType, hardness, resistance);
    }

    @Override
    protected EnumTier getTier()
    {
        return EnumTier.WORKSHOP;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return WorkshopBlock.VOXEL_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WorkshopBlockEntity(pos, state, this.getType());
    }
}
