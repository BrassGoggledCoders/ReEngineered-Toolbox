package xyz.brassgoggledcoders.reengineeredtoolbox;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.ID;

public class ItemGroupToolbox extends ItemGroup {
    public ItemGroupToolbox() {
        super("reengineeredtoolbox");
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return null;
    }
}
