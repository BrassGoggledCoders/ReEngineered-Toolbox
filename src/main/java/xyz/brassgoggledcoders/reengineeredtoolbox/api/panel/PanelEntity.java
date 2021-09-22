package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class PanelEntity implements INBTSerializable<CompoundNBT> {
    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
