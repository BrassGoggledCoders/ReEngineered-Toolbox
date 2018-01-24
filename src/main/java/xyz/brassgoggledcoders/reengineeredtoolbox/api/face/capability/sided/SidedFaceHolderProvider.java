package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.IFaceHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SidedFaceHolderProvider implements ICapabilityProvider {
    private ISidedFaceHolder faceHolder;

    public SidedFaceHolderProvider(ISidedFaceHolder faceHolder) {
        this.faceHolder = faceHolder;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilitySidedFaceHolder.SIDED_FACE_HOLDER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? CapabilitySidedFaceHolder.SIDED_FACE_HOLDER.cast(faceHolder) : null;
    }
}
