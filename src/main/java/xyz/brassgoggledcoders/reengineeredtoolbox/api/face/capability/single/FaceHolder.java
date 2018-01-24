package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single;

import com.teamacronymcoders.base.util.RegistryUtils;
import com.teamacronymcoders.base.util.TextUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nullable;

public class FaceHolder implements IFaceHolder {
    private Face face;

    @Override
    public Face getFace() {
        return face;
    }

    public FaceHolder() {

    }

    public FaceHolder(Face face) {
        this.face = face;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("face", TextUtils.getRegistryLocation(this.getFace()));
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        face = RegistryUtils.getEntry(ToolboxRegistries.FACES,
                new ResourceLocation(nbt.getString("face")));
    }
}
