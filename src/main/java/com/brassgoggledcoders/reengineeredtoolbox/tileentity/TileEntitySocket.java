package com.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySocket extends TileEntityBase {
    @Override
    protected void readFromDisk(NBTTagCompound data) {

    }

    @Override
    protected NBTTagCompound writeToDisk(NBTTagCompound data) {
        return data;
    }
}
