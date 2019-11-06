package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

public interface IFaceHolder extends INBTSerializable<CompoundNBT> {
    Face getFace();

    FaceInstance getFaceInstance();

    void setFace(Face face);
}
