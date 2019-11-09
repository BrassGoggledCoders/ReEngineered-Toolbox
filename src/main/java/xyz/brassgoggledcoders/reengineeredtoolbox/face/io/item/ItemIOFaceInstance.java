package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemIOFaceInstance extends FaceInstance {
    protected final ItemStackHandler currentStack;
    private final LazyOptional<IItemHandler> itemHandlerLazyOptional;
    protected int itemQueueNumber = 0;

    public ItemIOFaceInstance(Face face, ItemStackHandler itemStackHandler) {
        super(face);
        this.currentStack = itemStackHandler;
        this.itemHandlerLazyOptional = LazyOptional.of(() -> currentStack);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability ? itemHandlerLazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.put("itemHandler", currentStack.serializeNBT());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        currentStack.deserializeNBT(nbt.getCompound("itemHandler"));
    }
}
