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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.FaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.block.SocketFaceContainerProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.FaceProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SocketTileEntity extends TileEntity implements ISocketTile, ITickableTileEntity {
    private EnumMap<Direction, IFaceHolder> faceHolders;
    private EnumMap<Direction, LazyOptional<IFaceHolder>> faceHolderOptionals;

    private boolean updateRequested = true;

    public SocketTileEntity() {
        super(Objects.requireNonNull(Blocks.SOCKET_TYPE.get()));
        faceHolders = Maps.newEnumMap(Direction.class);
        faceHolderOptionals = Maps.newEnumMap(Direction.class);

        for (Direction direction : Direction.values()) {
            FaceHolder faceHolder = new SocketFaceHolder(new WeakReference<>(this));
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
                IFaceHolder faceHolder = faceHolders.get(facing);
                if (faceHolder != null && faceHolder.getFaceInstance() != null) {
                    return faceHolder.getFaceInstance().getCapability(capability);
                }
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public BlockPos getTilePos() {
        return this.getPos();
    }

    @Override
    public void requestUpdate() {
        updateRequested = true;
    }

    @Override
    public void openGui(PlayerEntity playerEntity, Direction side) {
        if (playerEntity instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new SocketFaceContainerProvider(this, side),
                    packetBuffer -> {
                        packetBuffer.writeBlockPos(this.getTilePos());
                        packetBuffer.writeString(side.getName());
                    });
        }

    }

    @Override
    public <T> LazyOptional<T> getInternalCapability(Capability<T> capability, Direction side) {
        IFaceHolder faceHolder = faceHolders.get(side);
        if (faceHolder != null && faceHolder.getFaceInstance() != null) {
            return faceHolder.getFaceInstance().getInternalCapability(capability);
        }

        return LazyOptional.empty();
    }

    @Override
    public void tick() {
        for (Direction facing : Direction.values()) {
            FaceInstance faceInstance = this.faceHolders.get(facing).getFaceInstance();
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
    public IModelData getModelData() {
        ModelDataMap.Builder map = new ModelDataMap.Builder();
        for (FaceProperty faceProperty : FaceProperty.VALUES) {
            map.withInitial(faceProperty.getModelProperty(), faceHolders.get(faceProperty.getDirection()).getFaceInstance());
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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("faceHolders", createFaceNBT(FaceInstance::serializeNBT));
        return compound;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getTilePos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
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
                    if (faceNBT.contains("instance")) {
                        handleInstanceNBT.accept(faceHolder.getFaceInstance(), faceNBT.getCompound("instance"));
                    }
                }
            }
        }
    }

    private CompoundNBT createFaceNBT(Function<FaceInstance, CompoundNBT> writeInstanceTag) {
        CompoundNBT faceHolderTag = new CompoundNBT();
        for (Direction direction: Direction.values()) {
            IFaceHolder faceHolder = faceHolders.get(direction);
            if (faceHolder.getFace() != null) {
                CompoundNBT faceNBT = new CompoundNBT();
                faceNBT.putString("face", String.valueOf(faceHolder.getFace().getRegistryName()));
                if (faceHolder.getFaceInstance() != null) {
                    faceNBT.put("instance", writeInstanceTag.apply(faceHolder.getFaceInstance()));
                }
                faceHolderTag.put(direction.getName(), faceNBT);
            }
        }
        return faceHolderTag;
    }

    public boolean onBlockActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        FaceInstance faceInstance = faceHolders.get(hit.getFace()).getFaceInstance();
        boolean activated = faceInstance != null && faceInstance.onActivated(this, player, hand, hit);
        updateFaces();
        return activated;
    }

    public FaceInstance getFaceInstance(Direction sideOpened) {
        return faceHolders.get(sideOpened).getFaceInstance();
    }

    public Face getFace(Direction side) {
        return faceHolders.get(side).getFace();
    }

    public int getComparatorSignal() {
        return this.faceHolders.values()
                .stream()
                .map(IFaceHolder::getFaceInstance)
                .filter(Objects::nonNull)
                .map(FaceInstance::getComparatorStrength)
                .max(Integer::compareTo)
                .orElse(0);
    }
}
