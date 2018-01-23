package xyz.brassgoggledcoders.reengineeredtoolbox;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

import java.util.List;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class RegistryEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void handleModels(ModelRegistryEvent event) {
        Item faceItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID, "face"));
        if (faceItem != null) {
            ModelLoader.registerItemVariants(faceItem, ToolboxRegistries.FACES.getValues().stream()
                    .map(Face::getTextureLocations)
                    .flatMap(List::stream)
                    .toArray(ResourceLocation[]::new));
        } else {
            throw new IllegalStateException("Could not find Face Item");
        }

    }
}
