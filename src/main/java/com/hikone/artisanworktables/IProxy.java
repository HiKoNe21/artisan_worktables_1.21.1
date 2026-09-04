package com.hikone.artisanworktables;

import com.hikone.artisanworktables.common.network.spi.packet.IPacketService;
import com.hikone.artisanworktables.common.network.spi.tile.data.service.ITileDataService;
import com.hikone.artisanworktables.common.recipe.ArtisanRecipe;
import com.hikone.artisanworktables.common.reference.EnumType;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public interface IProxy
{
    void initialize(ModContainer modContainer);
    
    void registerModEventHandlers(IEventBus eventBus);
    
    void registerGameEventHandlers(IEventBus eventBus);
    
    List<Block> getRegisteredWorktables();
    
    ITileDataService getTileDataService();
    
    IPacketService getPacketService();
    
    boolean isIntegratedServerRunning();
    
    EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped();
    
    EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless();
    
    @Nullable
    RecipeManager getRecipeManager();
}
