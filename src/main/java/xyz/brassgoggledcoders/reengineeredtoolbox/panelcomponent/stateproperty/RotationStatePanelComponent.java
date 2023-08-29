package xyz.brassgoggledcoders.reengineeredtoolbox.panelcomponent.stateproperty;

import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelUseOnContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.PanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.placement.IPlacementPanelComponent;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelcomponent.stateproperty.IStatePropertyPanelComponent;

public class RotationStatePanelComponent extends PanelComponent implements IStatePropertyPanelComponent<Rotation>,
        IPlacementPanelComponent {
    public static final EnumProperty<Rotation> ROTATION = EnumProperty.create("rotation", Rotation.class);

    @Override
    @Nullable
    public PanelState getPanelStateForPlacement(PanelUseOnContext context, IFrameEntity frame, @Nullable PanelState current) {
        if (current != null) {
            Vec2 location = context.hitResult().location();
            Rotation rotation;
            if (Math.abs(location.x - 8) > Math.abs(location.y - 8)) {
                rotation = location.x - 8 > 0 ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
            } else {
                rotation = location.y - 8 > 0 ? Rotation.NONE : Rotation.CLOCKWISE_180;
            }

            return current.setValue(ROTATION, rotation);
        }

        return null;
    }

    @Override
    @NotNull
    public Property<Rotation> getProperty() {
        return ROTATION;
    }

    @Override
    @Nullable
    public Rotation getDefaultValue() {
        return Rotation.NONE;
    }
}
