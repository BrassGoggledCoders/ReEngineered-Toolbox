package xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class PanelEntity implements ICapabilityProvider {
    @NotNull
    private final IFrameEntity frameEntity;
    @NotNull
    private final PanelEntityType<?> type;

    @NotNull
    private PanelState panelState;

    public PanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        this.frameEntity = frameEntity;
        this.panelState = panelState;
        this.type = type;
    }

    public void neighborChanged() {

    }

    public void setPortConnection(Port port, int slotNumber) {

    }

    public void scheduledTick() {

    }


    public void load(CompoundTag pTag) {
    }

    public void save(CompoundTag pTag) {

    }

    @NotNull
    public PanelState getPanelState() {
        return panelState;
    }

    public void setPanelState(@NotNull PanelState panelState) {
        this.panelState = panelState;
    }

    @NotNull
    public IFrameEntity getFrameEntity() {
        return frameEntity;
    }

    public Direction getFacing() {
        return this.getPanelState().getFacing();
    }

    @NotNull
    public PanelEntityType<?> getType() {
        return type;
    }

    public Level getLevel() {
        return this.getFrameEntity().getFrameLevel();
    }

    public BlockPos getBlockPos() {
        return this.getFrameEntity().getFramePos();
    }

    public final CompoundTag saveWithId() {
        CompoundTag compoundtag = this.saveWithoutMetadata();
        this.saveId(compoundtag);
        return compoundtag;
    }

    public final CompoundTag saveWithoutMetadata() {
        CompoundTag compoundtag = new CompoundTag();
        this.save(compoundtag);
        return compoundtag;
    }

    private void saveId(CompoundTag pTag) {
        ResourceLocation resourcelocation = ReEngineeredPanels.getPanelEntityRegistry().getKey(this.getType());
        if (resourcelocation == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            pTag.putString("id", resourcelocation.toString());
        }
    }

    public void slotUpdated(int slot) {

    }

    public Map<Port, Integer> getPorts() {
        return Collections.emptyMap();
    }

    public void serverTick() {

    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    public void invalidate() {

    }

    protected boolean stillValid(Player player) {
        if (player.distanceToSqr(Vec3.atCenterOf(this.getBlockPos())) <= 64.0D) {
            if (this.getLevel().getBlockEntity(this.getBlockPos()) == this.getFrameEntity()) {
                return this.getFrameEntity().getPanelEntity(this.getFacing()) == this;
            }
        }

        return false;
    }

    protected Panel getPanel() {
        return this.getPanelState().getPanel();
    }

    @Nullable
    public static PanelEntity loadStatic(IFrameEntity frame, PanelState pState, CompoundTag pTag) {
        String s = pTag.getString("id");
        ResourceLocation resourcelocation = ResourceLocation.tryParse(s);
        if (resourcelocation == null) {
            ReEngineeredToolbox.LOGGER.error("Panel entity has invalid type: {}", s);
            return null;
        } else {
            return Optional.ofNullable(ReEngineeredPanels.getPanelEntityRegistry().getValue(resourcelocation))
                    .map((panelEntityType) -> {
                        try {
                            return panelEntityType.createPanelEntity().apply(panelEntityType, frame, pState);
                        } catch (Throwable throwable) {
                            ReEngineeredToolbox.LOGGER.error("Failed to create panel entity {}", s, throwable);
                            return null;
                        }
                    })
                    .map((panelEntity) -> {
                        try {
                            panelEntity.load(pTag);
                            return panelEntity;
                        } catch (Throwable throwable) {
                            ReEngineeredToolbox.LOGGER.error("Failed to load data for panel entity {}", s, throwable);
                            return null;
                        }
                    }).orElseGet(() -> {
                        ReEngineeredToolbox.LOGGER.warn("Skipping PanelEntity with id {}", s);
                        return null;
                    });
        }
    }
}
