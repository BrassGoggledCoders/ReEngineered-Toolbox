package xyz.brassgoggledcoders.reengineeredtoolbox;

import com.teamacronymcoders.base.IBaseMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.ToolboxRegistries;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.core.BlankFace;
import xyz.brassgoggledcoders.reengineeredtoolbox.face.core.EmptyFace;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class RegistryEventHandler {
    @SubscribeEvent
    public static void buildFaceRegistry(RegistryEvent.NewRegistry newRegistryEvent) {
        ToolboxRegistries.FACES = new RegistryBuilder<Face>()
                .setName(new ResourceLocation(MOD_ID, "faces"))
                .setType(Face.class)
                .setDefaultKey(new ResourceLocation(MOD_ID, "empty"))
                .create();
    }

    @SubscribeEvent
    public static void registerFaces(RegistryEvent.Register<Face> faceRegister) {
        IForgeRegistry<Face> faceRegistry = faceRegister.getRegistry();

        //Core
        faceRegistry.register(new EmptyFace());
        faceRegistry.register(new BlankFace());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void textures(TextureStitchEvent.Pre textureStitchEvent) {
        ToolboxRegistries.FACES.getValues().parallelStream()
                .map(Face::getTextureLocation)
                .forEach((location) -> textureStitchEvent.getMap().registerSprite(location));
    }
}
