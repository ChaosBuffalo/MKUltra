package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.function.Function;

public class ButtonList<E> extends GuiScrollingList {

    private ArrayList<E> list;
    private Minecraft mc;
    private float partialTicks;
    private IListButtonHandler<E> handler;
    private int listId;
    private Function<E, String> getHumanReadable;

    public ButtonList(ArrayList<E> list,
                      IListButtonHandler<E> handler,
                      int listId,
                      Function<E, String> getHumanReadable,
                      Minecraft client,
                      int width, int height,
                      int top, int bottom,
                      int left, int entryHeight,
                      int screenWidth, int screenHeight){
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        this.list = list;
        mc = client;
        this.handler = handler;
        this.listId = listId;
        this.getHumanReadable = getHumanReadable;
    }

    @Override
    protected int getSize() {
        return list.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        handler.handleListButtonClicked(list.get(index), listId);
    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.partialTicks = partialTicks;
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        GuiButton syncButton = new GuiButton(slotIdx, left + 15, slotTop,
                listWidth - 30, 20, getHumanReadable.apply(list.get(slotIdx)));
        syncButton.drawButton(mc, mouseX, mouseY, partialTicks);
    }
}
