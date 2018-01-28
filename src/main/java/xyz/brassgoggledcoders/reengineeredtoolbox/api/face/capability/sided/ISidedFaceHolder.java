package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceData;

public interface ISidedFaceHolder extends INBTSerializable<NBTTagCompound> {
    Face getFace(EnumFacing facing);

    void setFace(EnumFacing facing, Face face);

    FaceData getFaceData(EnumFacing facing);

    Face[] getFaces();
}
