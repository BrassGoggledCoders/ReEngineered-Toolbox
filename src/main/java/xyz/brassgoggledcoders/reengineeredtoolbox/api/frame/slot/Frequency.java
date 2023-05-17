package xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot;

import net.minecraft.ChatFormatting;

import java.util.Optional;

public enum Frequency {
    BLACK(ChatFormatting.BLACK),
    DARK_BLUE(ChatFormatting.DARK_BLUE),
    DARK_GREEN(ChatFormatting.DARK_GREEN),
    DARK_AQUA(ChatFormatting.DARK_AQUA),
    DARK_RED(ChatFormatting.DARK_RED),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE),
    GOLD(ChatFormatting.GOLD),
    GRAY(ChatFormatting.GRAY),
    DARK_GRAY(ChatFormatting.DARK_GRAY),
    BLUE(ChatFormatting.BLUE),
    GREEN(ChatFormatting.GREEN),
    AQUA(ChatFormatting.AQUA),
    RED(ChatFormatting.RED),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE),
    YELLOW(ChatFormatting.YELLOW),
    WHITE(ChatFormatting.WHITE);

    private final ChatFormatting color;

    Frequency(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public static Optional<Frequency> getByName(String name) {
        for (Frequency frequency: Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(name)) {
                return Optional.of(frequency);
            }
        }
        return Optional.empty();
    }
}
