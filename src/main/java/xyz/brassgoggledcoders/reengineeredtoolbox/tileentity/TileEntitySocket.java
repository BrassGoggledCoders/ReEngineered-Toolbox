package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.google.common.collect.Lists;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.ISidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.SidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.FluidStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntitySocket extends TileEntityBase implements ISocketTile {
    private ISidedFaceHolder sidedFaceHolder;

    private List<ItemStackQueue> itemStackQueues;
    private List<FluidStackQueue> fluidStackQueues;

    public TileEntitySocket() {
        sidedFaceHolder = new SidedFaceHolder();

        itemStackQueues = Lists.newArrayList();
        itemStackQueues.add(new ItemStackQueue());
        itemStackQueues.add(new ItemStackQueue());

        fluidStackQueues = Lists.newArrayList();
        fluidStackQueues.add(new FluidStackQueue());
        fluidStackQueues.add(new FluidStackQueue());
    }

    @Override
    protected void readFromDisk(NBTTagCompound data) {
        sidedFaceHolder.deserializeNBT(data.getCompoundTag("faces"));
    }

    @Override
    protected NBTTagCompound writeToDisk(NBTTagCompound data) {
        data.setTag("faces", sidedFaceHolder.serializeNBT());
        return data;
    }

    public IExtendedBlockState setExtendedState(IExtendedBlockState blockState) {
        return blockState.withProperty(BlockSocket.SIDED_FACE_PROPERTY, sidedFaceHolder.getFaces());
    }

    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        boolean hasCap = capability == CapabilitySidedFaceHolder.SIDED_FACE_HOLDER;
        if (!hasCap && facing != null) {
            hasCap = sidedFaceHolder.getFace(facing).hasCapability(capability);
        }
        return hasCap;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        T cap = null;

        if (capability == CapabilitySidedFaceHolder.SIDED_FACE_HOLDER) {
            cap = CapabilitySidedFaceHolder.SIDED_FACE_HOLDER.cast(sidedFaceHolder);
        } else if (facing != null) {
            cap = this.sidedFaceHolder.getFace(facing).getCapability(capability);
        }

        return cap;
    }

    @Override
    public ItemStackQueue getItemStackQueue(int number) {
        return number < itemStackQueues.size() ? itemStackQueues.get(number) : itemStackQueues.get(0);
    }

    @Override
    public FluidStackQueue getFluidStackQueue(int number) {
        return number < fluidStackQueues.size() ? fluidStackQueues.get(number) : fluidStackQueues.get(0);
    }

    @Override
    public List<ItemStackQueue> getItemStackQueues() {
        return itemStackQueues;
    }

    @Override
    public List<FluidStackQueue> getFluidStackQueues() {
        return fluidStackQueues;
    }

    @Override
    public Face getFaceOnSide(EnumFacing facing) {
        return sidedFaceHolder.getFace(facing);
    }
}
