package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.energy.PosEnergyStorage;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.freezer.FreezerRecipe;

public class FreezerFaceInstance extends FaceInstance {
    private final PosInvHandler inputInventory;
    private final PosInvHandler outputInventory;
    private final PosFluidTank fluidTank;
    private final PosEnergyStorage energyStorage;
    private final PosProgressBar progressBar;

    private long lastRecipeCheck = 0;
    private FreezerRecipe currentRecipe;

    public FreezerFaceInstance(SocketContext socketContext) {
        super(socketContext);
        inputInventory = new PosInvHandler("Input", 56, 17, 1)
                .setInputFilter(((itemStack, slot) -> slot == 0))
                .setOnSlotChanged(((itemStack, slot) -> this.markDirty()));
        outputInventory = new PosInvHandler("Output", 56, 53, 1)
                .setInputFilter(((itemStack, slot) -> false))
                .setOnSlotChanged(((itemStack, slot) -> this.markDirty()));
        fluidTank = new PosFluidTank("Freezer Tank", 4000, 12, 12)
                .setOnContentChange(this::markDirty);
        energyStorage = new PosEnergyStorage(10000, 5, 10);
        progressBar = new PosProgressBar(12, 12, 100);
    }

    @Override
    public void onTick(ISocketTile tile) {
        super.onTick(tile);
        if (currentRecipe == null) {
            handleNoRecipe(tile);
        } else {
            handleRecipe(tile);
        }
    }

    private void handleRecipe(ISocketTile tile) {
        if (matches(currentRecipe)) {
            progressBar.setProgress(progressBar.getProgress() + 1);
            if (progressBar.getProgress() >= progressBar.getProgress()) {
                ItemStack inputStack = inputInventory.getStackInSlot(0);
                if (inputStack.hasContainerItem()) {
                    inputInventory.setStackInSlot(0, inputStack.getContainerItem());
                } else {
                    inputStack.shrink(1);
                }
                outputInventory.insertItem(0, currentRecipe.getRecipeOutput(), true);
            }
        } else {
            currentRecipe = null;
            progressBar.setProgress(0);
        }
        this.markDirty();
    }

    private void handleNoRecipe(ISocketTile tile) {
        if (tile.getWorld().getGameTime() - lastRecipeCheck > 10) {
            lastRecipeCheck = tile.getWorld().getGameTime();
            if (!(inputInventory.getStackInSlot(0).isEmpty() && fluidTank.getFluid().isEmpty())) {
                currentRecipe = RecipeUtil.getRecipes(tile.getWorld(), Recipes.FREEZER_TYPE)
                        .stream()
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
                progressBar.setProgress(0);
                progressBar.setMaxProgress(currentRecipe.time);
            }
        }
    }

    private boolean matches(FreezerRecipe freezerRecipe) {
        return freezerRecipe.matches(inputInventory.getStackInSlot(0), fluidTank.getFluid()) &&
                outputInventory.insertItem(0, freezerRecipe.getRecipeOutput(), false).isEmpty();
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
}
