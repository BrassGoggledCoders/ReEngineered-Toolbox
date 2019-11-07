package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstance implements INBTSerializable<CompoundNBT> {
    private final Face face;

    public FaceInstance(Face face) {
        this.face = face;
    }

    public void onAttach(ISocketTile tile) {

    }

    public void onTick(ISocketTile tile) {

    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public boolean onBlockActivated(PlayerEntity player, Hand hand) {
        return false;
    }

    public Face getFace() {
        return this.face;
    }

    public ResourceLocation getSpriteLocation() {
        return face.getSpriteLocation();
    }
}
