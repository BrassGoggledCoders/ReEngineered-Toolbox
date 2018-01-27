package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.util.EnumFacing;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.FluidStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;

import java.util.List;

public interface ISocketTile {
    ItemStackQueue getItemStackQueue(int number);

    FluidStackQueue getFluidStackQueue(int number);

    List<ItemStackQueue> getItemStackQueues();

    List<FluidStackQueue> getFluidStackQueues();

    Face getFaceOnSide(EnumFacing facing);
}
