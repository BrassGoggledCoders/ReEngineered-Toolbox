package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
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
            public INBT writeNBT(Capability<IFaceHolder> capability, IFaceHolder instance, Direction side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IFaceHolder> capability, IFaceHolder instance, Direction side, INBT nbt) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }, FaceHolder::new);
    }
}
