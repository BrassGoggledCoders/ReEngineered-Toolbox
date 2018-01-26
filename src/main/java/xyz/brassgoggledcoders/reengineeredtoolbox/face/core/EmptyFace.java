package xyz.brassgoggledcoders.reengineeredtoolbox.face.core;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.SocketBakedQuad;

import java.util.List;
import java.util.function.Function;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class EmptyFace extends Face {
    public EmptyFace() {
        this.setRegistryName(MOD_ID, "empty");
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public List<BakedQuad> getModel(EnumFacing socketSide, Function<ResourceLocation, TextureAtlasSprite> spriteFunction) {
        List<BakedQuad> bakedQuads = Lists.newArrayList();
        bakedQuads.add(new SocketBakedQuad(socketSide, new int[] {0,0,12, 12}, 0, EnumFacing.UP,
                spriteFunction.apply(new ResourceLocation(MOD_ID, "textures/faces/empty"))));
        return bakedQuads;
    }
}
