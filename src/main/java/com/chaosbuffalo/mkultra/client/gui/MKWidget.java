package com.chaosbuffalo.mkultra.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public class MKWidget extends Gui {
    public int width;
    public int height;
    public int x;
    public int y;
    public MKWidget parent;
    public boolean enabled;
    public boolean visible;
    public boolean skipBoundsCheck;
    protected boolean hovered;
    public UUID id;
    public ArrayList<MKWidget> children;
    public ArrayList<MKWidget> reverseChildren;
    public float sizeHintWidth;
    public float sizeHintHeight;
    public float posHintX;
    public float posHintY;
    public boolean doRelativeX;
    public boolean doRelativeY;
    public boolean doRelativeWidth;
    public boolean doRelativeHeight;

    public MKWidget(int x, int y){
        this(x, y, 200, 20);
    }

    public MKWidget(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        id = UUID.randomUUID();
        this.enabled = true;
        this.visible = true;
        this.hovered = false;
        this.parent = null;
        this.children = new ArrayList<>();
        this.reverseChildren = new ArrayList<>();
        this.skipBoundsCheck = false;
        this.doRelativeX = false;
        this.doRelativeY = false;
        this.doRelativeHeight = false;
        this.doRelativeWidth = false;
        this.sizeHintWidth = 1.0f;
        this.sizeHintHeight = 1.0f;
        this.posHintX = 0.0f;
        this.posHintY = 0.0f;
    }

    public MKWidget setDoRelativeWidth(float ratio){
        this.sizeHintWidth = ratio;
        this.doRelativeWidth = true;
        return this;
    }

    public MKWidget setDoRelativeHeight(float ratio){
        this.sizeHintHeight = ratio;
        this.doRelativeHeight = true;
        return this;
    }

    public MKWidget setDoRelativeX(float ratio){
        this.posHintX = ratio;
        this.doRelativeX = true;
        return this;
    }

    public MKWidget setDoRelativeY(float ratio){
        this.posHintY = ratio;
        this.doRelativeY = true;
        return this;
    }


    public MKWidget setParent(MKWidget parent){
        this.parent = parent;
        return this;
    }

    @Nullable
    public MKWidget getParent(){
        return parent;
    }

    public boolean mouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        for (MKWidget child : reverseChildren){
            if (child.mouseDragged(minecraft, mouseX, mouseY, mouseButton)){
                return true;
            }
        }
        if (onMouseDragged(minecraft, mouseX, mouseY, mouseButton)){
            return true;
        }
        return false;
    }

    public boolean onMouseDragged(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (MKWidget child : reverseChildren){
            if (child.mouseReleased(mouseX, mouseY, mouseButton)){
                return true;
            }
        }
        if (onMouseRelease(mouseX, mouseY, mouseButton)){
            return true;
        }
        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int mouseButton){
        return false;
    }

    public MKWidget mousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (!this.enabled || !this.visible || !this.isInBounds(mouseX, mouseY)){
            return null;
        }
        for (MKWidget child : reverseChildren){
            if (child.mousePressed(minecraft, mouseX, mouseY, mouseButton) != null){
                return child;
            }
        }
        if (onMousePressed(minecraft, mouseX, mouseY, mouseButton)){
            return this;
        }
        return null;
    }

    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
        return false;
    }

    public boolean isInBounds(int x, int y){
        if (this.skipBoundsCheck){
            return true;
        }
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void addWidget(MKWidget widget){
        widget.setParent(this);
        this.children.add(widget);
        this.reverseChildren = new ArrayList<>(Lists.reverse(children));
    }

    public void removeWidget(MKWidget widget){
        if (widget.getParent() != null && widget.getParent().id.equals(this.id)){
            if (children.removeIf((x) -> x.id.equals(widget.id))){
                this.reverseChildren = new ArrayList<>(Lists.reverse(children));
            }
            widget.setParent(null);
        }
    }

    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {

    }

    public void clearWidgets(){
        for (MKWidget widget : children){
            widget.setParent(null);
        }
        children.clear();
        reverseChildren.clear();
    }

    public int calcX(){
        if (doRelativeX && parent != null){
            return parent.x + (int)(parent.width * posHintX);
        }
        return x;
    }

    public int calcY(){
        if (doRelativeY && parent != null){
            return parent.y + (int)(parent.height * posHintY);
        }
        return y;
    }

    public int calcWidth(){
        if (doRelativeWidth && parent != null){
            return (int)(parent.width * sizeHintWidth);
        }
        return width;
    }

    public int calcHeight(){
        if (doRelativeHeight && parent != null){
            return (int)(parent.height * sizeHintHeight);
        }
        return height;
    }

    public void drawWidget(Minecraft mc, int mouseX, int mouseY, float partialTicks){
        draw(mc, calcX(), calcY(), calcWidth(), calcHeight(), mouseX, mouseY, partialTicks);
        for (MKWidget child : children){
            if (child.visible){
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
    }

}
