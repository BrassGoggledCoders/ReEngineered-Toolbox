package xyz.brassgoggledcoders.reengineeredtoolbox.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IPanelPlacement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.PanelInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FrameBlockEntity extends TileEntity implements IFrame {
    private final EnumMap<Direction, UUID> panelLocation;
    private final Map<UUID, PanelInfo> panelInfo;

    public FrameBlockEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.panelLocation = Maps.newEnumMap(Direction.class);
        this.panelInfo = Maps.newHashMap();
    }


    @Override
    public PanelInfo getInfoBySide(Direction direction) {
        return Optional.ofNullable(panelLocation.get(direction))
                .map(panelInfo::get)
                .orElse(null);
    }

    @Override
    public PanelInfo getInfoByUniqueId(UUID uniqueId) {
        return panelInfo.get(uniqueId);
    }

    @Override
    public boolean placePanel(IPanelPlacement panelPlacement) {
        PanelState panelState = panelPlacement.getPanelState();
        Direction face = panelPlacement.getPlacementFace();
        UUID uniqueId = this.panelLocation.get(face);
        if (panelState != null && (uniqueId == null || this.panelInfo.get(uniqueId) == null)) {
            panelState = panelState.getStateForPlacement(panelPlacement, this);
            if (panelState != null && panelState.isValidFor(face)) {
                PanelEntity panelEntity = panelState.createPanelEntity();
                if (panelEntity != null && panelPlacement.getPanelEntityNBT() != null) {
                    panelEntity.deserializeNBT(panelPlacement.getPanelEntityNBT());
                }
                PanelInfo panelInfo = new PanelInfo(UUID.randomUUID(), panelState, panelEntity);
                this.panelLocation.put(face, panelInfo.getUniqueId());
                this.panelInfo.put(panelInfo.getUniqueId(), panelInfo);
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockPos getFramePos() {
        return this.getBlockPos();
    }

    @Override
    public IWorld getFrameLevel() {
        return this.level;
    }
}
