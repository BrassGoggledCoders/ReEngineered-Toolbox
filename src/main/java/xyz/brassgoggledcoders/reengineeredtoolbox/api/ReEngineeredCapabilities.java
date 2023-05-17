package xyz.brassgoggledcoders.reengineeredtoolbox.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencySlotItemHandler;

public class ReEngineeredCapabilities {
    public static Capability<IFrequencySlotItemHandler> FREQUENCY_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
}
