package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstance implements INBTSerializable<CompoundNBT> {

    public void onAttach(ISocketTile tile) {

    }

    public void onTick(ISocketTile tile) {

    }

    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return false;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public void configureQueue(String name, int queueNumber) {

    }

    public boolean onBlockActivated(PlayerEntity player, Hand hand) {
        return false;
    }

    public boolean hasGui() {
        return false;
    }
}
