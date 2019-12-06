package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nullable;

public interface IFaceHolder extends INBTSerializable<CompoundNBT> {
    @Nullable
    Face getFace();

    void setFace(@Nullable Face face);
}
