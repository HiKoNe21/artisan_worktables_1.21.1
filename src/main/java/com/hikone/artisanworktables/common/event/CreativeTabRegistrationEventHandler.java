package com.hikone.artisanworktables.common.event;

import com.hikone.artisanworktables.ArtisanWorktablesMod;
import com.hikone.artisanworktables.common.reference.EnumTier;
import com.hikone.artisanworktables.common.reference.EnumType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class CreativeTabRegistrationEventHandler
{

    @SubscribeEvent
    public void on(RegisterEvent event)
    {
        if (!event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB))
        {
            return;
        }

        event.register(Registries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MODID, "general"),
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + ArtisanWorktablesMod.MODID))
                        .icon(() -> new ItemStack(ArtisanWorktablesMod.Blocks.TOOLBOX))
                        .displayItems((parameters, output) ->
                        {
                            for (EnumType type : EnumType.values())
                            {
                                for (EnumTier tier : EnumTier.values())
                                {
                                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                                            ArtisanWorktablesMod.MODID, tier.getName() + "_" + type.getName());
                                    Item item = BuiltInRegistries.ITEM.get(id);

                                    if (item != Items.AIR)
                                    {
                                        output.accept(item);
                                    }
                                }
                            }

                            output.accept(ArtisanWorktablesMod.Blocks.TOOLBOX);
                            output.accept(ArtisanWorktablesMod.Blocks.MECHANICAL_TOOLBOX);
                        })
                        .build());
    }
}
