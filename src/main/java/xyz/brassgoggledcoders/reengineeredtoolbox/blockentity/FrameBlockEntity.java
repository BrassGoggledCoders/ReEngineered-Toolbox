package xyz.brassgoggledcoders.reengineeredtoolbox.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ReEngineeredCapabilities;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.fluid.FrequencyFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item.FrequencyItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.redstone.FrequencyRedstoneHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.FrameMenuProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NbtHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
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

    private final Map<Direction, PanelState> panelStateMap;
    private final Map<Direction, PanelEntity> panelEntityMap;
    private final TreeMap<Long, Set<Direction>> scheduledTicks;

    private final FrequencyItemHandler frequencyItemHandler;
    private final LazyOptional<IFrequencyItemHandler> frequencyItemHandlerLazy;

    private final FrequencyRedstoneHandler frequencyRedstoneHandler;
    private final LazyOptional<IFrequencyRedstoneHandler> frequencyRedstoneHandlerLazy;

    private final FrequencyFluidHandler frequencyFluidHandler;
    private final LazyOptional<IFrequencyFluidHandler> frequencyFluidHandlerLazy;

    public FrameBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.panelStateMap = new ConcurrentHashMap<>();
        this.panelEntityMap = new EnumMap<>(Direction.class);
        this.scheduledTicks = new TreeMap<>(Long::compareTo);

        this.frequencyItemHandler = new FrequencyItemHandler(this::setChanged);
        this.frequencyItemHandlerLazy = LazyOptional.of(() -> frequencyItemHandler);

        this.frequencyRedstoneHandler = new FrequencyRedstoneHandler(this);
        this.frequencyRedstoneHandlerLazy = LazyOptional.of(() -> this.frequencyRedstoneHandler);

        this.frequencyFluidHandler = new FrequencyFluidHandler(this::setChanged);
        this.frequencyFluidHandlerLazy = LazyOptional.of(() -> this.frequencyFluidHandler);

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
    public InteractionResultHolder<PanelState> putPanelState(@NotNull Direction direction, PanelState panelState, boolean replace) {
        PanelState existingPanelState = this.panelStateMap.get(direction);
        if (existingPanelState == null || replace) {
            this.panelStateMap.put(direction, panelState);
            if (existingPanelState != null) {
                PanelEntity panelEntity = this.getPanelEntity(direction);
                if (panelEntity == null || existingPanelState.getPanel() != panelState.getPanel()) {
                    if (panelEntity != null) {
                        panelEntity.onRemove();
                    }
                    panelEntity = panelState.createPanelEntity(this);
                }
                if (panelEntity != null) {
                    panelEntity.setPanelState(panelState);
                    this.panelEntityMap.put(direction, panelEntity);
                }
            } else {
                PanelEntity panelEntity = panelState.createPanelEntity(this);
                if (panelEntity != null) {
                    this.panelEntityMap.put(direction, panelEntity);
                }
            }
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
    public PanelEntity getPanelEntity(@Nullable Direction direction) {
        if (direction != null) {
            return this.panelEntityMap.get(direction);
        } else {
            return null;
        }
    }

    @Override
    @NotNull
    public BlockPos getFramePos() {
        return this.getBlockPos();
    }

    @Override
    @NotNull
    public Level getFrameLevel() {
        return this.getNoNullLevel();
    }

    @Override
    public void openMenu(Player player, PanelEntity panelEntity, MenuProvider menuProvider, @NotNull Consumer<FriendlyByteBuf> friendlyByteBuf) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new FrameMenuProvider(menuProvider),
                    friendlyByteBuf
            );
        }
    }

    @Override
    public boolean isValid() {
        return !this.isRemoved();
    }

    @Override
    public void scheduleTick(@NotNull Direction direction, Panel panel, int ticks) {
        if (!this.getFrameLevel().isClientSide()) {
            this.getFrameLevel()
                    .scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), ticks);

            this.scheduledTicks.computeIfAbsent(this.getFrameLevel().getGameTime() + ticks, key -> new HashSet<>())
                    .add(direction);
        }
    }

    @Override
    public boolean changeFrameSlot(@NotNull BlockHitResult result, ItemStack toolStack) {
        PanelEntity panelEntity = this.getPanelEntity(result.getDirection());
        if (panelEntity != null) {
            List<FrameSlot> frameSlots = panelEntity.getFrameSlots();
            if (!frameSlots.isEmpty()) {
                Optional<DyeColor> toolStackDye = Optional.empty();
                if (toolStack.is(Tags.Items.DYES)) {
                    toolStackDye = Optional.ofNullable(DyeColor.getColor(toolStack));
                }

                for (FrameSlot frameSlot : frameSlots) {
                    if (frameSlot.getView().isInside(result.getLocation(), result.getDirection())) {
                        frameSlot.setFrequency(toolStackDye.flatMap(Frequency::getByDye)
                                .orElse(frameSlot.getFrequency().next())
                        );
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public <T> void notifyStorageChange(Capability<T> frequencyCapability) {
        for (PanelEntity panelEntity : this.panelEntityMap.values()) {
            panelEntity.notifyStorageChanged(frequencyCapability);
        }
    }

    public void doScheduledTick() {
        long gameTime = this.getFrameLevel().getGameTime();
        Map.Entry<Long, Set<Direction>> scheduledTick = this.scheduledTicks.lowerEntry(gameTime);
        while (scheduledTick != null) {
            for (Direction direction : scheduledTick.getValue()) {
                Optional.ofNullable(this.panelEntityMap.get(direction))
                        .ifPresent(PanelEntity::scheduledTick);
            }
            this.scheduledTicks.remove(scheduledTick.getKey());
            scheduledTick = this.scheduledTicks.lowerEntry(gameTime);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        CompoundTag panelsTag = new CompoundTag();
        writePanels(panelsTag);
        pTag.put("Panels", panelsTag);
        pTag.put("FrequencyItemHandler", this.frequencyItemHandler.serializeNBT());
        pTag.put("FrequencyFluidHandler", this.frequencyFluidHandler.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("Panels")) {
            CompoundTag panelsTag = pTag.getCompound("Panels");
            readPanels(panelsTag);
        }
        this.frequencyItemHandler.deserializeNBT(pTag.getCompound("FrequencyItemHandler"));
        this.frequencyFluidHandler.deserializeNBT(pTag.getCompound("FrequencyFluidHandler"));
    }

    private void readPanels(CompoundTag panelsTag) {
        for (Direction direction : Direction.values()) {
            CompoundTag panelTag = panelsTag.getCompound(direction.getName());
            if (!panelTag.isEmpty()) {
                PanelState panelState = NbtHelper.readPanelState(panelTag.getCompound("PanelState"));
                this.panelStateMap.put(direction, panelState);
                PanelEntity panelEntity;
                if (panelTag.contains("PanelEntity")) {
                    panelEntity = PanelEntity.loadStatic(this, panelState, panelTag.getCompound("PanelEntity"));
                } else {
                    panelEntity = panelState.createPanelEntity(this);
                }

                if (panelEntity != null) {
                    this.panelEntityMap.put(direction, panelEntity);
                }
            }
        }
    }

    private void writePanels(CompoundTag panelsTag) {
        for (Direction direction : Direction.values()) {
            PanelState panelState = this.panelStateMap.get(direction);
            if (panelState != null) {
                CompoundTag panelTag = new CompoundTag();
                panelTag.put("PanelState", NbtHelper.writePanelState(panelState));
                PanelEntity panelEntity = this.panelEntityMap.get(direction);
                if (panelEntity != null) {
                    panelTag.put("PanelEntity", panelEntity.saveWithId());
                }

                panelsTag.put(direction.getName(), panelTag);
            }
        }
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        CompoundTag panelsTag = new CompoundTag();
        writePanels(panelsTag);
        tag.put("Panels", panelsTag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readPanels(tag.getCompound("Panels"));
        this.requestModelDataUpdate();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.handleUpdateTag(pkt.getTag());
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.panelEntityMap.values().forEach(PanelEntity::invalidate);

        this.frequencyItemHandlerLazy.invalidate();
        this.frequencyRedstoneHandlerLazy.invalidate();
        this.frequencyFluidHandlerLazy.invalidate();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> lazyOptional = LazyOptional.empty();
        if (side == null) {
            //TODO Handle Frame Storage
            if (cap == ReEngineeredCapabilities.FREQUENCY_ITEM_HANDLER) {
                lazyOptional = this.frequencyItemHandlerLazy.cast();
            } else if (cap == ReEngineeredCapabilities.FREQUENCY_REDSTONE_HANDLER) {
                lazyOptional = this.frequencyRedstoneHandlerLazy.cast();
            } else if (cap == ReEngineeredCapabilities.FREQUENCY_FLUID_HANDLER) {
                lazyOptional = this.frequencyFluidHandlerLazy.cast();
            }
        } else {
            PanelEntity panelEntity = this.getPanelEntity(side);
            if (panelEntity != null) {
                lazyOptional = panelEntity.getCapability(cap, side);
            }
        }
        if (lazyOptional.isPresent()) {
            return lazyOptional;
        } else {
            return super.getCapability(cap, side);
        }
    }

    public void serverTick() {
        this.panelEntityMap.values().forEach(PanelEntity::serverTick);
        this.frequencyRedstoneHandler.tick();
    }
}
