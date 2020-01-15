package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.IConduitManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import java.util.UUID;

public interface ISocket extends ICapabilityProvider {
    World getWorld();

    BlockPos getBlockPos();

    BlockState getBlockState();

    void markDirty();

    void updateClient();

    void openScreen(PlayerEntity playerEntity, FaceInstance faceInstance);

    IConduitManager getConduitManager();

    FaceInstance getFaceInstance(UUID identifier);

    void refreshConduitConnections();
}
