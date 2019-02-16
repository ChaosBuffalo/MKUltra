package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchableResourceLocationList extends GuiScrollingList {

    private ArrayList<ResourceLocation> primaryLocations;
    private ArrayList<ResourceLocation> searchResults;
    private String searchString;

    public SearchableResourceLocationList(ArrayList<ResourceLocation> locations, Minecraft client,
                                          int width, int height,
                                          int top, int bottom,
                                          int left, int entryHeight,
                                          int screenWidth, int screenHeight){
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        primaryLocations = locations;
        searchResults = new ArrayList<>();
    }

    @Override
    protected int getSize() {
        return searchResults.size();
    }

    public void setSearchString(String searchString){
        this.searchString = searchString;
        searchResults.clear();
        searchResults.addAll(primaryLocations.stream().filter(x -> x.getResourcePath().startsWith(searchString))
                .collect(Collectors.toList()));
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

    }
}
