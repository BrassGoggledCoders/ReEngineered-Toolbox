package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.FaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.IFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.FaceProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.Objects;

public class SocketTileEntity extends TileEntity implements ISocketTile, ITickableTileEntity {
    private EnumMap<Direction, IFaceHolder> faceHolders;
    private EnumMap<Direction, LazyOptional<IFaceHolder>> faceHolderOptionals;

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
                    faceHolder.getFaceInstance().getCapability(capability);
                }
            }
        }

        return LazyOptional.empty();
    }

    @Override
    public Face getFaceOnSide(Direction facing) {
        return faceHolders.get(facing).getFace();
    }

    @Override
    public BlockPos getTilePos() {
        return this.getPos();
    }

    @Override
    public void tick() {
        for (Direction facing : Direction.values()) {
            FaceInstance faceInstance = this.faceHolders.get(facing).getFaceInstance();
            if (faceInstance != null) {
                faceInstance.onTick(this);
            }
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
            CompoundNBT faceHolderNBT = compound.getCompound("faceHolders");
            for (Direction direction : Direction.values()) {
                if (faceHolderNBT.contains(direction.getName())) {
                    CompoundNBT faceNBT = faceHolderNBT.getCompound(direction.getName());
                    Face face = RETRegistries.FACES.getValue(new ResourceLocation(faceNBT.getString("face")));
                    if (face != null) {
                        IFaceHolder faceHolder = faceHolders.get(direction);
                        faceHolder.setFace(face);
                        if (faceNBT.contains("instance")) {
                            faceHolder.getFaceInstance().deserializeNBT(faceNBT.getCompound("instance"));
                        }
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        CompoundNBT faceHolderNBT = new CompoundNBT();
        for (Direction direction : Direction.values()) {
            IFaceHolder faceHolder = faceHolders.get(direction);
            if (faceHolder.getFace() != null) {
                CompoundNBT faceNBT = new CompoundNBT();
                faceNBT.putString("face", Objects.requireNonNull(faceHolder.getFace().getRegistryName()).toString());
                if (faceHolder.getFaceInstance() != null) {
                    faceNBT.put("instance", faceHolder.getFaceInstance().serializeNBT());
                }
                faceHolderNBT.put(direction.getName(), faceNBT);
            }
        }
        compound.put("faceHolders", faceHolderNBT);
        return compound;
    }
}
