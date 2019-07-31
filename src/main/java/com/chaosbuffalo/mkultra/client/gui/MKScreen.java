package com.chaosbuffalo.mkultra.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class MKScreen extends GuiScreen {
    public ArrayList<MKWidget> children;
    public ArrayList<MKWidget> reverseChildren;
    private MKWidget selectedWidget;
    public boolean firstRender;

    public MKScreen(){
        super();
        this.selectedWidget = null;
        firstRender = true;
        children = new ArrayList<>();
        reverseChildren = new ArrayList<>();
    }

    public void setupScreen(){

    }

    public void addWidget(MKWidget widget){
        this.children.add(widget);
        this.reverseChildren = new ArrayList<>(Lists.reverse(children));
    }

    public void removeWidget(MKWidget widget){
        if (containsWidget(widget)){
            if (children.removeIf((x) -> x.id.equals(widget.id))){
                this.reverseChildren = new ArrayList<>(Lists.reverse(children));
            }
        }
    }

    public boolean containsWidget(MKWidget widget){
        for (MKWidget child : children){
            if (widget.id.equals(child.id)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (firstRender){
            setupScreen();
            firstRender = false;
        }
        for (MKWidget child : children){
            if (child.visible){
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long dt) {
        for (MKWidget child : reverseChildren){
            if (child.visible && child.mouseDragged(this.mc, mouseX, mouseY, mouseButton)){
                return;
            }
        }
        super.mouseClickMove(mouseX, mouseY, mouseButton, dt);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        for (MKWidget child : reverseChildren){
            if (!child.visible){
                continue;
            }
            MKWidget clickHandler = child.mousePressed(this.mc, mouseX, mouseY, mouseButton);
            if (clickHandler != null){
                selectedWidget = clickHandler;
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void clearWidgets(){
        for (MKWidget widget : children){
            widget.setParent(null);
        }
        children.clear();
        reverseChildren.clear();
    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton)
    {
        if (selectedWidget != null){
            MKWidget selected = selectedWidget;
            selectedWidget = null;
            if (selected.mouseReleased(mouseX, mouseY, mouseButton)){
                return;
            }
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

}
