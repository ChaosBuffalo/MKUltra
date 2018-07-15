package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.ClassData;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import com.chaosbuffalo.mkultra.network.packets.client.ClassLearnPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;


public class ChooseClassScreen extends GuiScreen {

    private boolean learning;
    private List<ResourceLocation> classes;
    private static int PER_PAGE = 8;
    private int currentPage;

    private static int CHOOSE_BUTTON = 0;
    private static int NEXT_BUTTON = 1;
    private static int PREV_BUTTON = 2;

    public ChooseClassScreen(boolean showAll) {
        currentPage = 0;
        this.learning = showAll;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int panelWidth = 250;
        int panelHeight = 166;
        int chooseButtonHeight = 23;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/demo_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, panelWidth, panelHeight,
                256, 256);
        int titleHeight = 15;
        this.fontRenderer.drawString("Choose Your Class: ", xPos + 15, yPos + 4, 8129636);


        EntityPlayer player = mc.player;
        IPlayerData data = MKUPlayerData.get(player);
        if (data == null)
            return;

        String text;
        if (player.getHeldItemMainhand().getItem() instanceof IClassProvider) {
            IClassProvider icon = (IClassProvider) player.getHeldItemMainhand().getItem();
            text = icon.getClassSelectionText();
        } else {
            text = "You shouldn't see this.";
        }

        this.fontRenderer.drawSplitString(text, xPos + 15, yPos + titleHeight + 2 + 4, 220, 0);
        int contentHeight = 20;
        int buttonStartY = contentHeight + yPos + titleHeight + 2 + 4 + 2;
        int buttonStartX = xPos + 15;

        this.buttonList.clear();

        List<ResourceLocation> knownClasses = data.getKnownClasses();

        if (learning) {
            ItemStack main = player.getHeldItemMainhand();
            classes = ClassData.getClassesProvidedByItem(main.getItem());
        } else {
            classes = ClassData.getValidClasses(knownClasses);
        }
        List<ResourceLocation> class_subset = classes;
        boolean wasLarge = class_subset.size() > PER_PAGE;
        if (wasLarge){
            classes = class_subset.subList(PER_PAGE*currentPage,
                      PER_PAGE*currentPage + Math.min(class_subset.size() - PER_PAGE*currentPage, PER_PAGE));
        }

        // draw choose class buttons
        for (int i = 0; i < classes.size(); i++) {
            ResourceLocation classId = classes.get(i);
            String className = ClassData.getClassName(classId);
            xPos = buttonStartX + i % 2 * 110;
            yPos = buttonStartY + i / 2 * chooseButtonHeight;

            ClassButton button = new ClassButton(i, CHOOSE_BUTTON, xPos, yPos, 105, 20, className);
            if (learning) {
                // Only allow selecting classes we don't know
                button.enabled = !knownClasses.contains(classId);
            } else {
                // Only allow selecting classes other than the current
                button.enabled = classId.compareTo(data.getClassId()) != 0;
            }
            button.drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.buttonList.add(button);
        }
        // draw next page button
        int afterChooseY = buttonStartY + 2 + (PER_PAGE / 2) * chooseButtonHeight;

        if (class_subset.size() > (currentPage + 1) * PER_PAGE){
            GuiButton button = new GuiButton(NEXT_BUTTON, buttonStartX + 90, afterChooseY, 80, 20, "Next");
            button.drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.buttonList.add(button);
        }

        if (currentPage > 0){
            GuiButton button = new GuiButton(PREV_BUTTON, buttonStartX, afterChooseY,
                    80, 20, "Back");
            button.drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.buttonList.add(button);
        }





    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == CHOOSE_BUTTON){
            ClassButton chooseButton = (ClassButton) button;
            MKUltra.packetHandler.sendToServer(new ClassLearnPacket(
                    classes.get(chooseButton.classInteger), learning));
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null)
                this.mc.setIngameFocus();
        }
        else if (button.id == NEXT_BUTTON){
            currentPage += 1;
        } else if (button.id == PREV_BUTTON){
            currentPage -= 1;
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
