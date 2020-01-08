package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.face.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory.InventoryFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.FreezerRecipe;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class FreezerFaceInstance extends BasicMachineFaceInstance<FreezerRecipe>
        implements IGuiAddonProvider {
    private final PosInvHandler inputInventory;
    private final PosInvHandler outputInventory;
    private final PosFluidTank fluidTank;

    public FreezerFaceInstance(SocketContext socketContext) {
        super(socketContext);
        inputInventory = new PosInvHandler("Input", 56, 44, 1)
                .setOutputFilter(((itemStack, slot) -> false))
                .setOnSlotChanged(((itemStack, slot) -> this.getSocket().markDirty()));
        outputInventory = new PosInvHandler("Output", 116, 44, 1)
                .setInputFilter(((itemStack, slot) -> false))
                .setOnSlotChanged(((itemStack, slot) -> this.getSocket().markDirty()));
        fluidTank = new PosFluidTank("Freezer Tank", 4000, 32, 24)
                .setOnContentChange(this.getSocket()::markDirty);
    }

    @Override
    protected boolean matches(FreezerRecipe freezerRecipe) {
        return freezerRecipe.matches(inputInventory.getStackInSlot(0), fluidTank.getFluid()) &&
                outputInventory.insertItem(0, freezerRecipe.getRecipeOutput(), true).isEmpty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT instanceNBT = super.serializeNBT();
        instanceNBT.put("inputInventory", inputInventory.serializeNBT());
        instanceNBT.put("outputInventory", outputInventory.serializeNBT());
        instanceNBT.put("fluidTank", fluidTank.writeToNBT(new CompoundNBT()));
        return instanceNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        inputInventory.deserializeNBT(nbt.getCompound("inputInventory"));
        outputInventory.deserializeNBT(nbt.getCompound("outputInventory"));
        fluidTank.readFromNBT(nbt.getCompound("fluidTank"));
    }

    @Override
    public int getTime(FreezerRecipe currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    protected boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == Recipes.FREEZER_TYPE;
    }

    @Override
    protected FreezerRecipe castRecipe(IRecipe<?> iRecipe) {
        return (FreezerRecipe) iRecipe;
    }

    @Override
    protected boolean inputsExist() {
        return !fluidTank.isEmpty() || !inputInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    protected void handleCompletedRecipe(FreezerRecipe currentRecipe) {
        if (currentRecipe.inputIngredient != null) {
            ItemStack itemStack = inputInventory.getStackInSlot(0);
            if (itemStack.hasContainerItem()) {
                inputInventory.setStackInSlot(0, itemStack.getContainerItem());
            } else {
                itemStack.shrink(1);
            }
        }

        if (currentRecipe.inputFluidStack != null) {
            fluidTank.drain(currentRecipe.inputFluidStack, FluidAction.EXECUTE);
        }

        outputInventory.insertItem(0, currentRecipe.getRecipeOutput(), false);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Lists.newArrayList(inputInventory, outputInventory, fluidTank, super::getGuiAddons)
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
