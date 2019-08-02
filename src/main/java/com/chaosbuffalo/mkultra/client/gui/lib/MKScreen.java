package com.chaosbuffalo.mkultra.client.gui.lib;

import com.chaosbuffalo.mkultra.client.gui.lib.MKWidget;
import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MKScreen extends GuiScreen {
    public ArrayList<MKWidget> children;
    public ArrayList<MKWidget> reverseChildren;
    public static String NO_STATE = "NO_STATE";
    private HashMap<Integer, MKWidget> selectedWidgets;
    public boolean firstRender;
    private String currentState;
    public HashMap<String, MKWidget> states;
    private HashSet<Runnable> postSetupCallbacks;

    public MKScreen(){
        super();
        firstRender = true;
        children = new ArrayList<>();
        reverseChildren = new ArrayList<>();
        states = new HashMap<>();
        postSetupCallbacks = new HashSet<>();
        selectedWidgets = new HashMap<>();
        currentState = NO_STATE;
    }

    @Override
    public void onResize(Minecraft minecraft, int width, int height) {
        super.onResize(minecraft, width, height);
        flagNeedSetup();
    }

    public void addRestoreStateCallbacks(){
        String state = getState();
        addPostSetupCallback(() -> setState(state));
    }

    public void addPostSetupCallback(Runnable callback){
        postSetupCallbacks.add(callback);
    }

    public void addState(String name, MKWidget root){
        this.states.put(name, root);
    }

    public void removeState(String name){
        this.states.remove(name);
    }

    public void setState(String newState){
        if (newState.equals(currentState)){
            return;
        }
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

    public String getState(){
        return currentState;
    }

    private void runSetup(){
        setupScreen();
        for (Runnable cb : postSetupCallbacks){
            cb.run();
        }
        postSetupCallbacks.clear();
    }

    public void setupScreen(){
        this.clearWidgets();
        this.states.clear();
        this.currentState = NO_STATE;
    }

    public void flagNeedSetup(){
        firstRender = true;
        addRestoreStateCallbacks();
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
            runSetup();
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
        if (selectedWidgets.get(mouseButton) != null){
            if (selectedWidgets.get(mouseButton).mouseDragged(this.mc, mouseX, mouseY, mouseButton)){
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
                selectedWidgets.put(mouseButton, clickHandler);
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
        if (selectedWidgets.get(mouseButton) != null){
            MKWidget selected = selectedWidgets.get(mouseButton);
            selectedWidgets.remove(mouseY);
            if (selected.mouseReleased(mouseX, mouseY, mouseButton)){
                return;
            }
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

}
