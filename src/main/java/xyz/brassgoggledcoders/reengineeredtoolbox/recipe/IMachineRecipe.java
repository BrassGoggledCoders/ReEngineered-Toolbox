package xyz.brassgoggledcoders.reengineeredtoolbox.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IMachineRecipe extends IRecipe<IInventory> {
    int getProcessingTime();
}
