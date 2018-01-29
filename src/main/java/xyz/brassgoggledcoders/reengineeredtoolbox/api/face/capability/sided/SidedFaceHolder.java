package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided;

import com.teamacronymcoders.base.util.TextUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nullable;
import java.util.Arrays;

public class SidedFaceHolder implements ISidedFaceHolder {
    @ObjectHolder("reengineeredtoolbox:empty")
    public static Face emptyFace;

    private Face[] faces;
    private FaceInstance[] faceInstances;

    public SidedFaceHolder() {
        this.faces = new Face[6];
        this.faceInstances = new FaceInstance[6];
        for(int i = 0; i < 6; i++) {
            faces[i] = emptyFace;
            faceInstances[i] = faces[i].createInstance();
        }
    }

    @Override
    public Face getFace(EnumFacing facing) {
        return faces[facing.ordinal()];
    }

    @Override
    public void setFace(EnumFacing facing, Face face) {
        faces[facing.ordinal()] = face;
        faceInstances[facing.ordinal()] = face.createInstance();
    }

    @Override
    @Nullable
    public FaceInstance getFaceInstance(EnumFacing facing) {
        return faceInstances[facing.ordinal()];
    }

    @Override
    public Face[] getFaces() {
        return faces;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        for (int i = 0; i < faces.length; i++) {
            NBTTagCompound thisFace = new NBTTagCompound();
            thisFace.setString("face", TextUtils.getRegistryLocation(faces[i]));
            thisFace.setTag("faceInstance", faceInstances[i].serializeNBT());
            nbtTagCompound.setTag(Integer.toString(i), thisFace);
        }
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        for (int i = 0; i < faces.length; i++) {
            NBTTagCompound thisFace = nbt.getCompoundTag(Integer.toString(i));
            faces[i] = ToolboxRegistries.FACES.getValue(new ResourceLocation(thisFace.getString("face")));
            FaceInstance faceInstance = faces[i].createInstance();
            faceInstance.deserializeNBT(thisFace.getCompoundTag("faceInstance"));
            faceInstances[i] = faceInstance;
        }
    }
}
