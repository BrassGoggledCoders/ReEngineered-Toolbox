package xyz.brassgoggledcoders.reengineeredtoolbox.model.panel;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IBakedPanelModel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.model.IPanelModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PanelModel implements IPanelModel {
    private final Map<String, ResourceLocation> textures;
    private final List<BlockPart> elements;

    public PanelModel(Map<String, ResourceLocation> textures, List<BlockPart> elements) {
        this.textures = textures;
        this.elements = elements;
    }

    public List<BlockPart> getElements() {
        return elements;
    }

    public Map<String, ResourceLocation> getTextures() {
        return textures;
    }

    @Override
    public IBakedPanelModel bake() {
        return null;
    }

    @Override
    public List<RenderMaterial> getTextures(Function<ResourceLocation, IUnbakedModel> pModelGetter,
                                            Set<Pair<String, String>> pMissingTextureErrors) {
        return textures.values()
                .stream()
                .map(texture -> new RenderMaterial(
                        PlayerContainer.BLOCK_ATLAS,
                        texture
                ))
                .collect(Collectors.toList());
    }
}
