package com.chaosbuffalo.mkultra.client.gui.lib;

import com.chaosbuffalo.mkultra.client.gui.lib.MKWidget;
import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MKScreen extends GuiScreen {
    public ArrayList<MKWidget> children;
    public ArrayList<MKWidget> reverseChildren;
    public static String NO_STATE = "NO_STATE";
    private MKWidget selectedWidget;
    public boolean firstRender;
    private String currentState;
    public HashMap<String, MKWidget> states;

    public MKScreen(){
        super();
        this.selectedWidget = null;
        firstRender = true;
        children = new ArrayList<>();
        reverseChildren = new ArrayList<>();
        states = new HashMap<>();
        currentState = NO_STATE;
    }

    public void addState(String name, MKWidget root){
        this.states.put(name, root);
    }

    public void removeState(String name){
        this.states.remove(name);
    }

    public void setState(String newState){
        if (newState.equals(NO_STATE) || states.containsKey(newState)){
            if (!newState.equals(NO_STATE)){
                this.addWidget(states.get(newState));
            }
            if (!currentState.equals(NO_STATE)){
                this.removeWidget(states.get(currentState));
            }
            this.currentState = newState;
            Log.info("New state is %s", currentState);

        } else {
            Log.info("Tried to set screen state to: %s, but doesn't exist.", newState);
        }
    }

    public void setupScreen(){
        this.clearWidgets();
        this.states.clear();
        this.currentState = NO_STATE;
        this.selectedWidget = null;
    }

    public void flagNeedSetup(){
        firstRender = true;
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
            if (child.isVisible()){
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long dt) {
        if (selectedWidget != null){
            if (selectedWidget.mouseDragged(this.mc, mouseX, mouseY, mouseButton)){
                return;
            }
        }
        super.mouseClickMove(mouseX, mouseY, mouseButton, dt);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        for (MKWidget child : reverseChildren){
            if (!child.isVisible()){
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
