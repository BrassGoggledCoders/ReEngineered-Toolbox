package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class FaceData {

    private Map<String, Object> data;

    public FaceData() {
        data = Maps.newHashMap();
    }

    public void setData(String name, Object value) {
        data.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String name) {
        return (T) data.get(name);
    }

    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    public void deserializeNBT(NBTTagCompound nbtTagCompound) {

    }
}
