package xyz.brassgoggledcoders.reengineeredtoolbox.api.panel;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyValue;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrame;

import javax.annotation.Nullable;
import java.util.UUID;

public class Panel extends ForgeRegistryEntry<Panel> {
    private final LazyValue<String> descriptionId;

    public Panel() {
        this.descriptionId = new LazyValue<>(() -> Util.makeDescriptionId("panel", this.getRegistryName()));
    }

    public String getDescriptionId() {
        return this.descriptionId.get();
    }

    public IFormattableTextComponent getName() {
        return new TranslationTextComponent(this.getDescriptionId());
    }

    @Nullable
    public <T extends Enum<T> & IStringSerializable> Enum<T> getState(UUID id, IFrame frame) {
        return null;
    }
}
