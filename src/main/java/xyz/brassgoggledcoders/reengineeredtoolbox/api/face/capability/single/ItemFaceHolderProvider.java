package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFaceHolderProvider implements ICapabilityProvider {
    private IFaceHolder faceHolder;

    public ItemFaceHolderProvider(ItemStack itemStack) {
        Face face = ToolboxRegistries.FACES.getValue(itemStack.getMetadata());
        faceHolder = new FaceHolder(face);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFaceHolder.FACE_HOLDER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return this.hasCapability(capability, facing) ? CapabilityFaceHolder.FACE_HOLDER.cast(faceHolder) : null;
    }
}
