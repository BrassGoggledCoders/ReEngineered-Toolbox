package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory.InventoryFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.content.Recipes;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class FurnaceFaceInstance extends BasicMachineFaceInstance<AbstractCookingRecipe> {
    private final PosInvHandler inputInventory;
    private final PosInvHandler outputInventory;

    private final RecipeWrapper recipeWrapper;

    public FurnaceFaceInstance(SocketContext socketContext) {
        super(socketContext);
        inputInventory = new PosInvHandler("Input", 56, 44, 1)
                .setOutputFilter(((itemStack, slot) -> false))
                .setOnSlotChanged(((itemStack, slot) -> this.getSocket().markDirty()));
        outputInventory = new PosInvHandler("Output", 116, 44, 1)
                .setInputFilter(((itemStack, slot) -> false))
                .setOnSlotChanged(((itemStack, slot) -> this.getSocket().markDirty()));
        this.recipeWrapper = new RecipeWrapper(inputInventory);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT instanceNBT = super.serializeNBT();
        instanceNBT.put("inputInventory", inputInventory.serializeNBT());
        instanceNBT.put("outputInventory", outputInventory.serializeNBT());
        return instanceNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        inputInventory.deserializeNBT(nbt.getCompound("inputInventory"));
        outputInventory.deserializeNBT(nbt.getCompound("outputInventory"));
    }

    @Override
    public int getTime(AbstractCookingRecipe recipe) {
        return recipe.getCookTime();
    }


    @Override
    protected boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == Recipes.FURNACE_TYPE || recipe.getType() == IRecipeType.SMELTING;
    }

    @Override
    protected AbstractCookingRecipe castRecipe(IRecipe<?> iRecipe) {
        return (AbstractCookingRecipe) iRecipe;
    }

    @Override
    protected boolean inputsExist() {
        return !inputInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    protected void handleCompletedRecipe(AbstractCookingRecipe currentRecipe) {

    }

    @Override
    protected boolean matches(AbstractCookingRecipe furnaceRecipe) {
        return furnaceRecipe.matches(recipeWrapper, this.getWorld()) &&
                outputInventory.insertItem(0, furnaceRecipe.getRecipeOutput(), true).isEmpty();
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Lists.newArrayList(inputInventory.getGuiAddons(), outputInventory.getGuiAddons(), super.getGuiAddons())
                .stream()
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
