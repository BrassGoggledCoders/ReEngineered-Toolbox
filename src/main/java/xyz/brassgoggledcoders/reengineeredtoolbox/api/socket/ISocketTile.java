package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.FluidStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;

import java.util.List;

public interface ISocketTile extends ICapabilityProvider {
    ItemStackQueue getItemStackQueue(int number);

    FluidStackQueue getFluidStackQueue(int number);

    List<ItemStackQueue> getItemStackQueues();

    List<FluidStackQueue> getFluidStackQueues();

    Face getFaceOnSide(EnumFacing facing);
}
