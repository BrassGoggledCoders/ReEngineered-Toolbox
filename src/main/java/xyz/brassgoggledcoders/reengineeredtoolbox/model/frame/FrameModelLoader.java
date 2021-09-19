package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class FrameModelLoader implements IModelLoader<FrameModel> {
    public static final ResourceLocation LOADER_ID = ReEngineeredToolbox.rl("frame");

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {

    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public FrameModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        BlockModel frameModel = deserializationContext.deserialize(JSONUtils.getAsJsonObject(modelContents, "frame"), BlockModel.class);
        if (frameModel == null) {
            throw new JsonParseException("Frame Model failed to Parse");
        } else {
            return new FrameModel(frameModel);
        }
    }
}
