package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ButtonListEntry<E> implements GuiListExtended.IGuiListEntry {

    private E entry;
    private BiConsumer<E, Integer> handler;
    private int listId;
    private Function<E, String> getHumanReadable;
    private Minecraft client;

    public ButtonListEntry(E entry, BiConsumer<E, Integer> handler, int listId,
                           Function<E, String> getHumanReadable, Minecraft mc) {
        this.entry = entry;
        this.handler = handler;
        this.listId = listId;
        this.getHumanReadable = getHumanReadable;
        client = mc;
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        GuiButton syncButton = new GuiButton(slotIndex, x + 15, y,
                listWidth - 30, 20, getHumanReadable.apply(entry));
        syncButton.drawButton(client, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        this.handler.accept(entry, listId);
        return true;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

    }
}
