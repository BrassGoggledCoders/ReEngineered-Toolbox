package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.ISocketContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.ISocketScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstance implements INBTSerializable<CompoundNBT> {
    private final Face face;

    public FaceInstance(Face face) {
        this.face = face;
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

    public Face getFace() {
        return this.face;
    }

    public ResourceLocation getSpriteLocation() {
        return face.getSpriteLocation();
    }

    public CompoundNBT getUpdateTag() {
        return new CompoundNBT();
    }

    public void handleUpdateTag(CompoundNBT updateNBT) {

    }

    public boolean onActivated(ISocketTile tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return false;
    }

    @Nullable
    public IFaceContainer getContainer(ISocketContainer container) {
        return null;
    }

    @Nullable
    public IFaceScreen getScreen(ISocketScreen screen) {
        return null;
    }

    /**
     * This call isn't side specific so the block will output based on highest strength through all faces
     *
     * @return The Strength of signal that a Comparator should output
     */
    public int getComparatorStrength() {
        return 0;
    }
}
