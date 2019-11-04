package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceHolderProvider implements ICapabilityProvider {
    private final LazyOptional<IFaceHolder> faceHolderOptional;

    public FaceHolderProvider(Face face) {
        FaceHolder faceHolder = new FaceHolder(face);
        faceHolderOptional = LazyOptional.of(() -> faceHolder);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        return cap == CapabilityFaceHolder.FACE_HOLDER ? faceHolderOptional.cast() : LazyOptional.empty();
    }
}
