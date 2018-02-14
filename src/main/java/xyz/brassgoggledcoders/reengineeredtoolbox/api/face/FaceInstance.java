package xyz.brassgoggledcoders.reengineeredtoolbox.api.face;

import com.teamacronymcoders.base.guisystem.GuiOpener;
import net.minecraft.client.gui.Gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstance implements INBTSerializable<NBTTagCompound> {

    public void onAttach(ISocketTile tile) {

    }

    public void onTick(ISocketTile tile) {

    }

    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return false;
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

    public void configureQueue(String name, int queueNumber) {

    }

    public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
        return false;
    }

    public boolean hasGui() {
        return false;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public Gui getGui(EntityPlayer player, ISocketTile socketTile) {
        return null;
    }

    @Nullable
    public Container getContainer(EntityPlayer player, ISocketTile socketTile) {
        return null;
    }
}
