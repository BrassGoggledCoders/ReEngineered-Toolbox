package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

public interface ISidedFaceHolder extends INBTSerializable<NBTTagCompound> {
    Face getFace(EnumFacing facing);

    void setFace(EnumFacing facing, Face face);

    FaceInstance getFaceInstance(EnumFacing facing);

    Face[] getFaces();
}
