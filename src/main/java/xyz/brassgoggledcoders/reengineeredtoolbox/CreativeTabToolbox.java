package xyz.brassgoggledcoders.reengineeredtoolbox;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

public class CreativeTabToolbox extends CreativeTabs {
    @GameRegistry.ObjectHolder(MOD_ID + ":socket")
    public static Block socket;

    private ItemStack iconStack;

    public CreativeTabToolbox() {
        super("reengineeredtoolbox");
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        return iconStack == null ? iconStack = new ItemStack(socket, 1, 0) : iconStack;
    }
}
