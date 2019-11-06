package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.FaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.IFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Objects;

public class SocketTileEntity extends TileEntity implements ISocketTile, ITickableTileEntity {
    private EnumMap<Direction, IFaceHolder> faceHolders;
    private EnumMap<Direction, LazyOptional<IFaceHolder>> faceHolderOptionals;

    public SocketTileEntity() {
        super(Objects.requireNonNull(Blocks.SOCKET_TYPE.get()));
        faceHolders = Maps.newEnumMap(Direction.class);
        faceHolderOptionals = Maps.newEnumMap(Direction.class);

        for (Direction direction: Direction.values()) {
            FaceHolder faceHolder = new FaceHolder();
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
            this.faceHolders.get(facing).getFaceInstance().onTick(this);
        }
    }
}
