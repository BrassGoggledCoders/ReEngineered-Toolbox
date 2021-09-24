package xyz.brassgoggledcoders.reengineeredtoolbox.model.frame;

import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.ModelProperty;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.PanelInfo;

import javax.annotation.Nullable;

public enum FrameModelProperty {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST);

    public static FrameModelProperty[] VALUES = values();

    private final Direction direction;
    private final ModelProperty<PanelInfo> modelProperty;

    FrameModelProperty(Direction direction) {
        this.direction = direction;
        this.modelProperty = new ModelProperty<>();
    }

    @Nullable
    public static ModelProperty<PanelInfo> getModelForSide(Direction side) {
        for (FrameModelProperty frameProperty : VALUES) {
            if (side == frameProperty.getDirection()) {
                return frameProperty.getModelProperty();
            }
        }
        return null;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public ModelProperty<PanelInfo> getModelProperty() {
        return this.modelProperty;
    }
}
