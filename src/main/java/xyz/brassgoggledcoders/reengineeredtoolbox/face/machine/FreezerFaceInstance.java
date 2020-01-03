package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory.InventoryFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.capability.energy.PosEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

public class FreezerFaceInstance extends FaceInstance implements IGuiAddonProvider {
    private final PosInvHandler inputInventory;
    private final PosInvHandler outputInventory;
    private final PosFluidTank fluidTank;
    private final PosEnergyStorage energyStorage;
    private final PosProgressBar progressBar;

    private long lastRecipeCheck = 0;
    private FreezerRecipe currentRecipe;

    public FreezerFaceInstance(SocketContext socketContext) {
        super(socketContext);
        inputInventory = new PosInvHandler("Input", 56, 44, 1)
                .setInputFilter(((itemStack, slot) -> slot == 0))
                .setOnSlotChanged(((itemStack, slot) -> this.getSocket().markDirty()));
        outputInventory = new PosInvHandler("Output", 116, 44, 1)
                .setInputFilter(((itemStack, slot) -> false))
                .setOnSlotChanged(((itemStack, slot) -> this.getSocket().markDirty()));
        fluidTank = new PosFluidTank("Freezer Tank", 4000, 32, 24)
                .setOnContentChange(this.getSocket()::markDirty);
        energyStorage = new PosEnergyStorage(10000, 10, 24);
        progressBar = new PosProgressBar(84, 44, 100)
                .setBarDirection(PosProgressBar.BarDirection.HORIZONTAL_RIGHT);
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

    private void handleRecipe() {
        if (matches(currentRecipe)) {
            progressBar.setProgress(progressBar.getProgress() + 1);
            if (progressBar.getProgress() >= progressBar.getProgress()) {
                ItemStack inputStack = inputInventory.getStackInSlot(0);
                if (inputStack.hasContainerItem()) {
                    inputInventory.setStackInSlot(0, inputStack.getContainerItem());
                } else {
                    inputStack.shrink(1);
                }
                outputInventory.insertItem(0, currentRecipe.getRecipeOutput(), false);
            }
        } else {
            currentRecipe = null;
            progressBar.setProgress(0);
        }
        this.getSocket().markDirty();
    }

    private void handleNoRecipe() {
        if (this.getWorld().getGameTime() - lastRecipeCheck > 20) {
            lastRecipeCheck = this.getWorld().getGameTime();
            if (!(inputInventory.getStackInSlot(0).isEmpty() && fluidTank.getFluid().isEmpty())) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == Recipes.FREEZER_TYPE)
                        .map(recipe -> (FreezerRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
                progressBar.setProgress(0);
                if (currentRecipe != null) {
                    progressBar.setMaxProgress(currentRecipe.time);
                }
            }
        }
    }

    private boolean matches(FreezerRecipe freezerRecipe) {
        return freezerRecipe.matches(inputInventory.getStackInSlot(0), fluidTank.getFluid()) &&
                outputInventory.insertItem(0, freezerRecipe.getRecipeOutput(), true).isEmpty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT instanceNBT = super.serializeNBT();
        instanceNBT.put("inputInventory", inputInventory.serializeNBT());
        instanceNBT.put("outputInventory", outputInventory.serializeNBT());
        instanceNBT.put("fluidTank", fluidTank.writeToNBT(new CompoundNBT()));
        instanceNBT.put("energyStorage", energyStorage.serializeNBT());
        instanceNBT.put("progressBar", progressBar.serializeNBT());
        return instanceNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        inputInventory.deserializeNBT(nbt.getCompound("inputInventory"));
        outputInventory.deserializeNBT(nbt.getCompound("outputInventory"));
        fluidTank.readFromNBT(nbt.getCompound("fluidTank"));
        energyStorage.deserializeNBT(nbt.getCompound("energyStorage"));
        progressBar.deserializeNBT(nbt.getCompound("progressBar"));
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Lists.newArrayList(inputInventory, outputInventory, fluidTank, energyStorage, progressBar)
                .stream()
                .map(IGuiAddonProvider::getGuiAddons)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this);
    }

    @Nullable
    @Override
    public IFaceContainer getContainer() {
        return new InventoryFaceContainer<>(this, inputInventory, outputInventory);
    }
}
