package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.ISidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.SidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.block.BlockSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySocket extends TileEntityBase {
    private ISidedFaceHolder sidedFaceHolder = new SidedFaceHolder();

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
}
