package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityFaceHolder {

    @CapabilityInject(IFaceHolder.class)
    public static Capability<IFaceHolder> FACE_HOLDER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IFaceHolder.class, new Capability.IStorage<IFaceHolder>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IFaceHolder> capability, IFaceHolder instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IFaceHolder> capability, IFaceHolder instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, FaceHolder::new);
    }
}
