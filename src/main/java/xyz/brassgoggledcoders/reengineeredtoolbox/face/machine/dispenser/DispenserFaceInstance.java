package xyz.brassgoggledcoders.reengineeredtoolbox.face.machine.dispenser;

import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.container.IFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.FaceInstance;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.screen.IFaceScreen;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.SocketContext;
import xyz.brassgoggledcoders.reengineeredtoolbox.container.face.inventory.SingleInventoryFaceContainer;
import xyz.brassgoggledcoders.reengineeredtoolbox.screen.face.GuiAddonFaceScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class DispenserFaceInstance extends FaceInstance {
    private static final Random RANDOM = new Random();

    private final FakeDispenserBlockSource blockSource;
    private final PosInvHandler inventory;

    private boolean powered = false;

    public DispenserFaceInstance(SocketContext socketContext) {
        super(socketContext);
        this.inventory = new PosInvHandler("Dispenser", 62, 26, 9)
                .setOnSlotChanged((itemStack, slot) -> this.markDirty())
                .setSlotPosition((index) -> Pair.of((index % 3) * 18, (index / 3) * 18));
        this.blockSource = new FakeDispenserBlockSource(socketContext);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onActivated(ISocketTile tile, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking()) {
            tile.openGui(player, this.getSocketContext());
        }
        return super.onActivated(tile, player, hand, hit);
    }

    @Override
    public void onTick(@Nonnull ISocketTile tile) {
        if (!tile.getWorld().isRemote) {
            boolean currentPowerState = Arrays.stream(Direction.values())
                    .map(tile::getFaceInstanceOnSide)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(faceInstance -> faceInstance.getStrongPower(tile, this.getSocketContext()))
                    .anyMatch(power -> power > 0);
            if (currentPowerState != powered) {
                if (currentPowerState) {
                    blockSource.setSocketTile(tile);
                    int i = this.getDispenseSlot();
                    if (i < 0) {
                        tile.getWorld().playEvent(1001, tile.getBlockPos(), 0);
                    } else {
                        ItemStack itemstack = inventory.getStackInSlot(i);
                        IDispenseItemBehavior idispenseitembehavior = DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.get(itemstack.getItem());
                        if (idispenseitembehavior != IDispenseItemBehavior.NOOP) {
                            inventory.setStackInSlot(i, idispenseitembehavior.dispense(blockSource, itemstack));
                        }

                    }
                    blockSource.setSocketTile(null);
                }

                powered = currentPowerState;
            }
        }
    }

    public int getDispenseSlot() {
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.inventory.getSlots(); ++k) {
            if (!this.inventory.getStackInSlot(k).isEmpty() && RANDOM.nextInt(j++) == 0) {
                i = k;
            }
        }

        return i;
    }

    @Override
    @Nullable
    public IFaceContainer getContainer() {
        return new SingleInventoryFaceContainer<>(this, this.inventory);
    }

    @Override
    @Nullable
    public IFaceScreen getScreen() {
        return new GuiAddonFaceScreen(this.inventory);
    }
}
