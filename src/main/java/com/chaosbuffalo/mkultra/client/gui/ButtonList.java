package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ButtonList<E> extends GuiListExtended {

    private ArrayList<ButtonListEntry<E>> list;

    public ButtonList(ArrayList<E> list,
                      BiConsumer<E, Integer> handler,
                      int listId,
                      Function<E, String> getHumanReadable,
                      Minecraft client,
                      int width, int height,
                      int top, int bottom,
                      int entryHeight, int left) {
        super(client, width, height, top, bottom, entryHeight);
        this.list = new ArrayList<>();
        for (E item : list) {
            this.list.add(new ButtonListEntry<>(item, handler, listId, getHumanReadable, client));
        }
        this.left = left;
        this.right = left + width;
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    protected int getScrollBarX() {
        return this.left + this.width - 6;
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {

    }

    @Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {

    }

    @Override
    protected int getSize() {
        return list.size();
    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return list.get(index);
    }
}
