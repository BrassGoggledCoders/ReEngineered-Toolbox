package xyz.brassgoggledcoders.reengineeredtoolbox.api.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface IPanelModel {
    @Nullable
    IBakedPanelModel bake();

    @Nonnull
    Collection<RenderMaterial> getTextures(Function<ResourceLocation, IUnbakedModel> pModelGetter,
                                           Set<Pair<String, String>> pMissingTextureErrors);
}
