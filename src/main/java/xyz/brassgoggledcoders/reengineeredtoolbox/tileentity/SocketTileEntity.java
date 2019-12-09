package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.FaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketFaceContainerProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.function.TriFunction;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.FaceProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SocketTileEntity extends TileEntity implements ISocketTile, ITickableTileEntity {
    private EnumMap<Direction, IFaceHolder> faceHolders;
    private EnumMap<Direction, FaceInstance> faceInstances;
    private EnumMap<Direction, LazyOptional<IFaceHolder>> faceHolderOptionals;

    private boolean updateRequested = false;

    public SocketTileEntity() {
        super(Objects.requireNonNull(Blocks.SOCKET_TYPE.get()));
        faceHolders = Maps.newEnumMap(Direction.class);
        faceHolderOptionals = Maps.newEnumMap(Direction.class);
        faceInstances = Maps.newEnumMap(Direction.class);
        for (Direction direction : Direction.values()) {
            FaceHolder faceHolder = new SocketFaceHolder(new WeakReference<>(this), direction);
            faceHolders.put(direction, faceHolder);
            faceHolderOptionals.put(direction, LazyOptional.of(() -> faceHolder));
        }
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (facing != null) {
            if (capability == CapabilityFaceHolder.FACE_HOLDER) {
                return faceHolderOptionals.getOrDefault(facing, LazyOptional.empty()).cast();
            } else {
                FaceInstance faceInstance = faceInstances.get(facing);
                if (faceInstance != null) {
                    return faceInstance.getCapability(capability);
                }
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public BlockPos getBlockPos() {
        return this.getPos();
    }

    @Override
    public void requestUpdate() {
        updateRequested = true;
    }

    @Override
    public void openGui(PlayerEntity playerEntity, SocketContext context) {
        if (playerEntity instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new SocketFaceContainerProvider(this, context),
                    packetBuffer -> {
                        packetBuffer.writeBlockPos(this.getBlockPos());
                        packetBuffer.writeString(context.getSide().getName());
                    });
        }

    }

    @Override
    public void onLoad() {
        this.updateRequested = true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side, SocketContext context) {
        FaceInstance faceInstance = faceInstances.get(side);
        if (faceInstance != null) {
            return faceInstance.getCapability(capability, context);
        }
        return LazyOptional.empty();
    }

    @Override
    public Optional<FaceInstance> getFaceInstanceOnSide(Direction side) {
        return Optional.ofNullable(this.faceInstances.get(side));
    }

    @Override
    public void tick() {
        for (Direction facing : Direction.values()) {
            FaceInstance faceInstance = this.faceInstances.get(facing);
            if (faceInstance != null) {
                faceInstance.onTick(this);
            }
        }
        if (updateRequested) {
            updateFaces();
            updateRequested = false;
        }
    }

    public void updateFaces() {
        requestModelDataUpdate();
        this.markDirty();
        if (this.getWorld() != null) {
            this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    @Nonnull
    public IModelData getModelData() {
        ModelDataMap.Builder map = new ModelDataMap.Builder();
        for (FaceProperty faceProperty : FaceProperty.VALUES) {
            map.withInitial(faceProperty.getModelProperty(), faceInstances.get(faceProperty.getDirection()));
        }
        return map.build();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("faceHolders")) {
            handleFaceHolderNBT(compound.getCompound("faceHolders"), FaceInstance::deserializeNBT);
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("faceHolders", createFaceNBT(FaceInstance::serializeNBT));
        return compound;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = new CompoundNBT();
        updateTag.put("faceHolders", this.createFaceNBT(FaceInstance::getUpdateTag));
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        if (tag.contains("faceHolders")) {
            handleFaceHolderNBT(tag.getCompound("faceHolders"), FaceInstance::handleUpdateTag);
        }
        updateFaces();
    }

    private void handleFaceHolderNBT(CompoundNBT faceHolderNBT, BiConsumer<FaceInstance, CompoundNBT> handleInstanceNBT) {
        for (Direction direction : Direction.values()) {
            if (faceHolderNBT.contains(direction.getName())) {
                CompoundNBT faceNBT = faceHolderNBT.getCompound(direction.getName());
                Face face = RETRegistries.FACES.getValue(new ResourceLocation(faceNBT.getString("face")));
                if (face != null) {
                    IFaceHolder faceHolder = faceHolders.get(direction);
                    faceHolder.setFace(face);
                    FaceInstance faceInstance = face.createInstance(new SocketContext(face, direction));
                    if (faceNBT.contains("instance")) {
                        handleInstanceNBT.accept(faceInstance, faceNBT.getCompound("instance"));
                    }
                    faceInstances.put(direction, faceInstance);
                }
            }
        }
    }

    private CompoundNBT createFaceNBT(Function<FaceInstance, CompoundNBT> writeInstanceTag) {
        CompoundNBT faceHolderTag = new CompoundNBT();
        for (Direction direction : Direction.values()) {
            CompoundNBT directionNBT = new CompoundNBT();
            Optional.ofNullable(this.faceHolders.get(direction))
                    .map(IFaceHolder::getFace)
                    .map(Face::getRegistryName)
                    .map(ResourceLocation::toString)
                    .ifPresent(name -> directionNBT.putString("face", name));
            Optional.ofNullable(this.faceInstances.get(direction))
                    .map(writeInstanceTag)
                    .ifPresent(tag -> directionNBT.put("instance", tag));
            faceHolderTag.put(direction.getName(), directionNBT);
        }
        return faceHolderTag;
    }

    public boolean onBlockActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        FaceInstance faceInstance = faceInstances.get(hit.getFace());
        boolean activated = faceInstance != null && faceInstance.onActivated(this, player, hand, hit);
        updateFaces();
        return activated;
    }

    public FaceInstance getFaceInstance(Direction side) {
        return faceInstances.get(side);
    }

    public int getComparatorSignal() {
        return this.faceInstances.values()
                .stream()
                .filter(Objects::nonNull)
                .map(FaceInstance::getComparatorStrength)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public int getStrongPower(Direction side) {
        return getValue(side.getOpposite(), FaceInstance::getStrongPower, 0);
    }

    public boolean canConnectRedstone(@Nullable Direction side) {
        if (side != null) {
            return getValue(side.getOpposite(), FaceInstance::canConnectRedstone, false);
        } else {
            return Arrays.stream(Direction.values())
                    .anyMatch(direction -> getValue(direction, FaceInstance::canConnectRedstone, false));
        }
    }

    private <W> W getValue(Direction side, TriFunction<FaceInstance, ISocketTile, SocketContext, W> function, W value) {
        FaceInstance faceInstance = faceInstances.get(side);
        if (faceInstance != null) {
            return function.apply(faceInstance, this, null);
        }
        return value;
    }

    public void updateFaceInstance(Face face, Direction side) {
        if (face != null) {
            faceInstances.put(side, face.createInstance(new SocketContext(face, side)));
        } else {
            faceInstances.put(side, null);
        }
    }
}
