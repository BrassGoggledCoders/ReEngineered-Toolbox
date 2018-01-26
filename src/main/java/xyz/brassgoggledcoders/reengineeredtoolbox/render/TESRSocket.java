package xyz.brassgoggledcoders.reengineeredtoolbox.render;

import com.teamacronymcoders.base.client.ClientHelper;
import com.teamacronymcoders.base.util.TextureUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.TileEntitySocket;

public class TESRSocket extends TileEntitySpecialRenderer<TileEntitySocket> {
    @Override
    public void render(TileEntitySocket socket, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        TextureUtils.pre(x, y, z);
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        TextureAtlasSprite up = ClientHelper.mc().getTextureMapBlocks()
                .getTextureExtry(socket.getFace(EnumFacing.UP).getTextureLocation().toString());
        TextureUtils.putTexturedQuad(bufferBuilder, up, 0.125, -0.125, 0.125, 0.75, 1, 0.75, EnumFacing.UP, -1, 225, false);

        TextureAtlasSprite down = ClientHelper.mc().getTextureMapBlocks()
                .getTextureExtry(socket.getFace(EnumFacing.DOWN).getTextureLocation().toString());
        TextureUtils.putTexturedQuad(bufferBuilder, down, 0.125, 0.125, 0.125, 0.75, 1, 0.75, EnumFacing.DOWN, -1, 225, false);

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            TextureAtlasSprite north = ClientHelper.mc().getTextureMapBlocks()
                    .getTextureExtry(socket.getFace(facing).getTextureLocation().toString());
            TextureUtils.putTexturedQuad(bufferBuilder, north, 0.125, 0.125, 0.125, 0.75, 0.75, 0.75, facing, -1, 225, false);

        }

        tessellator.draw();
        TextureUtils.post();
    }
}
