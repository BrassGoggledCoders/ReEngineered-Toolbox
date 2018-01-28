package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import com.teamacronymcoders.base.items.IHasItemMeshDefinition;
import com.teamacronymcoders.base.items.ItemBaseNoModel;
import com.teamacronymcoders.base.util.TextUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.CapabilitySidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.sided.ISidedFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.FaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.FaceHolderProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.capability.single.IFaceHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class ItemFace extends ItemBaseNoModel implements IHasItemMeshDefinition {
    @ObjectHolder(MOD_ID + ":empty")
    public static Face emptyFace;

    public ItemFace() {
        super("face");
    }

    public ItemFace(String name) {
        super(name);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return this.getFace(stack).getUnlocalizedName();
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        EnumActionResult result = EnumActionResult.PASS;
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.hasCapability(CapabilitySidedFaceHolder.SIDED_FACE_HOLDER, facing)) {
            ISidedFaceHolder sidedFaceHolder = tileEntity.getCapability(CapabilitySidedFaceHolder.SIDED_FACE_HOLDER, facing);
            if (sidedFaceHolder != null && sidedFaceHolder.getFace(facing).isReplaceable()) {
                ItemStack heldStack = player.getHeldItem(hand);
                if (heldStack.hasCapability(CapabilityFaceHolder.FACE_HOLDER, null)) {
                    IFaceHolder faceHolder = heldStack.getCapability(CapabilityFaceHolder.FACE_HOLDER, null);
                    if (faceHolder != null) {
                        sidedFaceHolder.setFace(facing, faceHolder.getFace());
                        heldStack.shrink(1);
                        result = EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        return ToolboxRegistries.FACES.getValues().stream()
                .filter(Face::createSubItem)
                .map(TextUtils::getRegistryLocation)
                .map(value -> {
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setString("face", value);
                    return compound;
                })
                .map(value -> new ItemStack(this, 1, 0, value))
                .collect(Collectors.toList());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable NBTTagCompound nbt) {
        return new FaceHolderProvider(new FaceHolder(Optional.ofNullable(nbt)
                .map(nbtTagCompound -> nbtTagCompound.getString("face"))
                .map(ResourceLocation::new)
                .map(ToolboxRegistries.FACES::getValue)
                .orElse(emptyFace)));
    }

    @Override
    public ResourceLocation getResourceLocation(ItemStack itemStack) {
        return this.getFace(itemStack).getModelLocation();
    }

    @Override
    public List<ResourceLocation> getAllVariants() {
        return ToolboxRegistries.FACES.getValues().stream()
                .map(Face::getModelLocation)
                .collect(Collectors.toList());
    }

    public Face getFace(ItemStack itemStack) {
        return this.getFaceCapability(itemStack)
                .map(IFaceHolder::getFace)
                .orElse(emptyFace);
    }

    public void setFace(ItemStack itemStack, Face face) {
        this.getFaceCapability(itemStack)
                .ifPresent(cap -> cap.setFace(face));
    }

    public Optional<IFaceHolder> getFaceCapability(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getCapability(CapabilityFaceHolder.FACE_HOLDER, null));
    }
}
