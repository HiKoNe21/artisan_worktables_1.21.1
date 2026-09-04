package com.hikone.artisanworktables.common.internal.network.tile.client;

import com.hikone.artisanworktables.ArtisanWorktablesModClientConfig;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class TileDataServiceOverlayRenderer
{
    private static final TileDataServiceOverlayRenderer INSTANCE = new TileDataServiceOverlayRenderer();

    @SubscribeEvent
    public static void onRenderGuiPostEvent(RenderGuiEvent.Post event)
    {
        if (!ArtisanWorktablesModClientConfig.enableTileDataServiceMonitor || Minecraft.getInstance().isPaused())
        {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        // --- Total ---

        INSTANCE.renderMonitor(guiGraphics, TileDataServiceClientMonitor.TOTAL, scaledWidth / 2 - 32 - 128, 100, "Total Rx");

        // --- Position ---

        HitResult traceResult = Minecraft.getInstance().hitResult;

        if (traceResult != null && traceResult.getType() == HitResult.Type.BLOCK)
        {
            BlockPos blockPos = ((BlockHitResult) traceResult).getBlockPos();
            TileDataServiceClientMonitor monitor = TileDataServiceClientMonitor.findMonitorForPosition(blockPos);

            int x = scaledWidth / 2 - 32 + 128;
            int y = 100;

            if (monitor != null)
            {
                String title = "[" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + "]";
                INSTANCE.renderMonitor(guiGraphics, monitor, x, y, title);
            }

            TileDataTrackerUpdateMonitor trackerUpdateMonitor = TileDataServiceClientMonitor.getTrackerUpdateMonitor();
            Object2ObjectArrayMap<BlockPos, Object2IntArrayMap<Class<?>>> updateMap = trackerUpdateMonitor.getPublicTrackerUpdateMap();
            Object2IntArrayMap<Class<?>> map = updateMap.get(blockPos);

            if (map != null)
            {
                ObjectIterator<Object2IntMap.Entry<Class<?>>> iterator = map.object2IntEntrySet().iterator();
                Font font = Minecraft.getInstance().font;
                int index = 0;

                while (iterator.hasNext())
                {
                    Object2IntMap.Entry<Class<?>> entry = iterator.next();

                    Class<?> dataClass = entry.getKey();
                    int count = entry.getIntValue();

                    guiGraphics.drawString(font, dataClass.getSimpleName() + " " + count, x + 64 /* TODO: const */ + 5, y + 9 + index * 10, Color.WHITE.getRGB());
                    index += 1;
                }
            }
        }
    }

    public void renderMonitor(GuiGraphics guiGraphics, TileDataServiceClientMonitor monitor, int x, int y, String title)
    {
        int trackedIndex = ArtisanWorktablesModClientConfig.tileDataServiceMonitorTrackedIndex;
        int totalWidth = 64; // TODO: const

        int size = monitor.size();

        if (size == 0)
        {
            return;
        }

        int max = 0;
        int min = Integer.MAX_VALUE;
        int minActual = Integer.MAX_VALUE;
        int total = 0;
        int tracked = 0;

        for (int i = 0; i < size; i++)
        {
            int count = monitor.get(i);
            total += count;

            if (i == trackedIndex)
            {
                tracked = count;
            }

            if (count > max)
            {
                max = count;
            }

            if (count > 0 && count < min)
            {
                min = count;
            }

            if (count < minActual)
            {
                minActual = count;
            }
        }

        if (min > max)
        {
            min = 0;
        }

        Font font = Minecraft.getInstance().font;

        {
            int textWidth = font.width(title);
            guiGraphics.drawString(font, title, (int) (x - (textWidth / 2.0) + (totalWidth / 2.0)), y - 9, Color.WHITE.getRGB());
        }

        {
            String text = "§a" + min + " §e" + (int) (total / (float) size) + " §c" + max + " §9" + tracked;
            int textWidth = font.width(text);
            guiGraphics.drawString(font, text, (int) (x - (textWidth / 2.0) + (totalWidth / 2.0)), y, Color.WHITE.getRGB());
        }

        if (max == 0)
        {
            return; // prevent div by zero
        }

        // Only render if the first value
        if (minActual != max)
        {
            int height = 1;
            y += 9;

            int avg = (int) (total / (float) size);

            fillColoredQuad(guiGraphics, x, y, totalWidth, size, 0, 0, 0, 0.75f);

            int trackedX = 0;
            int innerY = y - 1;
            for (int i = 0; i < size; i++)
            {
                float widthScalar = monitor.get(i) / (float) max;
                int width = (int) (totalWidth * widthScalar);
                innerY += height;

                if (i == trackedIndex)
                {
                    trackedX = width;

                }
                else if (width == totalWidth)
                {
                    fillColoredQuad(guiGraphics, x, innerY, width, height, 1, 0, 0, 0.75f);

                }
                else if (width < (min / (float) max) * totalWidth + 1)
                {
                    fillColoredQuad(guiGraphics, x, innerY, width, height, 0, 1, 0, 0.75f);

                }
                else
                {
                    fillColoredQuad(guiGraphics, x, innerY, width, height, 1, 1, 1, 0.5f);
                }
            }

            fillColoredQuad(guiGraphics, (int) (((avg / (float) max) * totalWidth) + x), y, 1, size, 1, 1, 0, 1);
            fillColoredQuad(guiGraphics, (int) (((min / (float) max) * totalWidth) + x), y, 1, size, 0, 1, 0, 1);
            fillColoredQuad(guiGraphics, totalWidth + x, y, 1, size, 1, 0, 0, 1);

            if (trackedX > 0)
            {
                fillColoredQuad(guiGraphics, x, trackedIndex + y, trackedX, 1, 85 / 255f, 85 / 255f, 1, 1);
                fillColoredQuad(guiGraphics, (trackedX + x), y, 1, size, 85 / 255f, 85 / 255f, 1, 1);
            }
        }
    }

    private static void fillColoredQuad(GuiGraphics guiGraphics, int x, int y, float width, int height, float red, float green, float blue, float alpha)
    {
        int color = ((int) (alpha * 255) & 0xFF) << 24 | ((int) (red * 255) & 0xFF) << 16 | ((int) (green * 255) & 0xFF) << 8 | ((int) (blue * 255) & 0xFF);

        guiGraphics.fill(x, y, x + (int) width, y + height, color);
    }
}
