package com.chaosbuffalo.mkultra.client.gui.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.UUID;

public class MKWidget extends Gui {
    private int width;
    private int height;
    private int x;
    private int y;
    public MKWidget parent;
    private boolean enabled;
    private boolean visible;
    public boolean skipBoundsCheck;
    private boolean hovered;
    private float hoveredTicks;
    public UUID id;
    public ArrayDeque<MKWidget> children;
    // These should be used by layouts.
    private float sizeHintWidth;
    private float sizeHintHeight;
    private float posHintX;
    private float posHintY;
    private int longHoverTicks;
    public MKScreen screen;


    public MKWidget(int x, int y) {
        this(x, y, 200, 20);
    }

    public MKWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        id = UUID.randomUUID();
        this.enabled = true;
        this.visible = true;
        this.hovered = false;
        this.parent = null;
        this.children = new ArrayDeque<>();
        this.skipBoundsCheck = false;
        this.sizeHintWidth = 1.0f;
        this.sizeHintHeight = 1.0f;
        this.posHintX = 0.0f;
        this.hoveredTicks = 0;
        this.posHintY = 0.0f;
        longHoverTicks = UIConstants.DEFAULT_LONG_HOVER_TICKS;
    }

    public void setScreen(MKScreen screen) {
        this.screen = screen;
        for (MKWidget child : children) {
            child.setScreen(screen);
        }
    }

    public MKWidget setLongHoverTime(int newTime) {
        longHoverTicks = newTime;
        return this;
    }

    public int getLongHoverTicks() {
        return longHoverTicks;
    }

    @Nullable
    public MKScreen getScreen() {
        return this.screen;
    }

    public MKWidget setSizeHintWidth(float ratio) {
        this.sizeHintWidth = ratio;
        return this;
    }

    public MKWidget setSizeHintHeight(float ratio) {
        this.sizeHintHeight = ratio;
        return this;
    }

    public MKWidget setPosHintX(float ratio) {
        this.posHintX = ratio;
        return this;
    }

    public MKWidget setPosHintY(float ratio) {
        this.posHintY = ratio;
        return this;
    }

    public Vec2i getParentCoords(Vec2i pos) {
        if (parent == null) {
            return pos;
        } else {
            return parent.getParentCoords(pos);
        }
    }


    public MKWidget setParent(MKWidget parent) {
        this.parent = parent;
        return this;
    }

    public MKWidget setHeight(int newHeight) {
        this.height = newHeight;
        return this;
    }

    public MKWidget setWidth(int newWidth) {
        this.width = newWidth;
        return this;
    }

    public MKWidget setX(int newX) {
        this.x = newX;
        return this;
    }

    public MKWidget setY(int newY) {
        this.y = newY;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getPosHintX() {
        return posHintX;
    }

    public float getPosHintY() {
        return posHintY;
    }

    public float getSizeHintWidth() {
        return sizeHintWidth;
    }

    public float getSizeHintHeight() {
        return sizeHintHeight;
    }

    @Nullable
    public MKWidget getParent() {
        return parent;
    }

    public boolean onMouseScrollWheel(Minecraft minecraft, int mouseX, int mouseY, int direction) {
        return false;
    }

    public boolean mouseScrollWheel(Minecraft minecraft, int mouseX, int mouseY, int direction) {
        if (!this.isEnabled() || !this.isVisible() || !this.isInBounds(mouseX, mouseY)) {
            return false;
        }
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mouseScrollWheel(minecraft, mouseX, mouseY, direction)) {
                return true;
            }
        }
        if (onMouseScrollWheel(minecraft, mouseX, mouseY, direction)) {
            return true;
        }
        return false;
    }

    public boolean mouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mouseDragged(minecraft, mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        if (onMouseDragged(minecraft, mouseX, mouseY, mouseButton)) {
            return true;
        }
        return false;
    }

    public boolean onMouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mouseReleased(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        if (onMouseRelease(mouseX, mouseY, mouseButton)) {
            return true;
        }
        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public MKWidget mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (!this.isEnabled() || !this.isVisible() || !this.isInBounds(mouseX, mouseY)) {
            return null;
        }
        Iterator<MKWidget> it = children.descendingIterator();
        while (it.hasNext()) {
            MKWidget child = it.next();
            if (child.mousePressed(minecraft, mouseX, mouseY, mouseButton) != null) {
                return child;
            }
        }
        if (onMousePressed(minecraft, mouseX, mouseY, mouseButton)) {
            return this;
        }
        return null;
    }

    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public boolean isInBounds(int x, int y) {
        if (this.skipBoundsCheck) {
            return true;
        }
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public void setHovered(boolean value) {
        this.hovered = value;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public MKWidget setVisible(boolean value) {
        this.visible = value;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public MKWidget setEnabled(boolean value) {
        this.enabled = value;
        return this;
    }

    public boolean addWidget(MKWidget widget) {
        widget.setParent(this);
        widget.setScreen(getScreen());
        this.children.add(widget);
        return true;
    }

    public void removeWidget(MKWidget widget) {
        if (widget.getParent() != null && widget.getParent().id.equals(this.id)) {
            children.removeIf((x) -> x.id.equals(widget.id));
            widget.setParent(null);
            widget.setScreen(null);
        }
    }

    public void preDraw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {

    }

    public void longHoverDraw(Minecraft mc, int x, int y, int width, int height,
                              int mouseX, int mouseY, float partialTicks) {

    }

    public void handleHoverDetection(int mouseX, int mouseY, float partialTicks) {
        boolean hovered = checkHovered(mouseX, mouseY);
        if (hovered) {
            setHoveredTicks(getHoveredTicks() + partialTicks);
        } else {
            setHoveredTicks(0);
        }
        setHovered(hovered);
    }

    public void handleLongHoverDraw(Minecraft mc, int x, int y, int width, int height,
                                    int mouseX, int mouseY, float partialTicks) {
        if (isHovered() && getHoveredTicks() > getLongHoverTicks()) {
            longHoverDraw(mc, x, y, width, height, mouseX, mouseY, partialTicks);
        }
    }

    public float getHoveredTicks() {
        return hoveredTicks;
    }

    public void setHoveredTicks(float value) {
        hoveredTicks = value;
    }

    public boolean checkHovered(int mouseX, int mouseY) {
        return isVisible() && isEnabled() && isInBounds(mouseX, mouseY);
    }

    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {

    }

    public void postDraw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {

    }

    public void clearWidgets() {
        for (MKWidget widget : children) {
            widget.setParent(null);
        }
        children.clear();
    }

    public int getRight() {
        return getX() + getWidth();
    }

    public int getTop() {
        return getY();
    }

    public int getLeft() {
        return getX();
    }

    public int getBottom() {
        return getY() + getHeight();
    }


    public void drawWidget(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        handleHoverDetection(mouseX, mouseY, partialTicks);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        preDraw(mc, x, y, width, height, mouseX, mouseY, partialTicks);
        draw(mc, x, y, width, height, mouseX, mouseY, partialTicks);
        for (MKWidget child : children) {
            if (child.isVisible()) {
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
        postDraw(mc, x, y, width, height, mouseX, mouseY, partialTicks);
        handleLongHoverDraw(mc, x, y, width, height, mouseX, mouseY, partialTicks);
    }

}
