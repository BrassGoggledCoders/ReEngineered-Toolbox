package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.IFrameEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panel.PanelState;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.panelentity.PanelEntity;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.energy.FrequencyBackedEnergyHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.recipe.RecipeCache;
import xyz.brassgoggledcoders.reengineeredtoolbox.util.functional.Option;

public abstract class MachinePanelEntity<T extends Recipe<U>, U extends Container> extends PanelEntity implements MenuProvider {
    private final FrameSlot energyIn;

    private final RecipeCache<T, U> cachedRecipe;

    private final FrequencyBackedEnergyHandler energyHandler;

    private int progress;
    private int coolDown;

    public MachinePanelEntity(@NotNull IFrameEntity frameEntity, @NotNull PanelState panelState) {
        super(frameEntity, panelState);
        this.energyIn = this.registerFrameSlot(this.createEnergyFrameSlot());
        this.cachedRecipe = new RecipeCache<>(this.getRecipeType());
        this.energyHandler = new FrequencyBackedEnergyHandler(energyIn, IOStyle.ONLY_EXTRACT, frameEntity);
    }

    abstract protected FrameSlot createEnergyFrameSlot();

    abstract protected RecipeType<T> getRecipeType();

    abstract protected U getRecipeContainer();

    @Override
    public void serverTick() {
        super.serverTick();
        if (coolDown <= 0 && this.energyHandler.getEnergyStored() > 0) {
            Option<T> recipe = this.cachedRecipe.getRecipe(this.getRecipeContainer(), this.getLevel());

            if (!recipe.exists(this::handleRecipe)) {
                this.coolDown = 20;
                this.setProgress(0);
            }
        } else {
            coolDown--;
        }
    }

    protected abstract boolean handleRecipe(T recipe);

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.progress = pTag.getInt("Progress");
        this.energyIn.deserializeNBT(pTag.getCompound("EnergyIn"));
    }

    @Override
    public void save(CompoundTag pTag) {
        super.save(pTag);
        pTag.putInt("Progress", this.progress);
        pTag.put("EnergyIn", this.energyIn.serializeNBT());
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.getPanel().getName();
    }


    public void incrementProgress() {
        this.setProgress(this.getProgress() + 1);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }

    public FrequencyBackedEnergyHandler getEnergyHandler() {
        return this.energyHandler;
    }

    public RecipeCache<T, U> getCachedRecipe() {
        return this.cachedRecipe;
    }
}
