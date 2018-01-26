package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import com.teamacronymcoders.base.items.ItemBase;
import com.teamacronymcoders.base.util.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.FaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.FaceHolderProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemFace extends ItemBase {
    public ItemFace() {
        super("face");
    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        return ToolboxRegistries.FACES.getValues().parallelStream()
                .map(TextUtils::getRegistryLocation)
                .map(value -> {
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setString("face", value);
                    return compound;
                })
                .map(value -> new ItemStack(this, 1, 1, value))
                .collect(Collectors.toList());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable NBTTagCompound nbt) {
        return Optional.ofNullable(nbt)
                .map(nbtTagCompound -> nbtTagCompound.getString("face"))
                .map(ResourceLocation::new)
                .map(ToolboxRegistries.FACES::getValue)
                .map(FaceHolder::new)
                .map(FaceHolderProvider::new)
                .orElseGet(() -> new FaceHolderProvider(null));
    }

    @Override
    public List<ResourceLocation> getResourceLocations(List<ResourceLocation> resourceLocations) {
        resourceLocations.addAll(ToolboxRegistries.FACES.getValues().parallelStream()
                .map(Face::getTextureLocation)
                .collect(Collectors.toList()));
        return resourceLocations;
    }
}
