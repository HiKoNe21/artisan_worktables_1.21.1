package com.hikone.artisanworktables.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;

import java.awt.*;

public final class GuiHelper
{
    public static final ResourceLocation GALACTIC_ALT_FONT = ResourceLocation.fromNamespaceAndPath("minecraft", "alt");
    public static final Style GALACTIC_STYLE = Style.EMPTY.withFont(GALACTIC_ALT_FONT);

    public static MutableComponent asGalactic(MutableComponent textComponent)
    {
        return textComponent.withStyle(GALACTIC_STYLE);
    }

    public static void drawModalRectWithCustomSizedTexture(PoseStack poseStack, int x, int y, int z, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, x, y + height, z).setUv(u * f, (v + (float) height) * f1);
        bufferbuilder.addVertex(matrix, x + width, y + height, z).setUv((u + (float) width) * f, (v + (float) height) * f1);
        bufferbuilder.addVertex(matrix, x + width, y, z).setUv((u + (float) width) * f, v * f1);
        bufferbuilder.addVertex(matrix, x, y, z).setUv(u * f, v * f1);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    public static void drawStringOutlined(PoseStack poseStack, Component textComponent, int x, int y, Font fontRenderer, int textShadowColor)
    {
        GuiHelper.drawStringOutlined(poseStack, textComponent, x, y, fontRenderer, textShadowColor, false);
    }

    public static void drawStringOutlined(PoseStack poseStack, Component textComponent, int x, int y, Font fontRenderer, int textShadowColor, boolean dropShadow)
    {
        Minecraft minecraft = Minecraft.getInstance();
        var bufferSource = minecraft.renderBuffers().bufferSource();
        Matrix4f matrix = poseStack.last().pose();

        if (dropShadow)
        {
            fontRenderer.drawInBatch(textComponent, (float) (x + 0), (float) (y + 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x + 1), (float) (y + 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x + 1), (float) (y - 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x + 1), (float) (y + 0), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            fontRenderer.drawInBatch(textComponent, (float) (x - 0), (float) (y - 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x - 1), (float) (y - 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x - 1), (float) (y + 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x - 1), (float) (y - 0), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            fontRenderer.drawInBatch(textComponent, (float) x, (float) y, Color.BLACK.getRGB(), false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

        }
        else
        {
            fontRenderer.drawInBatch(textComponent, (float) (x + 0), (float) (y + 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x + 1), (float) (y + 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x + 1), (float) (y - 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x + 1), (float) (y + 0), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            fontRenderer.drawInBatch(textComponent, (float) (x - 0), (float) (y - 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x - 1), (float) (y - 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x - 1), (float) (y + 1), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            fontRenderer.drawInBatch(textComponent, (float) (x - 1), (float) (y - 0), textShadowColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            fontRenderer.drawInBatch(textComponent, (float) x, (float) y, Color.BLACK.getRGB(), false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }

    public static void drawTexturedRect(Minecraft minecraft, ResourceLocation texture, PoseStack poseStack, int x, int y, int width, int height, int zLevel, float u0, float v0, float u1, float v1)
    {
        TextureManager renderEngine = minecraft.getTextureManager();
        RenderSystem.setShaderTexture(0, texture);

        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix, x, (y + height), zLevel).setUv(u0, v1);
        bufferbuilder.addVertex(matrix, (x + width), (y + height), zLevel).setUv(u1, v1);
        bufferbuilder.addVertex(matrix, (x + width), y, zLevel).setUv(u1, v0);
        bufferbuilder.addVertex(matrix, x, y, zLevel).setUv(u0, v0);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    public static void drawColoredRect(
            PoseStack poseStack,
            int x,
            int y,
            int width,
            int height,
            int red,
            int green,
            int blue,
            int alpha
    )
    {
        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder renderer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        renderer.addVertex(matrix, x + 0, y + 0, 0).setColor(red, green, blue, alpha);
        renderer.addVertex(matrix, x + 0, y + height, 0).setColor(red, green, blue, alpha);
        renderer.addVertex(matrix, x + width, y + height, 0).setColor(red, green, blue, alpha);
        renderer.addVertex(matrix, x + width, y + 0, 0).setColor(red, green, blue, alpha);
        BufferUploader.drawWithShader(renderer.buildOrThrow());
    }

    public static void drawVerticalScaledTexturedModalRectFromIconAnchorBottomLeft(
            PoseStack poseStack,
            int x,
            int y,
            float z,
            TextureAtlasSprite icon,
            int width,
            int height)
    {
        // TODO: this only handles tiling vertically, need to implement horizontal tiling as well

        if (icon == null)
        {
            return;
        }

        int iconHeight = icon.contents().height();
        int iconWidth = icon.contents().width();

        float minU = icon.getU0();
        float maxU = icon.getU1();
        float minV = icon.getV0();
        float maxV = icon.getV1();

        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        int sections = height / iconHeight;

        for (int i = 0; i < sections; i++)
        {
            buffer.addVertex(matrix, x, y + height - (i * iconHeight), z)
                    .setUv(minU, maxV);

            buffer.addVertex(matrix, x + width, y + height - (i * iconHeight), z)
                    .setUv(minU + (maxU - minU) * width / (float) iconWidth, maxV);

            buffer.addVertex(matrix, x + width, y + height - ((i + 1) * iconHeight), z)
                    .setUv(minU + (maxU - minU) * width / (float) iconWidth, maxV - (maxV - minV));

            buffer.addVertex(matrix, x, y + height - ((i + 1) * iconHeight), z)
                    .setUv(minU, maxV - (maxV - minV));
        }

        int remainder = height - sections * iconHeight;

        if (remainder > 0)
        {

            buffer.addVertex(matrix, x, y + height - (sections * iconHeight), z)
                    .setUv(minU, maxV);

            buffer.addVertex(matrix, x + width, y + height - (sections * iconHeight), z)
                    .setUv(minU + (maxU - minU) * width / (float) iconWidth, maxV);

            buffer.addVertex(matrix, x + width, y + height - (sections * iconHeight + remainder), z)
                    .setUv(minU + (maxU - minU) * width / (float) iconWidth, maxV - (maxV - minV) * remainder / (float) iconHeight);

            buffer.addVertex(matrix, x, y + height - (sections * iconHeight + remainder), z)
                    .setUv(minU, maxV - (maxV - minV) * remainder / (float) iconHeight);
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());

    }

    public static void drawScaledTexturedModalRectFromIconAnchorBottomLeft(
            PoseStack poseStack,
            int x,
            int y,
            float z,
            TextureAtlasSprite icon,
            int width,
            int height)
    {
        if (icon == null)
        {
            return;
        }

        int iconHeight = icon.contents().height();
        int iconWidth = icon.contents().width();

        float minU = icon.getU0();
        float maxU = icon.getU1();
        float minV = icon.getV0();
        float maxV = icon.getV1();

        int verticalSections = height / iconHeight + 1;
        int horizontalSections = width / iconWidth + 1;

        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        for (int i = 0; i < verticalSections; i++)
        {
            for (int j = 0; j < horizontalSections; j++)
            {

                int px1 = x + (j * iconWidth);
                int px2 = x + Math.min((j + 1) * iconWidth, width);
                int py1 = y + height - (i * iconHeight);
                int py2 = y + height - Math.min((i + 1) * iconHeight, height);

                float tu2 = minU + (maxU - minU) * (((j + 1) * iconWidth > width)
                        ? (width - (j * iconWidth)) / (float) iconWidth : 1);
                float tv2 = maxV - (maxV - minV) * (((i + 1) * iconHeight > height)
                        ? (height - (i * iconHeight)) / (float) iconHeight : 1);

                buffer.addVertex(matrix, px1, py1, z).setUv(minU, maxV);
                buffer.addVertex(matrix, px2, py1, z).setUv(tu2, maxV);
                buffer.addVertex(matrix, px2, py2, z).setUv(tu2, tv2);
                buffer.addVertex(matrix, px1, py2, z).setUv(minU, tv2);
            }
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    public static int getFluidHeight(int fluidAmount, int fluidCapacity, int displayHeight)
    {

        float fluidHeightScalar = GuiHelper.getFluidHeightScalar(fluidAmount, fluidCapacity, displayHeight);
        int elementHeightModified = (int) (fluidHeightScalar * displayHeight);
        return Math.max(0, Math.min(elementHeightModified, displayHeight));
    }

    public static float getFluidHeightScalar(int fluidAmount, int fluidCapacity, int displayHeight)
    {

        if (fluidAmount > 0)
        {
            return Math.max((float) fluidAmount / (float) fluidCapacity, 1 / (float) displayHeight);

        } else
        {
            return 0;
        }
    }

    public static int getFluidY(int fluidAmount, int fluidCapacity, int displayHeight, int offsetY)
    {

        float fluidHeightScalar = GuiHelper.getFluidHeightScalar(fluidAmount, fluidCapacity, displayHeight);
        int elementHeightModified = (int) (fluidHeightScalar * displayHeight);
        return displayHeight - Math.max(0, Math.min(elementHeightModified, displayHeight)) + offsetY;
    }

    // https://github.com/TheCBProject/CoFHLib/blob/master/src/main/java/cofh/lib/gui/GuiBase.java
    public static void drawScaledTexturedModalRectFromIcon(
            PoseStack poseStack,
            int x,
            int y,
            float z,
            TextureAtlasSprite icon,
            int width,
            int height)
    {
        if (icon == null)
        {
            return;
        }

        int iconHeight = icon.contents().height();
        int iconWidth = icon.contents().width();

        float minU = icon.getU0();
        float maxU = icon.getU1();
        float minV = icon.getV0();
        float maxV = icon.getV1();

        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer
                .addVertex(matrix, x, y + height, z)
                .setUv(minU, minV + (maxV - minV) * height / (float) iconHeight);
        buffer
                .addVertex(matrix, x + width, y + height, z)
                .setUv(minU + (maxU - minU) * width / (float) iconWidth, minV + (maxV - minV) * height / (float) iconHeight);
        buffer
                .addVertex(matrix, x + width, y, z)
                .setUv(minU + (maxU - minU) * width / (float) iconWidth, minV);
        buffer
                .addVertex(matrix, x, y, z)
                .setUv(minU, minV);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

    }

    /**
     * Draws a textured square with an optionally rotated texture.
     *
     * @param x        the x
     * @param y        the y
     * @param textureX the texture x
     * @param textureY the texture y
     * @param size     the size
     * @param rotation (clockwise) 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
     */
    public static void drawRotatedTexturedModalSquare(
            PoseStack poseStack,
            int x,
            int y,
            float z,
            int textureX,
            int textureY,
            int size,
            int rotation
    )
    {

        // TODO: these magic numbers tho...

        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder vertexbuffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        if (rotation == 1)
        {

            vertexbuffer
                    .addVertex(matrix, x, y + size, z)
                    .setUv((textureX + size) * 0.00390625F, (textureY + size) * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, (y + size), z)
                    .setUv((textureX + size) * 0.00390625F, textureY * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, y, z)
                    .setUv(textureX * 0.00390625F, textureY * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x, y, z)
                    .setUv(textureX * 0.00390625F, (textureY + size) * 0.00390625F);

        } else if (rotation == 2)
        {

            vertexbuffer
                    .addVertex(matrix, x, y + size, z)
                    .setUv((textureX + size) * 0.00390625F, textureY * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, (y + size), z)
                    .setUv(textureX * 0.00390625F, textureY * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, y, z)
                    .setUv(textureX * 0.00390625F, (textureY + size) * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x, y, z)
                    .setUv((textureX + size) * 0.00390625F, (textureY + size) * 0.00390625F);

        } else if (rotation == 3)
        {

            vertexbuffer
                    .addVertex(matrix, x, y + size, z)
                    .setUv(textureX * 0.00390625F, textureY * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, (y + size), z)
                    .setUv(textureX * 0.00390625F, (textureY + size) * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, y, z)
                    .setUv((textureX + size) * 0.00390625F, (textureY + size) * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x, y, z)
                    .setUv((textureX + size) * 0.00390625F, textureY * 0.00390625F);

        } else
        { // rotation 0 is default

            vertexbuffer
                    .addVertex(matrix, x, y + size, z)
                    .setUv(textureX * 0.00390625F, (textureY + size) * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, (y + size), z)
                    .setUv((textureX + size) * 0.00390625F, (textureY + size) * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x + size, y, z)
                    .setUv((textureX + size) * 0.00390625F, textureY * 0.00390625F);
            vertexbuffer
                    .addVertex(matrix, x, y, z)
                    .setUv(textureX * 0.00390625F, textureY * 0.00390625F);

        }

        BufferUploader.drawWithShader(vertexbuffer.buildOrThrow());

    }

    private GuiHelper()
    {
        //
    }

}
