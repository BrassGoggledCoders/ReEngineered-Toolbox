package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public interface ISocketTile extends ICapabilityProvider {
    Face getFaceOnSide(Direction facing);

    World getWorld();

    BlockPos getTilePos();
}
