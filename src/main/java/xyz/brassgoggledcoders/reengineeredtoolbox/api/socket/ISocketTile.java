package xyz.brassgoggledcoders.reengineeredtoolbox.api.socket;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
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

    World getWorld();

    BlockPos getTilePos();

    boolean isClient();

    IEnergyStorage getEnergyStorage();
}
