package com.chaosbuffalo.mkultra.client.gui.lib;


import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MKScreen extends GuiScreen {
    public ArrayDeque<MKWidget> children;
    public static String NO_STATE = "NO_STATE";
    private HashMap<Integer, MKWidget> selectedWidgets;
    public boolean firstRender;
    private String currentState;
    public HashMap<String, MKWidget> states;
    private ArrayList<Runnable> postSetupCallbacks;
    private ArrayList<Runnable> preDrawRunnables;
    private ArrayList<HoveringTextInstruction> hoveringText;
    private ArrayDeque<MKModal> modals;

    public MKScreen() {
        super();
        firstRender = true;
        children = new ArrayDeque<>();
        states = new HashMap<>();
        postSetupCallbacks = new ArrayList<>();
        selectedWidgets = new HashMap<>();
        preDrawRunnables = new ArrayList<>();
        hoveringText = new ArrayList<>();
        modals = new ArrayDeque<>();
        currentState = NO_STATE;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            int i = Integer.signum(scroll);

            Iterator<MKModal> modalIt = modals.descendingIterator();
            while (modalIt.hasNext()) {
                MKModal child = modalIt.next();
                if (!child.isVisible()) {
                    continue;
                }
                if (child.mouseScrollWheel(this.mc, x, y, i)) {
                    return;
                }
            }
            Iterator<MKWidget> it = children.descendingIterator();
            while (it.hasNext()) {
                MKWidget child = it.next();
                if (!child.isVisible()) {
                    continue;
                }
                if (child.mouseScrollWheel(this.mc, x, y, i)) {
                    return;
                }
            }
        }
    }

    public void addHoveringText(HoveringTextInstruction instruction) {
        hoveringText.add(instruction);
    }

    public void addModal(MKModal modal) {
        modal.setScreen(this);
        modal.setWidth(width);
        modal.setHeight(height);
        this.modals.add(modal);
    }

    public void closeModal(MKModal modal) {
        if (this.modals.removeIf((x) -> x.id.equals(modal.id))) {
            if (modal.getOnCloseCallback() != null) {
                modal.getOnCloseCallback().run();
            }
            modal.setScreen(null);
        }
    }


    @Override
    public void onResize(Minecraft minecraft, int width, int height) {
        super.onResize(minecraft, width, height);
        for (MKModal modal : modals){
            closeModal(modal);
        }
        flagNeedSetup();
    }

    public void addPreDrawRunnable(Runnable runnable) {
        preDrawRunnables.add(runnable);
    }

    public void removePreDrawRunnable(Runnable runnable) {
        preDrawRunnables.remove(runnable);
    }

    public void clearPreDrawRunnables() {
        preDrawRunnables.clear();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void addRestoreStateCallbacks() {
        String state = getState();
        addPostSetupCallback(() -> setState(state));
    }

    public void addPostSetupCallback(Runnable callback) {
        postSetupCallbacks.add(callback);
    }

    public void addState(String name, MKWidget root) {
        this.states.put(name, root);
    }

    public void removeState(String name) {
        this.states.remove(name);
    }

    public void setState(String newState) {
        if (newState.equals(currentState)) {
            return;
        }
        if (newState.equals(NO_STATE) || states.containsKey(newState)) {
            if (!newState.equals(NO_STATE)) {
                this.addWidget(states.get(newState));
            }
            if (!currentState.equals(NO_STATE)) {
                this.removeWidget(states.get(currentState));
            }
            this.currentState = newState;
//            Log.info("New state is %s", currentState);

        } else {
            Log.info("Tried to set screen state to: %s, but doesn't exist.", newState);
        }
    }

    public String getState() {
        return currentState;
    }

    private void runSetup() {
        setupScreen();
        for (Runnable cb : postSetupCallbacks) {
            cb.run();
        }
        postSetupCallbacks.clear();
    }

    public void setupScreen() {
        clearWidgets();
        clearPreDrawRunnables();
        this.states.clear();
        this.currentState = NO_STATE;
    }

    public void flagNeedSetup() {
        firstRender = true;
        addRestoreStateCallbacks();
    }


    public void addWidget(MKWidget widget) {
        widget.setScreen(this);
        this.children.add(widget);
    }

    public void removeWidget(MKWidget widget) {
        if (containsWidget(widget)) {
            if (children.removeIf((x) -> x.id.equals(widget.id))) {
                widget.setScreen(null);
            }
        }
    }

    public boolean containsWidget(MKWidget widget) {
        for (MKWidget child : children) {
            if (widget.id.equals(child.id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (firstRender) {
            runSetup();
            firstRender = false;
        }
        for (Runnable runnable : preDrawRunnables) {
            runnable.run();
        }
        for (MKWidget child : children) {
            if (child.isVisible()) {
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
        for (MKModal modal : modals) {
            if (modal.isVisible()) {
                modal.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
        for (HoveringTextInstruction instruction : hoveringText) {
            instruction.draw(fontRenderer, this.width, this.height);
        }
        hoveringText.clear();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long dt) {
        if (selectedWidgets.get(mouseButton) != null) {
            if (selectedWidgets.get(mouseButton).mouseDragged(this.mc, mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        super.mouseClickMove(mouseX, mouseY, mouseButton, dt);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Iterator<MKModal> modalIt = modals.descendingIterator();
        while (modalIt.hasNext()) {
            MKModal child = modalIt.next();
            if (!child.isVisible()) {
                continue;
            }
            MKWidget clickHandler = child.mousePressed(this.mc, mouseX, mouseY, mouseButton);
            if (clickHandler != null) {
                selectedWidgets.put(mouseButton, clickHandler);
                return;
            }
        }
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (!child.isVisible()) {
                continue;
            }
            MKWidget clickHandler = child.mousePressed(this.mc, mouseX, mouseY, mouseButton);
            if (clickHandler != null) {
                selectedWidgets.put(mouseButton, clickHandler);
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void clearWidgets() {
        for (MKWidget widget : children) {
            widget.setScreen(null);
        }
        children.clear();
    }

    public void clearModals() {
        for (MKModal modal : modals) {
            modal.setScreen(null);
        }
        modals.clear();
    }

    public void clear() {
        clearWidgets();
        clearModals();
        hoveringText.clear();
    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (selectedWidgets.get(mouseButton) != null) {
            MKWidget selected = selectedWidgets.get(mouseButton);
            selectedWidgets.remove(mouseButton);
            if (selected.mouseReleased(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

}
