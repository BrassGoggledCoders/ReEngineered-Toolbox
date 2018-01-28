package xyz.brassgoggledcoders.reengineeredtoolbox.render;

import com.teamacronymcoders.base.util.TextureUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.TileEntitySocket;

public class TESRSocket extends TileEntitySpecialRenderer<TileEntitySocket> {
    @Override
    public void render(TileEntitySocket socket, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        TextureUtils.pre(x, y, z);
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.pushMatrix();
        Face upFace = socket.getFace(EnumFacing.UP);
        TextureUtils.putTexturedQuad(bufferBuilder, upFace.getSprite(), 0.125, -upFace.getTextureOffset(), 0.125,
                0.75, 1, 0.75, EnumFacing.UP, -1, 225, false);
        GlStateManager.popMatrix();

        Face downFace = socket.getFace(EnumFacing.DOWN);
        TextureUtils.putTexturedQuad(bufferBuilder, downFace.getSprite(), 0.125, upFace.getTextureOffset(), 0.125,
                0.75, 1, 0.75, EnumFacing.DOWN, -1, 225, false);

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            Face face = socket.getFace(facing);
            TextureUtils.putTexturedQuad(bufferBuilder, face.getSprite(), 0.125, upFace.getTextureOffset(), 0.125,
                    0.75, 0.75, 0.75, facing, -1, 225, false);
        }

        tessellator.draw();
        TextureUtils.post();
    }
}
