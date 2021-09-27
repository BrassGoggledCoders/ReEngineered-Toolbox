package xyz.brassgoggledcoders.reengineeredtoolbox.blockentity;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IPanelPlacement;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.PanelInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.frame.FrameModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FrameBlockEntity extends TileEntity implements IFrame {
    private final EnumMap<Direction, UUID> panelLocation;
    private final Map<UUID, PanelInfo> panelInfo;
    private final Object2BooleanMap<Direction> needsUpdate;

    public FrameBlockEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.panelLocation = Maps.newEnumMap(Direction.class);
        this.panelInfo = Maps.newHashMap();
        this.needsUpdate = new Object2BooleanOpenHashMap<>();
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
                this.needsUpdate.put(face, true);
                World world = this.level;
                if (world != null) {
                    world.markAndNotifyBlock(
                            this.getFramePos(),
                            world.getChunkAt(this.getFramePos()),
                            this.getBlockState(),
                            this.getBlockState(),
                            Constants.BlockFlags.DEFAULT,
                            512
                    );
                }

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

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.getClientUpdates();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.handleClientUpdates(tag);
    }

    public CompoundNBT getClientUpdates() {
        CompoundNBT clientUpdates = new CompoundNBT();
        for (Direction direction : Direction.values()) {
            if (this.needsUpdate.getOrDefault(direction, true)) {
                UUID uniqueId = this.panelLocation.get(direction);
                if (uniqueId != null) {
                    PanelInfo panelInfo = this.panelInfo.get(uniqueId);
                    if (panelInfo != null) {
                        clientUpdates.put(direction.getName(), panelInfo.toNBT(PanelEntity::getClientUpdates));
                    }
                }
                this.needsUpdate.put(direction, false);
            }
        }
        return clientUpdates;
    }

    public void handleClientUpdates(CompoundNBT tag) {
        for (Direction direction : Direction.values()) {
            if (tag.contains(direction.getName())) {
                CompoundNBT panelInfoTag = tag.getCompound(direction.getName());
                PanelInfo panelInfo = PanelInfo.fromNBT(panelInfoTag, this.panelInfo::get, PanelEntity::handleClientUpdates);
                UUID oldUUID = this.panelLocation.put(direction, panelInfo.getUniqueId());
                if (oldUUID != null) {
                    this.panelInfo.remove(oldUUID);
                }
                this.panelInfo.put(panelInfo.getUniqueId(), panelInfo);
            }
        }
        ModelDataManager.requestModelDataRefresh(this);
        World world = this.level;
        if (world != null) {
            world.markAndNotifyBlock(
                    this.getFramePos(),
                    world.getChunkAt(this.getFramePos()),
                    this.getBlockState(),
                    this.getBlockState(),
                    Constants.BlockFlags.DEFAULT,
                    512
            );
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        CompoundNBT panelInfos = new CompoundNBT();
        for (Direction direction : Direction.values()) {
            UUID uniqueId = this.panelLocation.get(direction);
            if (uniqueId != null) {
                PanelInfo panelInfo = this.panelInfo.get(uniqueId);
                if (panelInfo != null) {
                    panelInfos.put(direction.getName(), panelInfo.toNBT(PanelEntity::serializeNBT));
                }
            }
        }
        nbt.put("panelInfos", panelInfos);
        return super.save(nbt);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        CompoundNBT panelInfos = nbt.getCompound("panelInfos");
        for (Direction direction : Direction.values()) {
            if (panelInfos.contains(direction.getName())) {
                CompoundNBT panelInfoTag = panelInfos.getCompound(direction.getName());
                PanelInfo panelInfo = PanelInfo.fromNBT(panelInfoTag, this.panelInfo::get, PanelEntity::deserializeNBT);
                this.panelLocation.clear();
                this.panelLocation.put(direction, panelInfo.getUniqueId());
                this.panelInfo.clear();
                this.panelInfo.put(panelInfo.getUniqueId(), panelInfo);
                this.needsUpdate.put(direction, true);
            }
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(
                this.getFramePos(),
                0,
                this.getClientUpdates()
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.handleClientUpdates(pkt.getTag());
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        ModelDataMap.Builder modelDataMapBuilder = new ModelDataMap.Builder();
        for (Direction direction : Direction.values()) {
            modelDataMapBuilder.withInitial(
                    FrameModelProperty.getModelForSide(direction),
                    this.getInfoBySide(direction)
            );
        }
        return modelDataMapBuilder.build();
    }
}
