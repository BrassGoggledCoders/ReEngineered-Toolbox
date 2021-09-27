package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame;

import net.minecraft.nbt.CompoundNBT;
import org.lwjgl.system.CallbackI;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PanelInfo {
    private final PanelState panelState;
    private final PanelEntity panelEntity;
    private final UUID uniqueId;

    public PanelInfo(UUID uniqueId, PanelState panelState) {
        this(uniqueId, panelState, panelState.createPanelEntity());
    }

    public PanelInfo(UUID uniqueId, PanelState panelState, PanelEntity panelEntity) {
        this.panelState = panelState;
        this.panelEntity = panelEntity;
        this.uniqueId = uniqueId;
    }

    public PanelInfo(UUID uniqueId, PanelState panelState, CompoundNBT panelEntityNBT) {
        this.panelState = panelState;
        this.panelEntity = panelState.createPanelEntity();
        if (this.panelEntity != null) {
            this.panelEntity.deserializeNBT(panelEntityNBT);
        }
        this.uniqueId = uniqueId;
    }

    @Nonnull
    public PanelState getPanelState() {
        return panelState;
    }

    @Nullable
    public PanelEntity getPanelEntity() {
        return panelEntity;
    }

    @Nonnull
    public UUID getUniqueId() {
        return uniqueId;
    }

    public CompoundNBT toNBT(Function<PanelEntity, CompoundNBT> panelEntityNBT) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("uniqueId", uniqueId);
        nbt.put("panelState", NBTHelper.writePanelState(panelState));
        if (panelEntity != null) {
            nbt.put("panelEntity", panelEntityNBT.apply(panelEntity));
        }
        return nbt;
    }

    public static PanelInfo fromNBT(@Nonnull CompoundNBT compoundNBT, Function<UUID, PanelInfo> existing,
                                    BiConsumer<PanelEntity, CompoundNBT> panelEntityNBT) {
        UUID uniqueId = compoundNBT.getUUID("uniqueId");
        PanelInfo existingPanelInfo = existing.apply(uniqueId);
        PanelState newPanelState = NBTHelper.readPanelState(compoundNBT.getCompound("panelState"));
        if (existingPanelInfo != null) {
            PanelEntity existingPanelEntity = existingPanelInfo.getPanelEntity();
            if (existingPanelEntity != null) {
                panelEntityNBT.accept(existingPanelEntity, compoundNBT.getCompound("panelEntity"));
            }
            if (newPanelState == existingPanelInfo.getPanelState()) {
                return existingPanelInfo;
            } else {
                return new PanelInfo(
                        existingPanelInfo.getUniqueId(),
                        newPanelState,
                        existingPanelInfo.getPanelEntity()
                );
            }
        } else {
            PanelEntity panelEntity = newPanelState.createPanelEntity();
            if (panelEntity != null) {
                panelEntityNBT.accept(panelEntity, compoundNBT.getCompound("panelEntity"));
            }
            return new PanelInfo(
                    uniqueId,
                    newPanelState,
                    panelEntity
            );
        }
    }
}
