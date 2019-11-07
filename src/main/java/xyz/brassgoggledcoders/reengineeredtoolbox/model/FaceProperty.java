package xyz.brassgoggledcoders.reengineeredtoolbox.model;

import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.ModelProperty;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nullable;

public enum FaceProperty {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    public static FaceProperty[] VALUES = values();

    private final Direction direction;
    private final ModelProperty<FaceInstance> modelProperty;

    FaceProperty(Direction direction) {
        this.direction = direction;
        this.modelProperty = new ModelProperty<>();
    }

    @Nullable
    public static ModelProperty<FaceInstance> getModelForSide(Direction side) {
        for (FaceProperty faceProperty: VALUES) {
            if (side == faceProperty.getDirection()) {
                return faceProperty.getModelProperty();
            }
        }
        return null;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public ModelProperty<FaceInstance> getModelProperty() {
        return this.modelProperty;
    }
}
