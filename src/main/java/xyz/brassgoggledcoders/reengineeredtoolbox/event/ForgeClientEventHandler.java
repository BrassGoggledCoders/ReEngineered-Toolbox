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
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.IPanelMenu;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.menu.PanelPortInfo;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.ClientPlayerConnectionTabManager;
import xyz.brassgoggledcoders.reengineeredtoolbox.menu.tab.PlayerConnectionTabManager;

import java.util.List;

@EventBusSubscriber(modid = ReEngineeredToolbox.ID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ForgeClientEventHandler {

    private static final ResourceLocation TABS = ReEngineeredToolbox.rl("textures/screen/components.png");

    @SubscribeEvent
    public static void onMenuOpen(ScreenEvent.Opening openingEvent) {
        if (openingEvent.getNewScreen() instanceof AbstractContainerScreen<?> screen && screen.getMenu() instanceof IPanelMenu panelMenu) {
            PanelPortInfo panelPortInfo = panelMenu.getConnectionInfo();
            if (panelPortInfo != null) {
                ClientPlayerConnectionTabManager.getInstance()
                        .setPanelConnectionInfo(panelPortInfo);
            }
        }
    }

    @SubscribeEvent
    public static void onMenuClose(ScreenEvent.Closing closingEvent) {
        ClientPlayerConnectionTabManager.getInstance()
                .clear();
    }

    @SubscribeEvent
    public static void onModuleTabClick(ScreenEvent.MouseButtonPressed mouseClickedEvent) {
        Screen screen = mouseClickedEvent.getScreen();
        if (mouseClickedEvent.getButton() == 0 && screen instanceof AbstractContainerScreen<?> containerScreen) {
            AbstractContainerMenu menu = containerScreen.getMenu();
            PlayerConnectionTabManager tabManager = ClientPlayerConnectionTabManager.getInstance();
            if (tabManager.isForMenu(menu)) {
                PanelPortInfo panelPortInfo = tabManager.getPanelConnectionInfo();
                String selectedConnection = tabManager.getSelectedConnection();
                int screenLeft = containerScreen.getGuiLeft();
                double mouseX = mouseClickedEvent.getMouseX();
                Range<Integer> outwardTabs = getOutwardTabs(containerScreen, panelPortInfo.ports(), selectedConnection);
                int screenTop = containerScreen.getGuiTop();
                double mouseY = mouseClickedEvent.getMouseY();
                if (mouseY > screenTop) {
                    double difference = mouseY - screenTop;
                    int tab = Math.floorDiv((int) Math.floor(difference), 28);
                    if (tab >= 0 && tab < panelPortInfo.ports().size()) {
                        if (outwardTabs.contains(tab)) {
                            screenLeft -= 79;
                        }
                        if (mouseX < screenLeft && mouseX > screenLeft - 32) {
                            PanelPortInfo.Port moduleTab = panelPortInfo.ports().get(tab);
                            if (!moduleTab.identifier().equals(selectedConnection)) {
                                tabManager.setSelectedConnection(moduleTab.identifier());
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderModuleTabs(ContainerScreenEvent.Render.Background drawScreenEvent) {
        AbstractContainerMenu menu = drawScreenEvent.getContainerScreen().getMenu();
        PlayerConnectionTabManager tabManager = ClientPlayerConnectionTabManager.getInstance();
        if (tabManager.isForMenu(menu)) {
            loopTabs(
                    drawScreenEvent.getContainerScreen(),
                    drawScreenEvent.getPoseStack(),
                    tabManager.getConnections(),
                    tabManager.getSelectedConnection()
            );
        }
    }

    private static Range<Integer> getOutwardTabs(AbstractContainerScreen<?> screen, List<PanelPortInfo.Port> tabs, String identity) {
        int maxSupportedTabs = (int) Math.floor(screen.getYSize() / 28F);
        int maxTabs = Math.min(tabs.size(), maxSupportedTabs);
        int matchingTab = -1;

        for (int x = 0; x < maxTabs; x++) {
            PanelPortInfo.Port port = tabs.get(x);
            if (port.identifier().equals(identity)) {
                matchingTab = x;
            }
        }

        int panelTabStart = Math.min(matchingTab, maxSupportedTabs - 3);
        if (matchingTab >= 0) {
            return Range.between(panelTabStart, panelTabStart + 2);
        } else {
            return Range.is(maxSupportedTabs);
        }
    }

    private static void loopTabs(AbstractContainerScreen<?> screen, PoseStack poseStack, List<PanelPortInfo.Port> tabs, String identity) {
        if (!tabs.isEmpty()) {
            Range<Integer> handledTabs = getOutwardTabs(screen, tabs, identity);
            int outward = -1;
            int maxSupportedTabs = (int) Math.floor(screen.getYSize() / 28F);
            int maxTabs = Math.min(tabs.size(), maxSupportedTabs);

            for (int x = 0; x < maxTabs; x++) {
                PanelPortInfo.Port port = tabs.get(x);
                boolean matches = port.identifier().equals(identity);
                if (x == handledTabs.getMinimum()) {
                    outward = 2;
                    RenderSystem.setShaderTexture(0, TABS);
                    int panelY = x > 0 ? 84 : 0;
                    screen.blit(poseStack, screen.getGuiLeft() - 79, screen.getGuiTop() + (x * 28), 68, panelY, 83, 84);
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

    private static void renderTab(AbstractContainerScreen<?> screen, PanelPortInfo.Port port, PoseStack matrixStack, boolean selected, int tab, int x, int y) {
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
