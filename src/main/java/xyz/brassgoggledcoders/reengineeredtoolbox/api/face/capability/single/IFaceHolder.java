package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public interface IFaceHolder extends INBTSerializable<NBTTagCompound> {
    Face getFace();
}
