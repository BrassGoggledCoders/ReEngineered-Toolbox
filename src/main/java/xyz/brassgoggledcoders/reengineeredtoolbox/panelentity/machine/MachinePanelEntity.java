package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntityType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

import java.util.List;

public abstract class MachinePanelEntity<T extends Recipe<C>, C extends Container> extends PanelEntity {
    public MachinePanelEntity(@NotNull PanelEntityType<?> type, @NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(type, frameEntity, panelState);
    }

    public abstract RecipeType<T> getRecipeType();

    @Override
    public void serverTick() {
        super.serverTick();

    }

    @Override
    public List<Port> getPorts() {
        return List.of(
                new Port("in", null, -1, TypedSlotTypes.ITEM.get()),
                new Port("out", null, -1, TypedSlotTypes.ITEM.get()),
                new Port("fuel", null, -1, TypedSlotTypes.ITEM.get())
        );
    }
}
