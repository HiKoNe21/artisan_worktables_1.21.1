package com.hikone.artisanworktables;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ArtisanWorktablesModClientConfig
{
    public static final ModConfigSpec CONFIG_SPEC;
    public static final ConfigClient CONFIG;

    static
    {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        CONFIG = new ConfigClient(builder);
        CONFIG_SPEC = builder.build();
    }

    public static boolean enableTileDataServiceMonitor;
    public static int tileDataServiceMonitorUpdateIntervalTicks;
    public static int tileDataServiceMonitorTrackedIndex;

    public static void onModConfigEvent(ModConfigEvent configEvent)
    {
        if (configEvent.getConfig().getSpec() == CONFIG_SPEC && CONFIG_SPEC.isLoaded())
        {
            bake();
        }
    }

    public static void bake()
    {
        enableTileDataServiceMonitor = CONFIG.enableTileDataServiceMonitor.get();
        tileDataServiceMonitorUpdateIntervalTicks = CONFIG.tileDataServiceMonitorUpdateIntervalTicks.get();
        tileDataServiceMonitorTrackedIndex = CONFIG.tileDataServiceMonitorTrackedIndex.get();
    }

    public static class ConfigClient
    {
        public final ModConfigSpec.BooleanValue enableTileDataServiceMonitor;
        public final ModConfigSpec.IntValue tileDataServiceMonitorUpdateIntervalTicks;
        public final ModConfigSpec.IntValue tileDataServiceMonitorTrackedIndex;

        public ConfigClient(ModConfigSpec.Builder builder)
        {
            this.enableTileDataServiceMonitor = builder
                    .comment(
                            "Enables / disables the tile data service monitor.",
                            "Default: false"
                    )
                    .define("enableTileDataServiceMonitor", false);

            this.tileDataServiceMonitorUpdateIntervalTicks = builder
                    .comment(
                            "Sets the tile data service monitor update interval in ticks.",
                            "Default: 20"
                    )
                    .defineInRange("tileDataServiceMonitorUpdateIntervalTicks", 20, 20, Integer.MAX_VALUE);

            this.tileDataServiceMonitorTrackedIndex = builder
                    .comment(
                            "Sets the tile data service monitor's tracked index.",
                            "Default: 10"
                    )
                    .defineInRange("tileDataServiceMonitorTrackedIndex", 10, 0, Integer.MAX_VALUE);
        }
    }
}
