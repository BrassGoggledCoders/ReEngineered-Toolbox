package xyz.brassgoggledcoders.reengineeredtoolbox.api.util;

import java.util.OptionalInt;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalMath {
    public static boolean lessThan(OptionalInt input, int testInt) {
        return !input.isPresent() || input.getAsInt() < testInt;
    }


    public static int subtract(int first, OptionalInt second) {
        return second.isPresent() ? first - second.getAsInt() : first;
    }

    public static OptionalInt add(OptionalInt first, OptionalInt second) {
        if (first.isPresent()) {
            return second.isPresent() ? OptionalInt.of(first.getAsInt() + second.getAsInt()) : first;
        } else {
            return second;
        }
    }
}
