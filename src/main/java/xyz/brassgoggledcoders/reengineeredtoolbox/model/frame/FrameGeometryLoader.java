package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class FrameGeometryLoader implements IGeometryLoader<FrameUnbakedGeometry> {
    public static final String NAME = "frame";

    @Override
    public FrameUnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        BlockModel frameModel = deserializationContext.deserialize(GsonHelper.getAsJsonObject(jsonObject, "frame"), BlockModel.class);
        if (frameModel == null) {
            throw new JsonParseException("Frame Model failed to Parse");
        } else {
            return new FrameUnbakedGeometry(frameModel);
        }
    }
}
