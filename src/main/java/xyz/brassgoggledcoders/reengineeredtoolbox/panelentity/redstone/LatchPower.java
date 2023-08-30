package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.redstone;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum LatchPower implements StringRepresentable {
    ONE,
    TWO,
    BOTH;

    public static final Property<LatchPower> PROPERTY = EnumProperty.create("latch_power", LatchPower.class);

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
