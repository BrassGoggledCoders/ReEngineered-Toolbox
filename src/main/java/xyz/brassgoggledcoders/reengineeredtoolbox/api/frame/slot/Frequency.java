package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.world.item.DyeColor;

import java.util.Optional;

public enum Frequency {
    WHITE(DyeColor.WHITE),
    ORANGE(DyeColor.ORANGE),
    MAGENTA(DyeColor.MAGENTA),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    PINK(DyeColor.PINK),
    GRAY(DyeColor.GRAY),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY),
    CYAN(DyeColor.CYAN),
    PURPLE(DyeColor.PURPLE),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    GREEN(DyeColor.GREEN),
    RED(DyeColor.RED),
    BLACK(DyeColor.BLACK);

    private final DyeColor color;

    Frequency(DyeColor color) {
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    public Frequency next() {
        int index = this.ordinal();
        int nextIndex = index + 1;
        Frequency[] frequencies = Frequency.values();
        nextIndex %= frequencies.length;
        return frequencies[nextIndex];
    }

    public static Optional<Frequency> getByName(String name) {
        for (Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(name)) {
                return Optional.of(frequency);
            }
        }
        return Optional.empty();
    }

    public static Optional<Frequency> getByDye(DyeColor toolStackDye) {
        for (Frequency frequency : Frequency.values()) {
            if (frequency.getColor() == toolStackDye) {
                return Optional.of(frequency);
            }
        }

        return Optional.empty();
    }
}
