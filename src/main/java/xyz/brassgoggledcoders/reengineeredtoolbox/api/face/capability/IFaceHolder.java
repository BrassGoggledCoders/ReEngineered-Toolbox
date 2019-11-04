package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public interface IFaceHolder extends INBTSerializable<CompoundNBT> {
    Face getFace();

    void setFace(Face face);
}
