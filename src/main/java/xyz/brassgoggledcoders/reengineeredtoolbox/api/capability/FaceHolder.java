package xyz.brassgoggledcoders.reengineeredtoolbox.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.RETRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import java.util.Optional;

public class FaceHolder implements IFaceHolder {
    private Face face;
    private FaceInstance faceInstance;

    public FaceHolder() {

    }

    public FaceHolder(Face face) {
        this.face = face;
        this.faceInstance = face.createInstance();
    }

    @Override
    public Face getFace() {
        return face;
    }

    @Override
    public FaceInstance getFaceInstance() {
        return faceInstance;
    }

    @Override
    public void setFace(Face face) {
        this.face = face;
        this.faceInstance = face.createInstance();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        Optional.ofNullable(face)
                .map(Face::getRegistryName)
                .map(ResourceLocation::toString)
                .ifPresent(registryName -> compoundNBT.putString("face", registryName));
        Optional.ofNullable(this.faceInstance)
                .map(FaceInstance::serializeNBT)
                .ifPresent(nbt -> compoundNBT.put("faceinstance", nbt));
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
        if (nbt.contains("faceinstance") && faceInstance != null) {
            faceInstance.deserializeNBT(nbt.getCompound("faceinstance"));
        }
    }
}
