package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.teamacronymcoders.base.modularguisystem.GuiBuilder;
import com.teamacronymcoders.base.modularguisystem.components.inventory.PlayerInventoryGuiComponent;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.GuiHostSocketTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FaceInstanceItemIO extends FaceInstance {
    protected ItemStackHandler currentStack;
    protected int itemQueueNumber = 0;

    public FaceInstanceItemIO(ItemStackHandler itemStackHandler) {
        this.currentStack = itemStackHandler;
    }

    @Override
    public void configureQueue(String name, int queueNumber) {
        if ("itemQueueNumber".equals(name)) {
            itemQueueNumber = queueNumber;
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability) {
        return hasCapability(capability) ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(currentStack) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("itemQueueNumber", itemQueueNumber);
        tagCompound.setTag("itemHandler", currentStack.serializeNBT());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        itemQueueNumber = nbt.getInteger("itemQueueNumber");
        currentStack.deserializeNBT(nbt.getCompoundTag("itemHandler"));
    }

    private GuiBuilder createGuiBuilder(EntityPlayer entityPlayer) {
        GuiBuilder guiBuilder = new GuiBuilder();
        guiBuilder.addComponent(new PlayerInventoryGuiComponent(entityPlayer));
        return guiBuilder;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public Gui getGui(EntityPlayer player, ISocketTile socketTile) {
        return this.createGuiBuilder(player).createGui(new GuiHostSocketTile(socketTile));
    }

    @Nullable
    public Container getContainer(EntityPlayer player, ISocketTile socketTile) {
        return this.createGuiBuilder(player).createContainer(new GuiHostSocketTile(socketTile));
    }
}
