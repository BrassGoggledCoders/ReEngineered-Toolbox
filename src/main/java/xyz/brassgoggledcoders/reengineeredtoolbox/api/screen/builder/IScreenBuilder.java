package xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.builder;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

public interface IScreenBuilder {
    IScreenBuilder withSlots(int xPos, int yPos, int slots, int color, Function<Integer, Pair<Integer, Integer>> slotPosition);

    IScreenBuilder withScreenAddon(IScreenAddon screenAddon);
}
