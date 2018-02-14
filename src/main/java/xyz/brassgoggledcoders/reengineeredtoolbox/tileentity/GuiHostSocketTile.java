package xyz.brassgoggledcoders.reengineeredtoolbox.tileentity;

import com.teamacronymcoders.base.modularguisystem.IModularGuiHost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.socket.ISocketTile;

public class GuiHostSocketTile implements IModularGuiHost {
    private ISocketTile socketTile;

    public GuiHostSocketTile(ISocketTile socketTile) {
        this.socketTile = socketTile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        BlockPos pos = socketTile.getTilePos();
        return player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
    }
}
