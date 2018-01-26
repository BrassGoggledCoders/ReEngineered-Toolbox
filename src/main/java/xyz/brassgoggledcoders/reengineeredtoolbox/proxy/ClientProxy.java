package xyz.brassgoggledcoders.reengineeredtoolbox.proxy;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.reengineeredtoolbox.render.TESRSocket;
import xyz.brassgoggledcoders.reengineeredtoolbox.tileentity.TileEntitySocket;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void initModelLoader() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySocket.class, new TESRSocket());
    }
}
