package xyz.brassgoggledcoders.reengineeredtoolbox.panelentity.world.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PanelEntityBlockSource implements BlockSource {
    private final DispenserPanelEntity panelEntity;
    private final ServerLevel serverLevel;

    public PanelEntityBlockSource(DispenserPanelEntity panelEntity, ServerLevel serverLevel) {
        this.panelEntity = panelEntity;
        this.serverLevel = serverLevel;
    }

    @Override
    public double x() {
        return this.getPos().getX() + 0.5D;
    }

    @Override
    public double y() {
        return this.getPos().getY() + 0.5D;
    }

    @Override
    public double z() {
        return this.getPos().getZ() + 0.5D;
    }

    @Override
    @NotNull
    public BlockPos getPos() {
        return this.panelEntity.getBlockPos();
    }

    @Override
    @NotNull
    public BlockState getBlockState() {
        return this.panelEntity.asDispenser();
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> T getEntity() {
        return (T) this.panelEntity.getDispenserEntity();
    }

    @Override
    @NotNull
    public ServerLevel getLevel() {
        return this.serverLevel;
    }
}
