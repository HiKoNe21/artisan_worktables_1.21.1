package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.container.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class MenuTypeRegistrationEventHandler
{
    @SubscribeEvent
    public void on(RegisterEvent event)
    {
        if (!event.getRegistryKey().equals(Registries.MENU))
        {
            return;
        }

        event.register(Registries.MENU,
                ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, WorktableContainer.NAME),
                () -> IMenuTypeExtension.create((id, playerInventory, data) ->
                {
                    BlockPos blockPos = data.readBlockPos();
                    double mouseX = data.readDouble();
                    double mouseY = data.readDouble();
                    Player player = playerInventory.player;
                    Level world = player.level();
                    return new WorktableContainer(id, world, blockPos, playerInventory, mouseX, mouseY);
                }));

        event.register(Registries.MENU,
                ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, WorkstationContainer.NAME),
                () -> IMenuTypeExtension.create((id, playerInventory, data) ->
                {
                    BlockPos blockPos = data.readBlockPos();
                    double mouseX = data.readDouble();
                    double mouseY = data.readDouble();
                    Player player = playerInventory.player;
                    Level world = player.level();
                    return new WorkstationContainer(id, world, blockPos, playerInventory, mouseX, mouseY);
                }));

        event.register(Registries.MENU,
                ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, WorkshopContainer.NAME),
                () -> IMenuTypeExtension.create((id, playerInventory, data) ->
                {
                    BlockPos blockPos = data.readBlockPos();
                    double mouseX = data.readDouble();
                    double mouseY = data.readDouble();
                    Player player = playerInventory.player;
                    Level world = player.level();
                    return new WorkshopContainer(id, world, blockPos, playerInventory, mouseX, mouseY);
                }));

        event.register(Registries.MENU,
                ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, ToolboxContainer.NAME),
                () -> IMenuTypeExtension.create((id, playerInventory, data) ->
                {
                    BlockPos blockPos = data.readBlockPos();
                    double mouseX = data.readDouble();
                    double mouseY = data.readDouble();
                    Player player = playerInventory.player;
                    Level world = player.level();
                    return new ToolboxContainer(id, world, blockPos, playerInventory);
                }));

        event.register(Registries.MENU,
                ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, ToolboxMechanicalContainer.NAME),
                () -> IMenuTypeExtension.create((id, playerInventory, data) ->
                {
                    BlockPos blockPos = data.readBlockPos();
                    double mouseX = data.readDouble();
                    double mouseY = data.readDouble();
                    Player player = playerInventory.player;
                    Level world = player.level();
                    return new ToolboxMechanicalContainer(id, world, blockPos, playerInventory);
                }));
    }
}
