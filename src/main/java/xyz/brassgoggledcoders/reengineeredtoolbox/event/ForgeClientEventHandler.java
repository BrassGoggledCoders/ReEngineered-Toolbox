package xyz.brassgoggledcoders.reengineeredtoolbox.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.commons.lang3.Range;
import xyz.brassgoggledcoders.reengineeredtoolbox.ReEngineeredToolbox;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.connection.Port;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ClientPlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.PlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotHolderState;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotState;

import java.util.*;
import java.util.Map.Entry;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ForgeClientEventHandler {

    private static final ResourceLocation TABS = ReEngineeredToolbox.rl("textures/screen/components.png");

    @SubscribeEvent
    public static void onMenuClose(ScreenEvent.Closing closingEvent) {
        if (closingEvent.getScreen() instanceof AbstractContainerScreen<?>) {
            ClientPlayerConnectionTabManager.getInstance()
                    .clear();
        }
    }

    @SubscribeEvent
    public static void onPortTabClick(ScreenEvent.MouseButtonPressed.Post mouseClickedEvent) {
        Screen screen = mouseClickedEvent.getScreen();
        if (mouseClickedEvent.getButton() == 0 && screen instanceof AbstractContainerScreen<?> containerScreen) {
            AbstractContainerMenu menu = containerScreen.getMenu();
            PlayerConnectionTabManager tabManager = ClientPlayerConnectionTabManager.getInstance();
            if (tabManager.isForMenu(menu)) {
                List<Port> panelPortInfo = new ArrayList<>(tabManager.getPanelPorts()
                        .keySet()
                );
                String selectedConnection = tabManager.getSelectedPort();
                int screenLeft = containerScreen.getGuiLeft();
                double mouseX = mouseClickedEvent.getMouseX();
                Range<Integer> outwardTabs = getOutwardTabs(containerScreen, panelPortInfo, selectedConnection);
                int screenTop = containerScreen.getGuiTop();
                double mouseY = mouseClickedEvent.getMouseY();
                if (mouseY > screenTop) {
                    double difference = mouseY - screenTop;
                    int tab = Math.floorDiv((int) Math.floor(difference), 28);
                    if (tab >= 0 && tab < panelPortInfo.size()) {
                        if (outwardTabs.contains(tab)) {
                            screenLeft -= 79;
                        }
                        if (mouseX < screenLeft && mouseX > screenLeft - 32) {
                            Port moduleTab = panelPortInfo.get(tab);
                            if (!moduleTab.identifier().equals(selectedConnection)) {
                                tabManager.setSelectedPort(moduleTab.identifier());
                            }
                        }
                    }
                    if (selectedConnection != null) {
                        int tabStart = outwardTabs.getMinimum();
                        int tabTop = screenTop + tabStart * 28;
                        int tabLeft = containerScreen.getGuiLeft() - 79;

                        if (mouseX > tabLeft && mouseX < containerScreen.getGuiLeft() && mouseY > tabTop && mouseY < tabTop + 84) {
                            TypedSlotHolderState state = tabManager.getTypedSlotHolderState();
                            int slotStartX = (tabLeft + 43) - (state.width() * 9);
                            int slotStartY = (tabTop + 42) - (state.height() * 9);

                            for (int slotX = 0; slotX < state.width(); slotX++) {
                                for (int slotY = 0; slotY < state.height(); slotY++) {
                                    int slot = slotX + (slotY * state.width());
                                    int slotPosX = slotStartX + (slotX * 18);
                                    int slotPosY = slotStartY + (slotY * 18);

                                    if (mouseX > slotPosX && mouseX < slotPosX + 18 && mouseY > slotPosY && mouseY < slotPosY + 18) {
                                        tabManager.setPortConnection(selectedConnection, slot);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderPortTabs(ContainerScreenEvent.Render.Background drawScreenEvent) {
        AbstractContainerMenu menu = drawScreenEvent.getContainerScreen().getMenu();
        PlayerConnectionTabManager tabManager = ClientPlayerConnectionTabManager.getInstance();
        if (tabManager.isForMenu(menu)) {
            loopTabs(
                    drawScreenEvent.getContainerScreen(),
                    drawScreenEvent.getPoseStack(),
                    tabManager.getPanelPorts(),
                    tabManager.getSelectedPort()
            );
        }
    }

    private static Range<Integer> getOutwardTabs(AbstractContainerScreen<?> screen, Collection<Port> tabs, String identity) {
        int maxSupportedTabs = (int) Math.floor(screen.getYSize() / 28F);
        int maxTabs = Math.min(tabs.size(), maxSupportedTabs);
        int matchingTab = -1;

        Iterator<Port> portIterator = tabs.iterator();

        int x = 0;
        while (portIterator.hasNext() && x < maxTabs) {
            Port port = portIterator.next();
            if (port.identifier().equals(identity)) {
                matchingTab = x;
            }
            x++;
        }

        int panelTabStart = Math.min(matchingTab, maxSupportedTabs - 3);
        if (matchingTab >= 0) {
            return Range.between(panelTabStart, panelTabStart + 2);
        } else {
            return Range.is(maxSupportedTabs);
        }
    }

    private static void loopTabs(AbstractContainerScreen<?> screen, PoseStack poseStack, Map<Port, Integer> ports, String identity) {
        if (!ports.isEmpty()) {
            Range<Integer> handledTabs = getOutwardTabs(screen, ports.keySet(), identity);
            int outward = -1;
            int maxSupportedTabs = (int) Math.floor(screen.getYSize() / 28F);
            int maxTabs = Math.min(ports.size(), maxSupportedTabs);

            int x = 0;
            Iterator<Entry<Port, Integer>> portIterator = ports.entrySet().iterator();
            while (portIterator.hasNext() && x < maxTabs) {
                Entry<Port, Integer> portConnection = portIterator.next();
                Port port = portConnection.getKey();
                boolean matches = port.identifier().equals(identity);
                if (x == handledTabs.getMinimum()) {
                    outward = 2;
                    RenderSystem.setShaderTexture(0, TABS);
                    int panelTexY = x > 0 ? 84 : 0;
                    int panelPosX = screen.getGuiLeft() - 79;
                    int panelPosY = screen.getGuiTop() + (x * 28);
                    screen.blit(poseStack, panelPosX, panelPosY, 68, panelTexY, 83, 84);

                    PlayerConnectionTabManager tabManager = ClientPlayerConnectionTabManager.getInstance();
                    TypedSlotHolderState state = tabManager.getTypedSlotHolderState();
                    int slotStartX = (panelPosX + 43) - (state.width() * 9);
                    int slotStartY = (panelPosY + 42) - (state.height() * 9);

                    for (int slotX = 0; slotX < state.width(); slotX++) {
                        for (int slotY = 0; slotY < state.height(); slotY++) {
                            int slot = slotX + (slotY * state.width());
                            int slotPosX = slotStartX + (slotX * 18);
                            int slotPosY = slotStartY + (slotY * 18);
                            RenderSystem.setShaderTexture(0, TABS);
                            TypedSlotState slotState = state.slotStates()[slot];
                            int textureY = slotState.type() == port.backingSlot() || slotState.empty() ? 18 : 0;
                            if (slot == portConnection.getValue()) {
                                textureY = 0;
                            }
                            screen.blit(poseStack, slotPosX, slotPosY, 152, textureY, 18, 18);
                            Minecraft minecraft = Minecraft.getInstance();
                            ItemRenderer itemRenderer = minecraft.getItemRenderer();
                            itemRenderer.blitOffset = 100.0F;
                            ItemStack itemstack = slotState.type().getDisplayStack();
                            itemRenderer.renderAndDecorateItem(itemstack, slotPosX + 1, slotPosY);
                            itemRenderer.renderGuiItemDecorations(minecraft.font, itemstack, slotPosX + 1, slotPosY);
                            itemRenderer.blitOffset = 0.0F;
                        }
                    }
                }
                renderTab(
                        screen,
                        port,
                        poseStack,
                        matches,
                        (outward >= 0 ? getOutwardTab(outward) : Math.min(x, 1)),
                        screen.getGuiLeft() - (outward >= 0 ? 107 : 28),
                        screen.getGuiTop() + (x * 28)
                );
                if (outward >= 0) {
                    outward--;
                }
                x++;
            }
        }
    }

    private static int getOutwardTab(int outward) {
        return switch (outward) {
            case 1 -> 1;
            case 0 -> 5;
            default -> 0;
        };
    }

    private static void renderTab(AbstractContainerScreen<?> screen, Port port, PoseStack matrixStack, boolean selected, int tab, int x, int y) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.enableBlend();
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TABS);
        screen.blit(matrixStack, x, y, selected ? 32 : 0, tab * 28, 32, 28);
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        itemRenderer.blitOffset = 100.0F;
        ItemStack itemstack = port.backingSlot().getDisplayStack();
        itemRenderer.renderAndDecorateItem(itemstack, x + 9, y + 6);
        itemRenderer.renderGuiItemDecorations(minecraft.font, itemstack, x + 9, y + 6);
        itemRenderer.blitOffset = 0.0F;
    }
}
