package xyz.brassgoggledcoders.reengineeredtoolbox.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyCapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.Frequency;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.loot.ReEngineeredLootAPI;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.*;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.FrequencyCapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.ReEngineeredPanels;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.FrameMenuProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.NbtHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FrameBlockEntity extends BlockEntity implements IFrameEntity {
    private static final PanelInfo EMPTY = new PanelInfo(
            ReEngineeredPanels.PLUG.getDefaultState(),
            null
    );

    public static final EnumMap<Direction, ModelProperty<PanelState>> PANEL_STATE_MODEL_PROPERTIES = Arrays.stream(Direction.values())
            .map(direction -> Pair.of(direction, new ModelProperty<PanelState>()))
            .collect(Collectors.toMap(
                    Pair::getFirst,
                    Pair::getSecond,
                    (u, v) -> u,
                    () -> new EnumMap<>(Direction.class)
            ));

    private final Map<IPanelPosition, PanelInfo> panelInfoMap;
    private final TreeMap<Long, Set<IPanelPosition>> scheduledTicks;
    private final FrequencyCapabilityProvider frequencyCapabilityProvider;

    public FrameBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.panelInfoMap = new ConcurrentHashMap<>();
        this.scheduledTicks = new TreeMap<>(Long::compareTo);

        this.frequencyCapabilityProvider = new FrequencyCapabilityProvider(this);
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
            modelData.with(entry.getValue(), this.getPanelState(BlockPanelPosition.fromDirection(entry.getKey())));
        }
        return modelData.build();
    }

    @Override
    public InteractionResultHolder<PanelState> putPanelState(@NotNull IPanelPosition panelPosition, @Nullable PanelState panelState, boolean replace) {
        PanelInfo existingPanelInfo = this.panelInfoMap.get(panelPosition);

        if (panelState == null || existingPanelInfo == null || replace) {
            if (panelState == null && existingPanelInfo != null) {
                this.panelInfoMap.remove(panelPosition);
                existingPanelInfo.ifEntityPresent(PanelEntity::onRemove);
            }

            if (panelState != null) {
                PanelEntity panelEntity = null;
                if (existingPanelInfo != null) {
                    panelEntity = existingPanelInfo.panelEntity();
                    if (panelEntity != null && existingPanelInfo.panelState().getPanel() != panelState.getPanel()) {
                        panelEntity.onRemove();
                        panelEntity = null;
                    }
                }
                if (panelEntity == null) {
                    panelEntity = panelState.createPanelEntity(this);
                }
                if (panelEntity != null) {
                    panelEntity.setPanelState(panelState);
                    panelEntity.setPanelPosition(panelPosition);
                }
                this.panelInfoMap.put(panelPosition, new PanelInfo(panelState, panelEntity));
            }

            this.setChanged();
            this.requestModelDataUpdate();
            this.getNoNullLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
            this.getNoNullLevel().updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
            if (panelState == null) {
                panelState = ReEngineeredPanels.PLUG.getDefaultState();
            }
            return InteractionResultHolder.sidedSuccess(panelState, this.getNoNullLevel().isClientSide());
        }

        return InteractionResultHolder.fail(this.getPanelState(panelPosition));
    }

    @Override
    public boolean hasPanel(@NotNull IPanelPosition panelPosition) {
        return panelInfoMap.containsKey(panelPosition);
    }

    @Override
    public List<ItemStack> removePanel(@NotNull IPanelPosition panelPosition, @Nullable Player player, @NotNull ItemStack heldItem) {
        if (this.hasPanel(panelPosition)) {
            PanelState panelState = this.getPanelState(panelPosition);
            PanelEntity panelEntity = this.getPanelEntity(panelPosition);
            this.putPanelState(panelPosition, null, true);

            if (this.getLevel() instanceof ServerLevel serverLevel) {
                LootContext.Builder lootContext = new LootContext.Builder(serverLevel)
                        .withRandom(serverLevel.getRandom())
                        .withParameter(ReEngineeredLootAPI.PANELSTATE, panelState)
                        .withOptionalParameter(ReEngineeredLootAPI.PANEL_ENTITY, panelEntity)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getFramePos()))
                        .withParameter(LootContextParams.BLOCK_ENTITY, this)
                        .withParameter(LootContextParams.BLOCK_STATE, this.getBlockState())
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

                if (player != null) {
                    lootContext.withLuck(player.getLuck());
                }

                return serverLevel.getServer()
                        .getLootTables()
                        .get(panelState.getPanel().getLootTable())
                        .getRandomItems(lootContext.create(ReEngineeredLootAPI.getPanelLootParamSet()));
            }
        }
        return Collections.emptyList();
    }

    @Override
    @NotNull
    public PanelState getPanelState(@NotNull IPanelPosition panelPosition) {
        return this.panelInfoMap.getOrDefault(panelPosition, EMPTY)
                .panelState();
    }

    @Override
    @Nullable
    public PanelEntity getPanelEntity(@Nullable IPanelPosition panelPosition) {
        if (panelPosition != null && this.panelInfoMap.containsKey(panelPosition)) {
            return this.panelInfoMap.getOrDefault(panelPosition, EMPTY)
                    .panelEntity();
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
    public void scheduleTick(@NotNull IPanelPosition direction, Panel panel, int ticks) {
        if (!this.getFrameLevel().isClientSide()) {
            this.getFrameLevel()
                    .scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), ticks);

            this.scheduledTicks.computeIfAbsent(this.getFrameLevel().getGameTime() + ticks, key -> new HashSet<>())
                    .add(direction);
        }
    }

    @Override
    public boolean changeFrameSlot(@NotNull BlockHitResult result, ItemStack toolStack) {
        PanelEntity panelEntity = this.getPanelEntity(BlockPanelPosition.fromDirection(result.getDirection()));
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
                        panelEntity.onFrameSlotChange(frameSlot);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public <T> void notifyStorageChange(Capability<T> frequencyCapability) {
        for (PanelInfo panelInfo : this.panelInfoMap.values()) {
            panelInfo.ifEntityPresent(panelEntity -> panelEntity.notifyStorageChanged(frequencyCapability));
        }
    }

    @Override
    public IFrequencyCapabilityProvider getFrequencyProvider() {
        return this.frequencyCapabilityProvider;
    }

    @Override
    public Map<IPanelPosition, PanelInfo> getPanelInfo() {
        return ImmutableMap.copyOf(this.panelInfoMap);
    }

    @Override
    public void needsSaved() {
        this.setChanged();
    }

    public void doScheduledTick() {
        long gameTime = this.getFrameLevel().getGameTime();
        Map.Entry<Long, Set<IPanelPosition>> scheduledTick = this.scheduledTicks.lowerEntry(gameTime);
        while (scheduledTick != null) {
            for (IPanelPosition panelPosition : scheduledTick.getValue()) {
                Optional.ofNullable(this.getPanelEntity(panelPosition))
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
        pTag.put("FrequencyCapabilityProvider", this.frequencyCapabilityProvider.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("Panels")) {
            CompoundTag panelsTag = pTag.getCompound("Panels");
            readPanels(panelsTag);
        }

        this.frequencyCapabilityProvider.deserializeNBT(pTag.getCompound("FrequencyCapabilityProvider"));
    }

    private void readPanels(CompoundTag panelsTag) {
        for (Direction direction : Direction.values()) {
            CompoundTag panelTag = panelsTag.getCompound(direction.getName());
            if (!panelTag.isEmpty()) {
                IPanelPosition panelPosition = BlockPanelPosition.fromDirection(direction);

                PanelState panelState = NbtHelper.readPanelState(panelTag.getCompound("PanelState"));

                PanelEntity panelEntity;
                if (panelTag.contains("PanelEntity")) {
                    panelEntity = PanelEntity.loadStatic(panelState, this, panelTag.getCompound("PanelEntity"));
                } else {
                    panelEntity = panelState.createPanelEntity(this);
                }

                if (panelEntity != null) {
                    panelEntity.setPanelPosition(BlockPanelPosition.fromDirection(direction));
                }
                this.panelInfoMap.put(panelPosition, new PanelInfo(panelState, panelEntity));
            }
        }
    }

    private void writePanels(CompoundTag panelsTag) {
        for (Direction direction : Direction.values()) {
            PanelInfo panelPair = this.panelInfoMap.get(BlockPanelPosition.fromDirection(direction));
            if (panelPair != null) {
                PanelState panelState = panelPair.panelState();
                CompoundTag panelTag = new CompoundTag();
                panelTag.put("PanelState", NbtHelper.writePanelState(panelState));
                PanelEntity panelEntity = panelPair.panelEntity();
                if (panelEntity != null) {
                    panelTag.put("PanelEntity", panelEntity.save());
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
        if (this.getLevel() != null) {
            BlockState blockState = this.getBlockState();
            this.getLevel().sendBlockUpdated(this.getBlockPos(), blockState, blockState, Block.UPDATE_ALL);
            this.getLevel().updateNeighborsAt(this.getBlockPos(), blockState.getBlock());
        }
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
        this.panelInfoMap.values()
                .forEach(panelInfo -> panelInfo.ifEntityPresent(PanelEntity::invalidate));

        this.frequencyCapabilityProvider.invalidate();
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
            lazyOptional = this.frequencyCapabilityProvider.getCapability(cap);
        } else {
            PanelEntity panelEntity = this.getPanelEntity(BlockPanelPosition.fromDirection(side));
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
        this.panelInfoMap.values()
                .forEach(panelInfo -> panelInfo.ifEntityPresent(PanelEntity::serverTick));
        this.frequencyCapabilityProvider.run();
    }
}
