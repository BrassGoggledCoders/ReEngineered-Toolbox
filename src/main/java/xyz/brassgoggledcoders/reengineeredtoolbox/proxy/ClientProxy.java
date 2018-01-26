package xyz.brassgoggledcoders.reengineeredtoolbox.proxy;

import xyz.brassgoggledcoders.reengineeredtoolbox.model.SocketModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void initModelLoader() {
        //ModelLoaderRegistry.registerLoader(new SocketModelLoader());
    }
}
