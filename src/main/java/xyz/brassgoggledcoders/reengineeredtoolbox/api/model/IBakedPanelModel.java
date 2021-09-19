package xyz.brassgoggledcoders.reengineeredtoolbox.api.model;

import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.Direction;

import java.util.List;

public interface IBakedPanelModel {
    List<BakedQuad> getQuads(Direction direction);
}
