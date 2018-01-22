package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySocket extends TileEntityBase {
    private FaceInstance[] faces = new FaceInstance[6];

    @Override
    protected void readFromDisk(NBTTagCompound data) {

    }

    @Override
    protected NBTTagCompound writeToDisk(NBTTagCompound data) {
        return data;
    }
}
