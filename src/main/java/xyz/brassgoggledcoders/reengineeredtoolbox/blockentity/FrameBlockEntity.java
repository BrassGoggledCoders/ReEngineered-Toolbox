package xyz.brassgoggledcoders.reengineeredtoolbox.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NbtHelper;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class FrameBlockEntity extends BlockEntity implements IFrameEntity {
    public static final EnumMap<Direction, ModelProperty<PanelState>> PANEL_STATE_MODEL_PROPERTIES = Arrays.stream(Direction.values())
            .map(direction -> Pair.of(direction, new ModelProperty<PanelState>()))
            .collect(Collectors.toMap(
                    Pair::left,
                    Pair::right,
                    (u, v) -> u,
                    () -> new EnumMap<>(Direction.class)
            ));

    private final ConcurrentMap<Direction, PanelState> panelStateMap;

    public FrameBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.panelStateMap = new ConcurrentHashMap<>();
    }

    @NotNull
    public Level getNoNullLevel() {
        return Objects.requireNonNull(this.getLevel());
    }

    @Override
    @NotNull
    public ModelData getModelData() {
        ModelData.Builder modelData = ModelData.builder();
        for (Map.Entry<Direction, ModelProperty<PanelState>> entry : PANEL_STATE_MODEL_PROPERTIES.entrySet()) {
            modelData.with(entry.getValue(), this.getPanelState(entry.getKey()));
        }
        return modelData.build();
    }

    @Override
    public InteractionResultHolder<PanelState> setPanelState(@NotNull Direction direction, PanelState panelState) {
        PanelState existingPanelState = this.panelStateMap.get(direction);
        if (existingPanelState == null) {
            this.panelStateMap.put(direction, panelState);
            this.setChanged();
            this.requestModelDataUpdate();
            this.getNoNullLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            return InteractionResultHolder.sidedSuccess(panelState, this.getNoNullLevel().isClientSide());
        } else {
            return InteractionResultHolder.fail(this.panelStateMap.get(direction));
        }
    }

    @Override
    @NotNull
    public PanelState getPanelState(@NotNull Direction direction) {
        return this.panelStateMap.getOrDefault(direction, ReEngineeredPanels.PLUG.withDirection(direction));
    }

    @Override
    @Nullable
    public PanelEntity getPanelEntity(@NotNull Direction direction) {
        return null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        CompoundTag panelsTag = new CompoundTag();
        writePanels(panelsTag, ((direction, compoundTag) -> {
        }));
        pTag.put("Panels", panelsTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("Panels")) {
            CompoundTag panelsTag = pTag.getCompound("Panels");
            readPanels(panelsTag, (direction, compoundTag) -> {
            });
        }
    }

    private void readPanels(CompoundTag panelsTag, BiConsumer<Direction, CompoundTag> readAdditional) {
        for (Direction direction : Direction.values()) {
            CompoundTag panelTag = panelsTag.getCompound(direction.getName());
            if (!panelTag.isEmpty()) {
                this.panelStateMap.put(direction, NbtHelper.readPanelState(panelTag.getCompound("PanelState")));
                readAdditional.accept(direction, panelTag);
            }
        }
    }

    private void writePanels(CompoundTag panelsTag, BiConsumer<Direction, CompoundTag> writeAdditional) {
        for (Direction direction : Direction.values()) {
            PanelState panelState = this.panelStateMap.get(direction);
            if (panelState != null) {
                CompoundTag panelTag = new CompoundTag();
                panelTag.put("PanelState", NbtHelper.writePanelState(panelState));
                writeAdditional.accept(direction, panelTag);
                panelsTag.put(direction.getName(), panelTag);
            }
        }
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        CompoundTag panelsTag = new CompoundTag();
        writePanels(panelsTag, (direction, compoundTag) -> {
        });
        tag.put("Panels", panelsTag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readPanels(tag.getCompound("Panels"), (direction, compoundTag) -> {
        });
        this.requestModelDataUpdate();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.handleUpdateTag(pkt.getTag());
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
