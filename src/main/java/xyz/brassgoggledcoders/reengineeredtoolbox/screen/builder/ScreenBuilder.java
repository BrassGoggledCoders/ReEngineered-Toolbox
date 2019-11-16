package xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenAddon;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder.IScreenBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.builder.interop.ScreenAddonGuiAddon;

import java.util.List;
import java.util.function.Function;

public class ScreenBuilder implements IScreenBuilder {
    private final List<IFactory<IGuiAddon>> guiAddonFactories;

    public ScreenBuilder() {
        guiAddonFactories = Lists.newArrayList();
    }

    @Override
    public IScreenBuilder withSlots(int xPos, int yPos, int slots, int color,
                                    Function<Integer, Pair<Integer, Integer>> slotPosition) {
        guiAddonFactories.add(() -> new SlotsScreenAddon(xPos, yPos, slots, color, slotPosition));
        return this;
    }

    @Override
    public IScreenBuilder withScreenAddon(IScreenAddon screenAddon) {
        guiAddonFactories.add(() -> new ScreenAddonGuiAddon(screenAddon));
        return this;
    }

    public List<IFactory<IGuiAddon>> getGuiAddonFactories() {
        return guiAddonFactories;
    }
}
