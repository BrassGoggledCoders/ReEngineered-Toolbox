package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class FrameGeometryLoader implements IGeometryLoader<FrameUnbakedGeometry> {
    @Override
    public FrameUnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new FrameUnbakedGeometry();
    }
}
