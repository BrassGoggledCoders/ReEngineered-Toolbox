package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item.ItemIOFaceInstance;

public interface ISocketTile extends ICapabilityProvider {
    World getWorld();

    BlockPos getTilePos();

    BlockState getBlockState();

    /**
     * Marks that the FaceInstance has updated and needs to be saved
     */
    void markDirty();

    /**
     * Requests an update to be sent to the front end
     */
    void requestUpdate();

    void openGui(PlayerEntity playerEntity, Direction side);
}
