package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.item;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory.SingleInventoryFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemIOFaceInstance extends FaceInstance implements IGuiAddonProvider {
    private final PosInvHandler inventory;
    private final LazyOptional<IItemHandler> itemHandlerLazyOptional;

    public ItemIOFaceInstance(SocketContext context, PosInvHandler itemStackHandler) {
        super(context);
        this.inventory = itemStackHandler;
        this.itemHandlerLazyOptional = LazyOptional.of(() -> inventory);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability ? itemHandlerLazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(ISocketTile tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            tile.openGui(player, this.getSocketContext());
        }
        return super.onActivated(tile, player, hand, hit);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.put("inventory", inventory.serializeNBT());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return new SingleInventoryFaceContainer<>(this, this.inventory);
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this);
    }

    public PosInvHandler getInventory() {
        return this.inventory;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return this.inventory.getGuiAddons();
    }
}
