package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.IConduitManager;

public interface ISocket extends ICapabilityProvider {
    World getWorld();

    BlockPos getBlockPos();

    BlockState getBlockState();

    void markDirty();

    void updateClient();

    void openGui(PlayerEntity playerEntity, SocketContext context);

    IConduitManager getConduitManager();
}
