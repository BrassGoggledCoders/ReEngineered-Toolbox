package xyz.brassgoggledcoders.reengineeredtoolbox.content;

import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Panel;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.ClearableObjectIntIdentityMap;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class RETRegistries {
    private static final ResourceLocation PANELSTATE_RL = ReEngineeredToolbox.rl("panelstate");

    public static final Supplier<IForgeRegistry<Panel>> PANELS = ReEngineeredToolbox.getRegistrate()
            .makeRegistry("panel", Panel.class, () -> new RegistryBuilder<Panel>()
                    .onCreate((owner, stage) -> owner.setSlaveMap(PANELSTATE_RL, new ClearableObjectIntIdentityMap<>()))
                    .onClear((owner, stage) -> owner.getSlaveMap(PANELSTATE_RL, ClearableObjectIntIdentityMap.class).clear())
                    .onBake((owner, stage) -> {
                        @SuppressWarnings("unchecked")
                        ClearableObjectIntIdentityMap<PanelState> map = owner.getSlaveMap(
                                PANELSTATE_RL,
                                ClearableObjectIntIdentityMap.class
                        );

                        for (Panel panel : owner) {
                            for (PanelState state : panel.getStateContainer().getPossibleStates()) {
                                map.add(state);
                            }
                        }
                    })
            );

    @SuppressWarnings("unchecked")
    public static ObjectIntIdentityMap<PanelState> getPanelStateIdentities() {
        return PANELS.get().getSlaveMap(PANELSTATE_RL, ClearableObjectIntIdentityMap.class);
    }

    public static void setup() {

    }
}
