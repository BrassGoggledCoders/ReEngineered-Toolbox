package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;

import javax.annotation.Nullable;
import java.util.Optional;

public class FaceHolder implements IFaceHolder {
    private Face face;

    public FaceHolder() {

    }

    public FaceHolder(Face face) {
        this.face = face;
    }

    @Override
    @Nullable
    public Face getFace() {
        return face;
    }

    @Override
    public void setFace(@Nullable Face face) {
        this.face = face;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        Optional.ofNullable(face)
                .map(Face::getRegistryName)
                .map(ResourceLocation::toString)
                .ifPresent(registryName -> compoundNBT.putString("face", registryName));
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("face")) {
            Optional.of(nbt.getString("face"))
                    .map(ResourceLocation::new)
                    .map(RETRegistries.FACES::getValue)
                    .ifPresent(this::setFace);
        }
    }
}
