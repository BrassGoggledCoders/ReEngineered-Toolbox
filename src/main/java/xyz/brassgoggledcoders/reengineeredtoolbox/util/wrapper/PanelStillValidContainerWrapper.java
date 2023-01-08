package xyz.brassgoggledcoders.reengineeredtoolbox.util.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class PanelStillValidContainerWrapper implements Container {
    private final Container container;
    private final Predicate<Player> predicate;

    public PanelStillValidContainerWrapper(Container container, Predicate<Player> predicate) {
        this.container = container;
        this.predicate = predicate;
    }

    @Override
    public int getContainerSize() {
        return this.container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.container.isEmpty();
    }

    @Override
    @NotNull
    public ItemStack getItem(int pSlot) {
        return this.container.getItem(pSlot);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int pSlot, int pAmount) {
        return this.container.removeItem(pSlot, pAmount);
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int pSlot) {
        return this.container.removeItemNoUpdate(pSlot);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        this.container.setItem(pSlot, pStack);
    }

    @Override
    public void setChanged() {
        this.container.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return this.predicate.test(pPlayer);
    }

    @Override
    public void clearContent() {
        this.container.clearContent();
    }
}
