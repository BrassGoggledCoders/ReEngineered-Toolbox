package xyz.brassgoggledcoders.reengineeredtoolbox.screen.addon.coreselector;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler.FaceMode;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.LangUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitClient;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitCore;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.conduit.ConduitType;

import java.util.List;
import java.util.stream.Collectors;

public class ConduitCoreSelectorButtonStateGuiAddon<CONTENT, CONTEXT, TYPE extends ConduitType<CONTENT, CONTEXT, TYPE>>
        extends StateButtonAddon {
    private final ConduitCore<CONTENT, CONTEXT, TYPE> conduitCore;
    private final ConduitClient<CONTENT, CONTEXT, TYPE> conduitClient;

    public ConduitCoreSelectorButtonStateGuiAddon(ConduitClient<CONTENT, CONTEXT, TYPE> conduitClient,
                                                  ConduitCore<CONTENT, CONTEXT, TYPE> conduitCore, int xPos, int yPos) {
        super(new PosButton(xPos, yPos, 14, 14), FaceMode.NONE.getInfo(), FaceMode.ENABLED.getInfo());
        this.conduitClient = conduitClient;
        this.conduitCore = conduitCore;
    }

    @Override
    public int getState() {
        return conduitCore.getClients().contains(conduitClient) ? FaceMode.ENABLED.getIndex() : FaceMode.NONE.getIndex();
    }

    @Override
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F, Minecraft.getInstance().player.getPosition()));
        if (screen instanceof ContainerScreen && ((ContainerScreen<?>)screen).getContainer() instanceof ILocatable) {
            ILocatable locatable = (ILocatable)((ContainerScreen<?>)screen).getContainer();
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(),
                    this.getButton().getId(), new CompoundNBT()));
        }

    }

    @Override
    public List<String> getTooltipLines() {
        List<String> toolTips = Lists.newArrayList(
                conduitCore.getName().applyTextStyle(TextFormatting.WHITE).getFormattedText(),
                LangUtil.get("tooltip." + ReEngineeredToolbox.ID + ".connected_clients")
        );

        toolTips.addAll(conduitCore.getClients().stream()
                .map(ConduitClient::getName)
                .map(ITextComponent::getFormattedText)
                .collect(Collectors.toSet())
        );

        return toolTips;
    }
}
