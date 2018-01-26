package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;

public class SocketBakedQuad extends BakedQuad {
    public SocketBakedQuad(EnumFacing socketFacing, int[] vertexData, int tintIndex, EnumFacing face, TextureAtlasSprite sprite) {
        super(offsetQuad(socketFacing, vertexData), tintIndex, face, sprite, true, DefaultVertexFormats.ITEM);
    }

    private static int[] offsetQuad(EnumFacing socketFacing, int[] vertexData) {
        return vertexData;
    }
}
