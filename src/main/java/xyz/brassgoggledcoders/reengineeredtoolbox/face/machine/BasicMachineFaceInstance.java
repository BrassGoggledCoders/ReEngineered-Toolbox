package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.FaceContainerBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.face.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.component.energy.PosEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.component.progressbar.ProgressBarReferenceHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicMachineFaceInstance<T extends IRecipe<IInventory>> extends FaceInstance
        implements IGuiAddonProvider {
    private T currentRecipe;
    private long lastRecipeCheck;

    private final PosEnergyStorage energyStorage;
    private final PosProgressBar progressBar;

    public BasicMachineFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.energyStorage = this.createEnergyStorage();
        this.progressBar = this.createProgressBar();
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
    public void onTick() {
        super.onTick();
        if (currentRecipe == null) {
            handleNoRecipe();
        } else {
            handleRecipe();
        }
    }

    protected void handleNoRecipe() {
        if (this.getWorld().getGameTime() - lastRecipeCheck > 20) {
            lastRecipeCheck = this.getWorld().getGameTime();
            if (inputsExist()) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(this::checkRecipe)
                        .map(this::castRecipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
                progressBar.setProgress(0);
                if (currentRecipe != null) {
                    progressBar.setMaxProgress(getTime(currentRecipe));
                }
            }
        }
    }

    @Override
    public void handleUpdateTag(CompoundNBT updateNBT) {
        super.handleUpdateTag(updateNBT);
        if (updateNBT.contains("progressBar")) {
            progressBar.deserializeNBT(updateNBT.getCompound("progressBar"));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("energyStorage", energyStorage.serializeNBT());
        nbt.put("progressBar", progressBar.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        energyStorage.deserializeNBT(nbt.getCompound("energyStorage"));
        progressBar.deserializeNBT(nbt.getCompound("progressBar"));
    }

    public abstract int getTime(T currentRecipe);

    protected abstract boolean checkRecipe(IRecipe<?> recipe);

    protected abstract T castRecipe(IRecipe<?> iRecipe);

    protected abstract boolean inputsExist();

    protected void handleRecipe() {
        if (matches(currentRecipe)) {
            progressBar.setProgress(progressBar.getProgress() + 1);
            if (progressBar.getProgress() >= progressBar.getMaxProgress()) {
                handleCompletedRecipe(currentRecipe);
            }
        } else {
            currentRecipe = null;
            progressBar.setProgress(0);
        }
        this.getSocket().markDirty();
    }

    protected abstract void handleCompletedRecipe(T currentRecipe);

    protected abstract boolean matches(T recipe);

    protected PosEnergyStorage createEnergyStorage() {
        return new PosEnergyStorage(10000, 10, 24);
    }

    protected PosProgressBar createProgressBar() {
        return new PosProgressBar(84, 44, 100)
                .setBarDirection(PosProgressBar.BarDirection.HORIZONTAL_RIGHT);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Lists.newArrayList(this.progressBar.getGuiAddons(), this.energyStorage.getGuiAddons())
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public PosEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public PosProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return this.createBuilder().finish();
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this);
    }

    public FaceContainerBuilder createBuilder() {
        return new FaceContainerBuilder()
                .withReferenceHolder(this.getEnergyStorage().getIntReferenceHolder())
                .withArrayReferenceHolder(new ProgressBarReferenceHolder(this.getProgressBar()));
    }
}
