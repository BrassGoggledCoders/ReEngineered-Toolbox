package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceHolderProvider implements ICapabilityProvider {
    private IFaceHolder faceHolder;

    public FaceHolderProvider(IFaceHolder faceHolder) {
        this.faceHolder = faceHolder;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFaceHolder.FACE_HOLDER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? CapabilityFaceHolder.FACE_HOLDER.cast(faceHolder) : null;
    }
}
