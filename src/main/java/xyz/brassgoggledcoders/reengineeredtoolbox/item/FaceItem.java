package xyz.brassgoggledcoders.reengineeredtoolbox.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.CapabilityFaceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.FaceHolderProvider;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFaceHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FaceItem extends Item {
    private final Supplier<Face> faceSupplier;

    public FaceItem(Item.Properties properties, Supplier<Face> faceSupplier) {
        super(properties);
        this.faceSupplier = faceSupplier;
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
        if (tileEntity != null) {
            LazyOptional<IFaceHolder> socketFaceHolder = tileEntity.getCapability(CapabilityFaceHolder.FACE_HOLDER, context.getFace());
            socketFaceHolder.ifPresent(socketFaceHolderValue -> {
                if (socketFaceHolderValue.getFace() == null) {
                    ItemStack heldStack = context.getItem();
                    heldStack.getCapability(CapabilityFaceHolder.FACE_HOLDER)
                            .ifPresent(stackFaceHolder -> {
                                Face face = stackFaceHolder.getFace();
                                if (face.isValidForSide(context.getFace())) {
                                    socketFaceHolderValue.setFace(stackFaceHolder.getFace());
                                    heldStack.shrink(1);
                                    BlockState blockState = context.getWorld().getBlockState(context.getPos());
                                    context.getWorld().notifyBlockUpdate(context.getPos(), blockState, blockState, 3);
                                    context.getWorld().notifyNeighborsOfStateChange(context.getPos(), blockState.getBlock());
                                }
                            });
                }
            });
            if (socketFaceHolder.isPresent()) {
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ITextComponent getName() {
        return faceSupplier.get().getName();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return faceSupplier.get().getTranslationKey();
    }


    @Override
    public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
        return new FaceHolderProvider(faceSupplier.get());
    }
}
