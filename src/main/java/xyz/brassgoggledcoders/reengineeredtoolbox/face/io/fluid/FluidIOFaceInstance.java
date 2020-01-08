package xyz.brassgoggledcoders.reengineeredtoolbox.face.io.fluid;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.BlankFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;

public class FluidIOFaceInstance extends FaceInstance implements IGuiAddonProvider {
    private final PosFluidTank fluidTank;
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional;
    private final IFluidHandler fluidHandlerWrapper;

    public FluidIOFaceInstance(SocketContext context, Function<IFluidHandler, IFluidHandler> createWrapper) {
        super(context);
        this.fluidTank = new PosFluidTank("Fluid Input", 4000, 80, 28)
                .setOnContentChange(this::requestUpdate);
        this.fluidHandlerWrapper = createWrapper.apply(this.fluidTank);
        this.fluidHandlerLazyOptional = LazyOptional.of(() -> fluidHandlerWrapper);
    }

    private void requestUpdate() {
        this.requestUpdate("fluidTank", () -> fluidTank.writeToNBT(new CompoundNBT()));
    }

    @Override
    public void handleUpdateTag(CompoundNBT updateNBT) {
        super.handleUpdateTag(updateNBT);
        if (updateNBT.contains("fluidTank")) {
            fluidTank.readFromNBT(updateNBT.getCompound("fluidTank"));
        }
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability) {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY == capability ? fluidHandlerLazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            this.openScreen(player);
            return true;
        } else {
            return super.onActivated(player, hand, hit);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tagCompound = super.serializeNBT();
        tagCompound.put("fluidTank", fluidTank.writeToNBT(new CompoundNBT()));
        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        fluidTank.readFromNBT(nbt.getCompound("fluidTank"));
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return new BlankFaceContainer();
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return this.fluidTank.getGuiAddons();
    }

    protected IFluidHandler getFluidHandlerWrapper() {
        return this.fluidHandlerWrapper;
    }
}
