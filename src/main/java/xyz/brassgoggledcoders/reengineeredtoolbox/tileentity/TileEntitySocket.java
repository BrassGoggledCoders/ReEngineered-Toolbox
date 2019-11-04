package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.google.common.collect.Lists;
import com.teamacronymcoders.base.capability.energy.EnergyStorageSerializable;
import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.base.guisystem.IHasGui;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.ISidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.SidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.FluidStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.queue.ItemStackQueue;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.SocketBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TileEntitySocket extends TileEntity implements ISocketTile, ITickable {
    private ISidedFaceHolder sidedFaceHolder;

    private List<ItemStackQueue> itemStackQueues;
    private List<FluidStackQueue> fluidStackQueues;

    private EnergyStorageSerializable energyStorage;

    private EnumFacing lastClickedSide;

    public TileEntitySocket() {
        sidedFaceHolder = new SidedFaceHolder();

        itemStackQueues = Lists.newArrayList();
        itemStackQueues.add(new ItemStackQueue());
        itemStackQueues.add(new ItemStackQueue());

        fluidStackQueues = Lists.newArrayList();
        fluidStackQueues.add(new FluidStackQueue());
        fluidStackQueues.add(new FluidStackQueue());

        energyStorage = new EnergyStorageSerializable(100000, 1000, 1000);
    }

    @Override
    protected void readFromDisk(CompoundNBT data) {
        sidedFaceHolder.deserializeNBT(data.getCompoundNBT("faces"));
        energyStorage.deserializeNBT(data.getCompoundNBT("energy"));
        NBTTagList itemQueues = data.getTagList("itemQueues", 10);
        itemStackQueues = Lists.newArrayList();
        for (int i = 0; i < itemQueues.tagCount(); i++) {
            ItemStackQueue itemStackQueue = new ItemStackQueue();
            itemStackQueue.deserializeValue(itemQueues.getCompoundNBTAt(i));
            itemStackQueues.add(itemStackQueue);
        }

        NBTTagList fluidQueues = data.getTagList("fluidQueues", 10);
        fluidStackQueues = Lists.newArrayList();
        for (int i = 0; i < fluidQueues.tagCount(); i++) {
            FluidStackQueue fluidStackQueue = new FluidStackQueue();
            fluidStackQueue.deserializeValue(fluidQueues.getCompoundNBTAt(i));
            fluidStackQueues.add(fluidStackQueue);
        }
    }

    @Override
    protected CompoundNBT writeToDisk(CompoundNBT data) {
        data.setTag("faces", sidedFaceHolder.serializeNBT());
        data.setTag("energy", energyStorage.serializeNBT());

        NBTTagList fluidQueueTag = new NBTTagList();
        for (FluidStackQueue fluidStackQueue : fluidStackQueues) {
            fluidQueueTag.appendTag(fluidStackQueue.serializeNBT());
        }
        data.setTag("fluidQueues", fluidQueueTag);

        NBTTagList itemQueueTag = new NBTTagList();
        for (ItemStackQueue itemStackQueue : itemStackQueues) {
            itemQueueTag.appendTag(itemStackQueue.serializeNBT());
        }
        data.setTag("itemQueues", itemQueueTag);
        return data;
    }

    public IExtendedBlockState setExtendedState(IExtendedBlockState blockState) {
        return blockState.withProperty(SocketBlock.SIDED_FACE_PROPERTY, sidedFaceHolder.getFaces());
    }

    protected void setWorldCreate(World world) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            this.sidedFaceHolder.getFaceInstance(facing).onAttach(this);
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        boolean hasCap = capability == CapabilitySidedFaceHolder.SIDED_FACE_HOLDER;
        if (!hasCap && facing != null) {
            hasCap = sidedFaceHolder.getFaceInstance(facing).hasCapability(capability);
        }
        return hasCap;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        T cap = null;

        if (capability == CapabilitySidedFaceHolder.SIDED_FACE_HOLDER) {
            cap = CapabilitySidedFaceHolder.SIDED_FACE_HOLDER.cast(sidedFaceHolder);
        } else if (facing != null) {
            cap = this.sidedFaceHolder.getFaceInstance(facing).getCapability(capability);
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

    @Override
    public BlockPos getTilePos() {
        return this.getPos();
    }

    @Override
    public boolean isClient() {
        return this.world.isRemote;
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void update() {
        for (EnumFacing facing : EnumFacing.values()) {
            this.sidedFaceHolder.getFaceInstance(facing).onTick(this);
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        CompoundNBT CompoundNBT = new CompoundNBT();
        CompoundNBT.setTag("faces", this.sidedFaceHolder.serializeNBT());
        return new SPacketUpdateTileEntity(this.pos, 3, CompoundNBT);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.writeToNBT(new CompoundNBT());
        nbt.setTag("faces", this.sidedFaceHolder.serializeNBT());
        return nbt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.sidedFaceHolder.deserializeNBT(pkt.getNbtCompound().getCompoundNBT("faces"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Gui getGui(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return Optional.ofNullable(this.getLastClickedSide())
                .map(sidedFaceHolder::getFaceInstance)
                .map(faceInstance -> faceInstance.getGui(entityPlayer, this))
                .orElse(null);
    }

    @Override
    public Container getContainer(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return Optional.ofNullable(this.getLastClickedSide())
                .map(sidedFaceHolder::getFaceInstance)
                .map(faceInstance -> faceInstance.getContainer(entityPlayer, this))
                .orElse(null);
    }

    public boolean onBlockActivated(EntityPlayer player, EnumHand hand, EnumFacing facing) {
        this.lastClickedSide = facing;
        FaceInstance faceInstance = this.sidedFaceHolder.getFaceInstance(facing);
        boolean openedGui = faceInstance.hasGui() && !player.isSneaking() && world.isRemote;
        if (openedGui) {
            GuiOpener.openTileEntityGui(ReEngineeredToolbox.INSTANCE, player, this.getWorld(), this.getTilePos());
        }
        return openedGui || faceInstance.onBlockActivated(player, hand);
    }

    @Nullable
    private EnumFacing getLastClickedSide() {
        final EnumFacing returnedSide = this.lastClickedSide;
        this.lastClickedSide = null;
        return returnedSide;
    }
}
