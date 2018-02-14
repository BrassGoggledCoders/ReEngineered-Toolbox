package xyz.brassgoggledcoders.reengineeredtoolbox.proxy;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.reengineeredtoolbox.model.SocketModelLoader;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void initModelLoader() {
        ModelLoaderRegistry.registerLoader(new SocketModelLoader());
    }
}
