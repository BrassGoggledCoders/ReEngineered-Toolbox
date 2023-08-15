package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyFluidHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencyRedstoneHandler;

public class ReEngineeredCapabilities {
    public static Capability<IFrequencyItemHandler> FREQUENCY_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<IFrequencyRedstoneHandler> FREQUENCY_REDSTONE_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<IFrequencyFluidHandler> FREQUENCY_FLUID_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<IFrequencyEnergyHandler> FREQUENCY_ENERGY_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
}
