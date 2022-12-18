package xyz.brassgoggledcoders.reengineeredtoolbox.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FrameBlockEntity extends BlockEntity {
    public static final EnumMap<Direction, ModelProperty<PanelState>> PANEL_STATE_MODEL_PROPERTIES = Arrays.stream(Direction.values())
            .map(direction -> Pair.of(direction, new ModelProperty<PanelState>()))
            .collect(Collectors.toMap(
                    Pair::left,
                    Pair::right,
                    (u, v) -> u,
                    () -> new EnumMap<>(Direction.class)
            ));

    public FrameBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    @NotNull
    public ModelData getModelData() {
        ModelData.Builder modelData = ModelData.builder();
        for (Map.Entry<Direction, ModelProperty<PanelState>> entry : PANEL_STATE_MODEL_PROPERTIES.entrySet()) {
            modelData.with(entry.getValue(), ReEngineeredPanels.PLUG.get()
                    .defaultPanelState()
                    .setValue(BlockStateProperties.FACING, entry.getKey())
            );
        }
        return modelData.build();
    }
}
