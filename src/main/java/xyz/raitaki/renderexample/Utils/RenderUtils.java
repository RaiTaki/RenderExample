package xyz.raitaki.renderexample.Utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.renderer.RendererUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.GameTestDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenderUtils {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final AtomicBoolean renderThroughWalls = new AtomicBoolean(false);
    public static void renderLine(MatrixStack matrices, Vec3d loc1, Vec3d loc2, double scale, Color color) {
        Vec3d loc3 = loc1.add(scale,0,scale);
        Vec3d loc4 = loc2.add(scale,0, scale);

        RenderUtils.renderQuad(matrices, loc1, loc3, loc2, loc4, color);
        RenderUtils.renderQuad(matrices, loc1.add(0,-scale,0), loc3.add(0,-scale,0), loc2.add(0,-scale,0), loc4.add(0,-scale,0), color);
        RenderUtils.renderQuad(matrices, loc1.add(0,-scale,0), loc2.add(0,-scale,0), loc1, loc2, color);
        RenderUtils.renderQuad(matrices, loc3.add(0,-scale,0), loc4.add(0,-scale,0), loc3, loc4, color);
    }

    public static void renderFilledT(MatrixStack stack, Vec3d start, Vec3d end, Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = client.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        start = start.subtract(camPos);
        end = end.subtract(camPos);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x1 = (float) start.x;
        float y1 = (float) start.y;
        float z1 = (float) start.z;
        float x2 = (float) end.x;
        float y2 = (float) end.y;
        float z2 = (float) end.z;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RendererUtils.setupRender();

        RenderSystem.disableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
        RendererUtils.endRender();
    }

    public static void renderFilled(MatrixStack stack, Vec3d start, Vec3d dimensions, Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = client.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        start = start.subtract(camPos);
        Vec3d end = start.add(dimensions);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x1 = (float) start.x;
        float y1 = (float) start.y;
        float z1 = (float) start.z;
        float x2 = (float) end.x;
        float y2 = (float) end.y;
        float z2 = (float) end.z;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        RendererUtils.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).next();

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RendererUtils.endRender();
    }

    public static void renderFlat(MatrixStack stack, Vec3d start, Vec3d end, Color color){
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = client.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        start = start.subtract(camPos);
        end = end.subtract(camPos);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x1 = (float) start.x;
        float y1 = (float) start.y;
        float z1 = (float) start.z;
        float x2 = (float) end.x;
        float y2 = (float) end.y;
        float z2 = (float) end.z;
    }

    public static void renderTriangle(MatrixStack stack, Vec3d dot1, Vec3d dot2, Vec3d dot3, Color color) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = client.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        dot1 = dot1.subtract(camPos);
        dot2 = dot2.subtract(camPos);
        dot3 = dot3.subtract(camPos);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x1 = (float) dot1.x;
        float y1 = (float) dot1.y;
        float z1 = (float) dot1.z;
        float x2 = (float) dot2.x;
        float y2 = (float) dot2.y;
        float z2 = (float) dot2.z;
        float x3 = (float) dot3.x;
        float y3 = (float) dot3.y;
        float z3 = (float) dot3.z;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RendererUtils.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x3, y2, z3).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RendererUtils.endRender();
    }

    public static void renderQuad(MatrixStack stack, Vec3d start, Vec3d end, Vec3d border, Vec3d border2, Color color){
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = client.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        start = start.subtract(camPos);
        end = end.subtract(camPos);
        border = border.subtract(camPos);
        border2 = border2.subtract(camPos);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x1 = (float) start.x;
        float y1 = (float) start.y;
        float z1 = (float) start.z;

        float x2 = (float) end.x;
        float y2 = (float) end.y;
        float z2 = (float) end.z;

        float x4 = (float) border.x;
        float y4 = (float) border.y;
        float z4 = (float) border.z;

        float x5 = (float) border2.x;
        float y5 = (float) border2.y;
        float z5 = (float) border2.z;

        BufferBuilder buffer3 = Tessellator.getInstance().getBuffer();
        RendererUtils.setupRender();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer3.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer3.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
        buffer3.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();
        buffer3.vertex(matrix, x5, y5, z5).color(red, green, blue, alpha).next();
        buffer3.vertex(matrix, x4, y4, z4).color(red,green,blue, alpha).next();

        BufferRenderer.drawWithShader(buffer3.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RendererUtils.endRender();
    }

    public static void renderText(BlockPos pos, String text, Color marker, Float size, boolean rotate, Float angle, boolean visiblethrough) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 0.75F);
        RenderSystem.disableTexture();
        DebugRenderer.drawBox(pos, 0.02F, marker.getBlue(), marker.getGreen(), marker.getAlpha(), marker.getRed());
        if (!text.isEmpty()) {
            double d = (double)pos.getX() + 0.5D;
            double e = (double)pos.getY() + 1.2D;
            double f = (double)pos.getZ() + 0.5D;
            drawString(text, d, e, f, -1, size, true, 0.0F, visiblethrough, rotate, angle);
        }

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawString(String string, double x, double y, double z, int color, float size, boolean center, float offset, boolean visibleThroughObjects, boolean rotate, Float angle) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Camera camera = minecraftClient.gameRenderer.getCamera();
        if (camera.isReady() && minecraftClient.getEntityRenderDispatcher().gameOptions != null) {
            TextRenderer textRenderer = minecraftClient.textRenderer;
            double d = camera.getPos().x;
            double e = camera.getPos().y;
            double f = camera.getPos().z;

            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.push();
            matrixStack.translate((double)((float)(x - d)), (double)((float)(y - e) + 0.07F), (double)((float)(z - f)));
            if(!rotate)
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(angle));
            else
                matrixStack.multiplyPositionMatrix(new Matrix4f(camera.getRotation()));
            matrixStack.scale(size, -size, size);
            RenderSystem.enableTexture();
            if (visibleThroughObjects) {
                RenderSystem.disableDepthTest();
            } else {
                RenderSystem.enableDepthTest();
            }

            RenderSystem.depthMask(true);
            matrixStack.scale(-1.0F, 1.0F, 1.0F);
            RenderSystem.applyModelViewMatrix();
            float g = center ? (float)(-textRenderer.getWidth(string)) / 2.0F : 0.0F;
            g -= offset / size;
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            textRenderer.draw(string, g, 0.0F, color, false, AffineTransformation.identity().getMatrix(), immediate, visibleThroughObjects, 0, 15728880);
            immediate.draw();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableDepthTest();
            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
        }
    }
}
