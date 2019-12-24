package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.IConduitManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import java.util.Optional;

public interface ISocket extends ICapabilityProvider {
    World getWorld();

    BlockPos getBlockPos();

    BlockState getBlockState();

    void markDirty();

    void updateClient();

    void openGui(PlayerEntity playerEntity, SocketContext context);

    IConduitManager getConduitManager();
}
