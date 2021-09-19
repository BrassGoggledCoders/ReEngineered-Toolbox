package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FrameModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    private ModelBuilder<T> frameBuilder;
    private ModelFile frameModel;

    public FrameModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(FrameModelLoader.LOADER_ID, parent, existingFileHelper);
    }

    public FrameModelBuilder<T> withFrame(ModelBuilder<T> frameBuilder) {
        Preconditions.checkNotNull(frameBuilder, "frameBuilder must not be null");
        Preconditions.checkArgument(this.frameModel == null, "cannot specific both frameBuilder and frameModel");
        this.frameBuilder = frameBuilder;
        return this;
    }

    public FrameModelBuilder<T> withFrame(ModelFile frameModel) {
        Preconditions.checkNotNull(frameModel, "frameModel must not be null");
        Preconditions.checkArgument(this.frameBuilder == null, "cannot specific both frameBuilder and frameModel");
        this.frameModel = frameModel;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        JsonObject jsonObject = super.toJson(json);
        if (frameBuilder != null) {
            JsonObject frameObject = frameBuilder.toJson();
            jsonObject.add("frame", frameObject);
        } else if (frameModel != null) {
            jsonObject.addProperty("frame", frameModel.getLocation().toString());
        }

        return jsonObject;
    }
}
