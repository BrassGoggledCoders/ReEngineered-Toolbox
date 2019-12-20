package xyz.brassgoggledcoders.reengineeredtoolbox.energy;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

public class PosEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>, IGuiAddonProvider {
    private final int xPos;
    private final int yPos;

    public PosEnergyStorage(int maxCapacity, int xPos, int yPos) {
        this(maxCapacity, maxCapacity, xPos, yPos);
    }

    public PosEnergyStorage(int maxCapacity, int maxIO, int xPos, int yPos) {
        this(maxCapacity, maxIO, maxIO, xPos, yPos);
    }

    public PosEnergyStorage(int maxCapacity, int maxReceive, int maxExtract, int xPos, int yPos) {
        super(maxCapacity, maxReceive, maxExtract);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energy", this.energy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Lists.newArrayList(() -> new EnergyBarGuiAddon(xPos, yPos, this));
    }
}
