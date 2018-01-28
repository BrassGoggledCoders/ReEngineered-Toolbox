package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class FaceData {

    private Map<String, Integer> integerData;
    private Map<String, ItemStack> itemStackData;

    public FaceData() {
        integerData = Maps.newHashMap();
        itemStackData = Maps.newHashMap();
    }

    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    public void deserializeNBT(NBTTagCompound nbtTagCompound) {

    }

    public Integer getIntegerData(String name) {
        return integerData.get(name);
    }

    public void setIntegerData(String name, Integer value) {
        this.integerData.put(name, value);
    }

    public ItemStack getItemStackData(String name) {
        return itemStackData.get(name);
    }

    public void setItemStackData(String name, ItemStack value) {
        this.itemStackData.put(name, value);
    }
}
